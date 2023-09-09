package ru.akirakozov.sd.refactoring;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.akirakozov.sd.refactoring.servlet.AddProductServlet;
import ru.akirakozov.sd.refactoring.servlet.GetProductsServlet;
import ru.akirakozov.sd.refactoring.servlet.QueryServlet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Main {

    private static final int DEFAULT_PORT = 8081;
    private static final String ROOT = "/";
    private static final String DATABASE_CONNECTION_URL = "jdbc:sqlite:test.db";
    private static final String CREATE_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS PRODUCT" +
        "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
        " NAME           TEXT    NOT NULL, " +
        " PRICE          INT     NOT NULL)";

    private static final String GET_MAPPING = "/get-products";
    private static final String ADD_MAPPING =  "/add-product";
    private static final String QUERY_MAPPING = "/query";

    public static void main(String[] args) throws Exception {
        try (Connection c = DriverManager.getConnection(DATABASE_CONNECTION_URL)) {
            Statement stmt = c.createStatement();
            stmt.executeUpdate(CREATE_TABLE_QUERY);
            stmt.close();
        }

        Server server = new Server(DEFAULT_PORT);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath(ROOT);
        server.setHandler(context);

        context.addServlet(new ServletHolder(new AddProductServlet()), ADD_MAPPING);
        context.addServlet(new ServletHolder(new GetProductsServlet()), GET_MAPPING);
        context.addServlet(new ServletHolder(new QueryServlet()), QUERY_MAPPING);

        server.start();
        server.join();
    }
}
