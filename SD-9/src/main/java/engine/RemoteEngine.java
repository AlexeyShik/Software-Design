package engine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;

public class RemoteEngine implements Engine {

    private static final int TIMEOUT = 3000;
    private static final String PREFIX = "http://";
    private static final String COLON = ":";
    private static final String METHOD_URL = "/search";
    private static final String GET = "GET";
    private static final String RESPONSE_KEY = "response";

    private final String name;
    private final String url;
    private final int port;

    public RemoteEngine(String name, String url, int port) {
        this.name = name;
        this.url = url;
        this.port = port;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getUrl() {
        return url;
    }

    public int getPort() {
        return port;
    }

    @Override
    public List<String> search(String query) {
        HttpURLConnection con;
        try {
            con = (HttpURLConnection) new URL(PREFIX + url + COLON + port + METHOD_URL).openConnection();
            con.setRequestMethod(GET);
            con.setReadTimeout(TIMEOUT);
        } catch (IOException e) {
            return Collections.emptyList();
        }

        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            JSONArray json = (JSONArray) ((JSONObject) new JSONParser().parse(content.toString())).get(RESPONSE_KEY);
            return json.stream().map(x -> (String) x).collect(Collectors.toList());
        } catch (IOException | ParseException e) {
            return Collections.emptyList();
        }
    }
}
