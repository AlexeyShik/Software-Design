package ru.akirakozov.sd.refactoring.servlet.handler;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class InvalidCommandHandler extends AbstractCommandHandler {

    private final String command;

    public InvalidCommandHandler(String command) {
        this.command = command;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        view.writeUnknownOperation(response, command);
    }
}
