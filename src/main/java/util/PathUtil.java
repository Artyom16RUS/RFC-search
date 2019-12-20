package util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PathUtil {

    private static final String UPLOAD_PATH = "UPLOAD_PATH";
    private static final String DOWNLOAD_PATH = "DOWNLOAD_PATH";

    public static String getUploadPathUri(){
        return System.getenv(UPLOAD_PATH);
    }

    public static String getDownloadPathUri(){
        return System.getenv(DOWNLOAD_PATH);
    }

    public static Path getDownloadPath(){
        Path path = Paths.get(PathUtil.getDownloadPathUri());
        try {
            if (Files.notExists(path)) {
                Files.createDirectory(path);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }


    public static Path getUploadPath(){
        Path path = Paths.get(PathUtil.getUploadPathUri());
        try {
            if (Files.notExists(path)) {
                Files.createDirectory(path);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }
}
