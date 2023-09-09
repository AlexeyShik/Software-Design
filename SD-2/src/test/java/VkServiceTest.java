import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.function.Consumer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glassfish.grizzly.http.Method;
import org.junit.Assert;
import org.junit.Test;

import com.xebialabs.restito.builder.stub.StubHttp;
import com.xebialabs.restito.semantics.Action;
import com.xebialabs.restito.semantics.Condition;
import com.xebialabs.restito.server.StubServer;

public class VkServiceTest {

    private static final String NEWSFEED_SEARCH = "/newsfeed.search";
    private static final String MOCK_BODY = "POSTS";
    private static final Log LOG = LogFactory.getLog(VkServiceTest.class);
    private static final int PORT = 32453;

    @Test
    public void testStubServerCorrectQuery() {
        withStubServer(s -> {
            StubHttp.whenHttp(s)
                .match(Condition.method(Method.POST), Condition.startsWithUri(NEWSFEED_SEARCH), Condition.parameter("q", "sport"),
                    Condition.parameter("startTime", "123456"), Condition.parameter("endTime", "234567"))
                .then(Action.stringContent(MOCK_BODY));

            try {
                URL url = new URL("http://localhost:" + PORT + NEWSFEED_SEARCH + "?q=sport&startTime=123456&endTime=234567");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                int status = con.getResponseCode();
                Assert.assertEquals(HttpURLConnection.HTTP_OK, status);

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
                Assert.assertEquals(MOCK_BODY, content.toString());

                con.disconnect();
            } catch (IOException e) {
                LOG.error("Cannot execute stub server query ", e);
            }
        });
    }

    @Test
    public void testStubServerInvalidQuery() {
        withStubServer(s -> {
            StubHttp.whenHttp(s)
                .match(Condition.method(Method.POST), Condition.startsWithUri(NEWSFEED_SEARCH), Condition.parameter("q", "sport"),
                    Condition.parameter("startTime", "123456"), Condition.parameter("endTime", "234567"))
                .then(Action.stringContent(MOCK_BODY));

            try {
                URL url = new URL("http://localhost:" + PORT + NEWSFEED_SEARCH);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                int status = con.getResponseCode();
                Assert.assertEquals(HttpURLConnection.HTTP_NOT_FOUND, status);
            } catch (IOException e) {
                LOG.error("Cannot execute stub server query ", e);
            }
        });
    }

    private void withStubServer(Consumer<StubServer> callback) {
        StubServer stubServer = null;
        try {
            stubServer = new StubServer(PORT).run();
            callback.accept(stubServer);
        } finally {
            if (stubServer != null) {
                stubServer.stop();
            }
        }
    }
}
