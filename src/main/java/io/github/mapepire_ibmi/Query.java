package io.github.mapepire_ibmi;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.github.mapepire_ibmi.types.QueryOptions;
import io.github.mapepire_ibmi.types.QueryResult;
import io.github.mapepire_ibmi.types.QueryState;
import io.github.mapepire_ibmi.types.exceptions.ClientException;
import io.github.mapepire_ibmi.types.exceptions.UnknownServerException;

/**
 * Represents a SQL query that can be executed and managed within a SQL job.
 */
public class Query {
    /**
     * A list of all global queries that are currently open.
     */
    private static List<Query> globalQueryList = new ArrayList<>();

    /**
     * The SQL job that this query will be executed in.
     */
    private SqlJob job;

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
    private boolean isPrepared;

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
     * Construct a new Query instance.
     * 
     * @param job   The SQL job that this query will be executed in.
     * @param query The SQL statement to be executed.
     * @param opts  The options for configuring a query.
     */
    public Query(SqlJob job, String query, QueryOptions opts) {
        this.job = job;
        this.sql = query;
        this.isPrepared = opts.getParameters() != null;
        this.parameters = opts.getParameters();
        this.isCLCommand = opts.getIsClCommand();
        this.isTerseResults = opts.getIsTerseResults();

        Query.globalQueryList.add(this);
    }

