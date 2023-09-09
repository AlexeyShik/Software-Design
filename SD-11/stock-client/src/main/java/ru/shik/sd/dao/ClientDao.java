package ru.shik.sd.dao;

import java.util.List;
import java.util.Optional;

import ru.shik.sd.model.Client;
import ru.shik.sd.model.ClientStock;

public interface ClientDao {

    void addClient(int id, String name);

    Optional<Client> getClient(int id);

    void setBalance(int id, double balance);

    Optional<Double> getBalance(int id);

    List<ClientStock> getStocks(int id);

    Optional<Long> getStockAmount(int id, int stockId);

    void buyNewStock(int id, int stockId, long amount);

    void setStockAmount(int id, int stockId, long amount);

}
