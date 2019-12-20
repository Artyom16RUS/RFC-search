package service;

import db.DataBaseResult;
import db.DataBaseSource;
import model.Document;
import util.Generates;
import util.PathUtil;

import java.io.*;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;

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
                String path = Paths.get(PathUtil.getUploadPathUri()) + File.separator + document.getId();
                if (new File(path).exists()) {
                    BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
                    LinkedList<String> subResult = new LinkedList<>();
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
                    bf.close();
                }
            }
            String newId;
            if (result.size() > 0) {
                newId = Generates.createId();
                String pathPublic = PathUtil.getDownloadPathUri() + File.separator + newId;
                FileWriter fw = new FileWriter(pathPublic, true);
                for (String string : result) {
                    fw.write(string);
                    fw.flush();
                }
                fw.close();
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
