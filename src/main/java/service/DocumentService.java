package service;

import db.DataBaseResult;
import db.DataBaseSource;
import model.Document;
import util.Generates;
import Constant.Constant;

import javax.naming.NamingException;
import javax.servlet.http.Part;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class DocumentService {

    private Collection<Document> document;
    private DataBaseSource dbs;
    private DataBaseResult dbr;
    private Collection<String> listNotAdded;

    public DocumentService() {
        updateCollection();
//        String path = "java:/comp/env/jdbc/db";
        try {
            dbs = new DataBaseSource(Constant.PATH);
            dbr = new DataBaseResult(Constant.PATH);
        } catch (NamingException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateCollection() {
        document = new ArrayList<>();
    }

    public Collection<String> getListNotAdded() {
        return listNotAdded;
    }

    public int addFile(List<Part> fileParts, Path path) throws Exception {
        listNotAdded = new ArrayList<>();
        int quantity = 0;
        boolean status = false;
//        String format = ".txt";

        for (Part part : fileParts) {
            String name = Paths.get(part.getSubmittedFileName()).getFileName().toString();
            int lineLength = name.length() - Constant.FORMAT.length();
            if (name.substring(lineLength).equals(Constant.FORMAT)) {
                String id = Generates.createId();
                dbs.create(id, name);
                writeDocument(id, part, path);
                updateCollection();
                status = true;
            }
            if (status){
                quantity++;
            } else {
                listNotAdded.add(name);
            }
        }
        return quantity;
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
