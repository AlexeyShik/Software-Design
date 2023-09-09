package ru.akirakozov.sd.refactoring.servlet.handler;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ru.akirakozov.sd.refactoring.dao.ProductEntry;

public class MinCommandHandler extends AbstractCommandHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Optional<ProductEntry> minProduct = dao.getMinProduct();

        if (minProduct.isPresent()) {
            view.writeMinProduct(response, minProduct.get());
        } else {
            view.writeEmptyMinProduct(response);
        }
    }
}
