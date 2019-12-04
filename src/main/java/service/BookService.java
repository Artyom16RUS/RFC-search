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

        ArrayList<String> result = new ArrayList<>();
        String newId = generateId();
        String pathPublic = Paths.get(System.getenv("PUBLIC_PATH")) + "\\" + newId;
        try {
            FileWriter fw = new FileWriter(pathPublic, true);
            for (Book book : books) {
                String id = book.getId();
                String path = Paths.get(System.getenv("UPLOAD_PATH")) + "\\" + id;
                if (new File(path).exists()) {
                    BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(path)));

                    String line;
                    while ((line = bf.readLine()) != null) {
                        if (line.toLowerCase().contains(text.toLowerCase())) {
                            result.add(line);


                        }
                    }
                }
            }
            for (String t : result) {
                fw.write(t);
                fw.append(System.getProperty("line.separator"));
                fw.flush();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(newId + " ID");
        newBook.add(new Book(newId, text));
        return newBook;
    }


    public Collection<Book> getBooks() {
        return books;
    }
}
