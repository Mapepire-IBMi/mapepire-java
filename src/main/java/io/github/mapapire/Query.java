package io.github.mapapire;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.github.mapapire.types.QueryOptions;
import io.github.mapapire.types.QueryResult;
import io.github.mapapire.types.QueryState;

public class Query<T> {
    private SqlJob job;
    private static List<Query<?>> globalQueryList = new ArrayList<>();
    private String correlationId;
    private String sql;
    private boolean isPrepared = false;
    private List<?> parameters;
    private int rowsToFetch = 100;
    private boolean isCLCommand;
    private QueryState state = QueryState.NOT_YET_RUN;
    private boolean isTerseResults;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public Query(SqlJob _job, String _query, QueryOptions _opts) {
        this.job = _job;
        this.sql = _query;

        // TODO: Fix constructor
        this.isPrepared = _opts.getParameters() != null;
        this.parameters = _opts.getParameters();
        this.isCLCommand = _opts.isClCommand();
        this.isTerseResults = _opts.isTerseResults();

        Query.globalQueryList.add(this);
    }

    public static Query<?> byId(String id) {
        if (id == null || id == "") {
            return null;
        } else {
            return Query.globalQueryList
                    .stream()
                    .filter(query -> query.correlationId.equals(id))
                    .findFirst()
                    .orElse(null);
        }
    }

    public static List<String> getOpenIds(SqlJob forJob) {
        return Query.globalQueryList.stream()
                .filter(q -> q.job.equals(forJob) || forJob == null)
                .filter(q -> q.getState() == QueryState.NOT_YET_RUN
                        || q.getState() == QueryState.RUN_MORE_DATA_AVAILABLE)
                .map(q -> q.correlationId)
                .collect(Collectors.toList());
    }

    public void cleanup() {
        List<CompletableFuture<Void>> closeFutures = globalQueryList.stream()
                .filter(query -> query.getState() == QueryState.RUN_DONE || query.getState() == QueryState.ERROR)
                .map(query -> CompletableFuture.runAsync(() -> {
                    try {
                        query.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }))
                .collect(Collectors.toList());

        // TODO: Fix this logic
        CompletableFuture<Void> allOf = CompletableFuture.allOf(closeFutures.toArray(new CompletableFuture[0]));
        try {
            allOf.get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        globalQueryList = globalQueryList.stream()
                .filter(q -> q.getState() != QueryState.RUN_DONE)
                .collect(Collectors.toList());
    }

    public CompletableFuture<QueryResult<T>> execute() {
        return this.execute(100);
    }

    public CompletableFuture<QueryResult<T>> execute(int rowsToFetch) {
        switch (this.state) {
            case RUN_MORE_DATA_AVAILABLE:
                throw new Error("Statement has already been run");
            case RUN_DONE:
                throw new Error("Statement has already been fully run");
        }

        ObjectNode queryObject = objectMapper.createObjectNode();
        if (this.isCLCommand) {
            queryObject.put("id", SqlJob.getNewUniqueId("clcommand"));
            queryObject.put("type", "cl");
            queryObject.put("terse", this.isTerseResults);
            queryObject.put("cmd", this.sql);
        } else {
            queryObject.put("id", SqlJob.getNewUniqueId("query"));
            queryObject.put("type", this.isPrepared ? "prepare_sql_execute" : "sql");
            queryObject.put("sql", this.sql);
            queryObject.put("terse", this.isTerseResults);
            queryObject.put("rows", rowsToFetch);
            JsonNode parameters = objectMapper.valueToTree(this.parameters);
            queryObject.set("parameters", parameters);
        }

        this.rowsToFetch = rowsToFetch;

        try {
            return job.send(objectMapper.writeValueAsString(queryObject))
                    .thenCompose(result -> {
                        try {
                            // TODO: Type safety: The expression of type QueryResult needs unchecked
                            // conversion to conform to QueryResult<T>
                            QueryResult<T> queryResult = objectMapper.readValue(result, QueryResult.class);

                            this.state = queryResult.isDone() ? QueryState.RUN_DONE
                                    : QueryState.RUN_MORE_DATA_AVAILABLE;

                            if (!queryResult.isSuccess() && !this.isCLCommand) {
                                this.state = QueryState.ERROR;

                                List<String> errorList = new ArrayList<>();
                                String error = queryResult.getError();
                                if (error != null) {
                                    errorList.add(error);
                                }
                                String sqlState = queryResult.getSqlState();
                                if (sqlState != null) {
                                    errorList.add(sqlState);
                                }
                                String sqlRc = String.valueOf(queryResult.getSqlRc());
                                if (sqlRc != null) {
                                    errorList.add(sqlRc);
                                }
                                if (errorList.isEmpty()) {
                                    errorList.add("Failed to run query (unknown error)");
                                }

                                throw new Error(String.join(", ", errorList));
                            }

                            this.correlationId = queryResult.getId();
                            return CompletableFuture.completedFuture(queryResult);
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                            return CompletableFuture.completedFuture(null);
                        }
                    });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return CompletableFuture.completedFuture(null);
        }
    }

    public CompletableFuture<QueryResult<T>> fetchMore() {
        return this.fetchMore(this.rowsToFetch);
    }

    public CompletableFuture<QueryResult<T>> fetchMore(int rowsToFetch) {
        switch (this.state) {
            case NOT_YET_RUN:
                throw new Error("Statement has not yet been run");
            case RUN_DONE:
                throw new Error("Statement has already been fully run");
        }

        ObjectNode queryObject = objectMapper.createObjectNode();
        queryObject.put("id", SqlJob.getNewUniqueId("fetchMore"));
        queryObject.put("cont_id", this.correlationId);
        queryObject.put("type", "sqlmore");
        queryObject.put("sql", this.sql);
        queryObject.put("rows", rowsToFetch);

        this.rowsToFetch = rowsToFetch;

        try {
            return job.send(objectMapper.writeValueAsString(queryObject))
                    .thenCompose(result -> {
                        try {
                            // TODO: Type safety: The expression of type QueryResult needs unchecked
                            // conversion to conform to QueryResult<T>
                            QueryResult<T> queryResult = objectMapper.readValue(result, QueryResult.class);

                            this.state = queryResult.isDone() ? QueryState.RUN_DONE
                                    : QueryState.RUN_MORE_DATA_AVAILABLE;

                            if (!queryResult.isSuccess()) {
                                this.state = QueryState.ERROR;

                                String error = queryResult.getError();
                                if (error != null) {
                                    throw new Error(error.toString());
                                } else {
                                    throw new Error("Failed to run query (unknown error)");
                                }
                            }

                            return CompletableFuture.completedFuture(queryResult);
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                            return CompletableFuture.completedFuture(null);
                        }
                    });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return CompletableFuture.completedFuture(null);
        }
    }

    public CompletableFuture<String> close() {
        if (correlationId != null && state != QueryState.RUN_DONE) {
            state = QueryState.RUN_DONE;
            ObjectNode queryObject = objectMapper.createObjectNode();
            queryObject.put("id", SqlJob.getNewUniqueId("sqlclose"));
            queryObject.put("cont_id", correlationId);
            queryObject.put("type", "sqlclose");

            try {
                return job.send(objectMapper.writeValueAsString(queryObject));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return CompletableFuture.completedFuture(null);
            }
        } else if (correlationId == null) {
            state = QueryState.RUN_DONE;
            return CompletableFuture.completedFuture(null);
        } else {
            return CompletableFuture.completedFuture(null);
        }
    }

    public String getId() {
        return this.correlationId;
    }

    public QueryState getState() {
        return this.state;
    }
}
