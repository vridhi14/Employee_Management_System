package com.employee.management;

import com.employee.management.model.Employee;
import com.employee.management.model.Payroll;
import com.employee.management.model.Department;
import com.employee.management.service.EmployeeService;
import com.employee.management.service.PayrollService;
import com.employee.management.dao.DepartmentDAO;
import com.employee.management.util.DatabaseConnection;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

/**
 * Employee Management and Payroll System - Main Application
 */
public class EmployeeManagementApp {
    
    private static Scanner scanner = new Scanner(System.in);
    private static EmployeeService employeeService = new EmployeeService();
    private static PayrollService payrollService = new PayrollService();
    private static DepartmentDAO departmentDAO = new DepartmentDAO();
    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║  EMPLOYEE MANAGEMENT & PAYROLL SYSTEM                      ║");
        System.out.println("║  Java + Maven + JDBC + MySQL                               ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
        
        // Test database connection
        if (!DatabaseConnection.testConnection()) {
            System.out.println("\n⚠ WARNING: Database connection failed!");
            System.out.println("Please ensure MySQL is running and database is configured correctly.");
            System.out.println("Check db.properties file for configuration.");
            System.out.println("\nTo setup the database, run the schema.sql file:");
            System.out.println("mysql -u root -p < src/main/resources/schema.sql");
            return;
        }
        
        System.out.println("\n✓ Database connection successful!");
        
        boolean running = true;
        
        while (running) {
            displayMainMenu();
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    employeeManagementMenu();
                    break;
                case 2:
                    payrollManagementMenu();
                    break;
                case 3:
                    departmentManagementMenu();
                    break;
                case 4:
                    displayReports();
                    break;
                case 5:
                    System.out.println("\n👋 Thank you for using Employee Management System!");
                    running = false;
                    break;
                default:
                    System.out.println("\n❌ Invalid choice. Please try again.");
            }
        }
        
