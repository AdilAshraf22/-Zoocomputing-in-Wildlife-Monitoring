CREATE DATABASE IF NOT EXISTS zoo;

USE zoo;

-- Wildlife monitoring table
CREATE TABLE IF NOT EXISTS WildlifeMonitoring (
                                                  ID INT AUTO_INCREMENT PRIMARY KEY,
                                                  Species VARCHAR(100),
    Count INT,
    LastObserved DATE,
    Location VARCHAR(100),
    AnimalType VARCHAR(50)
    );

-- Zoo staff table
CREATE TABLE IF NOT EXISTS ZooStaff (
                                        ID INT AUTO_INCREMENT PRIMARY KEY,
                                        Name VARCHAR(100),
    Role VARCHAR(100),
    Salary DECIMAL(10, 2),
    AccessRole VARCHAR(20) DEFAULT 'STAFF'
    );

-- Zoo visitors table
CREATE TABLE IF NOT EXISTS ZooVisitors (
                                           ID INT AUTO_INCREMENT PRIMARY KEY,
                                           Name VARCHAR(100),
    TicketNo VARCHAR(50)
    );

-- Application users table
CREATE TABLE IF NOT EXISTS ZooUsers (
                                        ID INT AUTO_INCREMENT PRIMARY KEY,
                                        Username VARCHAR(50) UNIQUE NOT NULL,
    Password VARCHAR(100) NOT NULL,
    AccessRole VARCHAR(20) NOT NULL
    );

-- Demo login accounts
INSERT IGNORE INTO ZooUsers (Username, Password, AccessRole)
VALUES
('admin', 'admin123', 'ADMIN'),
('staff', 'staff123', 'STAFF'),
('viewer', 'viewer123', 'VIEWER');
