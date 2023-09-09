import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ru.akirakozov.sd.refactoring.servlet.AddProductServlet;
import ru.akirakozov.sd.refactoring.servlet.GetProductsServlet;
import ru.akirakozov.sd.refactoring.servlet.QueryServlet;

public class ProductServiceTest {

    private static final String SERVER_ADDRESS = "http://localhost:8081";
    private static final String DATABASE_CONNECTION_URL = "jdbc:sqlite:test.db";
    private static final int DEFAULT_PORT = 8081;

    private static final String GET_RESPONSE_MESSAGE = "OK";
    private static final String METHOD_GET = "GET";
    private static final String METHOD_POST = "POST";

    private static final String ROOT = "/";
    private static final String GET_MAPPING = "/get-products";
    private static final String ADD_MAPPING =  "/add-product";
    private static final String QUERY_MAPPING = "/query";
    private static final String INVALID_MAPPING = "/abacaba";

    private static final String SUM_COMMAND = "?command=sum";
    private static final String COUNT_COMMAND = "?command=count";
    private static final String MIN_COMMAND = "?command=min";
    private static final String MAX_COMMAND = "?command=max";

    private static final String BODY_BEGIN = "<body>";
    private static final String BODY_END = "</body>";

    private Server server;

    @Before
    public void startServer() throws Exception {
        try (Connection c = DriverManager.getConnection(DATABASE_CONNECTION_URL)) {
            String sql = "DROP TABLE IF EXISTS PRODUCT;";
            processSqlQuery(c, sql);

            sql = "CREATE TABLE IF NOT EXISTS PRODUCT" +
                "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                " NAME           TEXT    NOT NULL, " +
                " PRICE          INT     NOT NULL)";
            processSqlQuery(c, sql);
        }

        server = new Server(DEFAULT_PORT);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath(ROOT);
        server.setHandler(context);

        context.addServlet(new ServletHolder(new AddProductServlet()), ADD_MAPPING);
        context.addServlet(new ServletHolder(new GetProductsServlet()), GET_MAPPING);
        context.addServlet(new ServletHolder(new QueryServlet()), QUERY_MAPPING);

        server.start();
    }

    @After
    public void stopServer() throws Exception {
        server.stop();
    }

    @Test
    public void testEmptyGet() throws IOException {
        HttpURLConnection conn = getConnection(GET_MAPPING);
        conn.disconnect();
    }

    @Test
    public void testPostAndGet() throws IOException {
        HttpURLConnection conn = getConnection(ADD_MAPPING + "?name=book1&price=100");
        Assert.assertEquals("OK\n", readFromConnection(conn));
        conn.disconnect();

        conn = getConnection(ADD_MAPPING + "?name=book2&price=200");
        Assert.assertEquals("OK\n", readFromConnection(conn));
        conn.disconnect();

        conn = getConnection(GET_MAPPING);
        Assert.assertEquals("\nbook1\t100</br>\nbook2\t200</br>\n", getHtmlBody(conn));
        conn.disconnect();
    }

    @Test
    public void testQueries() throws IOException {
        HttpURLConnection conn = getConnection(ADD_MAPPING + "?name=book1&price=100");
        Assert.assertEquals("OK\n", readFromConnection(conn));
        conn.disconnect();

        conn = getConnection(ADD_MAPPING + "?name=book2&price=200");
        Assert.assertEquals("OK\n", readFromConnection(conn));
        conn.disconnect();

        conn = getConnection(QUERY_MAPPING + SUM_COMMAND);
        Assert.assertEquals("\nSummary price: \n300\n", getHtmlBody(conn));
        conn.disconnect();

        conn = getConnection(QUERY_MAPPING + COUNT_COMMAND);
        Assert.assertEquals("\nNumber of products: \n2\n", getHtmlBody(conn));
        conn.disconnect();

        conn = getConnection(QUERY_MAPPING + MIN_COMMAND);
        Assert.assertEquals("\n<h1>Product with min price: </h1>\nbook1\t100</br>\n", getHtmlBody(conn));
        conn.disconnect();

        conn = getConnection(QUERY_MAPPING + MAX_COMMAND);
        Assert.assertEquals("\n<h1>Product with max price: </h1>\nbook2\t200</br>\n", getHtmlBody(conn));
        conn.disconnect();
    }

    @Test
    public void testInvalidUrl() throws IOException {
        URL url = new URL(SERVER_ADDRESS + INVALID_MAPPING);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod(METHOD_GET);
        conn.connect();
        Assert.assertEquals(HttpServletResponse.SC_NOT_FOUND, conn.getResponseCode());
        conn.disconnect();
    }

    @Test
    public void testWrongRequestMethod() throws IOException {
        URL url = new URL(SERVER_ADDRESS + GET_MAPPING);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod(METHOD_POST);
        conn.connect();
        Assert.assertEquals(HttpServletResponse.SC_METHOD_NOT_ALLOWED, conn.getResponseCode());
        conn.disconnect();
    }


    private String getHtmlBody(HttpURLConnection conn) throws IOException {
        String data = readFromConnection(conn);
        return data.substring(data.indexOf(BODY_BEGIN) + 6, data.lastIndexOf(BODY_END));
    }

    private String readFromConnection(HttpURLConnection conn) throws IOException {
        byte[] bodyBytes = new byte[conn.getContentLength()];
        int offset = 0;
        int curr;
        do {
            curr = conn.getInputStream().read(bodyBytes, offset, bodyBytes.length - offset);
            offset += curr;
        } while (curr > 0);
        return new String(bodyBytes, StandardCharsets.UTF_8);
    }

    private HttpURLConnection getConnection(String path) throws IOException {
        URL url = new URL(SERVER_ADDRESS + path);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod(METHOD_GET);
        conn.setDoOutput(true);
        conn.connect();
        Assert.assertEquals(HttpURLConnection.HTTP_OK, conn.getResponseCode());
        Assert.assertEquals(GET_RESPONSE_MESSAGE, conn.getResponseMessage());
        return conn;
    }

    private void processSqlQuery(Connection c, String sql) throws SQLException {
        Statement stmt = c.createStatement();
        stmt.executeUpdate(sql);
        stmt.close();
    }
}