        scanner.close();
    }
    
    private static void displayMainMenu() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("                    MAIN MENU");
        System.out.println("=".repeat(60));
        System.out.println("1. Employee Management");
        System.out.println("2. Payroll Management");
        System.out.println("3. Department Management");
        System.out.println("4. Reports");
        System.out.println("5. Exit");
        System.out.println("=".repeat(60));
    }
    
    // ========================= EMPLOYEE MANAGEMENT =========================
    
    private static void employeeManagementMenu() {
        boolean back = false;
        
        while (!back) {
            System.out.println("\n" + "-".repeat(60));
            System.out.println("              EMPLOYEE MANAGEMENT");
            System.out.println("-".repeat(60));
            System.out.println("1. Add New Employee");
            System.out.println("2. View All Employees");
            System.out.println("3. Search Employee");
            System.out.println("4. Update Employee");
            System.out.println("5. Delete Employee");
            System.out.println("6. View Employees by Department");
            System.out.println("7. Back to Main Menu");
            System.out.println("-".repeat(60));
            
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    addEmployee();
                    break;
                case 2:
                    employeeService.displayAllEmployees();
                    break;
                case 3:
                    searchEmployee();
                    break;
                case 4:
                    updateEmployee();
                    break;
                case 5:
                    deleteEmployee();
                    break;
                case 6:
                    viewEmployeesByDepartment();
                    break;
                case 7:
                    back = true;
                    break;
                default:
                    System.out.println("\n❌ Invalid choice. Please try again.");
            }
        }
    }
    
    private static void addEmployee() {
        System.out.println("\n--- Add New Employee ---");
        
        scanner.nextLine(); // Consume newline
        System.out.print("First Name: ");
        String firstName = scanner.nextLine();
        
        System.out.print("Last Name: ");
        String lastName = scanner.nextLine();
        
        System.out.print("Email: ");
        String email = scanner.nextLine();
        
        System.out.print("Phone: ");
        String phone = scanner.nextLine();
        
        System.out.print("Hire Date (yyyy-MM-dd): ");
        LocalDate hireDate = getDateInput();
        
        System.out.print("Job Title: ");
        String jobTitle = scanner.nextLine();
        
        // Display departments
        displayDepartmentList();
        int departmentId = getIntInput("Department ID: ");
        
        double salary = getDoubleInput("Salary: ");
        
        Employee employee = new Employee(firstName, lastName, email, phone, hireDate, 
                                        jobTitle, departmentId, salary);
        
        if (employeeService.createEmployee(employee)) {
            System.out.println("✓ Employee added successfully! Employee ID: " + employee.getEmployeeId());
        } else {
            System.out.println("❌ Failed to add employee.");
        }
    }
    
    private static void searchEmployee() {
        scanner.nextLine(); // Consume newline
        System.out.print("Enter name to search: ");
        String name = scanner.nextLine();
        
        List<Employee> employees = employeeService.searchEmployees(name);
        
        if (employees == null || employees.isEmpty()) {
            System.out.println("No employees found with name: " + name);
        } else {
            System.out.println("\n=== Search Results ===");
            for (Employee emp : employees) {
                System.out.println(emp);
            }
        }
    }
    
    private static void updateEmployee() {
        int employeeId = getIntInput("Enter Employee ID to update: ");
        
        Employee employee = employeeService.getEmployee(employeeId);
        if (employee == null) {
            System.out.println("❌ Employee not found.");
            return;
        }
        
        System.out.println("\nCurrent Employee Information:");
        System.out.println(employee);
        
        scanner.nextLine(); // Consume newline
        System.out.println("\nEnter new information (press Enter to keep current value):");
        
        System.out.print("First Name [" + employee.getFirstName() + "]: ");
        String firstName = scanner.nextLine();
        if (!firstName.trim().isEmpty()) {
            employee.setFirstName(firstName);
        }
        
        System.out.print("Last Name [" + employee.getLastName() + "]: ");
        String lastName = scanner.nextLine();
        if (!lastName.trim().isEmpty()) {
            employee.setLastName(lastName);
        }
        
        System.out.print("Email [" + employee.getEmail() + "]: ");
        String email = scanner.nextLine();
        if (!email.trim().isEmpty()) {
            employee.setEmail(email);
        }
        
        System.out.print("Phone [" + employee.getPhone() + "]: ");
        String phone = scanner.nextLine();
        if (!phone.trim().isEmpty()) {
            employee.setPhone(phone);
        }
        
        System.out.print("Job Title [" + employee.getJobTitle() + "]: ");
        String jobTitle = scanner.nextLine();
        if (!jobTitle.trim().isEmpty()) {
            employee.setJobTitle(jobTitle);
        }
        
        System.out.print("Salary [" + employee.getSalary() + "]: ");
        String salaryStr = scanner.nextLine();
        if (!salaryStr.trim().isEmpty()) {
            try {
                employee.setSalary(Double.parseDouble(salaryStr));
            } catch (NumberFormatException e) {
                System.out.println("Invalid salary, keeping current value.");
            }
        }
        
        if (employeeService.updateEmployee(employee)) {
            System.out.println("✓ Employee updated successfully!");
        } else {
            System.out.println("❌ Failed to update employee.");
        }
    }
    
    private static void deleteEmployee() {
        int employeeId = getIntInput("Enter Employee ID to delete: ");
        
        Employee employee = employeeService.getEmployee(employeeId);
        if (employee == null) {
            System.out.println("❌ Employee not found.");
            return;
        }
        
        System.out.println("\nEmployee to be deleted:");
        System.out.println(employee);
        
        scanner.nextLine(); // Consume newline
        System.out.print("Are you sure you want to delete this employee? (yes/no): ");
        String confirm = scanner.nextLine();
        
        if (confirm.equalsIgnoreCase("yes")) {
            if (employeeService.deleteEmployee(employeeId)) {
                System.out.println("✓ Employee deleted successfully!");
            } else {
                System.out.println("❌ Failed to delete employee.");
            }
        } else {
            System.out.println("Delete operation cancelled.");
        }
    }
    
    private static void viewEmployeesByDepartment() {
        displayDepartmentList();
        int departmentId = getIntInput("Enter Department ID: ");
        
        List<Employee> employees = employeeService.getEmployeesByDepartment(departmentId);
        
        if (employees.isEmpty()) {
            System.out.println("No employees found in this department.");
        } else {
            System.out.println("\n=== Employees in Department " + departmentId + " ===");
            for (Employee emp : employees) {
                System.out.println(emp);
            }
        }
    }
    
    // ========================= PAYROLL MANAGEMENT =========================
    
    private static void payrollManagementMenu() {
        boolean back = false;
        
        while (!back) {
            System.out.println("\n" + "-".repeat(60));
            System.out.println("              PAYROLL MANAGEMENT");
            System.out.println("-".repeat(60));
            System.out.println("1. Generate Payroll for Employee");
            System.out.println("2. View All Payroll Records");
            System.out.println("3. View Employee Payroll History");
            System.out.println("4. Process Payment");
            System.out.println("5. Generate Monthly Payroll for All Employees");
            System.out.println("6. Update Payroll");
            System.out.println("7. Delete Payroll Record");
            System.out.println("8. Back to Main Menu");
            System.out.println("-".repeat(60));
            
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    generatePayrollForEmployee();
                    break;
                case 2:
                    payrollService.displayAllPayrolls();
                    break;
                case 3:
                    viewEmployeePayrollHistory();
                    break;
                case 4:
                    processPayment();
                    break;
                case 5:
                    generateMonthlyPayroll();
                    break;
                case 6:
                    updatePayroll();
                    break;
                case 7:
                    deletePayroll();
                    break;
                case 8:
                    back = true;
                    break;
                default:
                    System.out.println("\n❌ Invalid choice. Please try again.");
            }
        }
    }
    
    private static void generatePayrollForEmployee() {
        System.out.println("\n--- Generate Payroll for Employee ---");
        
        int employeeId = getIntInput("Enter Employee ID: ");
        
        Employee employee = employeeService.getEmployee(employeeId);
        if (employee == null) {
            System.out.println("❌ Employee not found.");
            return;
        }
        
        System.out.println("Generating payroll for: " + employee.getFullName());
        System.out.println("Basic Salary: $" + employee.getSalary());
        
        scanner.nextLine(); // Consume newline
        System.out.print("Pay Period Start Date (yyyy-MM-dd): ");
        LocalDate startDate = getDateInput();
        
        System.out.print("Pay Period End Date (yyyy-MM-dd): ");
        LocalDate endDate = getDateInput();
        
        double bonus = getDoubleInput("Bonus Amount: ");
        double deductions = getDoubleInput("Deductions Amount: ");
        
        if (payrollService.generatePayroll(employeeId, startDate, endDate, bonus, deductions)) {
            System.out.println("✓ Payroll generated successfully!");
            double netSalary = employee.getSalary() + bonus - deductions;
            System.out.printf("Net Salary: $%.2f%n", netSalary);
        } else {
            System.out.println("❌ Failed to generate payroll.");
        }
    }
    
    private static void viewEmployeePayrollHistory() {
        int employeeId = getIntInput("Enter Employee ID: ");
        
        payrollService.displayEmployeePayrollSummary(employeeId);
        
        List<Payroll> payrolls = payrollService.getEmployeePayrollHistory(employeeId);
        
        if (!payrolls.isEmpty()) {
            System.out.println("\n=== Detailed Payroll History ===");
            for (Payroll p : payrolls) {
                System.out.println(p);
            }
        }
    }
    
    private static void processPayment() {
        int payrollId = getIntInput("Enter Payroll ID to process payment: ");
        
        Payroll payroll = payrollService.getPayroll(payrollId);
        if (payroll == null) {
            System.out.println("❌ Payroll record not found.");
            return;
        }
        
        System.out.println("\nPayroll Details:");
        System.out.println(payroll);
        
        if ("PAID".equals(payroll.getPaymentStatus())) {
            System.out.println("⚠ This payroll has already been paid.");
            return;
        }
        
        scanner.nextLine(); // Consume newline
        System.out.print("Confirm payment? (yes/no): ");
        String confirm = scanner.nextLine();
        
        if (confirm.equalsIgnoreCase("yes")) {
            if (payrollService.processPayment(payrollId)) {
                System.out.println("✓ Payment processed successfully!");
            } else {
                System.out.println("❌ Failed to process payment.");
            }
        } else {
            System.out.println("Payment cancelled.");
        }
    }
    
    private static void generateMonthlyPayroll() {
        System.out.println("\n--- Generate Monthly Payroll for All Active Employees ---");
        
        scanner.nextLine(); // Consume newline
        System.out.print("Pay Period Start Date (yyyy-MM-dd): ");
        LocalDate startDate = getDateInput();
        
        System.out.print("Pay Period End Date (yyyy-MM-dd): ");
        LocalDate endDate = getDateInput();
        
        System.out.print("Confirm generation of monthly payroll? (yes/no): ");
        String confirm = scanner.nextLine();
        
        if (confirm.equalsIgnoreCase("yes")) {
            payrollService.generateMonthlyPayrollForAllEmployees(startDate, endDate);
        } else {
            System.out.println("Operation cancelled.");
        }
    }
    
    private static void updatePayroll() {
        int payrollId = getIntInput("Enter Payroll ID to update: ");
        
        Payroll payroll = payrollService.getPayroll(payrollId);
        if (payroll == null) {
            System.out.println("❌ Payroll record not found.");
            return;
        }
        
        System.out.println("\nCurrent Payroll Information:");
        System.out.println(payroll);
        
        System.out.println("\nEnter new information:");
        
        double bonus = getDoubleInput("New Bonus Amount: ");
        payroll.setBonus(bonus);
        
        double deductions = getDoubleInput("New Deductions Amount: ");
        payroll.setDeductions(deductions);
        
        if (payrollService.updatePayroll(payroll)) {
            System.out.println("✓ Payroll updated successfully!");
            System.out.printf("New Net Salary: $%.2f%n", payroll.getNetSalary());
        } else {
            System.out.println("❌ Failed to update payroll.");
        }
    }
    
    private static void deletePayroll() {
        int payrollId = getIntInput("Enter Payroll ID to delete: ");
        
        Payroll payroll = payrollService.getPayroll(payrollId);
        if (payroll == null) {
            System.out.println("❌ Payroll record not found.");
            return;
        }
        
        System.out.println("\nPayroll to be deleted:");
        System.out.println(payroll);
        
        scanner.nextLine(); // Consume newline
        System.out.print("Are you sure you want to delete this payroll record? (yes/no): ");
        String confirm = scanner.nextLine();
        
        if (confirm.equalsIgnoreCase("yes")) {
            if (payrollService.deletePayroll(payrollId)) {
                System.out.println("✓ Payroll record deleted successfully!");
            } else {
                System.out.println("❌ Failed to delete payroll record.");
            }
        } else {
            System.out.println("Delete operation cancelled.");
        }
    }
    
    // ========================= DEPARTMENT MANAGEMENT =========================
    
    private static void departmentManagementMenu() {
        boolean back = false;
        
        while (!back) {
            System.out.println("\n" + "-".repeat(60));
            System.out.println("              DEPARTMENT MANAGEMENT");
            System.out.println("-".repeat(60));
            System.out.println("1. Add New Department");
            System.out.println("2. View All Departments");
            System.out.println("3. Update Department");
            System.out.println("4. Delete Department");
            System.out.println("5. Back to Main Menu");
            System.out.println("-".repeat(60));
            
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    addDepartment();
                    break;
                case 2:
                    displayDepartmentList();
                    break;
                case 3:
                    updateDepartment();
                    break;
                case 4:
                    deleteDepartment();
                    break;
                case 5:
                    back = true;
                    break;
                default:
                    System.out.println("\n❌ Invalid choice. Please try again.");
            }
        }
    }
    
    private static void addDepartment() {
        System.out.println("\n--- Add New Department ---");
        
        scanner.nextLine(); // Consume newline
        System.out.print("Department Name: ");
        String name = scanner.nextLine();
        
        System.out.print("Description: ");
        String description = scanner.nextLine();
        
        Department department = new Department(name, description);
        
        if (departmentDAO.addDepartment(department)) {
            System.out.println("✓ Department added successfully! Department ID: " + department.getDepartmentId());
        } else {
            System.out.println("❌ Failed to add department.");
        }
    }
    
    private static void displayDepartmentList() {
        List<Department> departments = departmentDAO.getAllDepartments();
        
        if (departments.isEmpty()) {
            System.out.println("No departments found.");
            return;
        }
        
        System.out.println("\n=== Departments ===");
        System.out.printf("%-5s %-30s %-50s%n", "ID", "Name", "Description");
        System.out.println("-".repeat(85));
        
        for (Department dept : departments) {
            System.out.printf("%-5d %-30s %-50s%n",
                    dept.getDepartmentId(),
                    dept.getDepartmentName(),
                    dept.getDescription());
        }
        System.out.println("-".repeat(85));
    }
    
    private static void updateDepartment() {
        int departmentId = getIntInput("Enter Department ID to update: ");
        
        Department department = departmentDAO.getDepartmentById(departmentId);
        if (department == null) {
            System.out.println("❌ Department not found.");
            return;
        }
        
        System.out.println("\nCurrent Department Information:");
        System.out.println(department);
        
        scanner.nextLine(); // Consume newline
        System.out.print("New Name [" + department.getDepartmentName() + "]: ");
        String name = scanner.nextLine();
        if (!name.trim().isEmpty()) {
            department.setDepartmentName(name);
        }
        
        System.out.print("New Description [" + department.getDescription() + "]: ");
        String description = scanner.nextLine();
        if (!description.trim().isEmpty()) {
            department.setDescription(description);
        }
        
        if (departmentDAO.updateDepartment(department)) {
            System.out.println("✓ Department updated successfully!");
        } else {
            System.out.println("❌ Failed to update department.");
        }
    }
    
    private static void deleteDepartment() {
        int departmentId = getIntInput("Enter Department ID to delete: ");
        
        Department department = departmentDAO.getDepartmentById(departmentId);
        if (department == null) {
            System.out.println("❌ Department not found.");
            return;
        }
        
        System.out.println("\nDepartment to be deleted:");
        System.out.println(department);
        
        scanner.nextLine(); // Consume newline
        System.out.print("Are you sure? (yes/no): ");
        String confirm = scanner.nextLine();
        
        if (confirm.equalsIgnoreCase("yes")) {
            if (departmentDAO.deleteDepartment(departmentId)) {
                System.out.println("✓ Department deleted successfully!");
            } else {
                System.out.println("❌ Failed to delete department. It may have associated employees.");
            }
        } else {
            System.out.println("Delete operation cancelled.");
        }
    }
    
    // ========================= REPORTS =========================
    
    private static void displayReports() {
        System.out.println("\n" + "-".repeat(60));
        System.out.println("                    REPORTS");
        System.out.println("-".repeat(60));
        System.out.println("1. All Employees Report");
        System.out.println("2. All Payroll Records Report");
        System.out.println("3. Department-wise Employee Count");
        System.out.println("4. Employee Payroll Summary");
        System.out.println("-".repeat(60));
        
        int choice = getIntInput("Enter your choice: ");
        
        switch (choice) {
            case 1:
                employeeService.displayAllEmployees();
                break;
            case 2:
                payrollService.displayAllPayrolls();
                break;
            case 3:
                displayDepartmentEmployeeCount();
                break;
            case 4:
                int empId = getIntInput("Enter Employee ID: ");
                payrollService.displayEmployeePayrollSummary(empId);
                break;
            default:
                System.out.println("\n❌ Invalid choice.");
        }
    }
    
    private static void displayDepartmentEmployeeCount() {
        List<Department> departments = departmentDAO.getAllDepartments();
        
        System.out.println("\n=== Department-wise Employee Count ===");
        System.out.printf("%-30s %-15s%n", "Department", "Employee Count");
        System.out.println("-".repeat(45));
        
        for (Department dept : departments) {
            List<Employee> employees = employeeService.getEmployeesByDepartment(dept.getDepartmentId());
            System.out.printf("%-30s %-15d%n", dept.getDepartmentName(), employees.size());
        }
        System.out.println("-".repeat(45));
    }
    
    // ========================= UTILITY METHODS =========================
    
    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return scanner.nextInt();
            } catch (Exception e) {
                System.out.println("❌ Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear buffer
            }
        }
    }
    
    private static double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return scanner.nextDouble();
            } catch (Exception e) {
                System.out.println("❌ Invalid input. Please enter a valid number.");
                scanner.nextLine(); // Clear buffer
            }
        }
    }
    
    private static LocalDate getDateInput() {
        while (true) {
            try {
                String dateStr = scanner.nextLine();
                return LocalDate.parse(dateStr, dateFormatter);
            } catch (DateTimeParseException e) {
                System.out.print("❌ Invalid date format. Please use yyyy-MM-dd: ");
            }
        }
    }
}
