# 🐾 Overview

The Zoocomputing in Wildlife Monitoring project is a Java-based desktop application designed to streamline the management of zoo operations, including wildlife monitoring, staff management, and visitor tracking. The application uses Java Swing for the graphical user interface (GUI) and MySQL as the backend database to store and manage data.

# 🚀 Features

• Animal Management: Add and manage animal species, population count, last observed dates, and location within the zoo.

• Staff Management: Add and track zoo staff details, including name, role, and salary information.

• Visitor Management: Track visitor details, including names and ticket numbers.

• Real-Time Updates: Refresh and display updated records in real-time.

• Input Validation: Ensures data integrity by validating user inputs.

• User-Friendly Interface: Intuitive tabbed interface for easy navigation.

# 🛠 Technologies Used

• Java SE (Standard Edition): Primary programming language for application development.

• Java Swing: Used for creating the graphical user interface (GUI).

• MySQL: Relational database management system for storing and managing data.

• JDBC (Java Database Connectivity): API for connecting the Java application to the MySQL database.

• IntelliJ IDEA/Eclipse: Integrated Development Environment (IDE) for development and debugging.

# 🏗 How to Run

1. Clone this repository:
   ```sh
   git clone https://github.com/AdilAshraf22/-Zoocomputing-in-Wildlife-Monitoring.git
   cd zoocomputing-wildlife-monitoring

2. Set up MySQL Database:

   • Install MySQL on your system.

   • Create a database named - zoo

   • Run the following SQL commands to create the necessary tables
```sh
CREATE TABLE IF NOT EXISTS WildlifeMonitoring (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    Species VARCHAR(100),
    Count INT,
    LastObserved DATE,
    Location VARCHAR(100),
    AnimalType VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS ZooStaff (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    Name VARCHAR(100),
    Role VARCHAR(100),
    Salary DECIMAL(10, 2)
);

CREATE TABLE IF NOT EXISTS ZooVisitors (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    Name VARCHAR(100),
    TicketNo VARCHAR(50)
);
```

3. Compile and Run the Java Application:

   • Open the project in your preferred IDE (IntelliJ IDEA or Eclipse).

   • Ensure the MySQL JDBC driver is added to your project's classpath.

   • Update the database credentials in the ZooWildlifeMonitoringApp.java file:
```sh
private static final String DB_URL = "jdbc:mysql://localhost:3306/zoo";
private static final String DB_USER = "root";
private static final String DB_PASSWORD = "<your_mysql_password>";
```

   • Compile and run the ZooWildlifeMonitoringApp.java file.

4. Run the Application:

   • The application will launch with a GUI interface.

   • Use the tabs to navigate between Animal Management, Staff Management, and Visitor Management.

# 🎮 Usage Instructions

1. Animal Management:

   • Add new animals by entering species, count, last observed date, and location.

   • View and update existing animal records.

2. Staff Management:

   • Add new staff members by entering their name, role, and salary.

   • View and update staff records.

3. Visitor Management:

   • Add new visitors by entering their name and ticket number.

   • View visitor records.

4. Refresh Data: Click the refresh button to update the displayed records in real-time.

# 👨‍💻 Contributing
Feel free to fork this repository and contribute to the project. Open a pull request with any enhancements or bug fixes.

# 📜 License
This project is open-source and available under the MIT License.




























