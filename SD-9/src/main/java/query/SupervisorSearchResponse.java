package query;

import java.util.List;
import java.util.Map;

import engine.Engine;

public class SupervisorSearchResponse {

    private Map<Engine, List<String>> responseMap;

    public SupervisorSearchResponse(Map<Engine, List<String>> responseMap) {
        this.responseMap = responseMap;
    }

    public Map<Engine, List<String>> getResponseMap() {
        return responseMap;
    }

    public void setResponseMap(Map<Engine, List<String>> responseMap) {
        this.responseMap = responseMap;
    }
}
