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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

public class ServletService extends HttpServlet {

    private BookService bookService;
    private Path uploadPath;


    @Override
    public void init()  {
        bookService = new BookService();
        uploadPath = Paths.get(System.getenv("UPLOAD_PATH"));
        if(Files.notExists(uploadPath)){
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


        req.setCharacterEncoding("UTF-8");
        req.setAttribute("books", bookService.getBooks());

        if(req.getMethod().equals("POST")){
            if(req.getParameter("action").equals("save")){
                String name = req.getParameter("name");
                Part file = req.getPart("file");
                bookService.addFile(name, file, uploadPath);
//                resp.sendRedirect(req.getRequestURI());
            }

        }
        if(req.getMethod().equals("POST")){  //можно по ссылке передать что ищем
            Collection<Book> books;
            if(req.getParameter("action").equals("search")){

                String searchName = req.getParameter("search");
                books = bookService.searchText(searchName);
                req.setAttribute("catalog", books);
//                resp.sendRedirect("/");
               req.getRequestDispatcher("/WEB-INF/searchPage.jsp").forward(req, resp);
//                req.getRequestDispatcher("/WEB-INF/houses.jsp").forward(req, resp);



            }

            if(req.getParameter("action").equals("return")){
                req.getRequestDispatcher("/WEB-INF/mainPage.jsp").forward(req, resp);
            }
        }




        req.getRequestDispatcher("/WEB-INF/mainPage.jsp").forward(req, resp);




    }
}
