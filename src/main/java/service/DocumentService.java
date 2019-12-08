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

    public String addFile(Part part, Path path) throws Exception {

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

    private String generateId() {
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

    public Collection searchText(String text) { //поиск из списка имени http

        Collection<Document> newDocument = new ArrayList<>();// новй каталог файлов
        ArrayList<String> result = new ArrayList<>();
        String newId = generateId();//1211
        String pathPublic = Paths.get(System.getenv("PUBLIC_PATH")) + "\\" + newId;//C:\1211 id для еще не записаного файла
        try {

            for (Document document : documents) {
                String id = document.getId(); //325
                String path = Paths.get(System.getenv("UPLOAD_PATH")) + "\\" + id;//C:\325 находим фаил с этим id
                if (new File(path).exists()) {
                    BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(path)));//C:\325 откуда читаем
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
                    System.out.println(result.size());
                }
            }
            if (result.size() > 0) {
                FileWriter fw = new FileWriter(pathPublic, true);//
                for (String string : result) {
                    fw.write(string);
                    fw.append(System.getProperty("line.separator"));
                    fw.flush();
                }
                newDocument.add(new Document(newId, text));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return newDocument;
    }


    public Collection<Document> getDocuments() {
        return documents;
    }
}
