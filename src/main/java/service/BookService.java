package service;

import model.Book;

import javax.servlet.http.Part;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;


public class BookService {

    private Collection<Book> books = new ArrayList<>();

    public void addFile(String name, Part part, Path path){

        String id = generateId();
        if (part != null) {
            writeBook(id, part, path);
        }
        create(new Book(id, name));

    }

    public String generateId(){
        return UUID.randomUUID().toString();
    }

    public void create(Book book){
        books.add(book);
    }

    public void writeBook(String id, Part part, Path path){
        try {
            part.write(path.resolve(id).toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Collection<Book> getBooks() {
        return books;
    }
}
