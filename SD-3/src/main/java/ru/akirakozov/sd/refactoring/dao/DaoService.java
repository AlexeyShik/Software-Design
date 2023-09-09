package ru.akirakozov.sd.refactoring.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.logging.Logger;

public class DaoService implements IDaoService {

    private static final Logger LOG = Logger.getLogger(DaoService.class.getSimpleName());

    private static final String DATABASE_CONNECTION_URL = "jdbc:sqlite:test.db";
    private static final String PRODUCT_NAME = "name";
    private static final String PRODUCT_PRICE = "price";

    private static final String GET_DATABASE_QUERY = "SELECT * FROM PRODUCT";
    private static final String MAX_DATABASE_QUERY = "SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1";
    private static final String MIN_DATABASE_QUERY = "SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1";
    private static final String SUM_DATABASE_QUERY = "SELECT SUM(price) FROM PRODUCT";
    private static final String COUNT_DATABASE_QUERY = "SELECT COUNT(*) FROM PRODUCT";

    private static final Function<ResultSet, Optional<ProductEntry>> GET_FIRST_ENTRY = rs -> {
        try {
            if (rs.next()) {
                return Optional.of(new ProductEntry(rs.getString(PRODUCT_NAME), rs.getInt(PRODUCT_PRICE)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    };

    private static final Function<ResultSet, Optional<Integer>> GET_FIRST_INT = rs -> {
        try {
            return Optional.of(rs.getInt(1));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    };

    @Override
    public void addProduct(ProductEntry entry) {
        processQuery( "INSERT INTO PRODUCT (NAME, PRICE) VALUES (\""
            + entry.getName() + "\"," + entry.getPrice() + ")", rc -> Optional.empty());
    }

    @Override
    public List<ProductEntry> getProducts() {
        return processQuery(GET_DATABASE_QUERY, rs -> {
            List<ProductEntry> products = new ArrayList<>();
            try {
                while (rs.next()) {
                    products.add(new ProductEntry(rs.getString(PRODUCT_NAME), rs.getInt(PRODUCT_PRICE)));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return Optional.of(products);
        }).orElseGet(ArrayList::new);
    }

    @Override
    public Optional<ProductEntry> getMaxProduct() {
        return processQuery(MAX_DATABASE_QUERY, GET_FIRST_ENTRY);
    }

    @Override
    public Optional<ProductEntry> getMinProduct() {
        return processQuery(MIN_DATABASE_QUERY, GET_FIRST_ENTRY);
    }

    @Override
    public Optional<Integer> getPricesSum() {
        return processQuery(SUM_DATABASE_QUERY, GET_FIRST_INT);
    }

    @Override
    public Optional<Integer> getProductsCount() {
        return processQuery(COUNT_DATABASE_QUERY, GET_FIRST_INT);
    }

    private <T> Optional<T> processQuery(String sqlQuery, Function<ResultSet, Optional<T>> func) {
        Optional<T> result = Optional.empty();

        try (Connection c = DriverManager.getConnection(DATABASE_CONNECTION_URL)) {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery(sqlQuery);

            result = func.apply(rs);

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            LOG.throwing(DaoService.class.getSimpleName(), "Exception while executing SQL query", e);
        }

        return result;
    }

}
