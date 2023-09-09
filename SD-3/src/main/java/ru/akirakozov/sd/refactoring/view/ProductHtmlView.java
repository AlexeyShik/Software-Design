package ru.akirakozov.sd.refactoring.view;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import ru.akirakozov.sd.refactoring.dao.ProductEntry;

public class ProductHtmlView implements ProductView {

    private static final String OK = "OK";
    private static final String LINE_BREAK = "</br>";
    private static final String TAB = "\t";

    private static final String UNKNOWN_COMMAND = "Unknown command: ";
    private static final String MAX_PRICE_HEADER = "<h1>Product with max price: </h1>";
    private static final String MIN_PRICE_HEADER = "<h1>Product with min price: </h1>";
    private static final String PRICES_SUM_HEADER = "Summary price: ";
    private static final String PRODUCTS_COUNT_HEADER = "Number of products: ";
    private static final String TEXT_HEADER = "text/html";
    private static final String BODY_BEGIN = "<html><body>";
    private static final String BODY_END = "</body></html>";

    @Override
    public void writeSuccessAdd(HttpServletResponse response) throws IOException {
        writeContentType(response);
        response.getWriter().println(OK);
    }

    @Override
    public void writeAllProducts(HttpServletResponse response, List<ProductEntry> products) throws IOException {
        writeBodyBegin(response);
        for (ProductEntry entry : products) {
            response.getWriter().println(entry.getName() + TAB + entry.getPrice()+ LINE_BREAK);
        }
        writeBodyEnd(response);
        writeContentType(response);
    }

    @Override
    public void writeMaxProduct(HttpServletResponse response, ProductEntry product) throws IOException {
        writeBodyBegin(response);
        response.getWriter().println(MAX_PRICE_HEADER);
        response.getWriter().println(product.getName() + TAB + product.getPrice() + LINE_BREAK);
        writeBodyEnd(response);
        writeContentType(response);
    }

    @Override
    public void writeMinProduct(HttpServletResponse response, ProductEntry product) throws IOException {
        writeBodyBegin(response);
        response.getWriter().println(MIN_PRICE_HEADER);
        response.getWriter().println(product.getName() + TAB + product.getPrice() + LINE_BREAK);
        writeBodyEnd(response);
        writeContentType(response);
    }

    @Override
    public void writePricesSum(HttpServletResponse response, int pricesSum) throws IOException {
        writeBodyBegin(response);
        response.getWriter().println(PRICES_SUM_HEADER);
        response.getWriter().println(pricesSum);
        writeBodyEnd(response);
        writeContentType(response);
    }

    @Override
    public void writeProductsCount(HttpServletResponse response, int productsCount) throws IOException {
        writeBodyBegin(response);
        response.getWriter().println(PRODUCTS_COUNT_HEADER);
        response.getWriter().println(productsCount);
        writeBodyEnd(response);
        writeContentType(response);
    }

    @Override
    public void writeEmptyMaxProduct(HttpServletResponse response) throws IOException {
        writeBodyBegin(response);
        response.getWriter().println(MAX_PRICE_HEADER);
        writeBodyEnd(response);
        writeContentType(response);
    }

    @Override
    public void writeEmptyMinProduct(HttpServletResponse response) throws IOException {
        writeBodyBegin(response);
        response.getWriter().println(MIN_PRICE_HEADER);
        writeBodyEnd(response);
        writeContentType(response);
    }

    @Override
    public void writeEmptyPricesSum(HttpServletResponse response) throws IOException {
        writeBodyBegin(response);
        response.getWriter().println(PRICES_SUM_HEADER);
        writeBodyEnd(response);
        writeContentType(response);
    }

    @Override
    public void writeEmptyProductsCount(HttpServletResponse response) throws IOException {
        writeBodyBegin(response);
        response.getWriter().println(PRODUCTS_COUNT_HEADER);
        writeBodyEnd(response);
        writeContentType(response);
    }

    @Override
    public void writeUnknownOperation(HttpServletResponse response, String command) throws IOException {
        response.getWriter().println(UNKNOWN_COMMAND + command);
        writeContentType(response);
    }

    private void writeContentType(HttpServletResponse response) {
        response.setContentType(TEXT_HEADER);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private void writeBodyBegin(HttpServletResponse response) throws IOException {
        response.getWriter().println(BODY_BEGIN);
    }

    private void writeBodyEnd(HttpServletResponse response) throws IOException {
        response.getWriter().println(BODY_END);
    }
}
