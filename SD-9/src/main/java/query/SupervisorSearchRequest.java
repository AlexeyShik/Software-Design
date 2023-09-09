package query;

import config.SearchConfig;

public class SupervisorSearchRequest {

    private String query;
    private SearchConfig config;

    public SupervisorSearchRequest(String query, SearchConfig config) {
        this.query = query;
        this.config = config;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public SearchConfig getConfig() {
        return config;
    }

    public void setConfig(SearchConfig config) {
        this.config = config;
    }
}
