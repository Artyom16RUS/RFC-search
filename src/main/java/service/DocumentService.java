package service;

import db.DataBaseResult;
import db.DataBaseSource;
import model.Document;

import javax.naming.NamingException;
import javax.servlet.http.Part;
import java.io.*;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;


public class DocumentService {

    private Collection<Document> document;

    private DataBaseSource dbs;
    private DataBaseResult dbr;

    public DocumentService() {

        updateCollection();
        try {
            dbs = new DataBaseSource();
            dbr = new DataBaseResult();
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateCollection() {
        document = new ArrayList<>();
    }

    public boolean addFile(String name, Part part, Path path) throws Exception {
        boolean status = false;
        String format = ".txt";
        int lineLength = name.length() - format.length();
        if (name.substring(lineLength).equals(format)) {
            String id = UUID.randomUUID().toString();
            dbs.create(id, name);
            writeDocument(id, part, path);
            status = true;
            updateCollection();
        }
        return status;
    }

    private void writeDocument(String id, Part part, Path path) {
        try {
            part.write(path.resolve(id).toString());
            part.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Collection<Document> searchByName(String name) {

        new Thread(new SearchService(document, dbs, dbr, name)).start();
//        int core = Runtime.getRuntime().availableProcessors();
//        Executors.newFixedThreadPool(core).execute(new SearchService(document, dbs, dbr, name));
        return document;
    }
}
