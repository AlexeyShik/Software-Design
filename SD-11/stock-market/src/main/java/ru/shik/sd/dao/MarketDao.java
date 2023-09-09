package ru.shik.sd.dao;

import java.util.Optional;

import ru.shik.sd.model.Company;
import ru.shik.sd.model.Stock;

public interface MarketDao {

    void addCompany(String name);

    Optional<Company> getCompany(String name);

    void addStock(int id, String name, String companyName, long amount, double price);

    Optional<Stock> getStock(String name);

    Optional<Stock> getStock(int id);

    void changeStockPrice(int id, double price);

    void changeStockAmount(int id, long amount);

}
