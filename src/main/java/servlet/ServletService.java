package servlet;

import model.Document;
import service.DocumentService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ServletService extends HttpServlet {

    private DocumentService documentService;
    private Path uploadPath;
    private Path publicPath;
    private Collection<Document> documents;
    private Collection<String> notAdded;
    private int quantity;

    @Override
    public void init() {

        documentService = new DocumentService();
        uploadPath = Paths.get(System.getenv("UPLOAD_PATH"));
        publicPath = Paths.get(System.getenv("PUBLIC_PATH"));
        documents = new ArrayList<>();

        if (Files.notExists(uploadPath)) {
            try {
                Files.createDirectory(uploadPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setAttribute("statusAdd", quantity);
        req.setAttribute("statusNotAdd", notAdded);
        String url = req.getRequestURI().substring(req.getContextPath().length());
        if (url.equals("/search")) {
            req.setAttribute("catalog", documents);
            req.getRequestDispatcher("/WEB-INF/result.jsp").forward(req, resp);
            return;
        }

        if (url.startsWith("/text/")) {
            String id = url.substring("/text/".length());
            Path path = publicPath.resolve(id);
            if (Files.exists(path)) {
                Files.copy(path, resp.getOutputStream());
            }
            return;
        }
        quantity = 0;
        req.getRequestDispatcher("/WEB-INF/main.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        if (req.getParameter("action").equals("save")) {
            notAdded = new ArrayList<>();
            try {
                List<Part> fileParts = req.getParts() //TODO transfer in DocumentService
                        .stream()
                        .filter(part -> "file".equals(part.getName()))
                        .collect(Collectors.toList());
                for (Part part : fileParts) { //TODO compare to speed HDD
                    String name = Paths.get(part.getSubmittedFileName()).getFileName().toString();
                    boolean status = documentService.addFile(name, part, uploadPath);
                    if (status) {
                        quantity++;
                    } else {
                        notAdded.add(name);
                    }
                }
                resp.sendRedirect(req.getRequestURI());
            } catch (Exception e) {
                e.printStackTrace();
                req.getRequestDispatcher("/WEB-INF/404.jsp").forward(req, resp);
            }
            return;
        }


        if (req.getParameter("action").equals("search")) {
            String text = req.getParameter("search");
            documents = documentService.searchByName(text);
            resp.sendRedirect(req.getRequestURI());
            return;
        }

        if (req.getParameter("action").equals("return")) {
            req.getRequestDispatcher("/WEB-INF/main.jsp").forward(req, resp);
            return;
        }
        req.getRequestDispatcher("/WEB-INF/main.jsp").forward(req, resp);
    }
}
