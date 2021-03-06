package service;

import Constant.Constant;
import db.DataBaseResult;
import db.DataBaseSource;
import model.Document;
import util.Generates;

import javax.naming.NamingException;
import javax.servlet.http.Part;
import java.io.*;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ExecutorService;


public class DocumentService {

    private Collection<Document> document;
    private DataBaseSource dbs;
    private DataBaseResult dbr;
    private Collection<String> listNotAdded;
    private int quantity;

    public DocumentService() {
        updateCollection();
        try {
            dbs = new DataBaseSource(Constant.PATH);
            dbr = new DataBaseResult(Constant.PATH);
        } catch (NamingException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateCollection() {
        document = new LinkedList<>();
    }

    public Collection<String> getListNotAdded() {
        return listNotAdded;
    }

    public int getQuantity() {
        return quantity;
    }

    public void addFile(List<Part> fileParts, Path path) throws Exception {
        listNotAdded = new LinkedList<>();
        quantity = 0;

        for (Part part : fileParts) {
            String name = part.getSubmittedFileName();
            int lineLength = name.length() - Constant.FORMAT.length();
            if (name.substring(lineLength).equals(Constant.FORMAT)) {
                String id = Generates.createId();
                dbs.create(id, name);
                writeDocument(id, part, path);
                updateCollection();
                quantity++;
            } else {
                listNotAdded.add(name);
            }
        }
    }

    private void writeDocument(String id, Part part, Path path) { //22.42s recording speed HDD vs 35.10s Web download => 12.68s application speed
        try {
            part.write(path.resolve(id).toString());
            part.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Collection<Document> searchByName(String name, ExecutorService service) {
         service.execute(new SearchService(document, dbs, dbr, name));
        return document;
    }
}
