package service;

import db.DataBaseResult;
import db.DataBaseSource;
import model.Document;
import util.Generates;

import javax.naming.NamingException;
import javax.servlet.http.Part;
import java.io.*;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class DocumentService {

    private Collection<Document> document;
    private DataBaseSource dbs;
    private DataBaseResult dbr;

    public DocumentService() {
        updateCollection();
        String path = "java:/comp/env/jdbc/db";
        try {
            dbs = new DataBaseSource(path);
            dbr = new DataBaseResult(path);
        } catch (NamingException | SQLException e) {
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
            String id = Generates.createId();
            dbs.create(id, name);
            writeDocument(id, part, path);
            status = true;
            updateCollection();
        }
        return status;
    }

    private void writeDocument(String id, Part part, Path path) { //22.42s recording speed HDD vs 35.10s Web download => 12.68s application speed
        try {
            part.write(path.resolve(id).toString());
            part.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Collection<Document> searchByName(String name) {

        ExecutorService service = Executors.newFixedThreadPool(Generates.getNumberCores());
//        ExecutorService service = Executors.newCachedThreadPool();
        service.execute(new SearchService(document, dbs, dbr, name));
        service.shutdown();
//        new Thread(new SearchService(document, dbs, dbr, name)).start();
        return document;
    }
}
