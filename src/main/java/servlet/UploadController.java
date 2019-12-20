package servlet;

import Constant.Constant;
import Constant.ConstantJSP;
import service.DocumentService;
import util.PathUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class UploadController extends HttpServlet {

    private Path uploadPath;

    @Override
    public void init() {
        uploadPath = PathUtil.getUploadPath();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        req.setAttribute(ConstantJSP.QUANTITY, Constant.DOCUMENT_SERVICE.getQuantity());
        req.setAttribute(ConstantJSP.STATUS_NOT_ADD, Constant.DOCUMENT_SERVICE.getListNotAdded());
        req.getRequestDispatcher("/WEB-INF/main.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        try {
            List<Part> fileParts = req.getParts()
                    .stream()
                    .filter(part -> "file".equals(part.getName()))
                    .collect(Collectors.toList());
            Constant.DOCUMENT_SERVICE.addFile(fileParts, uploadPath);
            resp.sendRedirect(req.getRequestURI());
        } catch (Exception e) {
            e.printStackTrace();
            req.getRequestDispatcher("/WEB-INF/404.jsp").forward(req, resp);
        }
    }
}
