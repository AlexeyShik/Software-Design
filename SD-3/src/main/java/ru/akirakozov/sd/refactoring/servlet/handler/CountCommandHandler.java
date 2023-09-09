package ru.akirakozov.sd.refactoring.servlet.handler;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CountCommandHandler extends AbstractCommandHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Optional<Integer> productsCount = dao.getProductsCount();

        if (productsCount.isPresent()) {
            view.writeProductsCount(response, productsCount.get());
        } else {
            view.writeEmptyProductsCount(response);
        }
    }
}
