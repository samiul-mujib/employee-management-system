================================================
  TEAM DATAFORGE — Employee Management System
  Setup & Run Instructions
================================================

REQUIREMENTS
------------
- Java JDK 8 or higher
- MySQL Server (running locally)
- MySQL Connector/J JAR file (provided in /lib folder)
- VS Code (or any Java-capable IDE)


STEP 1 — SET UP THE DATABASE
-----------------------------
1. Open MySQL Workbench, dBeaver, or any MySQL client
2. Run the schema script first:
      schema.sql
3. Then run the data/seed script:
      data.sql
   These will create the 'employeedataproject' database
   and populate the required tables.
4. Then run this script for creating the user table and adding the users to your database:

CREATE TABLE users (
    userID    INT AUTO_INCREMENT PRIMARY KEY,
    empID     INT NOT NULL,
    username  VARCHAR(50) NOT NULL UNIQUE,
    password  VARCHAR(64) NOT NULL,
    role      ENUM('HR_ADMIN', 'EMPLOYEE') NOT NULL,
    CONSTRAINT fk_user_emp FOREIGN KEY (empID) REFERENCES employees(empID)
);

-- Insert default users
INSERT INTO users (username, password, role, empID)
VALUES
  ('admin',   SHA2('admin123456',   256), 'HR_ADMIN', 1),
  ('charlie', SHA2('charlie123456', 256), 'EMPLOYEE', 2),
  ('lucy',    SHA2('lucy123456',    256), 'EMPLOYEE', 3),
  ('elmer',   SHA2('elmer123456',   256), 'EMPLOYEE', 4);


STEP 2 — OPEN THE PROJECT
--------------------------
1. Open VS Code
2. Go to File > Open Folder
3. Select the project root folder (the one containing /src and /lib)


STEP 3 — CONFIGURE YOUR DATABASE CONNECTION
--------------------------------------------
1. Navigate to:
      src/config.properties
2. Open the file and fill in YOUR MySQL credentials:

      db.url=jdbc:mysql://localhost:3306/employeedataproject?allowPublicKeyRetrieval=true&useSSL=false
      db.user=YOUR_MYSQL_USERNAME
      db.password=YOUR_MYSQL_PASSWORD

   Example:
      db.user=root
      db.password=mypassword123

   NOTE: Do NOT share or copy someone else's config.properties.
   Each person must enter their own MySQL credentials.


STEP 4 — VERIFY THE CLASSPATH (MySQL Connector JAR)
----------------------------------------------------
The /lib folder contains the MySQL Connector JAR file.
If VS Code shows errors related to DBConnection.java:
1. Open .vscode/settings.json
2. Make sure this line exists and points to the JAR:

      "java.project.referencedLibraries": ["lib/*.jar"]

If settings.json doesn't exist, create it inside the .vscode folder
with the content above.


STEP 5 — RUN THE PROGRAM
-------------------------
1. Open src/Main.java in VS Code
2. Click the Run button (top right) or right-click > Run Java
3. The program will launch in the terminal


DEFAULT LOGIN CREDENTIALS
--------------------------
   HR Admin:
      Username: admin
      Password: admin123456

   Employee:
      Username: charlie
      Password: charlie123456
      Username: elmer
      Password: elmer123456



FOLDER STRUCTURE
----------------
   project-root/
   ├── lib/
   │     └── mysql-connector-j-x.x.x.jar
   ├── src/
   │     ├── Main.java
   │     ├── DBConnection.java
   │     ├── AuthManager.java
   │     ├── Employee.java
   │     ├── Address.java
   │     ├── MainMenu.java
   │     ├── HRAdminMenu.java
   │     ├── EmployeeMenu.java
   │     ├── InputValidator.java
   │     └── config.properties
   ├── schema.sql
   └── data.sql


COMMON ERRORS
-------------
- "Access denied for user" → Wrong username or password in config.properties
- "Unknown database" → schema.sql was not run. Go back to Step 1.
- "Cannot add or update a child row" → Invalid ID entered. Check your
  cities/states tables for valid IDs before adding an employee.
- "ClassNotFoundException: com.mysql.cj.jdbc.Driver" → JAR not linked.
  Check Step 4.
- Build errors in VS Code → Make sure you opened the root folder,
  not a subfolder like /src.


