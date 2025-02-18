import java.sql.*;
import javax.swing.*;

public class StaffManagement {

    private Connection conn;

    public StaffManagement(Connection conn) {
        this.conn = conn;
    }

    // Add Staff Method
    public void addStaff(String name, String role, double salary) throws SQLException {
        String sql = "INSERT INTO ZooStaff (Name, Role, Salary) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, role);
            pstmt.setDouble(3, salary);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Staff Added Successfully!");
        }
    }

    // Refresh Staff List
    public String getAllStaff() throws SQLException {
        StringBuilder sb = new StringBuilder();
        String query = "SELECT * FROM ZooStaff";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                sb.append("ID: ").append(rs.getInt("ID")).append(", ")
                        .append("Name: ").append(rs.getString("Name")).append(", ")
                        .append("Role: ").append(rs.getString("Role")).append(", ")
                        .append("Salary: ").append(rs.getDouble("Salary")).append("\n");
            }
        }
        return sb.toString();
    }
}
