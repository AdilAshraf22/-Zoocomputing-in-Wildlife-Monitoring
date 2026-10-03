import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ZooWildlifeMonitoringApp {

    // ================= DATABASE =================
    private static final String DB_URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = System.getenv("DB_PASSWORD");

    private static Connection conn;
    private static Statement stmt;

    // ================= USER / RBAC =================
    private String loggedInUser;
    private String accessRole;

    // ================= GUI =================
    private JFrame frame;
    private JTabbedPane tabbedPane;

    // Animal
    private JTextField animalIdField;
    private JTextField speciesField;
    private JTextField countField;
    private JTextField lastObservedField;
    private JTextField locationField;

    private JButton addAnimalButton;
    private JButton updateAnimalButton;
    private JButton deleteAnimalButton;

    private JTextArea animalTextArea;

    // Staff
    private JTextField staffIdField;
    private JTextField staffNameField;
    private JTextField staffRoleField;
    private JTextField salaryField;

    private JButton addStaffButton;
    private JButton updateStaffButton;
    private JButton deleteStaffButton;

    private JTextArea staffTextArea;

    // Visitor
    private JTextField visitorIdField;
    private JTextField visitorNameField;
    private JTextField visitorTicketField;

    private JButton addVisitorButton;
    private JButton updateVisitorButton;
    private JButton deleteVisitorButton;

    private JTextArea visitorTextArea;

    // Report
    private JTextArea reportTextArea;


    // ================= MAIN =================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            try {

                ZooWildlifeMonitoringApp app =
                        new ZooWildlifeMonitoringApp();

                app.createDatabase();

                if (!app.login()) {
                    app.closeConnection();
                    return;
                }

                app.createGUI();

                app.refreshAll();

            } catch (SQLException e) {

                showErrorDialog(
                        "Database error: " + e.getMessage()
                );
            }
        });
    }


    // ================= CONSTRUCTOR =================

    public ZooWildlifeMonitoringApp() {

        try {

            conn = DriverManager.getConnection(
                    DB_URL,
                    DB_USER,
                    DB_PASSWORD
            );

            stmt = conn.createStatement();

        } catch (SQLException e) {

            showErrorDialog(
                    "Error connecting to database: "
                            + e.getMessage()
            );
        }
    }


    // ================= DATABASE SETUP =================

    private void createDatabase() throws SQLException {

        // Create database if it does not already exist
        stmt.executeUpdate(
                "CREATE DATABASE IF NOT EXISTS zoo"
        );

        conn.close();

        // Connect directly to zoo database
        conn = DriverManager.getConnection(
                DB_URL + "zoo",
                DB_USER,
                DB_PASSWORD
        );

        stmt = conn.createStatement();


        // Wildlife table
        stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS WildlifeMonitoring (" +
                        "ID INT AUTO_INCREMENT PRIMARY KEY, " +
                        "Species VARCHAR(100), " +
                        "Count INT, " +
                        "LastObserved DATE, " +
                        "Location VARCHAR(100), " +
                        "AnimalType VARCHAR(50))"
        );


        // Staff table
        stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS ZooStaff (" +
                        "ID INT AUTO_INCREMENT PRIMARY KEY, " +
                        "Name VARCHAR(100), " +
                        "Role VARCHAR(100), " +
                        "Salary DECIMAL(10,2), " +
                        "AccessRole VARCHAR(20) DEFAULT 'STAFF')"
        );


        // Visitor table
        stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS ZooVisitors (" +
                        "ID INT AUTO_INCREMENT PRIMARY KEY, " +
                        "Name VARCHAR(100), " +
                        "TicketNo VARCHAR(50))"
        );


        // Login table
        stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS ZooUsers (" +
                        "ID INT AUTO_INCREMENT PRIMARY KEY, " +
                        "Username VARCHAR(50) UNIQUE NOT NULL, " +
                        "Password VARCHAR(100) NOT NULL, " +
                        "AccessRole VARCHAR(20) NOT NULL)"
        );
    }


    // ================= LOGIN =================

    private boolean login() throws SQLException {

        JPanel panel =
                new JPanel(new GridLayout(2, 2, 10, 10));

        JTextField usernameField =
                new JTextField();

        JPasswordField passwordField =
                new JPasswordField();

        panel.add(new JLabel("Username:"));
        panel.add(usernameField);

        panel.add(new JLabel("Password:"));
        panel.add(passwordField);


        int result = JOptionPane.showConfirmDialog(
                null,
                panel,
                "Zoo Management System - Login",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );


        if (result != JOptionPane.OK_OPTION) {
            return false;
        }


        String username =
                usernameField.getText().trim();

        String password =
                new String(
                        passwordField.getPassword()
                );


        String sql =
                "SELECT AccessRole FROM ZooUsers " +
                        "WHERE Username = ? AND Password = ?";


        try (PreparedStatement pstmt =
                     conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);

            ResultSet rs =
                    pstmt.executeQuery();


            if (rs.next()) {

                loggedInUser = username;

                accessRole =
                        rs.getString("AccessRole");


                JOptionPane.showMessageDialog(
                        null,
                        "Login successful!\nRole: "
                                + accessRole,
                        "Welcome",
                        JOptionPane.INFORMATION_MESSAGE
                );

                return true;
            }
        }


        JOptionPane.showMessageDialog(
                null,
                "Invalid username or password.",
                "Login Failed",
                JOptionPane.ERROR_MESSAGE
        );


        return login();
    }


    // ================= CREATE GUI =================

    private void createGUI() {

        frame = new JFrame(
                "Zoo Wildlife and Management System - "
                        + loggedInUser
                        + " ("
                        + accessRole
                        + ")"
        );


        frame.setSize(950, 650);

        frame.setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        frame.setLocationRelativeTo(null);


        tabbedPane = new JTabbedPane();


        // Create every panel first
        tabbedPane.addTab(
                "Animal Management",
                createAnimalPanel()
        );


        tabbedPane.addTab(
                "Staff Management",
                createStaffPanel()
        );


        tabbedPane.addTab(
                "Visitor Management",
                createVisitorPanel()
        );


        tabbedPane.addTab(
                "Reports",
                createReportPanel()
        );


        // IMPORTANT:
        // Apply permissions ONLY after all panels
        // and their components have been created.
        applyPermissions();


        frame.add(tabbedPane);

        frame.setVisible(true);
    }


    // ================= ANIMAL PANEL =================

    private JPanel createAnimalPanel() {

        JPanel panel =
                new JPanel(new BorderLayout());


        JPanel inputPanel =
                new JPanel(
                        new GridLayout(6, 2, 5, 5)
                );


        animalIdField = new JTextField();

        speciesField = new JTextField();

        countField = new JTextField();

        lastObservedField = new JTextField();

        locationField = new JTextField();


        addAnimalButton =
                new JButton("Add");

        updateAnimalButton =
                new JButton("Update");

        deleteAnimalButton =
                new JButton("Delete");


        inputPanel.add(
                new JLabel("Animal ID:")
        );

        inputPanel.add(
                animalIdField
        );


        inputPanel.add(
                new JLabel("Species:")
        );

        inputPanel.add(
                speciesField
        );


        inputPanel.add(
                new JLabel("Count:")
        );

        inputPanel.add(
                countField
        );


        inputPanel.add(
                new JLabel(
                        "Last Observed (YYYY-MM-DD):"
                )
        );

        inputPanel.add(
                lastObservedField
        );


        inputPanel.add(
                new JLabel("Location:")
        );

        inputPanel.add(
                locationField
        );


        JPanel buttonPanel =
                new JPanel();

        buttonPanel.add(
                addAnimalButton
        );

        buttonPanel.add(
                updateAnimalButton
        );

        buttonPanel.add(
                deleteAnimalButton
        );


        inputPanel.add(
                new JLabel("Actions:")
        );

        inputPanel.add(
                buttonPanel
        );


        animalTextArea =
                new JTextArea();

        animalTextArea.setEditable(false);


        panel.add(
                inputPanel,
                BorderLayout.NORTH
        );

        panel.add(
                new JScrollPane(animalTextArea),
                BorderLayout.CENTER
        );


        // ADD
        addAnimalButton.addActionListener(e -> {

            try {

                addAnimal();

                refreshAnimalTextArea();

                clearAnimalFields();

            } catch (Exception ex) {

                showErrorDialog(
                        "Error adding animal: "
                                + ex.getMessage()
                );
            }
        });


        // UPDATE
        updateAnimalButton.addActionListener(e -> {

            try {

                updateAnimal();

                refreshAnimalTextArea();

            } catch (Exception ex) {

                showErrorDialog(
                        "Error updating animal: "
                                + ex.getMessage()
                );
            }
        });


        // DELETE
        deleteAnimalButton.addActionListener(e -> {

            try {

                deleteAnimal();

                refreshAnimalTextArea();

                clearAnimalFields();

            } catch (Exception ex) {

                showErrorDialog(
                        "Error deleting animal: "
                                + ex.getMessage()
                );
            }
        });


        // DO NOT CALL applyPermissions() HERE

        return panel;
    }


    // ================= STAFF PANEL =================

    private JPanel createStaffPanel() {

        JPanel panel =
                new JPanel(new BorderLayout());


        JPanel inputPanel =
                new JPanel(
                        new GridLayout(5, 2, 5, 5)
                );


        staffIdField =
                new JTextField();

        staffNameField =
                new JTextField();

        staffRoleField =
                new JTextField();

        salaryField =
                new JTextField();


        addStaffButton =
                new JButton("Add");

        updateStaffButton =
                new JButton("Update");

        deleteStaffButton =
                new JButton("Delete");


        inputPanel.add(
                new JLabel("Staff ID:")
        );

        inputPanel.add(
                staffIdField
        );


        inputPanel.add(
                new JLabel("Name:")
        );

        inputPanel.add(
                staffNameField
        );


        inputPanel.add(
                new JLabel("Job Role:")
        );

        inputPanel.add(
                staffRoleField
        );


        inputPanel.add(
                new JLabel("Salary:")
        );

        inputPanel.add(
                salaryField
        );


        JPanel buttonPanel =
                new JPanel();


        buttonPanel.add(
                addStaffButton
        );

        buttonPanel.add(
                updateStaffButton
        );

        buttonPanel.add(
                deleteStaffButton
        );


        inputPanel.add(
                new JLabel("Actions:")
        );

        inputPanel.add(
                buttonPanel
        );


        staffTextArea =
                new JTextArea();

        staffTextArea.setEditable(false);


        panel.add(
                inputPanel,
                BorderLayout.NORTH
        );

        panel.add(
                new JScrollPane(staffTextArea),
                BorderLayout.CENTER
        );


        // ADD STAFF
        addStaffButton.addActionListener(e -> {

            try {

                addStaff();

                refreshStaffTextArea();

                clearStaffFields();

            } catch (Exception ex) {

                showErrorDialog(
                        "Error adding staff: "
                                + ex.getMessage()
                );
            }
        });


        // UPDATE STAFF
        updateStaffButton.addActionListener(e -> {

            try {

                updateStaff();

                refreshStaffTextArea();

            } catch (Exception ex) {

                showErrorDialog(
                        "Error updating staff: "
                                + ex.getMessage()
                );
            }
        });


        // DELETE STAFF
        deleteStaffButton.addActionListener(e -> {

            try {

                deleteStaff();

                refreshStaffTextArea();

                clearStaffFields();

            } catch (Exception ex) {

                showErrorDialog(
                        "Error deleting staff: "
                                + ex.getMessage()
                );
            }
        });


        // DO NOT CALL applyPermissions() HERE

        return panel;
    }


    // ================= VISITOR PANEL =================

    private JPanel createVisitorPanel() {

        JPanel panel =
                new JPanel(new BorderLayout());


        JPanel inputPanel =
                new JPanel(
                        new GridLayout(4, 2, 5, 5)
                );


        visitorIdField =
                new JTextField();

        visitorNameField =
                new JTextField();

        visitorTicketField =
                new JTextField();


        addVisitorButton =
                new JButton("Add");

        updateVisitorButton =
                new JButton("Update");

        deleteVisitorButton =
                new JButton("Delete");


        inputPanel.add(
                new JLabel("Visitor ID:")
        );

        inputPanel.add(
                visitorIdField
        );


        inputPanel.add(
                new JLabel("Name:")
        );

        inputPanel.add(
                visitorNameField
        );


        inputPanel.add(
                new JLabel("Ticket No:")
        );

        inputPanel.add(
                visitorTicketField
        );


        JPanel buttonPanel =
                new JPanel();


        buttonPanel.add(
                addVisitorButton
        );

        buttonPanel.add(
                updateVisitorButton
        );

        buttonPanel.add(
                deleteVisitorButton
        );


        inputPanel.add(
                new JLabel("Actions:")
        );

        inputPanel.add(
                buttonPanel
        );


        visitorTextArea =
                new JTextArea();

        visitorTextArea.setEditable(false);


        panel.add(
                inputPanel,
                BorderLayout.NORTH
        );

        panel.add(
                new JScrollPane(visitorTextArea),
                BorderLayout.CENTER
        );


        // ADD VISITOR
        addVisitorButton.addActionListener(e -> {

            try {

                addVisitor();

                refreshVisitorTextArea();

                clearVisitorFields();

            } catch (Exception ex) {

                showErrorDialog(
                        "Error adding visitor: "
                                + ex.getMessage()
                );
            }
        });


        // UPDATE VISITOR
        updateVisitorButton.addActionListener(e -> {

            try {

                updateVisitor();

                refreshVisitorTextArea();

            } catch (Exception ex) {

                showErrorDialog(
                        "Error updating visitor: "
                                + ex.getMessage()
                );
            }
        });


        // DELETE VISITOR
        deleteVisitorButton.addActionListener(e -> {

            try {

                deleteVisitor();

                refreshVisitorTextArea();

                clearVisitorFields();

            } catch (Exception ex) {

                showErrorDialog(
                        "Error deleting visitor: "
                                + ex.getMessage()
                );
            }
        });


        // DO NOT CALL applyPermissions() HERE

        return panel;
    }


    // ================= REPORT PANEL =================

    private JPanel createReportPanel() {

        JPanel panel =
                new JPanel(new BorderLayout());


        JButton generateReportButton =
                new JButton(
                        "Generate Automated Report"
                );


        reportTextArea =
                new JTextArea();

        reportTextArea.setEditable(false);


        generateReportButton.addActionListener(e -> {

            try {

                generateReport();

            } catch (SQLException ex) {

                showErrorDialog(
                        "Error generating report: "
                                + ex.getMessage()
                );
            }
        });


        panel.add(
                generateReportButton,
                BorderLayout.NORTH
        );


        panel.add(
                new JScrollPane(reportTextArea),
                BorderLayout.CENTER
        );


        return panel;
    }


    // ================= RBAC =================

    private void applyPermissions() {

        boolean isAdmin =
                accessRole.equals("ADMIN");

        boolean isStaff =
                accessRole.equals("STAFF");

        boolean isViewer =
                accessRole.equals("VIEWER");


        // ================= VIEWER =================
        // Can only view and generate reports

        if (isViewer) {

            addAnimalButton.setEnabled(false);
            updateAnimalButton.setEnabled(false);
            deleteAnimalButton.setEnabled(false);


            addStaffButton.setEnabled(false);
            updateStaffButton.setEnabled(false);
            deleteStaffButton.setEnabled(false);


            addVisitorButton.setEnabled(false);
            updateVisitorButton.setEnabled(false);
            deleteVisitorButton.setEnabled(false);
        }


        // ================= STAFF =================
        // Can add/update but cannot delete
        // Cannot edit salary

        if (isStaff) {

            deleteAnimalButton.setEnabled(false);

            deleteStaffButton.setEnabled(false);

            deleteVisitorButton.setEnabled(false);


            salaryField.setEnabled(false);
        }


        // ================= ADMIN =================
        // Full access

        if (isAdmin) {

            addAnimalButton.setEnabled(true);
            updateAnimalButton.setEnabled(true);
            deleteAnimalButton.setEnabled(true);


            addStaffButton.setEnabled(true);
            updateStaffButton.setEnabled(true);
            deleteStaffButton.setEnabled(true);


            addVisitorButton.setEnabled(true);
            updateVisitorButton.setEnabled(true);
            deleteVisitorButton.setEnabled(true);


            salaryField.setEnabled(true);
        }
    }


    // ================= ANIMAL CRUD =================

    private void addAnimal() throws SQLException {

        String sql =
                "INSERT INTO WildlifeMonitoring " +
                        "(Species, Count, LastObserved, Location, AnimalType) " +
                        "VALUES (?, ?, ?, ?, ?)";


        try (PreparedStatement pstmt =
                     conn.prepareStatement(sql)) {

            pstmt.setString(
                    1,
                    speciesField.getText()
            );

            pstmt.setInt(
                    2,
                    Integer.parseInt(
                            countField.getText()
                    )
            );

            pstmt.setString(
                    3,
                    lastObservedField.getText()
            );

            pstmt.setString(
                    4,
                    locationField.getText()
            );

            pstmt.setString(
                    5,
                    "Wildlife"
            );


            pstmt.executeUpdate();


            JOptionPane.showMessageDialog(
                    frame,
                    "Animal added successfully!"
            );
        }
    }


    private void updateAnimal()
            throws SQLException {

        int id =
                Integer.parseInt(
                        animalIdField.getText()
                );


        String sql =
                "UPDATE WildlifeMonitoring " +
                        "SET Species=?, Count=?, " +
                        "LastObserved=?, Location=? " +
                        "WHERE ID=?";


        try (PreparedStatement pstmt =
                     conn.prepareStatement(sql)) {

            pstmt.setString(
                    1,
                    speciesField.getText()
            );

            pstmt.setInt(
                    2,
                    Integer.parseInt(
                            countField.getText()
                    )
            );

            pstmt.setString(
                    3,
                    lastObservedField.getText()
            );

            pstmt.setString(
                    4,
                    locationField.getText()
            );

            pstmt.setInt(
                    5,
                    id
            );


            int rows =
                    pstmt.executeUpdate();


            JOptionPane.showMessageDialog(
                    frame,
                    rows > 0
                            ? "Animal updated successfully!"
                            : "Animal ID not found."
            );
        }
    }


    private void deleteAnimal()
            throws SQLException {

        int id =
                Integer.parseInt(
                        animalIdField.getText()
                );


        String sql =
                "DELETE FROM WildlifeMonitoring " +
                        "WHERE ID=?";


        try (PreparedStatement pstmt =
                     conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);


            int rows =
                    pstmt.executeUpdate();


            JOptionPane.showMessageDialog(
                    frame,
                    rows > 0
                            ? "Animal deleted successfully!"
                            : "Animal ID not found."
            );
        }
    }


    // ================= STAFF CRUD =================

    private void addStaff() throws SQLException {

        String sql =
                "INSERT INTO ZooStaff " +
                        "(Name, Role, Salary, AccessRole) " +
                        "VALUES (?, ?, ?, 'STAFF')";


        try (PreparedStatement pstmt =
                     conn.prepareStatement(sql)) {

            pstmt.setString(
                    1,
                    staffNameField.getText()
            );

            pstmt.setString(
                    2,
                    staffRoleField.getText()
            );


            double salary;


            if (accessRole.equals("STAFF")) {

                // Staff cannot edit salary.
                // New staff records added by STAFF
                // receive salary 0.

                salary = 0.0;

            } else {

                salary =
                        Double.parseDouble(
                                salaryField.getText()
                        );
            }


            pstmt.setDouble(
                    3,
                    salary
            );


            pstmt.executeUpdate();


            JOptionPane.showMessageDialog(
                    frame,
                    "Staff added successfully!"
            );
        }
    }


    private void updateStaff()
            throws SQLException {

        int id =
                Integer.parseInt(
                        staffIdField.getText()
                );


        if (accessRole.equals("ADMIN")) {

            String sql =
                    "UPDATE ZooStaff " +
                            "SET Name=?, Role=?, Salary=? " +
                            "WHERE ID=?";


            try (PreparedStatement pstmt =
                         conn.prepareStatement(sql)) {

                pstmt.setString(
                        1,
                        staffNameField.getText()
                );

                pstmt.setString(
                        2,
                        staffRoleField.getText()
                );

                pstmt.setDouble(
                        3,
                        Double.parseDouble(
                                salaryField.getText()
                        )
                );

                pstmt.setInt(
                        4,
                        id
                );


                pstmt.executeUpdate();
            }

        } else {

            String sql =
                    "UPDATE ZooStaff " +
                            "SET Name=?, Role=? " +
                            "WHERE ID=?";


            try (PreparedStatement pstmt =
                         conn.prepareStatement(sql)) {

                pstmt.setString(
                        1,
                        staffNameField.getText()
                );

                pstmt.setString(
                        2,
                        staffRoleField.getText()
                );

                pstmt.setInt(
                        3,
                        id
                );


                pstmt.executeUpdate();
            }
        }


        JOptionPane.showMessageDialog(
                frame,
                "Staff updated successfully!"
        );
    }


    private void deleteStaff()
            throws SQLException {

        int id =
                Integer.parseInt(
                        staffIdField.getText()
                );


        String sql =
                "DELETE FROM ZooStaff WHERE ID=?";


        try (PreparedStatement pstmt =
                     conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);


            int rows =
                    pstmt.executeUpdate();


            JOptionPane.showMessageDialog(
                    frame,
                    rows > 0
                            ? "Staff deleted successfully!"
                            : "Staff ID not found."
            );
        }
    }


    // ================= VISITOR CRUD =================

    private void addVisitor()
            throws SQLException {

        String sql =
                "INSERT INTO ZooVisitors " +
                        "(Name, TicketNo) VALUES (?, ?)";


        try (PreparedStatement pstmt =
                     conn.prepareStatement(sql)) {

            pstmt.setString(
                    1,
                    visitorNameField.getText()
            );

            pstmt.setString(
                    2,
                    visitorTicketField.getText()
            );


            pstmt.executeUpdate();


            JOptionPane.showMessageDialog(
                    frame,
                    "Visitor added successfully!"
            );
        }
    }


    private void updateVisitor()
            throws SQLException {

        int id =
                Integer.parseInt(
                        visitorIdField.getText()
                );


        String sql =
                "UPDATE ZooVisitors " +
                        "SET Name=?, TicketNo=? " +
                        "WHERE ID=?";


        try (PreparedStatement pstmt =
                     conn.prepareStatement(sql)) {

            pstmt.setString(
                    1,
                    visitorNameField.getText()
            );

            pstmt.setString(
                    2,
                    visitorTicketField.getText()
            );

            pstmt.setInt(
                    3,
                    id
            );


            int rows =
                    pstmt.executeUpdate();


            JOptionPane.showMessageDialog(
                    frame,
                    rows > 0
                            ? "Visitor updated successfully!"
                            : "Visitor ID not found."
            );
        }
    }


    private void deleteVisitor()
            throws SQLException {

        int id =
                Integer.parseInt(
                        visitorIdField.getText()
                );


        String sql =
                "DELETE FROM ZooVisitors " +
                        "WHERE ID=?";


        try (PreparedStatement pstmt =
                     conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);


            int rows =
                    pstmt.executeUpdate();


            JOptionPane.showMessageDialog(
                    frame,
                    rows > 0
                            ? "Visitor deleted successfully!"
                            : "Visitor ID not found."
            );
        }
    }


    // ================= REFRESH ALL =================

    private void refreshAll() {

        refreshAnimalTextArea();

        refreshStaffTextArea();

        refreshVisitorTextArea();
    }


    // ================= REFRESH ANIMALS =================

    private void refreshAnimalTextArea() {

        try {

            String query =
                    "SELECT * FROM WildlifeMonitoring";


            ResultSet rs =
                    stmt.executeQuery(query);


            StringBuilder sb =
                    new StringBuilder();


            while (rs.next()) {

                sb.append("ID: ")
                        .append(
                                rs.getInt("ID")
                        )

                        .append(" | Species: ")
                        .append(
                                rs.getString("Species")
                        )

                        .append(" | Count: ")
                        .append(
                                rs.getInt("Count")
                        )

                        .append(
                                " | Last Observed: "
                        )

                        .append(
                                rs.getDate(
                                        "LastObserved"
                                )
                        )

                        .append(" | Location: ")
                        .append(
                                rs.getString("Location")
                        )

                        .append("\n");
            }


            animalTextArea.setText(
                    sb.toString()
            );

        } catch (SQLException ex) {

            showErrorDialog(
                    "Error loading animals: "
                            + ex.getMessage()
            );
        }
    }


    // ================= REFRESH STAFF =================

    private void refreshStaffTextArea() {

        try {

            String query =
                    "SELECT * FROM ZooStaff";


            ResultSet rs =
                    stmt.executeQuery(query);


            StringBuilder sb =
                    new StringBuilder();


            while (rs.next()) {

                sb.append("ID: ")
                        .append(
                                rs.getInt("ID")
                        )

                        .append(" | Name: ")
                        .append(
                                rs.getString("Name")
                        )

                        .append(" | Role: ")
                        .append(
                                rs.getString("Role")
                        )

                        .append(" | Salary: ")
                        .append(
                                rs.getDouble("Salary")
                        )

                        .append(" | Access: ")
                        .append(
                                rs.getString(
                                        "AccessRole"
                                )
                        )

                        .append("\n");
            }


            staffTextArea.setText(
                    sb.toString()
            );

        } catch (SQLException ex) {

            showErrorDialog(
                    "Error loading staff: "
                            + ex.getMessage()
            );
        }
    }


    // ================= REFRESH VISITORS =================

    private void refreshVisitorTextArea() {

        try {

            String query =
                    "SELECT * FROM ZooVisitors";


            ResultSet rs =
                    stmt.executeQuery(query);


            StringBuilder sb =
                    new StringBuilder();


            while (rs.next()) {

                sb.append("ID: ")
                        .append(
                                rs.getInt("ID")
                        )

                        .append(" | Name: ")
                        .append(
                                rs.getString("Name")
                        )

                        .append(" | Ticket: ")
                        .append(
                                rs.getString("TicketNo")
                        )

                        .append("\n");
            }


            visitorTextArea.setText(
                    sb.toString()
            );

        } catch (SQLException ex) {

            showErrorDialog(
                    "Error loading visitors: "
                            + ex.getMessage()
            );
        }
    }


    // ================= AUTOMATED REPORT =================

    private void generateReport()
            throws SQLException {

        StringBuilder report =
                new StringBuilder();


        report.append(
                "========================================\n"
        );

        report.append(
                "       ZOO WILDLIFE MONITORING REPORT\n"
        );

        report.append(
                "========================================\n\n"
        );


        // Total animal records
        ResultSet rs =
                stmt.executeQuery(
                        "SELECT COUNT(*) AS total " +
                                "FROM WildlifeMonitoring"
                );


        if (rs.next()) {

            report.append(
                    "Total Animal Records: "
            );

            report.append(
                    rs.getInt("total")
            );

            report.append("\n");
        }


        // Total animals
        rs =
                stmt.executeQuery(
                        "SELECT COALESCE(SUM(Count),0) " +
                                "AS total " +
                                "FROM WildlifeMonitoring"
                );


        if (rs.next()) {

            report.append(
                    "Total Animals: "
            );

            report.append(
                    rs.getInt("total")
            );

            report.append("\n");
        }


        // Different species
        rs =
                stmt.executeQuery(
                        "SELECT COUNT(DISTINCT Species) " +
                                "AS total " +
                                "FROM WildlifeMonitoring"
                );


        if (rs.next()) {

            report.append(
                    "Different Species: "
            );

            report.append(
                    rs.getInt("total")
            );

            report.append("\n");
        }


        // Total staff
        rs =
                stmt.executeQuery(
                        "SELECT COUNT(*) AS total " +
                                "FROM ZooStaff"
                );


        if (rs.next()) {

            report.append(
                    "Total Staff: "
            );

            report.append(
                    rs.getInt("total")
            );

            report.append("\n");
        }


        // Total visitors
        rs =
                stmt.executeQuery(
                        "SELECT COUNT(*) AS total " +
                                "FROM ZooVisitors"
                );


        if (rs.next()) {

            report.append(
                    "Total Visitors: "
            );

            report.append(
                    rs.getInt("total")
            );

            report.append("\n");
        }


        // Average salary
        rs =
                stmt.executeQuery(
                        "SELECT COALESCE(AVG(Salary),0) " +
                                "AS avgSalary " +
                                "FROM ZooStaff"
                );


        if (rs.next()) {

            report.append(
                    "Average Staff Salary: "
            );

            report.append(
                    String.format(
                            "%.2f",
                            rs.getDouble(
                                    "avgSalary"
                            )
                    )
            );

            report.append("\n");
        }


        // Animals by location
        report.append(
                "\n----------------------------------------\n"
        );

        report.append(
                "Animals by Location\n"
        );

        report.append(
                "----------------------------------------\n"
        );


        rs =
                stmt.executeQuery(
                        "SELECT Location, " +
                                "SUM(Count) AS total " +
                                "FROM WildlifeMonitoring " +
                                "GROUP BY Location"
                );


        while (rs.next()) {

            report.append(
                    rs.getString("Location")
            );

            report.append(" : ");

            report.append(
                    rs.getInt("total")
            );

            report.append(
                    " animals\n"
            );
        }


        report.append(
                "\nReport generated automatically "
                        + "from database."
        );


        reportTextArea.setText(
                report.toString()
        );
    }


    // ================= CLEAR FIELDS =================

    private void clearAnimalFields() {

        animalIdField.setText("");

        speciesField.setText("");

        countField.setText("");

        lastObservedField.setText("");

        locationField.setText("");
    }


    private void clearStaffFields() {

        staffIdField.setText("");

        staffNameField.setText("");

        staffRoleField.setText("");

        salaryField.setText("");
    }


    private void clearVisitorFields() {

        visitorIdField.setText("");

        visitorNameField.setText("");

        visitorTicketField.setText("");
    }


    // ================= CLOSE CONNECTION =================

    private void closeConnection() {

        try {

            if (stmt != null) {
                stmt.close();
            }

            if (conn != null) {
                conn.close();
            }

        } catch (SQLException ignored) {
        }
    }


    // ================= ERROR DIALOG =================

    private static void showErrorDialog(
            String message
    ) {

        JOptionPane.showMessageDialog(
                null,
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}