package io.github.mapapire;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.json.JSONObject;

import io.github.mapapire.types.QueryOptions;
import io.github.mapapire.types.QueryState;

public class Query<T> {
    private SqlJob job;
    private static List<Query<Object>> globalQueryList = new ArrayList<>();
    private String correlationId;
    private String sql;
    private boolean isPrepared = false;
    private List<Object> parameters;
    private int rowsToFetch = 100;
    private boolean isCLCommand;
    private QueryState state = QueryState.NOT_YET_RUN;
    private boolean isTerseResults;

    public Query(SqlJob _job, String _query, QueryOptions _opts) {
        this.job = _job;
        this.sql = _query;

        // TODO: Fix constructor
        this.isPrepared = _opts.getParameters() != null;
        this.parameters = _opts.getParameters();
        this.isCLCommand = _opts.getIsClCommand();
        this.isTerseResults = _opts.getIsTerseResults();

        Query.globalQueryList.add(this);
    }

    public static Query<Object> byId(String id) {
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

    public void execute() {
        //TODO: 
    }

    public void fetchMore() {
        //TODO: 
    }

    public CompletableFuture<String> close() {
        if (correlationId != null && state != QueryState.RUN_DONE) {
            state = QueryState.RUN_DONE;
            JSONObject queryObject = new JSONObject();
            queryObject.put("id", SqlJob.getNewUniqueId("sqlclose"));
            queryObject.put("cont_id", correlationId);
            queryObject.put("type", "sqlclose");

            return job.send(queryObject.toString());
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
