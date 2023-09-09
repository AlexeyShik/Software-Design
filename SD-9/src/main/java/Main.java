import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import config.MockSearchConfig;
import config.SearchConfig;
import engine.Engine;
import query.SearchRequest;
import query.SearchResponse;
import service.SearchService;

public class Main {

    private static final String CONFIG_PATH = "src/main/resources/mock_config.yaml";

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        if (args == null || args.length != 1 || args[0] == null) {
            System.err.println("Bad arguments, should be: <search request string>");
            return;
        }
        Yaml yamlReader = new Yaml(new Constructor(MockSearchConfig.class));
        SearchConfig config;
        try {
            InputStream is = new FileInputStream(CONFIG_PATH);
            config = yamlReader.load(is);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        SearchService searchService = new SearchService(config);
        SearchResponse response = searchService.search(new SearchRequest(args[0])).get();
        System.out.println("Found responses from " + response.getResponseMap().size() + " engines");
        for (Map.Entry<Engine, List<String>> entry : response.getResponseMap().entrySet()) {
            System.out.print("For engine " + entry.getKey().getName() + ": ");
            StringBuilder builder = new StringBuilder();
            for (String url : entry.getValue()) {
                if (!builder.isEmpty()) {
                    builder.append(", ");
                }
                builder.append(url);
            }
            System.out.println(builder);
        }
    }
}
