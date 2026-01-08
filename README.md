# Employee Management and Payroll System 

A comprehensive employee management and payroll system built using **Java**, **Maven**, **JDBC**, and **MySQL**.

## Features

### Employee Management
- Add, update, delete, and search employees
- View all employees or filter by department
- Store employee details including name, email, phone, hire date, job title, salary, and status

### Payroll Management
- Generate payroll records for individual employees
- Generate monthly payroll for all active employees
- Track bonuses, deductions, and net salary calculations
- Process payments and maintain payment history
- View payroll history for specific employees

### Department Management
- Create and manage departments
- Assign employees to departments
- View department-wise employee statistics

### Reports
- Comprehensive employee reports
- Payroll records reports
- Department-wise employee count
- Individual employee payroll summaries

## Technologies Used

- **Java 17**: Programming language
- **Maven**: Build and dependency management
- **JDBC**: Database connectivity
- **MySQL 8.0**: Relational database
- **MySQL Connector/J**: JDBC driver for MySQL

## Prerequisites

- Java Development Kit (JDK) 17 or higher
- Apache Maven 3.6 or higher
- MySQL 8.0 or higher
- MySQL Workbench (optional, for database management)

## Installation and Setup

### 1. Clone the Repository

```bash
git clone <repository-url>
cd xyzz
```

### 2. Setup MySQL Database

Start your MySQL server and run the following commands:

```bash
# Login to MySQL
mysql -u root -p

# Run the schema script to create database and tables
source src/main/resources/schema.sql

# Or if you prefer, run it directly
mysql -u root -p < src/main/resources/schema.sql
```

This will:
- Create the `employee_payroll_db` database
- Create tables: `departments`, `employees`, and `payroll`
- Insert sample data for testing

### 3. Configure Database Connection

Edit the `src/main/resources/db.properties` file with your MySQL credentials:

```properties
db.url=jdbc:mysql://localhost:3306/employee_payroll_db
db.username=root
db.password=your_password_here
db.driver=com.mysql.cj.jdbc.Driver
```

### 4. Build the Project

```bash
mvn clean install
```

This will:
- Download all dependencies
- Compile the source code
- Run tests (if any)
- Package the application into a JAR file

### 5. Run the Application

```bash
# Option 1: Run using Maven
mvn exec:java -Dexec.mainClass="com.employee.management.EmployeeManagementApp"

# Option 2: Run the JAR file
java -jar target/employee-management-payroll-1.0-SNAPSHOT.jar

# Option 3: Run directly with Java (if compiled)
cd target/classes
java com.employee.management.EmployeeManagementApp
```

## Project Structure

```
xyzz/
├── pom.xml                                 # Maven configuration
├── README.md                               # This file
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── employee/
│   │   │           └── management/
│   │   │               ├── EmployeeManagementApp.java    # Main application
│   │   │               ├── model/                        # Entity classes
│   │   │               │   ├── Employee.java
│   │   │               │   ├── Payroll.java
│   │   │               │   └── Department.java
│   │   │               ├── dao/                          # Data Access Objects
│   │   │               │   ├── EmployeeDAO.java
│   │   │               │   ├── PayrollDAO.java
│   │   │               │   └── DepartmentDAO.java
│   │   │               ├── service/                      # Business logic layer
│   │   │               │   ├── EmployeeService.java
│   │   │               │   └── PayrollService.java
│   │   │               └── util/                         # Utility classes
│   │   │                   └── DatabaseConnection.java
│   │   └── resources/
│   │       ├── db.properties              # Database configuration
│   │       └── schema.sql                 # Database schema
│   └── test/
│       └── java/                          # Test classes (optional)
└── target/                                # Build output (generated)
```

## Usage

After running the application, you'll see a menu-driven interface:

### Main Menu
1. **Employee Management** - Add, view, update, delete employees
2. **Payroll Management** - Generate and manage payroll records
3. **Department Management** - Manage departments
4. **Reports** - View various reports
5. **Exit** - Exit the application

### Sample Operations

#### Add an Employee
1. Select "Employee Management" from main menu
2. Choose "Add New Employee"
3. Enter employee details:
   - First Name, Last Name
   - Email, Phone
   - Hire Date (format: yyyy-MM-dd)
   - Job Title
   - Department ID
   - Salary

#### Generate Payroll
1. Select "Payroll Management" from main menu
2. Choose "Generate Payroll for Employee"
3. Enter Employee ID
4. Enter pay period dates
5. Enter bonus and deductions
6. System calculates and saves net salary

#### Process Payment
1. Select "Payroll Management"
2. Choose "Process Payment"
3. Enter Payroll ID
4. Confirm payment
5. Payment status updates to "PAID"

## Database Schema

### employees
- `employee_id` (Primary Key)
- `first_name`, `last_name`
- `email` (Unique), `phone`
- `hire_date`, `job_title`
- `department_id` (Foreign Key)
- `salary`, `status`
- `created_at`, `updated_at`

### payroll
- `payroll_id` (Primary Key)
- `employee_id` (Foreign Key)
- `pay_period_start`, `pay_period_end`
- `basic_salary`, `bonus`, `deductions`
- `net_salary`
- `payment_date`, `payment_status`
- `created_at`

### departments
- `department_id` (Primary Key)
- `department_name` (Unique)
- `description`
- `created_at`

## Troubleshooting

### Database Connection Issues
- Ensure MySQL server is running
- Verify database credentials in `db.properties`
- Check if the database `employee_payroll_db` exists
- Verify MySQL port (default: 3306)

### Build Issues
- Ensure JDK 17 is installed: `java -version`
- Ensure Maven is installed: `mvn -version`
- Clear Maven cache: `mvn clean`
- Rebuild: `mvn clean install`

### Runtime Issues
- Check if MySQL JDBC driver is in classpath
- Verify `db.properties` is in the correct location
- Check MySQL user permissions

## Future Enhancements

- Web-based UI using Spring Boot
- REST API endpoints
- User authentication and authorization
- Advanced reporting with charts
- Email notifications for payroll processing
- Export reports to PDF/Excel
- Attendance tracking integration
- Leave management system

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is open source and available under the MIT License.

## Author

Created as part of the Employee Management System project using Java, Maven, JDBC, and MySQL.

## Support

For issues, questions, or contributions, please open an issue in the repository.

