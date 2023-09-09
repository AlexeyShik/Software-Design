package ru.akirakozov.sd.refactoring.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ru.akirakozov.sd.refactoring.servlet.handler.CommandHandler;

public class QueryServlet extends AbstractProductServlet {

    private static final String COMMAND_PARAMETER = "command";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        CommandHandler.ofCommand(request.getParameter(COMMAND_PARAMETER)).handle(request, response);
    }

}
