package servlet;

import Constant.Constant;
import Constant.ConstantJSP;
import model.Document;
import util.Generates;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SearchController extends HttpServlet {

    private Collection<Document> documents;
    ExecutorService executorService;

    @Override
    public void init() {
        documents = new ArrayList<>();
        executorService = Executors.newFixedThreadPool(Generates.getNumberCores());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute(ConstantJSP.CATALOG, documents);
        req.getRequestDispatcher("/WEB-INF/result.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String text = req.getParameter("search");
        documents = Constant.DOCUMENT_SERVICE.searchByName(text, executorService);
        resp.sendRedirect(req.getRequestURI());
    }

    @Override
    public void destroy() {
        executorService.shutdown();
    }
}
