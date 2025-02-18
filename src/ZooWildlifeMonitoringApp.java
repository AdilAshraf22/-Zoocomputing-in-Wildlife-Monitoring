import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ZooWildlifeMonitoringApp {

    // JDBC Database credentials
    private static final String DB_URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "hellosql123"; // Change as per your DB configuration

    private static Connection conn;
    private static Statement stmt;

    // GUI components
    private JFrame frame;
    private JTabbedPane tabbedPane;
    private JTextField speciesField, countField, lastObservedField, locationField;
    private JTextField staffNameField, staffRoleField, salaryField, visitorNameField, visitorTicketField;
    private JButton addAnimalButton, addStaffButton, addVisitorButton;
    private JTextArea animalTextArea, staffTextArea, visitorTextArea;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                ZooWildlifeMonitoringApp app = new ZooWildlifeMonitoringApp();
                app.createDatabase();
                app.createGUI();
            } catch (SQLException e) {
                showErrorDialog("Error creating database: " + e.getMessage());
            }
        });
    }

    // Constructor
    public ZooWildlifeMonitoringApp() {
        try {
            // Establishing JDBC Connection to MySQL
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            stmt = conn.createStatement();
        } catch (SQLException e) {
            showErrorDialog("Error connecting to database: " + e.getMessage());
        }
    }

    // Create the Database, Tables and necessary schema if not exists
    private void createDatabase() throws SQLException {
        // Create Zoo database if it doesn't exist
        String createDBSQL = "CREATE DATABASE IF NOT EXISTS zoo";
        stmt.executeUpdate(createDBSQL);
        // Switch to the zoo database
        conn = DriverManager.getConnection(DB_URL + "zoo", DB_USER, DB_PASSWORD);
        stmt = conn.createStatement();

        // Create tables for Zoo Management
        String createWildlifeTableSQL = "CREATE TABLE IF NOT EXISTS WildlifeMonitoring ("
                + "ID INT AUTO_INCREMENT PRIMARY KEY, "
                + "Species VARCHAR(100), "
                + "Count INT, "
                + "LastObserved DATE, "
                + "Location VARCHAR(100), "
                + "AnimalType VARCHAR(50));";

        String createStaffTableSQL = "CREATE TABLE IF NOT EXISTS ZooStaff ("
                + "ID INT AUTO_INCREMENT PRIMARY KEY, "
                + "Name VARCHAR(100), "
                + "Role VARCHAR(100), "
                + "Salary DECIMAL(10, 2));";

        String createVisitorTableSQL = "CREATE TABLE IF NOT EXISTS ZooVisitors ("
                + "ID INT AUTO_INCREMENT PRIMARY KEY, "
                + "Name VARCHAR(100), "
                + "TicketNo VARCHAR(50));";

        // Execute SQL queries to create tables
        stmt.execute(createWildlifeTableSQL);
        stmt.execute(createStaffTableSQL);
        stmt.execute(createVisitorTableSQL);
    }

    // Create the GUI components
    private void createGUI() {
        frame = new JFrame("Zoo Wildlife and Management System");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create the tabbed pane
        tabbedPane = new JTabbedPane();

        // Create and add the panels for each feature
        tabbedPane.addTab("Animal Management", createAnimalPanel());
        tabbedPane.addTab("Staff Management", createStaffPanel());
        tabbedPane.addTab("Visitor Management", createVisitorPanel());

        frame.add(tabbedPane);
        frame.setVisible(true);
    }

    // Create Animal Management Panel
    private JPanel createAnimalPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Input fields for animal information
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(5, 2));

        speciesField = new JTextField();
        countField = new JTextField();
        lastObservedField = new JTextField();
        locationField = new JTextField();

        addAnimalButton = new JButton("Add Animal");

        inputPanel.add(new JLabel("Species:"));
        inputPanel.add(speciesField);
        inputPanel.add(new JLabel("Count:"));
        inputPanel.add(countField);
        inputPanel.add(new JLabel("Last Observed:"));
        inputPanel.add(lastObservedField);
        inputPanel.add(new JLabel("Location:"));
        inputPanel.add(locationField);
        inputPanel.add(addAnimalButton);

        // Text area to display animals
        animalTextArea = new JTextArea(10, 40);
        animalTextArea.setEditable(false);
        JScrollPane animalScrollPane = new JScrollPane(animalTextArea);

        // Add Action Listener for adding animal
        addAnimalButton.addActionListener(e -> {
            try {
                addAnimal();
                refreshAnimalTextArea();
            } catch (SQLException ex) {
                showErrorDialog("Error adding animal: " + ex.getMessage());
            }
        });

        // Add input panel and text area to the main panel
        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(animalScrollPane, BorderLayout.CENTER);

        return panel;
    }

    // Create Staff Management Panel
    private JPanel createStaffPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Input fields for staff information
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(4, 2));

        staffNameField = new JTextField();
        staffRoleField = new JTextField();
        salaryField = new JTextField();

        addStaffButton = new JButton("Add Staff");

        inputPanel.add(new JLabel("Staff Name:"));
        inputPanel.add(staffNameField);
        inputPanel.add(new JLabel("Role:"));
        inputPanel.add(staffRoleField);
        inputPanel.add(new JLabel("Salary:"));
        inputPanel.add(salaryField);
        inputPanel.add(addStaffButton);

        // Text area to display staff
        staffTextArea = new JTextArea(10, 40);
        staffTextArea.setEditable(false);
        JScrollPane staffScrollPane = new JScrollPane(staffTextArea);

        // Add Action Listener for adding staff
        addStaffButton.addActionListener(e -> {
            try {
                addStaff();
                refreshStaffTextArea();
            } catch (SQLException ex) {
                showErrorDialog("Error adding staff: " + ex.getMessage());
            }
        });

        // Add input panel and text area to the main panel
        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(staffScrollPane, BorderLayout.CENTER);

        return panel;
    }

    // Create Visitor Management Panel
    private JPanel createVisitorPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Input fields for visitor information
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 2));

        visitorNameField = new JTextField();
        visitorTicketField = new JTextField();

        addVisitorButton = new JButton("Add Visitor");

        inputPanel.add(new JLabel("Visitor Name:"));
        inputPanel.add(visitorNameField);
        inputPanel.add(new JLabel("Ticket No:"));
        inputPanel.add(visitorTicketField);
        inputPanel.add(addVisitorButton);

        // Text area to display visitors
        visitorTextArea = new JTextArea(10, 40);
        visitorTextArea.setEditable(false);
        JScrollPane visitorScrollPane = new JScrollPane(visitorTextArea);

        // Add Action Listener for adding visitor
        addVisitorButton.addActionListener(e -> {
            try {
                addVisitor();
                refreshVisitorTextArea();
            } catch (SQLException ex) {
                showErrorDialog("Error adding visitor: " + ex.getMessage());
            }
        });

        // Add input panel and text area to the main panel
        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(visitorScrollPane, BorderLayout.CENTER);

        return panel;
    }

    // Add Animal Method
    private void addAnimal() throws SQLException {
        String species = speciesField.getText();
        int count = Integer.parseInt(countField.getText());
        String lastObserved = lastObservedField.getText();
        String location = locationField.getText();

        String sql = "INSERT INTO WildlifeMonitoring (Species, Count, LastObserved, Location, AnimalType) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, species);
            pstmt.setInt(2, count);
            pstmt.setString(3, lastObserved);
            pstmt.setString(4, location);
            pstmt.setString(5, "Unknown");
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(frame, "Animal Added Successfully!");
        }
    }

    // Add Staff Method
    private void addStaff() throws SQLException {
        String name = staffNameField.getText();
        String role = staffRoleField.getText();
        double salary = Double.parseDouble(salaryField.getText());

        String sql = "INSERT INTO ZooStaff (Name, Role, Salary) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, role);
            pstmt.setDouble(3, salary);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(frame, "Staff Added Successfully!");
        }
    }

    // Add Visitor Method
    private void addVisitor() throws SQLException {
        String name = visitorNameField.getText();
        String ticketNo = visitorTicketField.getText();

        String sql = "INSERT INTO ZooVisitors (Name, TicketNo) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, ticketNo);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(frame, "Visitor Added Successfully!");
        }
    }

    // Refresh Animal Text Area
    private void refreshAnimalTextArea() {
        try {
            String query = "SELECT * FROM WildlifeMonitoring";
            ResultSet rs = stmt.executeQuery(query);
            StringBuilder sb = new StringBuilder();
            while (rs.next()) {
                sb.append("ID: ").append(rs.getInt("ID")).append(", ")
                        .append("Species: ").append(rs.getString("Species")).append(", ")
                        .append("Count: ").append(rs.getInt("Count")).append(", ")
                        .append("Last Observed: ").append(rs.getDate("LastObserved")).append(", ")
                        .append("Location: ").append(rs.getString("Location")).append(", ")
                        .append("Animal Type: ").append(rs.getString("AnimalType")).append("\n");
            }
            animalTextArea.setText(sb.toString());
        } catch (SQLException ex) {
            showErrorDialog("Error refreshing animal text area: " + ex.getMessage());
        }
    }

    // Refresh Staff Text Area
    private void refreshStaffTextArea() {
        try {
            String query = "SELECT * FROM ZooStaff";
            ResultSet rs = stmt.executeQuery(query);
            StringBuilder sb = new StringBuilder();
            while (rs.next()) {
                sb.append("ID: ").append(rs.getInt("ID")).append(", ")
                        .append("Name: ").append(rs.getString("Name")).append(", ")
                        .append("Role: ").append(rs.getString("Role")).append(", ")
                        .append("Salary: ").append(rs.getDouble("Salary")).append("\n");
            }
            staffTextArea.setText(sb.toString());
        } catch (SQLException ex) {
            showErrorDialog("Error refreshing staff text area: " + ex.getMessage());
        }
    }

    // Refresh Visitor Text Area
    private void refreshVisitorTextArea() {
        try {
            String query = "SELECT * FROM ZooVisitors";
            ResultSet rs = stmt.executeQuery(query);
            StringBuilder sb = new StringBuilder();
            while (rs.next()) {
                sb.append("ID: ").append(rs.getInt("ID")).append(", ")
                        .append("Name: ").append(rs.getString("Name")).append(", ")
                        .append("Ticket No: ").append(rs.getString("TicketNo")).append("\n");
            }
            visitorTextArea.setText(sb.toString());
        } catch (SQLException ex) {
            showErrorDialog("Error refreshing visitor text area: " + ex.getMessage());
        }
    }

    // Helper method to show error dialog
    private static void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
