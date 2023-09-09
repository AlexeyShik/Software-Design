package ru.shik.sd.dao;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import ru.shik.sd.model.Client;
import ru.shik.sd.model.ClientStock;

public class ClientDaoImpl extends JdbcDaoSupport implements ClientDao {

    public ClientDaoImpl(DataSource dataSource) {
        super();
        setDataSource(dataSource);
        getJdbcTemplateSafe().execute(
            "CREATE TABLE IF NOT EXISTS Clients("
            + "id INTEGER NOT NULL PRIMARY KEY,"
            + "name VARCHAR(50) NOT NULL,"
            + "balance DOUBLE PRECISION NOT NULL"
            + ");"
        );
        getJdbcTemplateSafe().execute(
            "CREATE TABLE IF NOT EXISTS ClientStocks("
            + "id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,"
            + "clientId INTEGER NOT NULL,"
            + "stockId INTEGER NOT NULL,"
            + "amount BIGINT NOT NULL,"
            + "UNIQUE (clientId, stockId)"
            + ");"
        );
    }

    @Override
    public void addClient(int id, String name) {
        getJdbcTemplateSafe().update(
            "INSERT INTO Clients(id, name, balance) VALUES (?1, ?2, 0.0)", id, name
        );
    }

    @Override
    public Optional<Client> getClient(int id) {
        List<Client> clients = getJdbcTemplateSafe().query(
            "SELECT * FROM Clients WHERE id = ?",
            new BeanPropertyRowMapper<>(Client.class), id
        );
        return clients.isEmpty() ? Optional.empty() : Optional.of(clients.get(0));
    }

    @Override
    public void setBalance(int id, double balance) {
        getJdbcTemplateSafe().update("UPDATE Clients SET balance = ?2 WHERE id = ?1", id, balance);
    }

    @Override
    public Optional<Double> getBalance(int id) {
        List<Client> clients = getJdbcTemplateSafe().query(
            "SELECT * FROM Clients WHERE id = ?",
            new BeanPropertyRowMapper<>(Client.class), id
        );
        return clients.isEmpty() ? Optional.empty() : Optional.of(clients.get(0).getBalance());
    }

    @Override
    public List<ClientStock> getStocks(int id) {
        return getJdbcTemplateSafe().query(
            "SELECT * FROM ClientStocks WHERE clientId = ?",
            new BeanPropertyRowMapper<>(ClientStock.class), id
        );
    }

    @Override
    public Optional<Long> getStockAmount(int id, int stockId) {
        List<ClientStock> clientStocks = getJdbcTemplateSafe().query(
            "SELECT * FROM ClientStocks WHERE clientId = ?1 AND stockId = ?2",
            new BeanPropertyRowMapper<>(ClientStock.class), id, stockId
        );
        return clientStocks.isEmpty() ? Optional.empty() : Optional.of(clientStocks.get(0).getAmount());
    }

    @Override
    public void buyNewStock(int id, int stockId, long amount) {
        getJdbcTemplateSafe().update(
            "INSERT INTO ClientStocks(clientId, stockId, amount) VALUES (?1, ?2, ?3)",
            id, stockId, amount
        );
    }

    @Override
    public void setStockAmount(int id, int stockId, long amount) {
        getJdbcTemplateSafe().update(
            "UPDATE ClientStocks SET amount = ?3 WHERE clientId = ?1 AND stockId = ?2",
            id, stockId, amount
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
