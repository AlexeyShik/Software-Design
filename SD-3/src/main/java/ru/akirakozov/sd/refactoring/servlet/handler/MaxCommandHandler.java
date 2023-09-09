package ru.akirakozov.sd.refactoring.servlet.handler;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ru.akirakozov.sd.refactoring.dao.ProductEntry;

public class MaxCommandHandler extends AbstractCommandHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Optional<ProductEntry> maxProduct = dao.getMaxProduct();

        if (maxProduct.isPresent()) {
            view.writeMaxProduct(response, maxProduct.get());
        } else {
            view.writeEmptyMaxProduct(response);
        }
    }
}
