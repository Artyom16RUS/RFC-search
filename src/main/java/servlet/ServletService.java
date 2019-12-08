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
    private Collection<Document> documents; //TODO replaced by DB
    private Collection<String> notAdded;
    private Collection<String> added;

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

        req.setAttribute("books", added);//TODO  remove
        req.setAttribute("status", notAdded);
        String url = req.getRequestURI().substring(req.getContextPath().length()); //получаем URL запроса
        if (url.equals("/search")) { // для запроса /search
            req.getRequestDispatcher("/WEB-INF/result.jsp").forward(req, resp);
            return;
        }

        if (url.startsWith("/text/")) {
            String id = url.substring("/text/".length());
            Path path = publicPath.resolve(id);
            if (Files.exists(path)) {
                Files.copy(path, resp.getOutputStream());
                return;
            }
        }
        req.getRequestDispatcher("/WEB-INF/main.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (req.getParameter("action").equals("save")) { //TODO status
            notAdded = new ArrayList<>();
            added = new ArrayList<>();
            try {
                List<Part> fileParts = req.getParts().stream().filter(part -> "file".equals(part.getName())).collect(Collectors.toList());//множественное добавлнение
                for (Part part : fileParts) {
                    String t = documentService.addFile(part, uploadPath);
                    if(!t.equals("Complete")){
                        notAdded.add(t);
                    } else {
                        added.add(t);
                    }
                }

                resp.sendRedirect(req.getRequestURI());
                return;
            } catch (Exception e) {
                e.printStackTrace();
                req.getRequestDispatcher("/WEB-INF/404.jsp").forward(req, resp);
            }
        }

        if (req.getParameter("action").equals("search")) { //поиск по названию
            String searchName = req.getParameter("search"); //имя
            documents = documentService.searchText(searchName); // отдали список найденых имен
            req.setAttribute("catalog", documents);
            req.getRequestDispatcher("/WEB-INF/result.jsp").forward(req, resp);
            return;
        }

        if (req.getParameter("action").equals("return")) {
            req.getRequestDispatcher("/WEB-INF/main.jsp").forward(req, resp);
            return;
        }
        req.getRequestDispatcher("/WEB-INF/main.jsp").forward(req, resp);
    }
}
