package ru.akirakozov.sd.refactoring.servlet.handler;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SumCommandHandler extends AbstractCommandHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Optional<Integer> pricesSum = dao.getPricesSum();

        if (pricesSum.isPresent()) {
            view.writePricesSum(response, pricesSum.get());
        } else {
            view.writeEmptyPricesSum(response);
        }
    }
}
