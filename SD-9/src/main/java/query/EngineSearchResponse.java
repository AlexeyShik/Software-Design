package query;

import java.util.List;
import java.util.Objects;

import engine.Engine;

public class EngineSearchResponse {

    private Engine engine;
    private List<String> urls;

    public EngineSearchResponse(Engine engine, List<String> urls) {
        this.engine = engine;
        this.urls = urls;
    }

    public Engine getEngine() {
        return engine;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EngineSearchResponse response = (EngineSearchResponse) o;
        return Objects.equals(engine, response.engine) && Objects.equals(urls, response.urls);
    }

    @Override
    public int hashCode() {
        return Objects.hash(engine, urls);
    }
}
