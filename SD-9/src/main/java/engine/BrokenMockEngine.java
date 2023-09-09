package engine;

import java.util.List;
import java.util.Map;

public class BrokenMockEngine extends MockEngine {

    public BrokenMockEngine(String name, String url, Map<String, List<String>> cache) {
        super(name, url, cache);
    }

    @Override
    public List<String> search(String query) {
        try {
            Thread.sleep(10_000);
        } catch (InterruptedException ignored) {
        }
        return List.of();
    }
}
