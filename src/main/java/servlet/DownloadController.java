package servlet;

import util.PathUtil;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DownloadController extends HttpServlet {

    private Path downloadPath;

    @Override
    public void init() {
        downloadPath = PathUtil.getDownloadPath();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id = req.getPathInfo().substring(1);
        Path path = downloadPath.resolve(id);
        if (Files.exists(path)) {
            Files.copy(path, resp.getOutputStream());
        }
    }
}
