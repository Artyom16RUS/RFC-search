package db;

import model.Document;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DataBaseResult {
    private final DataSource ds;

    public DataBaseResult() throws NamingException, SQLException {
        Context context = new InitialContext();
        ds = (DataSource) context.lookup("java:/comp/env/jdbc/db");
        try (Connection conn = ds.getConnection()) {
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("CREATE TABLE IF NOT EXISTS result (id TEXT PRIMARY KEY , name TEXT NOT NULL)");
            }
        }
    }

    public List<Document> getAll() throws SQLException {
        try (Connection conn = ds.getConnection()) {
            try (Statement stmt = conn.createStatement()) {
                try (ResultSet rs = stmt.executeQuery("SELECT id, name FROM document")) {
                    ArrayList<Document> list = new ArrayList<>();

                    while (rs.next()) {
                        list.add(new Document(
                                rs.getString("id"),
                                rs.getString("name")
                        ));
                    }
                    return list;
                }
            }
        }
    }

    public String create(String name) throws SQLException {
        String id;
        try (Connection conn = ds.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO result (id, name) VALUES (?, ?)")) {
                id = UUID.randomUUID().toString();
                stmt.setString(1, id);
                stmt.setString(2, name);
                stmt.execute();
            }
        }
        return id;
    }

    public void createEmpty(String  id, String name) throws SQLException {
//        String id;
        try (Connection conn = ds.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO result (id, name) VALUES (?, ?)")) {
//                id = UUID.randomUUID().toString();
                stmt.setString(1, id);
                stmt.setString(2, name);
                stmt.execute();
            }
        }
//        return id;
    }
}

