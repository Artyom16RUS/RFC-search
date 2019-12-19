package servlet;

import Constant.ConstantJSP;
import model.Document;
import service.DocumentService;
import util.PathUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;

public class SearchController extends HttpServlet {

    private DocumentService documentService;
    private Collection<Document> documents;

    @Override
    public void init() {
        documentService = new DocumentService();
        documents = new ArrayList<>();
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
        documents = documentService.searchByName(text);
        System.out.println(documents.size());
        resp.sendRedirect(req.getRequestURI());
    }
}
