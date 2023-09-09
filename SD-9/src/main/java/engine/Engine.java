package engine;

import java.util.List;

public interface Engine {

    String getName();

    String getUrl();

    List<String> search(String query);
}
