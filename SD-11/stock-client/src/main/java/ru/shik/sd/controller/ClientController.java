package ru.shik.sd.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ru.shik.sd.dao.ClientDao;
import ru.shik.sd.http.MarketHttpClient;
import ru.shik.sd.model.Client;
import ru.shik.sd.model.ClientStock;
import ru.shik.sd.model.ClientStockDTO;
import ru.shik.sd.model.StockDTO;

@RestController
@RequestMapping("/client")
public class ClientController {

    private final ClientDao clientDao;
    private final MarketHttpClient marketHttpClient;

    public ClientController(ClientDao clientDao) {
        this.clientDao = clientDao;
        marketHttpClient = new MarketHttpClient();
    }

    @RequestMapping("/add")
    public ResponseEntity<HttpStatus> addClient(@RequestParam int id, @RequestParam String name) {
        Optional<Client> client = clientDao.getClient(id);
        if (client.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        clientDao.addClient(id, name);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    @RequestMapping("/balance/fill")
    public ResponseEntity<HttpStatus> fillBalance(@RequestParam int id, @RequestParam double deposit) {
        Optional<Client> client = clientDao.getClient(id);
        if (client.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        clientDao.setBalance(id, client.get().getBalance() + deposit);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    @RequestMapping("/balance/get")
    public ResponseEntity<Double> getBalance(@RequestParam int id) {
        Optional<Double> balanceOptional = clientDao.getBalance(id);
        if (balanceOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        double balance = balanceOptional.get();
        List<ClientStock> stockIds = clientDao.getStocks(id);
        for (ClientStock clientStock : stockIds) {
            Optional<StockDTO> stock = marketHttpClient.getStock(clientStock.getStockId());
            if (stock.isPresent()) {
                balance += stock.get().getPrice() * clientStock.getAmount();
            }
        }
        return new ResponseEntity<>(balance, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping("/stocks/get")
    public ResponseEntity<List<ClientStockDTO>> getStocks(@RequestParam int id) {
        Optional<Client> client = clientDao.getClient(id);
        if (client.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<ClientStock> clientStocks = clientDao.getStocks(id);
        List<ClientStockDTO> stockDTOS = new ArrayList<>(clientStocks.size());
        for (ClientStock clientStock : clientStocks) {
            Optional<StockDTO> stock = marketHttpClient.getStock(clientStock.getStockId());
            if (stock.isPresent()) {
                stockDTOS.add(new ClientStockDTO(clientStock.getStockId(), stock.get().getName(),
                    stock.get().getCompanyName(), clientStock.getAmount(), stock.get().getPrice()));
            }
        }
        return new ResponseEntity<>(stockDTOS, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping("/stocks/buy")
    public ResponseEntity<HttpStatus> buyStocks(@RequestParam int id, @RequestParam int stockId, @RequestParam long amount) {
        Optional<Client> clientOptional = clientDao.getClient(id);
        if (clientOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Client client = clientOptional.get();

        Optional<StockDTO> stockOptional = marketHttpClient.getStock(stockId);
        if (stockOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        StockDTO stock = stockOptional.get();

        long stocksCount = stock.getAmount();
        if (stocksCount < amount) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        double totalPrice = stock.getPrice() * amount;
        if (client.getBalance() < totalPrice) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Optional<Long> currentAmount = clientDao.getStockAmount(id, stockId);
        if (currentAmount.isEmpty()) {
            clientDao.buyNewStock(id, stockId, amount);
        } else {
            clientDao.setStockAmount(id, stockId, currentAmount.get() + amount);
        }

        clientDao.setBalance(id, client.getBalance() - totalPrice);
        return new ResponseEntity<>(marketHttpClient.buyStock(stockId, amount));
    }

    @Transactional
    @RequestMapping("/stocks/sell")
    public ResponseEntity<HttpStatus> sellStocks(@RequestParam int id, @RequestParam int stockId, @RequestParam long amount) {
        Optional<Client> clientOptional = clientDao.getClient(id);
        if (clientOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Client client = clientOptional.get();

        Optional<StockDTO> stockOptional = marketHttpClient.getStock(stockId);
        if (stockOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        StockDTO stock = stockOptional.get();

        double totalPrice = stock.getPrice() * amount;
        if (client.getBalance() < totalPrice) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Optional<Long> currentAmount = clientDao.getStockAmount(id, stockId);
        if (currentAmount.isEmpty() || currentAmount.get() < amount) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        clientDao.setStockAmount(id, stockId, currentAmount.get() - amount);
        clientDao.setBalance(id, client.getBalance() + totalPrice);
        return new ResponseEntity<>(marketHttpClient.sellStock(stockId, amount));
    }
}
