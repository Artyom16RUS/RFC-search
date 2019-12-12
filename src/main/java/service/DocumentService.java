package service;

import db.DataBase;
import model.Document;

import javax.naming.NamingException;
import javax.servlet.http.Part;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.UUID;


public class DocumentService {

    private Collection<Document> document; //TreeMap
    private DataBase db;

    public DocumentService() {
        updateCollection();
        try {
            db = new DataBase();
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
            String id = db.create(name);
            writeBook(id, part, path);
            status = true;
            updateCollection();
        }
        return status;
    }

    private void writeBook(String id, Part part, Path path) {
        try {
            part.write(path.resolve(id).toString());
            part.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean replay(String name) {
        for (Document doc : document) {
            if (name.equals(doc.getName())) {
                document.add(doc);
                return true;
            }
        }
        return false;
    }

    public Collection<Document> searchByName(String name) { //TODO Thread

        if (replay(name)) {
            return document;
        }

        Collection<String> result = new LinkedHashSet<>();
        try {
            for (Document document : db.getAll()) {
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
            if (result.size() > 0) {
                String newId = UUID.randomUUID().toString();
                String pathPublic = Paths.get(System.getenv("PUBLIC_PATH")) + "\\" + newId;
                FileWriter fw = new FileWriter(pathPublic, true);
                for (String string : result) {
                    fw.write(string);
                    fw.flush();
                }
                document.add(new Document(newId, name));
            } else {
                document.add(new Document("0", name));
            }

        } catch (FileNotFoundException e) {
//            e.printStackTrace();
        } catch (IOException e) {
//            e.printStackTrace();
        } catch (SQLException e) {
//            e.printStackTrace();
        }

        return document;
    }


}
