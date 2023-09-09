import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import javax.sql.DataSource;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.UriComponentsBuilder;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.images.PullPolicy;

import ru.shik.sd.controller.ClientController;
import ru.shik.sd.dao.ClientDaoImpl;

public class ClientTest {

    private static final int CLIENT_PORT = 8080;
    private static final int MARKET_PORT = 8083;

    private static PostgreSQLContainer<?> stockMarketPostgresContainer;
    private static GenericContainer<?> stockMarketContainer;
    private static PostgreSQLContainer<?> stockClientPostgresContainer;
    private static MockMvc mockMvc;

    @BeforeClass
    public static void setUp() {
        Network network = Network.newNetwork();

        DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build();
        mockMvc = MockMvcBuilders.standaloneSetup(new ClientController(new ClientDaoImpl(dataSource))).build();

        stockMarketPostgresContainer = new PostgreSQLContainer<>("postgres")
            .withNetwork(network)
            .withExposedPorts(5432)
            .withNetworkAliases("stock-market-db")
            .withDatabaseName("stock-market-db")
            .withUsername("stock-market-db")
            .withPassword("stock-market-db");
        stockMarketPostgresContainer.start();

        stockMarketContainer = new FixedHostPortGenericContainer<>("stock-market:1.0-SNAPSHOT")
            .withFixedExposedPort(MARKET_PORT, MARKET_PORT)
            .withImagePullPolicy(PullPolicy.defaultPolicy())
            .withEnv(Map.of(
                "PORT", String.valueOf(MARKET_PORT),
                "DATASOURCE_URL", "jdbc:postgresql://stock-market-db:5432/stock-market?loggerLevel=OFF",
                "DATASOURCE_USERNAME", stockMarketPostgresContainer.getUsername(),
                "DATASOURCE_PASSWORD", stockMarketPostgresContainer.getPassword()
            ))
            .withNetwork(network)
            .withExposedPorts(MARKET_PORT)
            .withNetworkAliases("stock-market")
            .withLogConsumer(new Slf4jLogConsumer(LoggerFactory.getLogger(ClientTest.class)));
        stockMarketContainer.start();

        stockClientPostgresContainer = new PostgreSQLContainer<>("postgres")
            .withNetwork(network)
            .withNetworkAliases("stock-client-db")
            .withExposedPorts(5432)
            .withDatabaseName("stock-client-db")
            .withUsername("stock-client-db")
            .withPassword("stock-client-db");
        stockClientPostgresContainer.start();

        System.setProperty("PORT", String.valueOf(CLIENT_PORT));
        System.setProperty(
            "DATASOURCE_URL",
            "jdbc:postgresql://localhost:" + stockClientPostgresContainer.getMappedPort(5432) + "/stock-client"
        );
        System.setProperty("DATASOURCE_USERNAME", stockClientPostgresContainer.getUsername());
        System.setProperty("DATASOURCE_PASSWORD", stockClientPostgresContainer.getPassword());
        System.setProperty("STOCK_MARKET_HOST", stockMarketContainer.getHost());
        System.setProperty("STOCK_MARKET_PORT", stockMarketContainer.getMappedPort(MARKET_PORT).toString());
    }

    @AfterClass
    public static void tearDown() {
        close(stockMarketContainer);
        close(stockMarketPostgresContainer);
        close(stockClientPostgresContainer);
    }

    @Test
    public void testCreateClient() throws Exception {
        int clientId = 1;
        createClient(clientId);

        mockMvc.perform(MockMvcRequestBuilders
            .post("/client/add")
            .param("id", String.valueOf(clientId))
            .param("name", "Andrey")
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testFillBalance() throws Exception {
        int clientId = 2;
        createClient(clientId);
        fillBalance(clientId, 100000.0);

        mockMvc.perform(MockMvcRequestBuilders
            .post("/client/balance/get")
            .param("id", String.valueOf(clientId))
        ).andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().string("100000.0"));;
    }

    @Test
    public void testBuyStock() throws Exception {
        int clientId = 3;
        int stockId = 1;
        createClient(clientId);
        createStock(stockId,"stock1", "company1", 100L, 5.0);
        fillBalance(clientId, 100000.0);
        buyStock(clientId, stockId, 10L);

        mockMvc.perform(MockMvcRequestBuilders
            .post("/client/stocks/get")
            .param("id", String.valueOf(clientId))
        ).andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().json(
                Files.readString(Path.of("src/test/resources/test3.json"))
            ));
    }

