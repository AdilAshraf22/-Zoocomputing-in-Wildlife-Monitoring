import java.sql.*;

public class DatabaseHandler {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/zoo";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "hellosql123";

    private Connection conn;
    private Statement stmt;

    public DatabaseHandler() throws SQLException {
        conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        stmt = conn.createStatement();
    }

    public ResultSet executeQuery(String query) throws SQLException {
        return stmt.executeQuery(query);
    }

    public int executeUpdate(String query) throws SQLException {
        return stmt.executeUpdate(query);
    }

    public void close() throws SQLException {
        stmt.close();
        conn.close();
    }
}
