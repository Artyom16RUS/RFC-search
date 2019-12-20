package Constant;

import service.DocumentService;

public class Constant {
    public static final DocumentService DOCUMENT_SERVICE =new DocumentService();
    public static final String FORMAT = ".txt";
    public static final String PATH = "java:/comp/env/jdbc/db";

    private Constant() {
    }
}
