package io.github.mapepire;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.github.mapepire.types.QueryOptions;
import io.github.mapepire.types.QueryResult;
import io.github.mapepire.types.QueryState;

/**
 * Represents a SQL query that can be executed and managed within a SQL job.
 */
public class Query<T> {
    /**
     * The SQL job that this query will be executed in.
     */
    private SqlJob job;

    /**
     * A list of all global queries that are currently open.
     */
    private static List<Query<?>> globalQueryList = new ArrayList<>();

    /**
     * The correlation ID associated with the query.
     */
    private String correlationId;

    /**
     * The SQL statement to be executed.
     */
    private String sql;

    /**
     * Whether the query has been prepared.
     */
    private boolean isPrepared = false;

    /**
     * The parameters to be used with the SQL query.
     */
    private List<?> parameters;

    /**
     * The number of rows to fetch in each execution.
     */
    private int rowsToFetch = 100;

    /**
     * Whether the query is a CL command.
     */
    private boolean isCLCommand;

    /**
     * The current state of the query execution.
     */
    private QueryState state = QueryState.NOT_YET_RUN;

    /**
     * Whether the results should be terse.
     */
    private boolean isTerseResults;

    /**
     * TODO: Remove
     */
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Construct a new Query instance.
     * @param job   The SQL job that this query will be executed in.
     * @param query The SQL statement to be executed.
     * @param opts  The options for configuring a query.
     */
    public Query(SqlJob job, String query, QueryOptions opts) {
        this.job = job;
        this.sql = query;

        // TODO: Fix constructor
        this.isPrepared = opts.getParameters() != null;
        this.parameters = opts.getParameters();
        this.isCLCommand = opts.getIsClCommand();
        this.isTerseResults = opts.getIsTerseResults();

        Query.globalQueryList.add(this);
    }

    /**
     * Get a Query instance by its correlation ID.
     * @param id The correlation ID of the query.
     * @return The corresponding Query instance or null if not found.
     */
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

    /**
     * Get a list of open correlation IDs.
     * @return A list of correlation IDs for open queries.
     */
    public static List<String> getOpenIds() {
        return Query.getOpenIds(null);
    }

    /**
     * Get a list of open correlation IDs for the specified job.
     * @param forJob The job to filter the queries by.
     * @return A list of correlation IDs for open queries.
     */
    public static List<String> getOpenIds(SqlJob forJob) {
        return Query.globalQueryList.stream()
                .filter(q -> q.job.equals(forJob) || forJob == null)
                .filter(q -> q.getState() == QueryState.NOT_YET_RUN
                        || q.getState() == QueryState.RUN_MORE_DATA_AVAILABLE)
                .map(q -> q.correlationId)
                .collect(Collectors.toList());
    }

    /**
     * Clean up queries that are done or are in error state from the global query
     * list.
     */
    public void cleanup() {
        List<CompletableFuture<Void>> futures = globalQueryList.stream()
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
        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        try {
            allOf.get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        globalQueryList = globalQueryList.stream()
                .filter(q -> q.getState() != QueryState.RUN_DONE)
                .collect(Collectors.toList());
    }

    /**
     * Execute an SQL command and returns the result.
     * @param <T> The type of data to be returned.
     * @return A CompletableFuture that resolves to the query result.
     */
    public <T> CompletableFuture<QueryResult<T>> execute() throws Exception {
        return this.execute(100);
    }

    /**
     * Execute an SQL command and returns the result.
     * @param <T>         The type of data to be returned.
     * @param rowsToFetch The number of rows to fetch.
     * @return A CompletableFuture that resolves to the query result.
     */
    public <T> CompletableFuture<QueryResult<T>> execute(int rowsToFetch) throws Exception {
        switch (this.state) {
            case RUN_MORE_DATA_AVAILABLE:
                throw new Exception("Statement has already been run");
            case RUN_DONE:
                throw new Exception("Statement has already been fully run");
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
            if (this.parameters != null) {
                JsonNode parameters = objectMapper.valueToTree(this.parameters);
                queryObject.set("parameters", parameters);
            }
        }

        this.rowsToFetch = rowsToFetch;

        String result;
        try {
            result = job.send(objectMapper.writeValueAsString(queryObject)).get();
        } catch (Exception e) {
            e.printStackTrace();
            return CompletableFuture.completedFuture(null);
        }

        // TODO: Type safety: The expression of type QueryResult needs unchecked
        // conversion to conform to QueryResult<T>
        QueryResult<T> queryResult;
        try {
            queryResult = objectMapper.readValue(result, QueryResult.class);
        } catch (Exception e) {
            e.printStackTrace();
            return CompletableFuture.completedFuture(null);
        }

        this.state = queryResult.getIsDone() ? QueryState.RUN_DONE
                : QueryState.RUN_MORE_DATA_AVAILABLE;

        if (!queryResult.getSuccess() && !this.isCLCommand) {
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

            throw new Exception(String.join(", ", errorList));
        }

        this.correlationId = queryResult.getId();
        return CompletableFuture.completedFuture(queryResult);
    }

    /**
     * Fetch more rows from the currently running query.
     * @return A CompletableFuture that resolves to the query result.
     */
    public CompletableFuture<QueryResult<T>> fetchMore() throws Exception {
        return this.fetchMore(this.rowsToFetch);
    }

    /**
     * Fetch more rows from the currently running query.
     * @param rowsToFetch The number of additional rows to fetch.
     * @return A CompletableFuture that resolves to the query result.
     */
    public CompletableFuture<QueryResult<T>> fetchMore(int rowsToFetch) throws Exception {
        switch (this.state) {
            case NOT_YET_RUN:
                throw new Exception("Statement has not yet been run");
            case RUN_DONE:
                throw new Exception("Statement has already been fully run");
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

                            this.state = queryResult.getIsDone() ? QueryState.RUN_DONE
                                    : QueryState.RUN_MORE_DATA_AVAILABLE;

                            if (!queryResult.getSuccess()) {
                                this.state = QueryState.ERROR;

                                String error = queryResult.getError();
                                if (error != null) {
                                    throw new Exception(error.toString());
                                } else {
                                    throw new Exception("Failed to run query (unknown error)");
                                }
                            }

                            return CompletableFuture.completedFuture(queryResult);
                        } catch (Exception e) {
                            e.printStackTrace();
                            return CompletableFuture.completedFuture(null);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            return CompletableFuture.completedFuture(null);
        }
    }

    /**
     * Close the query.
     * @return A CompletableFuture that resolves when the query is closed.
     */
    public CompletableFuture<String> close() {
        if (correlationId != null && state != QueryState.RUN_DONE) {
            state = QueryState.RUN_DONE;
            ObjectNode queryObject = objectMapper.createObjectNode();
            queryObject.put("id", SqlJob.getNewUniqueId("sqlclose"));
            queryObject.put("cont_id", correlationId);
            queryObject.put("type", "sqlclose");

            try {
                return job.send(objectMapper.writeValueAsString(queryObject));
            } catch (Exception e) {
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

    /**
     * Get the correlation ID of the query.
     * @return The correlation ID of the query.
     */
    public String getId() {
        return this.correlationId;
    }

    /**
     * Get the current state of the query execution.
     * @return The current state of the query execution.
     */
    public QueryState getState() {
        return this.state;
    }
}
