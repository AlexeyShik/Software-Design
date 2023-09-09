package ru.shik.sd.dao;

import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import ru.shik.sd.model.Company;
import ru.shik.sd.model.Stock;

public class MarketDaoImpl extends JdbcDaoSupport implements MarketDao {

    public MarketDaoImpl(DataSource dataSource) {
        super();
        setDataSource(dataSource);
        getJdbcTemplateSafe().execute(
            "CREATE TABLE IF NOT EXISTS COMPANIES ("
                + "id INTEGER PRIMARY KEY AUTO_INCREMENT,"
                + "name VARCHAR(50) NOT NULL"
                + ");");
        getJdbcTemplateSafe().execute(
            "CREATE TABLE IF NOT EXISTS STOCKS ("
                + "id INTEGER PRIMARY KEY,"
                + "name VARCHAR(30) NOT NULL,"
                + "companyName VARCHAR(50) NOT NULL,"
                + "amount BIGINT NOT NULL,"
                + "price DOUBLE PRECISION NOT NULL"
                + ");");
    }

    @Override
    public void addCompany(String name) {
        getJdbcTemplateSafe().update(
            "INSERT INTO COMPANIES(name) VALUES (?)", name
        );
    }

    @Override
    public Optional<Company> getCompany(String name) {
        List<Company> companies =  getJdbcTemplateSafe().query(
            "SELECT * FROM COMPANIES WHERE name = ?",
            new BeanPropertyRowMapper<>(Company.class), name
        );
        return companies.isEmpty() ? Optional.empty() : Optional.of(companies.get(0));
    }

    @Override
    public void addStock(int id, String name, String companyName, long amount, double price) {
        getJdbcTemplateSafe().update(
            "INSERT INTO STOCKS(id, name, companyName, amount, price) VALUES (?1, ?2, ?3, ?4, ?5)",
            id, name, companyName, amount, price
        );
    }

    @Override
    public Optional<Stock> getStock(String name) {
        List<Stock> stocks =  getJdbcTemplateSafe().query(
            "SELECT * FROM STOCKS WHERE name = ?",
            new BeanPropertyRowMapper<>(Stock.class), name
        );
        return stocks.isEmpty() ? Optional.empty() : Optional.of(stocks.get(0));
    }

    @Override
    public Optional<Stock> getStock(int id) {
        List<Stock> stocks =  getJdbcTemplateSafe().query(
            "SELECT * FROM STOCKS WHERE id = ?",
            new BeanPropertyRowMapper<>(Stock.class), id
        );
        return stocks.isEmpty() ? Optional.empty() : Optional.of(stocks.get(0));
    }

    @Override
    public void changeStockPrice(int id, double price) {
        getJdbcTemplateSafe().update(
            "UPDATE STOCKS SET price = ?2 WHERE id = ?1", id, price
        );
    }

    @Override
    public void changeStockAmount(int id, long amount) {
        getJdbcTemplateSafe().update(
            "UPDATE STOCKS SET amount = ?2 WHERE id = ?1", id, amount
        );
    }

    private JdbcTemplate getJdbcTemplateSafe() {
        JdbcTemplate template = getJdbcTemplate();
        if (template == null) {
            throw new RuntimeException("Not found JdbcTemplate");
        }
        return template;
    }
}
