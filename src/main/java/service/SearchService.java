package service;

import db.DataBaseResult;
import db.DataBaseSource;
import model.Document;
import util.Generates;
import Constant.Constant;

import java.io.*;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;

public class SearchService implements Runnable {

    private Collection<Document> document;
    private DataBaseSource dbs;
    private DataBaseResult dbr;
    private String name;

    public SearchService(Collection<Document> document, DataBaseSource dbs, DataBaseResult dbr, String name) {
        this.document = document;
        this.dbs = dbs;
        this.dbr = dbr;
        this.name = name;
    }

       @Override
    public void run() {

        if (replay(name)) {
            return;
        }

        Collection<String> result = new LinkedHashSet<>();
        try {
            for (Document document : dbs.getAll()) {
                String id = document.getId();
                String path = Paths.get(System.getenv(Constant.UPLOAD_PATH)) + "\\" + id;
                if (new File(path).exists()) {
                    BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
                    ArrayList<String> subResult = new ArrayList<>();
                    subResult.add("[" + document.getName() + "]: ");
                    String line;
                    while ((line = bf.readLine()) != null) { //TODO make compact
                        if (line.toLowerCase().contains(name.toLowerCase())) {
                            subResult.add(line);
                        }
                    }
                    if (subResult.size() > 1) {
                        subResult.add(System.getProperty("line.separator"));
                        result.add(String.join(System.getProperty("line.separator"), subResult));
                    }
                }
            }
            String newId;
            if (result.size() > 0) {
                newId = Generates.createId();
                String pathPublic = Paths.get(System.getenv(Constant.PUBLIC_PATH)) + "\\" + newId;
                FileWriter fw = new FileWriter(pathPublic, true);
                for (String string : result) {
                    fw.write(string);
                    fw.flush();
                }
            } else {
                newId = Generates.createIdZero();
            }
            dbr.create(newId, name);
            getDocument().add(new Document(newId, name));
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }

    }

    private boolean replay(String name) {
        try {
            for (Document doc : dbr.getAll()) {
                if (name.equals(doc.getName())) {
                    getDocument().add(doc);
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private synchronized Collection<Document> getDocument() {
        return document;
    }
}
