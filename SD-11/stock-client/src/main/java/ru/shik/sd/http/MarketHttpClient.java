package ru.shik.sd.http;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import ru.shik.sd.model.StockDTO;

public class MarketHttpClient {

    private static final int MARKET_PORT = 8083;

    private final HttpClient httpClient;

    public MarketHttpClient() {
        httpClient = HttpClient.newHttpClient();
    }

    public Optional<StockDTO> getStock(int stockId) {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port(MARKET_PORT)
                .path("market/stock/get")
                .queryParam("id", stockId)
                .build()
                .toUri())
            .GET()
            .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            return response.statusCode() == HttpStatus.OK.value()
                ? Optional.of(new ObjectMapper().readValue(response.body(), StockDTO.class)) : Optional.empty();
        } catch (IOException | InterruptedException e) {
            return Optional.empty();
        }
    }

    public HttpStatus buyStock(int stockId, long amount) {
        return modifyStockAmount(stockId, amount, "buy");
    }

    public HttpStatus sellStock(int stockId, long amount) {
        return modifyStockAmount(stockId, amount, "sell");
    }

    private HttpStatus modifyStockAmount(int stockId, long amount, String method) {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port(MARKET_PORT)
                .path("market/stock/" + method)
                .queryParam("id", stockId)
                .queryParam("amount", amount)
                .build()
                .toUri())
            .GET()
            .build();
        try {
            return HttpStatus.valueOf(
                httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8)).statusCode()
            );
        } catch (IOException | InterruptedException e) {
            return HttpStatus.BAD_GATEWAY;
        }
    }
}
