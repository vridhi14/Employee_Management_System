package com.employee.management.service;

import com.employee.management.dao.EmployeeDAO;
import com.employee.management.model.Employee;

import java.util.List;

/**
 * Service layer for Employee business logic
 */
public class EmployeeService {
    
    private EmployeeDAO employeeDAO;
    
    public EmployeeService() {
        this.employeeDAO = new EmployeeDAO();
    }
    
    /**
     * Create a new employee
     */
    public boolean createEmployee(Employee employee) {
        // Validation logic can be added here
        if (employee.getFirstName() == null || employee.getFirstName().trim().isEmpty()) {
            System.out.println("Error: First name is required");
            return false;
        }
        
        if (employee.getLastName() == null || employee.getLastName().trim().isEmpty()) {
            System.out.println("Error: Last name is required");
            return false;
        }
        
        if (employee.getEmail() == null || employee.getEmail().trim().isEmpty()) {
            System.out.println("Error: Email is required");
            return false;
        }
        
        if (employee.getSalary() <= 0) {
            System.out.println("Error: Salary must be greater than 0");
            return false;
        }
        
        return employeeDAO.addEmployee(employee);
    }
    
    /**
     * Get employee by ID
     */
    public Employee getEmployee(int employeeId) {
        return employeeDAO.getEmployeeById(employeeId);
    }
    
    /**
     * Get all employees
     */
    public List<Employee> getAllEmployees() {
        return employeeDAO.getAllEmployees();
    }
    
    /**
     * Get employees by department
     */
    public List<Employee> getEmployeesByDepartment(int departmentId) {
        return employeeDAO.getEmployeesByDepartment(departmentId);
    }
    
    /**
     * Update employee information
     */
    public boolean updateEmployee(Employee employee) {
        // Validation logic
        if (employee.getEmployeeId() <= 0) {
            System.out.println("Error: Invalid employee ID");
            return false;
        }
        
        if (employee.getFirstName() == null || employee.getFirstName().trim().isEmpty()) {
            System.out.println("Error: First name is required");
            return false;
        }
        
        if (employee.getLastName() == null || employee.getLastName().trim().isEmpty()) {
            System.out.println("Error: Last name is required");
            return false;
        }
        
        if (employee.getSalary() <= 0) {
            System.out.println("Error: Salary must be greater than 0");
            return false;
        }
        
        return employeeDAO.updateEmployee(employee);
    }
    
    /**
     * Delete employee
     */
    public boolean deleteEmployee(int employeeId) {
        if (employeeId <= 0) {
            System.out.println("Error: Invalid employee ID");
            return false;
        }
        
        return employeeDAO.deleteEmployee(employeeId);
    }
    
    /**
     * Search employees by name
     */
    public List<Employee> searchEmployees(String name) {
        if (name == null || name.trim().isEmpty()) {
            System.out.println("Error: Search term is required");
            return null;
        }
        
        return employeeDAO.searchEmployeesByName(name);
    }
    
    /**
     * Display all employees
     */
    public void displayAllEmployees() {
        List<Employee> employees = getAllEmployees();
        
        if (employees.isEmpty()) {
            System.out.println("No employees found.");
            return;
        }
        
        System.out.println("\n" + "=".repeat(120));
        System.out.printf("%-5s %-15s %-15s %-30s %-15s %-12s %-20s %-8s %-10s%n",
                "ID", "First Name", "Last Name", "Email", "Phone", "Hire Date", "Job Title", "Dept ID", "Salary");
        System.out.println("=".repeat(120));
        
        for (Employee emp : employees) {
            System.out.printf("%-5d %-15s %-15s %-30s %-15s %-12s %-20s %-8d $%-9.2f%n",
                    emp.getEmployeeId(),
                    emp.getFirstName(),
                    emp.getLastName(),
                    emp.getEmail(),
                    emp.getPhone(),
                    emp.getHireDate(),
                    emp.getJobTitle(),
                    emp.getDepartmentId(),
                    emp.getSalary());
        }
        System.out.println("=".repeat(120));
    }
}
