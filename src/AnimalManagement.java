import java.sql.*;
import javax.swing.*;

public class AnimalManagement {

    private Connection conn;

    public AnimalManagement(Connection conn) {
        this.conn = conn;
    }

    // Add Animal Method
    public void addAnimal(String species, int count, String lastObserved, String location) throws SQLException {
        String sql = "INSERT INTO WildlifeMonitoring (Species, Count, LastObserved, Location, AnimalType) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, species);
            pstmt.setInt(2, count);
            pstmt.setString(3, lastObserved);
            pstmt.setString(4, location);
            pstmt.setString(5, "Unknown");
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Animal Added Successfully!");
        }
    }

    // Refresh Animal List
    public String getAllAnimals() throws SQLException {
        StringBuilder sb = new StringBuilder();
        String query = "SELECT * FROM WildlifeMonitoring";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                sb.append("ID: ").append(rs.getInt("ID")).append(", ")
                        .append("Species: ").append(rs.getString("Species")).append(", ")
                        .append("Count: ").append(rs.getInt("Count")).append(", ")
                        .append("Last Observed: ").append(rs.getDate("LastObserved")).append(", ")
                        .append("Location: ").append(rs.getString("Location")).append(", ")
                        .append("Animal Type: ").append(rs.getString("AnimalType")).append("\n");
            }
        }
        return sb.toString();
    }
}
