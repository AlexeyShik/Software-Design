package engine;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MockEngine implements Engine {

    private String name;
    private String url;
    private Map<String, List<String>> cache;

    public MockEngine() {
        super();
    }

    public MockEngine(String name, String url, Map<String, List<String>> cache) {
        this.name = name;
        this.url = url;
        this.cache = cache;
    }

    @Override
    public List<String> search(String query) {
        return cache.getOrDefault(query, Collections.emptyList());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, List<String>> getCache() {
        return cache;
    }

    public void setCache(Map<String, List<String>> cache) {
        this.cache = cache;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MockEngine engine = (MockEngine) o;
        return Objects.equals(name, engine.name) && Objects.equals(url, engine.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, url);
    }
}
