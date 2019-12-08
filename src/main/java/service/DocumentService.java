package service;

import model.Document;

import javax.servlet.http.Part;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;


public class DocumentService {

    private Collection<Document> documents = new ArrayList<>();

    public String addFile(Part part, Path path) {

        String status = "Complete";
        String format = ".txt";
        String name = Paths.get(part.getSubmittedFileName()).getFileName().toString();//получаем имя из файла
        int lineLength = name.length() - format.length();
        
        if (name.substring(lineLength).equals(format)) {
            name = name.substring(0, lineLength); //вырезаем .txt
            String id = generateId();
            writeBook(id, part, path);
            addToCatalog(new Document(id, name));
        } else {
            status = "File don't added " + name;
        }
        return status;
    }

    public String generateId() {
        return UUID.randomUUID().toString();
    }

    public void addToCatalog(Document document) {
        documents.add(document);
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
        Collection<Document> newDocument = new ArrayList<>();

        ArrayList<String> result = new ArrayList<>();
        String newId = generateId();
        String pathPublic = Paths.get(System.getenv("PUBLIC_PATH")) + "\\" + newId;
        try {
            FileWriter fw = new FileWriter(pathPublic, true);
            for (Document document : documents) {
                String id = document.getId();
                String path = Paths.get(System.getenv("UPLOAD_PATH")) + "\\" + id;
                if (new File(path).exists()) {
                    BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
                    ArrayList<String> subResult = new ArrayList<>();
                    subResult.add("[" + document.getName() + ".txt]: ");
                    String line;
                    while ((line = bf.readLine()) != null) {
                        if (line.toLowerCase().contains(text.toLowerCase())) {
                            subResult.add(line);
                        }
                    }
                    if (subResult.size() > 1) {
                        result.addAll(subResult);
                    }
                    result.add("\n");
                }
            }
            for (String t : result) {
                fw.write(t);
                fw.append(System.getProperty("line.separator"));
                fw.flush();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(newId + " ID");
        newDocument.add(new Document(newId, text));
        return newDocument;
    }


    public Collection<Document> getDocuments() {
        return documents;
    }
}
