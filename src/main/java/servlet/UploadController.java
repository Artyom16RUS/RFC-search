package servlet;

import service.DocumentService;
import util.PathUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class UploadController extends HttpServlet {
    private DocumentService documentService;
    private Path uploadPath;

    @Override
    public void init() {
        uploadPath = PathUtil.getUploadPath();
        documentService = new DocumentService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.sendRedirect(req.getContextPath());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        try {
            List<Part> fileParts = req.getParts()
                    .stream()
                    .filter(part -> "file".equals(part.getName()))
                    .collect(Collectors.toList());
            documentService.addFile(fileParts, uploadPath);
            resp.sendRedirect(req.getContextPath());
        } catch (Exception e) {
            e.printStackTrace();
            req.getRequestDispatcher("/WEB-INF/404.jsp").forward(req, resp);
        }
    }
}
