# Employee Management System
> A console-based Java application with MySQL backend, built as a university team project.

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-00758F?style=for-the-badge&logo=mysql&logoColor=white)

---

## Overview

The Employee Management System is a role-based console application developed as part of a university software engineering course (CSc3350). It allows HR Admins and Employees to interact with a relational MySQL database to manage employee records, addresses, and user accounts — built entirely with core Java and JDBC, no external frameworks.

### Key Features
- Role-based login system (HR Admin / Employee)
- Secure SHA-256 password hashing
- Employee record creation and management
- Address and emergency contact tracking
- Input validation and login attempt limiting
- Relational MySQL schema with foreign key enforcement

---

## Team

**Team DataForge**

| Name | Role |
|------|------|
| Samiul (Sami) | Integration Lead & Scrum Master |
| Manna | Developer |
| Banhi | Developer |
| Abigail | Developer |

---

## Tech Stack

- **Language:** Java (JDK 8+)
- **Database:** MySQL
- **Connectivity:** JDBC (MySQL Connector/J)
- **Authentication:** SHA-256 hashing via MySQL `SHA2()`
- **IDE:** VS Code

---

## Project Structure

```
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
│     └── config.properties        ← NOT included (see setup below)
├── schema.sql
├── data.sql
└── README.md
```

---

## Setup & Installation

### Requirements
- Java JDK 8 or higher
- MySQL Server (running locally)
- MySQL Connector/J JAR (included in `/lib`)
- VS Code or any Java-capable IDE

---

### Step 1 — Set Up the Database

Open MySQL Workbench, dBeaver, or any MySQL client and run the following in order:

```sql
-- 1. Run the schema script
source schema.sql;

-- 2. Run the data/seed script
source data.sql;

-- 3. Create the users table
CREATE TABLE users (
    userID    INT AUTO_INCREMENT PRIMARY KEY,
    empID     INT NOT NULL,
    username  VARCHAR(50) NOT NULL UNIQUE,
    password  VARCHAR(64) NOT NULL,
    role      ENUM('HR_ADMIN', 'EMPLOYEE') NOT NULL,
    CONSTRAINT fk_user_emp FOREIGN KEY (empID) REFERENCES employees(empID)
);

-- 4. Insert default user accounts
INSERT INTO users (username, password, role, empID)
VALUES
  ('admin',   SHA2('admin123456',   256), 'HR_ADMIN', 1),
  ('charlie', SHA2('charlie123456', 256), 'EMPLOYEE', 2),
  ('lucy',    SHA2('lucy123456',    256), 'EMPLOYEE', 3),
  ('elmer',   SHA2('elmer123456',   256), 'EMPLOYEE', 4);
```

> **Note:** Make sure `empID` values 1–4 exist in your `employees` table before inserting users.

---

### Step 2 — Configure Your Database Connection

Navigate to `src/config.properties` and fill in your own MySQL credentials:

```properties
db.url=jdbc:mysql://localhost:3306/employeedataproject?allowPublicKeyRetrieval=true&useSSL=false
db.user=YOUR_MYSQL_USERNAME
db.password=YOUR_MYSQL_PASSWORD
```

> ⚠️ `config.properties` is excluded from this repository. Each person must create and fill in their own. Never share or commit this file.

---

### Step 3 — Link the MySQL Connector JAR

In VS Code, ensure `.vscode/settings.json` contains:

```json
{
  "java.project.referencedLibraries": ["lib/*.jar"]
}
```

Create the `.vscode` folder and `settings.json` file if they don't exist.

---

### Step 4 — Run the Program

1. Open `src/Main.java` in VS Code
2. Click **Run Java** (top right) or right-click > **Run Java**
3. The program launches in the terminal

---

## Default Login Credentials

| Role | Username | Password |
|------|----------|----------|
| HR Admin | `admin` | `admin123456` |
| Employee | `charlie` | `charlie123456` |
| Employee | `lucy` | `lucy123456` |
| Employee | `elmer` | `elmer123456` |

---

## Common Errors

| Error | Fix |
|-------|-----|
| `Access denied for user` | Wrong credentials in `config.properties` |
| `Unknown database` | `schema.sql` was not run — redo Step 1 |
| `Cannot add or update a child row` | Invalid foreign key ID — check your `cities` or `employees` table for valid IDs |
| `ClassNotFoundException: com.mysql.cj.jdbc.Driver` | JAR not linked — check Step 3 |
| Build errors in VS Code | Make sure you opened the root folder, not `/src` |

---

## My Role

As **Integration Lead and Scrum Master**, I was responsible for:
- Coordinating task distribution across all team members
- Designing and implementing the core foundation files (`DBConnection.java`, `AuthManager.java`, `Employee.java`, `Address.java`, `Main.java`)
- Combining and testing all team-contributed modules into a single working application
- Resolving technical blockers for teammates (MySQL config, classpath setup, foreign key issues)
- Managing sprint deliverables and ensuring deadlines were met

---

## License

This project was developed for academic purposes as part of GSU CSC3350 — Spring 2026.