    /**
     * Get a Query instance by its correlation ID.
     * 
     * @param id The correlation ID of the query.
     * @return The corresponding Query instance or null if not found.
     */
    public static Query byId(String id) {
        if (id == null || id.equals("")) {
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
     * 
     * @return A list of correlation IDs for open queries.
     */
    public static List<String> getOpenIds() {
        return Query.getOpenIds(null);
    }

    /**
     * Get a list of open correlation IDs for the specified job.
     * 
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
     * 
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public CompletableFuture<Void> cleanup() throws InterruptedException, ExecutionException {
        List<CompletableFuture<Void>> futures = globalQueryList.stream()
                .filter(query -> query.getState() == QueryState.RUN_DONE || query.getState() == QueryState.ERROR)
                .map(query -> CompletableFuture.runAsync(() -> {
                    try {
                        query.close();
                    } catch (JsonProcessingException | InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }))
                .collect(Collectors.toList());

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenRun(() -> {
                    globalQueryList = globalQueryList.stream()
                            .filter(q -> q.getState() != QueryState.RUN_DONE)
                            .collect(Collectors.toList());
                });
    }

    /**
     * Execute an SQL command and returns the result.
     * 
     * @param <T> The type of data to be returned.
     * @return A CompletableFuture that resolves to the query result.
     * @throws SQLException
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws ClientException
     * @throws JsonProcessingException
     * @throws JsonMappingException
     */
    public <T> CompletableFuture<QueryResult<T>> execute() throws JsonMappingException, JsonProcessingException,
            ClientException, InterruptedException, ExecutionException, SQLException {
        return this.execute(100);
    }

    /**
     * Execute an SQL command and returns the result.
     * 
     * @param <T>         The type of data to be returned.
     * @param rowsToFetch The number of rows to fetch.
     * @return A CompletableFuture that resolves to the query result.
     * @throws ClientException
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws JsonProcessingException
     * @throws JsonMappingException
     * @throws SQLException
     */
    public <T> CompletableFuture<QueryResult<T>> execute(int rowsToFetch) throws ClientException, JsonMappingException,
            JsonProcessingException, InterruptedException, ExecutionException, SQLException {
        if (rowsToFetch <= 0) {
            throw new ClientException("Rows to fetch must be greater than 0");
        }

        switch (this.state) {
            case RUN_MORE_DATA_AVAILABLE:
                throw new ClientException("Statement has already been run");
            case RUN_DONE:
                throw new ClientException("Statement has already been fully run");
            default:
        }

        ObjectMapper objectMapper = SingletonObjectMapper.getInstance();
        ObjectNode executeRequest = objectMapper.createObjectNode();
        if (this.isCLCommand) {
            executeRequest.put("id", SqlJob.getNewUniqueId("clcommand"));
            executeRequest.put("type", "cl");
            executeRequest.put("terse", this.isTerseResults);
            executeRequest.put("cmd", this.sql);
        } else {
            executeRequest.put("id", SqlJob.getNewUniqueId("query"));
            executeRequest.put("type", this.isPrepared ? "prepare_sql_execute" : "sql");
            executeRequest.put("sql", this.sql);
            executeRequest.put("terse", this.isTerseResults);
            executeRequest.put("rows", rowsToFetch);
            if (this.parameters != null) {
                JsonNode parameters = objectMapper.valueToTree(this.parameters);
                executeRequest.set("parameters", parameters);
            }
        }

        this.rowsToFetch = rowsToFetch;

        return job.send(objectMapper.writeValueAsString(executeRequest))
                .thenApply(result -> {
                    QueryResult<T> queryResult;
                    try {
                        queryResult = objectMapper.readValue(result, QueryResult.class);
                    } catch (Exception e) {
                        throw new CompletionException(e);
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
                            errorList.add("Failed to run query");
                        }

                        throw new CompletionException(
                                new SQLException(String.join(", ", errorList), queryResult.getSqlState()));
                    }

                    this.correlationId = queryResult.getId();
                    return queryResult;
                });
    }

    /**
     * Fetch more rows from the currently running query.
     * 
     * @return A CompletableFuture that resolves to the query result.
     * @throws SQLException
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws UnknownServerException
     * @throws JsonProcessingException
     * @throws JsonMappingException
     * @throws ClientException
     */
    public <T> CompletableFuture<QueryResult<T>> fetchMore() throws JsonMappingException, JsonProcessingException,
            UnknownServerException, InterruptedException, ExecutionException, SQLException, ClientException {
        return this.fetchMore(this.rowsToFetch);
    }

    /**
     * Fetch more rows from the currently running query.
     * 
     * @param rowsToFetch The number of additional rows to fetch.
     * @return A CompletableFuture that resolves to the query result.
     * @throws ClientException
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws JsonProcessingException
     * @throws JsonMappingException
     * @throws SQLException
     * @throws UnknownServerException
     */
    public <T> CompletableFuture<QueryResult<T>> fetchMore(int rowsToFetch)
            throws ClientException, JsonMappingException,
            JsonProcessingException, InterruptedException, ExecutionException, SQLException, UnknownServerException {
        if (rowsToFetch <= 0) {
            throw new ClientException("Rows to fetch must be greater than 0");
        }

        switch (this.state) {
            case NOT_YET_RUN:
                throw new ClientException("Statement has not yet been run");
            case RUN_DONE:
                throw new ClientException("Statement has already been fully run");
            default:
        }

        ObjectMapper objectMapper = SingletonObjectMapper.getInstance();
        ObjectNode fetchMoreRequest = objectMapper.createObjectNode();
        fetchMoreRequest.put("id", SqlJob.getNewUniqueId("fetchMore"));
        fetchMoreRequest.put("cont_id", this.correlationId);
        fetchMoreRequest.put("type", "sqlmore");
        fetchMoreRequest.put("sql", this.sql);
        fetchMoreRequest.put("rows", rowsToFetch);

        this.rowsToFetch = rowsToFetch;

        return job.send(objectMapper.writeValueAsString(fetchMoreRequest))
                .thenApply(result -> {
                    QueryResult<T> queryResult;
                    try {
                        queryResult = objectMapper.readValue(result, QueryResult.class);
                    } catch (Exception e) {
                        throw new CompletionException(e);
                    }

                    this.state = queryResult.getIsDone() ? QueryState.RUN_DONE
                            : QueryState.RUN_MORE_DATA_AVAILABLE;

                    if (!queryResult.getSuccess()) {
                        this.state = QueryState.ERROR;

                        String error = queryResult.getError();
                        if (error != null) {
                            throw new CompletionException(
                                    new SQLException(error.toString(), queryResult.getSqlState()));
                        } else {
                            throw new CompletionException(new UnknownServerException("Failed to fetch more"));
                        }
                    }

                    return queryResult;
                });
    }

    /**
     * Close the query.
     * 
     * @return A CompletableFuture that resolves when the query is closed.
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws JsonProcessingException
     * @throws JsonMappingException
     */
    public CompletableFuture<String> close()
            throws JsonMappingException, JsonProcessingException, InterruptedException, ExecutionException {
        if (correlationId != null && state != QueryState.RUN_DONE) {
            state = QueryState.RUN_DONE;

            ObjectMapper objectMapper = SingletonObjectMapper.getInstance();
            ObjectNode closeRequest = objectMapper.createObjectNode();
            closeRequest.put("id", SqlJob.getNewUniqueId("sqlclose"));
            closeRequest.put("cont_id", correlationId);
            closeRequest.put("type", "sqlclose");

            return job.send(objectMapper.writeValueAsString(closeRequest));
        } else if (correlationId == null) {
            state = QueryState.RUN_DONE;
            return CompletableFuture.completedFuture(null);
        } else {
            return CompletableFuture.completedFuture(null);
        }
    }

    /**
     * Get the correlation ID of the query.
     * 
     * @return The correlation ID of the query.
     */
    public String getId() {
        return this.correlationId;
    }

    /**
     * Get the current state of the query execution.
     * 
     * @return The current state of the query execution.
     */
    public QueryState getState() {
        return this.state;
    }
}
