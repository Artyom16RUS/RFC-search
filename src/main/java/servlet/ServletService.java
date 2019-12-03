package servlet;

import model.Book;
import service.BookService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;

public class ServletService extends HttpServlet {

    private BookService bookService;
    private Path uploadPath;


    @Override
    public void init() {
        bookService = new BookService();
        uploadPath = Paths.get(System.getenv("UPLOAD_PATH"));
        if (Files.notExists(uploadPath)) {
            try {
                Files.createDirectory(uploadPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        bookService.create(new Book("1", "Anna"));
        bookService.create(new Book("2", "George"));
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String url = req.getRequestURI().substring(req.getContextPath().length()); //получаем URL запроса

        req.setCharacterEncoding("UTF-8");
        req.setAttribute("books", bookService.getBooks());
        Collection<Book> books = new ArrayList<>();

        if (req.getMethod().equals("POST")) { // метод - сохраняем фаил
            if (req.getParameter("action").equals("save")) {
                String name = req.getParameter("name");
                Part file = req.getPart("file");
                bookService.addFile(name, file, uploadPath); //добавляем в список фаил
                resp.sendRedirect(req.getRequestURI());
                return;
            }


            if (req.getParameter("action").equals("search")) { //поиск по названию

                String searchName = req.getParameter("search"); //имя
                books = bookService.searchText(searchName); // отдали список найденых имен
                req.setAttribute("catalog", books);
                req.getRequestDispatcher("/WEB-INF/searchPage.jsp").forward(req, resp);
                return;

            }

            if (req.getParameter("action").equals("return")) {
                req.getRequestDispatcher("/WEB-INF/mainPage.jsp").forward(req, resp);
                return;
            }
        }

        if (url.equals("/search")) { // для запроса /search
            if (req.getMethod().equals("GET")) {
//                System.out.println(url);
                req.setAttribute("catalog", books);
                req.getRequestDispatcher("/WEB-INF/searchPage.jsp").forward(req, resp);
                return;
            }
        }

        if (url.startsWith("/images/")) {
            String id = url.substring("/images/".length());
            System.out.println(id);
            Path image = uploadPath.resolve(id);
            if (Files.exists(image)) {
                Files.copy(image, resp.getOutputStream());
                return;
            }

//            try {
//                Files.copy(Paths.get(getServletContext().getResource("/WEB-INF/404.png").toURI()), resp.getOutputStream());
//            } catch (URISyntaxException e) {
//                throw new IOException(e);
//            }
        }


        req.getRequestDispatcher("/WEB-INF/mainPage.jsp").forward(req, resp);


    }
}
