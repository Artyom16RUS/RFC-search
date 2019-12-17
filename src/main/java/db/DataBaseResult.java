package db;

import model.Document;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class DataBaseResult { //TODO Lection13 / 02. Rateil
    private final DataSource ds;

    public DataBaseResult(String path) throws NamingException, SQLException {
        Context context = new InitialContext();
        ds = (DataSource) context.lookup(path);
        try (
                Connection conn = ds.getConnection();
                Statement stmt = conn.createStatement();
        ) {
            stmt.execute("CREATE TABLE IF NOT EXISTS result (id TEXT PRIMARY KEY , name TEXT NOT NULL)");
        }
    }

    public List<Document> getAll() throws SQLException {
        try (
                Connection conn = ds.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT id, name FROM result")
        ) {
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

    public void create(String id, String name) throws SQLException {
        try (
                Connection conn = ds.getConnection();
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO result (id, name) VALUES (?, ?)")
        ) {
            stmt.setString(1, id);
            stmt.setString(2, name);
            stmt.execute();
        }
    }
}

