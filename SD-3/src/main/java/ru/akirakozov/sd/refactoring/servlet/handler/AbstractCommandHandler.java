package ru.akirakozov.sd.refactoring.servlet.handler;

import ru.akirakozov.sd.refactoring.dao.DaoService;
import ru.akirakozov.sd.refactoring.dao.IDaoService;
import ru.akirakozov.sd.refactoring.view.ProductHtmlView;
import ru.akirakozov.sd.refactoring.view.ProductView;

public abstract class AbstractCommandHandler implements CommandHandler {

    protected final ProductView view;
    protected final IDaoService dao;

    public AbstractCommandHandler() {
        view = new ProductHtmlView();
        dao = new DaoService();
    }
}
