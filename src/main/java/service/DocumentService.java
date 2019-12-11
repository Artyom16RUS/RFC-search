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
import java.util.UUID;


public class DocumentService {

    private Collection<Document> document = new ArrayList<>();
    private DataBase db;

    {
        try {
            db = new DataBase();
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public boolean addFile(String name, Part part, Path path) throws Exception {
        boolean status = false;
        String format = ".txt";
        int lineLength = name.length() - format.length();
        if (name.substring(lineLength).equals(format)) {
            String id = db.create(name);
            writeBook(id, part, path);
            status = true;
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

    public Collection<Document> searchText(String text) { //TODO Thread

        ArrayList<String> result = new ArrayList<>();
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
                        if (line.toLowerCase().contains(text.toLowerCase())) {
                            subResult.add(line);
                        }
                    }
                    if (subResult.size() > 1) {
                        result.addAll(subResult);
                    }
                }
            }
            if (result.size() > 0) {
                String id = UUID.randomUUID().toString();
                String pathPublic = Paths.get(System.getenv("PUBLIC_PATH")) + "\\" + id;
                FileWriter fw = new FileWriter(pathPublic, true);
                for (String string : result) {
                    fw.write(string);
                    fw.append(System.getProperty("line.separator"));
                    fw.flush();
                }
                document.add(new Document(id, text));
            } else {
                document.add(new Document("0", text));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return document;
    }


}