    @Test
    public void testSellStock() throws Exception {
        int clientId = 4;
        int stockId = 2;
        createClient(clientId);
        createStock(stockId,"stock2", "company2", 100L, 5.0);
        fillBalance(clientId, 100000.0);
        buyStock(clientId, stockId, 10L);
        sellStock(clientId, stockId, 5L);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/client/stocks/get")
                .param("id", String.valueOf(clientId))
            ).andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().json(
                Files.readString(Path.of("src/test/resources/test4.json"))
            ));
    }

    @Test
    public void testChangePrice() throws Exception {
        int clientId = 5;
        int stockId = 3;
        createClient(clientId);
        createStock(stockId,"stock3", "company3", 100L, 5.0);
        fillBalance(clientId, 100000.0);
        buyStock(clientId, stockId, 10L);
        checkBalance(clientId, "100000.0");
        changePrice(stockId, 6);
        checkBalance(clientId, "100010.0");
    }

    @Test
    public void testUnknownClient() throws Exception {
        int clientId = 6;
        mockMvc.perform(MockMvcRequestBuilders
            .post("/client/balance/fill")
            .param("id", String.valueOf(clientId))
            .param("deposit", String.valueOf(100.0))
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testUnknownStock() throws Exception {
        int clientId = 7;
        createClient(clientId);
        fillBalance(clientId, 100000.0);

        mockMvc.perform(MockMvcRequestBuilders
            .post("/client/stocks/buy")
            .param("id", String.valueOf(clientId))
            .param("stockId", String.valueOf(12345678))
            .param("amount", String.valueOf(10))
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testNotEnoughMoney() throws Exception {
        int clientId = 8;
        int stockId = 4;
        createClient(clientId);
        createStock(stockId,"stock4", "company4", 100L, 6.0);
        fillBalance(clientId, 10.0);

        mockMvc.perform(MockMvcRequestBuilders
            .post("/client/stocks/buy")
            .param("id", String.valueOf(clientId))
            .param("stockId", String.valueOf(stockId))
            .param("amount", String.valueOf(2))
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testExistingClient() throws Exception {
        int clientId = 9;
        createClient(clientId);

        mockMvc.perform(MockMvcRequestBuilders
            .post("/client/add")
            .param("id", String.valueOf(clientId))
            .param("name", "Alexey")
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testManyStocks() throws Exception {
        int clientId = 10;
        createClient(clientId);
        fillBalance(clientId, 100000.0);

        createStock(5,"stock5", "company5", 100L, 300.0);
        buyStock(clientId, 5, 2L);

        createStock(6,"stock6", "company6", 100L, 20.0);
        buyStock(clientId, 6, 3L);

        createStock(7,"stock7", "company7", 100L, 10.0);
        buyStock(clientId, 7, 10L);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/client/stocks/get")
                .param("id", String.valueOf(clientId))
            ).andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().json(
                Files.readString(Path.of("src/test/resources/test10.json"))
            ));
    }

    private void createClient(int id) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
            .post("/client/add")
            .param("id", String.valueOf(id))
            .param("name", "Alexey")
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    private void createStock(int id, String name, String companyName, long amount, double price) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(UriComponentsBuilder.newInstance()
                .scheme("http")
                .host(stockMarketContainer.getHost())
                .port(stockMarketContainer.getMappedPort(MARKET_PORT))
                .path("/market/company/add")
                .queryParam("name", companyName)
                .build()
                .toUri()
            )
            .POST(HttpRequest.BodyPublishers.noBody())
            .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        Assert.assertEquals(HttpStatus.OK.value(), response.statusCode());

        request = HttpRequest.newBuilder()
            .uri(UriComponentsBuilder.newInstance()
                .scheme("http")
                .host(stockMarketContainer.getHost())
                .port(stockMarketContainer.getMappedPort(MARKET_PORT))
                .path("/market/stock/add")
                .queryParam("id", id)
                .queryParam("name", name)
                .queryParam("companyName", companyName)
                .queryParam("amount", amount)
                .queryParam("price", price)
                .build()
                .toUri()
            )
            .POST(HttpRequest.BodyPublishers.noBody())
            .build();
        response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        Assert.assertEquals(HttpStatus.OK.value(), response.statusCode());
    }

    private void fillBalance(int id, double deposit) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
            .post("/client/balance/fill")
            .param("id", String.valueOf(id))
            .param("deposit", String.valueOf(deposit))
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    private void buyStock(int clientId, int stockId, long amount) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
            .post("/client/stocks/buy")
            .param("id", String.valueOf(clientId))
            .param("stockId", String.valueOf(stockId))
            .param("amount", String.valueOf(amount))
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    private void sellStock(int clientId, int stockId, long amount) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
            .post("/client/stocks/sell")
            .param("id", String.valueOf(clientId))
            .param("stockId", String.valueOf(stockId))
            .param("amount", String.valueOf(amount))
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    private void checkBalance(int id, String expected) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/client/balance/get")
                .param("id", String.valueOf(id))
            ).andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().string(expected));
    }

    private void changePrice(int id, double price) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(UriComponentsBuilder.newInstance()
                .scheme("http")
                .host(stockMarketContainer.getHost())
                .port(stockMarketContainer.getMappedPort(MARKET_PORT))
                .path("/market/stock/admin/price")
                .queryParam("id", id)
                .queryParam("price", price)
                .build()
                .toUri()
            )
            .POST(HttpRequest.BodyPublishers.noBody())
            .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        Assert.assertEquals(HttpStatus.OK.value(), response.statusCode());
    }

    private static void close(AutoCloseable autoCloseable) {
        try {
            if (autoCloseable != null) {
                autoCloseable.close();
            }
        } catch (Exception ignored) {
        }
    }
}
