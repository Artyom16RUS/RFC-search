package service;

import model.Book;
import org.omg.CORBA_2_3.portable.OutputStream;

import javax.servlet.http.Part;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;


public class BookService {

    private Collection<Book> books = new ArrayList<>();

    public void addFile(String name, Part part, Path path) {

        String id = generateId();
        System.out.println(id + " id");
        if (part != null) {
            writeBook(id, part, path);
        }
        create(new Book(id, name));

    }

    public String generateId() {
        return UUID.randomUUID().toString();
    }

    public void create(Book book) {
        books.add(book);
    }

    public void writeBook(String id, Part part, Path path) { //записываем фаил с таким же ID как имя в базе
        try {
            part.write(path.resolve(id).toString());
            part.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Collection searchText(String text) { //поиск из списка имени
        Collection<Book> newBook = new ArrayList<>();
        Collection<String> arrayText = new ArrayList<>();

        for (Book book : books) {
            String id = book.getId();
            String path = Paths.get(System.getenv("UPLOAD_PATH")) +"/" + id;
            String pathPublic = "D:\\U4eba\\Java\\OtherWork\\Diplom\\public\\http.txt";
            try {
                if(new File(path).exists()){
                    BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
                    FileWriter fw = new FileWriter(pathPublic, true);
                    String line;
                    while ((line = bf.readLine())!= null){

                        if(line.contains(text)){
//                            System.out.println(line + " after");
                            fw.write(line);

//                            arrayText.add(line);
                            newBook.add(book);

                        }

                    }
                    fw.flush();
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return newBook;
    }




    public Collection<Book> getBooks() {
        return books;
    }
}
