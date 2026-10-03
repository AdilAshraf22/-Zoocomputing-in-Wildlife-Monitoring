# 🐾 Zoocomputing in Wildlife Monitoring

## #️⃣ Overview

The **Zoocomputing in Wildlife Monitoring** project is a Java-based desktop application designed to streamline the management of zoo operations, including wildlife monitoring, staff management, visitor tracking, and administrative reporting.

The application uses **Java Swing** for the graphical user interface (GUI) and **MySQL** as the backend relational database. **JDBC** is used to connect the Java application with the database.

The system also includes **role-based access control (RBAC)** to provide different levels of access for administrators, staff, and viewers.

# 🚀 Features

• **Animal Management:** Add, view, update, and delete animal records including species, population count, last observed date, location, and animal type.

• **Staff Management:** Add, view, update, and delete staff records including name, job role, and salary information.

• **Visitor Management:** Add, view, update, and delete visitor records including names and ticket numbers.

• **User Authentication:** Users can log in using their username and password.

• **Role-Based Access Control:** Provides different permissions for ADMIN, STAFF, and VIEWER users.

• **CRUD Operations:** Supports Create, Read, Update, and Delete operations for animal, staff, and visitor records.

• **Automated Reporting:** Generates database-based reports containing animal, staff, visitor, salary, and location statistics.

• **Input Validation:** Validates user inputs before performing database operations.

• **User-Friendly Interface:** Uses a tabbed Java Swing interface for easy navigation between different management modules.

# 🔐 Role-Based Access Control

The application provides three different access levels:

| Access Role | Permissions                                                            |
| ----------- | ---------------------------------------------------------------------- |
| **ADMIN**   | Full access including Add, Update, Delete, salary editing, and reports |
| **STAFF**   | Add, Update, and View access; Delete and salary editing are disabled   |
| **VIEWER**  | View-only access                                                       |

The access role determines which operations are available to the logged-in user.

# 📊 Automated Reporting

The application includes an automated reporting module that retrieves information directly from the MySQL database.

The generated report includes:

• Total number of animal records

• Total animal population

• Number of different species

• Total number of staff members

• Total number of visitors

• Average staff salary

• Animal population grouped by location

# 🛠 Technologies Used

• **Java SE:** Primary programming language for application development.

• **Java Swing:** Used for creating the graphical user interface (GUI).

• **MySQL:** Relational database management system for storing and managing zoo data.

• **JDBC:** Used for connecting the Java application with the MySQL database.

• **PreparedStatement:** Used for parameterized database operations.

• **IntelliJ IDEA:** Integrated Development Environment used for development and debugging.

• **Git & GitHub:** Used for version control and project management.

# 🗄️ Database Structure

The project uses a MySQL database named **`zoo`**.

The database contains the following tables:

### 🦁 WildlifeMonitoring

Stores wildlife monitoring information:

• `ID`

• `Species`

• `Count`

• `LastObserved`

• `Location`

• `AnimalType`

### 👨‍💼 ZooStaff

Stores zoo staff information:

• `ID`

• `Name`

• `Role`

• `Salary`

• `AccessRole`

The `Role` field represents the staff member's job role, while `AccessRole` determines the user's application permissions.

### 🎫 ZooVisitors

Stores visitor information:

• `ID`

• `Name`

• `TicketNo`

### 🔑 ZooUsers

Stores application login credentials and access roles:

• `ID`

• `Username`

• `Password`

• `AccessRole`

# 🏗 How to Run

## 1. Clone this repository

```sh
git clone https://github.com/AdilAshraf22/-Zoocomputing-in-Wildlife-Monitoring.git
cd -Zoocomputing-in-Wildlife-Monitoring
```

## 2. Set up MySQL Database

• Install **MySQL** on your system.

• Create a database named `zoo`.

• Run the SQL script provided in the project:

```text
zoo_database.sql
```

The script creates the required tables:

```text
WildlifeMonitoring
ZooStaff
ZooVisitors
ZooUsers
```

For the `ZooStaff` table, the application uses the following additional field:

```sql
AccessRole VARCHAR(20) DEFAULT 'STAFF'
```

The `ZooUsers` table stores the application login accounts.

## 3. Configure the Database Password

The application reads the MySQL password from the Windows environment variable:

```text
DB_PASSWORD
```

The password is **not stored directly in the Java source code**.

On Windows Command Prompt, you can set the environment variable using:

```cmd
setx DB_PASSWORD "your_mysql_password"
```

After setting the variable, restart IntelliJ IDEA.

## 4. Add MySQL JDBC Driver

Make sure the **MySQL Connector/J JDBC driver** is available in the project's classpath.

## 5. Compile and Run the Java Application

• Open the project in **IntelliJ IDEA**.

• Locate:

```text
ZooWildlifeMonitoringApp.java
```

• Run the Java application.

# 🔑 Demo Login Accounts

The project includes the following accounts for local academic demonstration:

| Username | Password    | Access Role |
| -------- | ----------- | ----------- |
| `admin`  | `admin123`  | ADMIN       |
| `staff`  | `staff123`  | STAFF       |
| `viewer` | `viewer123` | VIEWER      |

These accounts are intended for **local academic demonstration purposes only**.

# 🎮 Usage Instructions

## 1. Login

Enter a valid username and password on the login screen.

After successful authentication, the application displays the user's access role.

## 2. Animal Management

• Add new animal records.

• View existing animal records.

• Update animal information.

• Delete animal records when permitted.

Animal information includes:

• Species

• Population Count

• Last Observed Date

• Location

• Animal Type

## 3. Staff Management

• Add new staff members.

• View staff records.

• Update staff information.

• Delete staff records when permitted.

• Manage staff name, job role, and salary.

• Manage application access roles.

## 4. Visitor Management

• Add new visitors.

• View visitor records.

• Update visitor information.

• Delete visitor records when permitted.

Visitor information includes:

• Visitor Name

• Ticket Number

## 5. Role-Based Permissions

The available operations depend on the logged-in user's role.

### ADMIN

The administrator can:

• Add records

• View records

• Update records

• Delete records

• Edit staff salaries

• Generate automated reports

### STAFF

Staff users can:

• Add records

• View records

• Update records

Staff users cannot:

• Delete records

• Edit staff salaries

### VIEWER

Viewer users have read-only access.

Viewers can:

• View existing records

Viewers cannot:

• Add records

• Update records

• Delete records

# 📊 Automated Reports

To generate a report:

1. Log in with an appropriate account.
2. Open the **Reports** tab.
3. Click **Generate Automated Report**.
4. The application retrieves the latest information from the MySQL database.
5. The generated statistics are displayed in the application.

# 📁 Project Structure

```text
-Zoocomputing-in-Wildlife-Monitoring
│
├── src
│   ├── AnimalManagement
│   ├── DatabaseHandler
│   ├── StaffManagement
│   ├── VisitorManagement
│   └── ZooWildlifeMonitoringApp
│
├── README.md
├── zoo_database.sql
└── .gitignore
```

# 🔒 Security Notes

• Database credentials are read using the `DB_PASSWORD` environment variable.

• Database operations use `PreparedStatement` for parameterized database queries.

• Role-based permissions are enforced within the application.

• The demo login credentials are intended only for local academic demonstration.

# 👨‍💻 Contributing

Feel free to fork this repository and contribute to the project.

You can open a pull request with enhancements, improvements, or bug fixes.

# 📜 License

This project is open-source and available under the MIT License.





























