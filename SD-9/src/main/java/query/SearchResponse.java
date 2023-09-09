package query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import engine.Engine;

public class SearchResponse {

    private Map<Engine, List<String>> responseMap;

    public SearchResponse() {
        responseMap = new HashMap<>();
    }

    public SearchResponse(Map<Engine, List<String>> responseMap) {
        this.responseMap = responseMap;
    }

    public Map<Engine, List<String>> getResponseMap() {
        return responseMap;
    }

    public void setResponseMap(Map<Engine, List<String>> responseMap) {
        this.responseMap = responseMap;
    }
}
