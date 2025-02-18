import java.sql.*;
import javax.swing.*;

public class VisitorManagement {

    private Connection conn;

    public VisitorManagement(Connection conn) {
        this.conn = conn;
    }

    // Add Visitor Method
    public void addVisitor(String name, String ticketNo) throws SQLException {
        String sql = "INSERT INTO ZooVisitors (Name, TicketNo) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, ticketNo);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Visitor Added Successfully!");
        }
    }

    // Refresh Visitor List
    public String getAllVisitors() throws SQLException {
        StringBuilder sb = new StringBuilder();
        String query = "SELECT * FROM ZooVisitors";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                sb.append("ID: ").append(rs.getInt("ID")).append(", ")
                        .append("Name: ").append(rs.getString("Name")).append(", ")
                        .append("Ticket No: ").append(rs.getString("TicketNo")).append("\n");
            }
        }
        return sb.toString();
    }
}
