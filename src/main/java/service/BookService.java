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

    public void addFile(String name, Part part, Path path) {

        String id = generateId();
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
        for (Book book : books) {
            if (book.getName().equals(text)) {
                newBook.add(book);

            }
        }
        return newBook;
    }

    public Collection<Book> getBooks() {
        return books;
    }
}
