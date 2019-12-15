package service;

import db.DataBaseResult;
import db.DataBaseSource;
import model.Document;

import java.io.*;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.UUID;

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
                String path = Paths.get(System.getenv("UPLOAD_PATH")) + "\\" + id;
                if (new File(path).exists()) {
                    BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
                    ArrayList<String> subResult = new ArrayList<>();
                    subResult.add("[" + document.getName() + "]: ");
                    String line;
                    while ((line = bf.readLine()) != null) {
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
            String newId = "0";
            if (result.size() > 0) {
                newId = UUID.randomUUID().toString();
                String pathPublic = Paths.get(System.getenv("PUBLIC_PATH")) + "\\" + newId;
                FileWriter fw = new FileWriter(pathPublic, true);
                for (String string : result) {
                    fw.write(string);
                    fw.flush();
                }
            }
            dbr.create(newId, name);
            getDocument().add(new Document(newId, name));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
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

    public synchronized Collection<Document> getDocument() {
        return document;
    }
}
