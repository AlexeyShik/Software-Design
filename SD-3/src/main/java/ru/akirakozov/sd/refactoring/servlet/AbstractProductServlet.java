package ru.akirakozov.sd.refactoring.servlet;

import javax.servlet.http.HttpServlet;

import ru.akirakozov.sd.refactoring.dao.DaoService;
import ru.akirakozov.sd.refactoring.dao.IDaoService;
import ru.akirakozov.sd.refactoring.view.ProductHtmlView;
import ru.akirakozov.sd.refactoring.view.ProductView;

public abstract class AbstractProductServlet extends HttpServlet {

    protected final IDaoService dao;
    protected final ProductView view;

    public AbstractProductServlet() {
        dao = new DaoService();
        view = new ProductHtmlView();
    }
}
