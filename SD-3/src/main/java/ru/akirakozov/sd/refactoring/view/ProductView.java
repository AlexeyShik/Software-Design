package ru.akirakozov.sd.refactoring.view;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import ru.akirakozov.sd.refactoring.dao.ProductEntry;

public interface ProductView {

    void writeSuccessAdd(HttpServletResponse response) throws IOException;

    void writeAllProducts(HttpServletResponse response, List<ProductEntry> products) throws IOException;

    void writeMaxProduct(HttpServletResponse response, ProductEntry product) throws IOException;

    void writeMinProduct(HttpServletResponse response, ProductEntry product) throws IOException;

    void writePricesSum(HttpServletResponse response, int pricesSum) throws IOException;

    void writeProductsCount(HttpServletResponse response, int productsCount) throws IOException;

    void writeEmptyMaxProduct(HttpServletResponse response) throws IOException;

    void writeEmptyMinProduct(HttpServletResponse response) throws IOException;

    void writeEmptyPricesSum(HttpServletResponse response) throws IOException;

    void writeEmptyProductsCount(HttpServletResponse response) throws IOException;

    void writeUnknownOperation(HttpServletResponse response, String command) throws IOException;
}
