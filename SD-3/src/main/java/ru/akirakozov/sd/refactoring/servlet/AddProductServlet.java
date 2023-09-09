package ru.akirakozov.sd.refactoring.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ru.akirakozov.sd.refactoring.dao.ProductEntry;

public class AddProductServlet extends AbstractProductServlet {

    private static final String NAME_PARAMETER = "name";
    private static final String PRICE_PARAMETER = "price";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter(NAME_PARAMETER);
        long price = Long.parseLong(request.getParameter(PRICE_PARAMETER));

        dao.addProduct(new ProductEntry(name, price));
        view.writeSuccessAdd(response);
    }
}
