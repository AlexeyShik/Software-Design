package query;

import engine.Engine;

public class EngineSearchRequest {

    private String query;
    private Engine engine;

    public EngineSearchRequest(String query, Engine engine) {
        this.query = query;
        this.engine = engine;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Engine getEngine() {
        return engine;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }
}
