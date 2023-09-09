package ru.akirakozov.sd.refactoring.servlet.handler;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface CommandHandler {

    String MAX_COMMAND = "max";
    String MIN_COMMAND = "min";
    String SUM_COMMAND = "sum";
    String COUNT_COMMAND = "count";

    void handle(HttpServletRequest request, HttpServletResponse response) throws IOException;

    static CommandHandler ofCommand(String command) {
        CommandHandler commandHandler;

        switch (command) {
            case MAX_COMMAND: {
                commandHandler = new MaxCommandHandler();
                break;
            }
            case MIN_COMMAND: {
                commandHandler = new MinCommandHandler();
                break;
            }
            case SUM_COMMAND: {
                commandHandler = new SumCommandHandler();
                break;
            }
            case COUNT_COMMAND: {
                commandHandler = new CountCommandHandler();
                break;
            }
            default: {
                commandHandler = new InvalidCommandHandler(command);
            }
        }

        return commandHandler;
    }
}
