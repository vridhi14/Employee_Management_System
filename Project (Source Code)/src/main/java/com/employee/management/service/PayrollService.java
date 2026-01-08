package com.employee.management.service;

import com.employee.management.dao.PayrollDAO;
import com.employee.management.dao.EmployeeDAO;
import com.employee.management.model.Payroll;
import com.employee.management.model.Employee;

import java.time.LocalDate;
import java.util.List;

/**
 * Service layer for Payroll business logic
 */
public class PayrollService {
    
    private PayrollDAO payrollDAO;
    private EmployeeDAO employeeDAO;
    
    public PayrollService() {
        this.payrollDAO = new PayrollDAO();
        this.employeeDAO = new EmployeeDAO();
    }
    
    /**
     * Generate payroll for an employee
     */
    public boolean generatePayroll(int employeeId, LocalDate payPeriodStart, 
                                   LocalDate payPeriodEnd, double bonus, double deductions) {
        // Get employee to fetch their salary
        Employee employee = employeeDAO.getEmployeeById(employeeId);
        
        if (employee == null) {
            System.out.println("Error: Employee not found");
            return false;
        }
        
        // Calculate payroll
        double basicSalary = employee.getSalary();
        Payroll payroll = new Payroll(employeeId, payPeriodStart, payPeriodEnd, 
                                      basicSalary, bonus, deductions);
        
        return payrollDAO.addPayroll(payroll);
    }
    
    /**
     * Create a new payroll record
     */
    public boolean createPayroll(Payroll payroll) {
        // Validation
        if (payroll.getEmployeeId() <= 0) {
            System.out.println("Error: Invalid employee ID");
            return false;
        }
        
        if (payroll.getPayPeriodStart() == null || payroll.getPayPeriodEnd() == null) {
            System.out.println("Error: Pay period dates are required");
            return false;
        }
        
        if (payroll.getPayPeriodStart().isAfter(payroll.getPayPeriodEnd())) {
            System.out.println("Error: Pay period start date must be before end date");
            return false;
        }
        
        if (payroll.getBasicSalary() < 0) {
            System.out.println("Error: Basic salary cannot be negative");
            return false;
        }
        
        // Calculate net salary
        payroll.calculateNetSalary();
        
        return payrollDAO.addPayroll(payroll);
    }
    
    /**
     * Get payroll by ID
     */
    public Payroll getPayroll(int payrollId) {
        return payrollDAO.getPayrollById(payrollId);
    }
    
    /**
     * Get all payroll records
     */
    public List<Payroll> getAllPayrolls() {
        return payrollDAO.getAllPayrolls();
    }
    
    /**
     * Get payroll history for an employee
     */
    public List<Payroll> getEmployeePayrollHistory(int employeeId) {
        return payrollDAO.getPayrollsByEmployeeId(employeeId);
    }
    
    /**
     * Get payroll records for a date range
     */
    public List<Payroll> getPayrollsByDateRange(LocalDate startDate, LocalDate endDate) {
        return payrollDAO.getPayrollsByDateRange(startDate, endDate);
    }
    
    /**
     * Update payroll record
     */
    public boolean updatePayroll(Payroll payroll) {
        if (payroll.getPayrollId() <= 0) {
            System.out.println("Error: Invalid payroll ID");
            return false;
        }
        
        // Recalculate net salary
        payroll.calculateNetSalary();
        
        return payrollDAO.updatePayroll(payroll);
    }
    
    /**
     * Delete payroll record
     */
    public boolean deletePayroll(int payrollId) {
        if (payrollId <= 0) {
            System.out.println("Error: Invalid payroll ID");
            return false;
        }
        
        return payrollDAO.deletePayroll(payrollId);
    }
    
    /**
     * Process payment for payroll
     */
    public boolean processPayment(int payrollId) {
        return payrollDAO.markPayrollAsPaid(payrollId, LocalDate.now());
    }
    
    /**
     * Calculate monthly payroll for all active employees
     */
    public void generateMonthlyPayrollForAllEmployees(LocalDate payPeriodStart, LocalDate payPeriodEnd) {
        List<Employee> employees = employeeDAO.getAllEmployees();
        
        int successCount = 0;
        int failCount = 0;
        
        for (Employee employee : employees) {
            if ("ACTIVE".equals(employee.getStatus())) {
                Payroll payroll = new Payroll(
                    employee.getEmployeeId(),
                    payPeriodStart,
                    payPeriodEnd,
                    employee.getSalary(),
                    0.0,  // No bonus by default
                    0.0   // No deductions by default
                );
                
                if (payrollDAO.addPayroll(payroll)) {
                    successCount++;
                } else {
                    failCount++;
                }
            }
        }
        
        System.out.println("\nMonthly Payroll Generation Complete:");
        System.out.println("Successfully generated: " + successCount);
        System.out.println("Failed: " + failCount);
    }
    
    /**
     * Display all payroll records
     */
    public void displayAllPayrolls() {
        List<Payroll> payrolls = getAllPayrolls();
        
        if (payrolls.isEmpty()) {
            System.out.println("No payroll records found.");
            return;
        }
        
        System.out.println("\n" + "=".repeat(120));
        System.out.printf("%-8s %-10s %-15s %-15s %-12s %-10s %-12s %-12s %-12s %-10s%n",
                "ID", "Emp ID", "Period Start", "Period End", "Basic Sal", "Bonus", "Deductions", "Net Salary", "Pay Date", "Status");
        System.out.println("=".repeat(120));
        
        for (Payroll p : payrolls) {
            System.out.printf("%-8d %-10d %-15s %-15s $%-11.2f $%-9.2f $%-11.2f $%-11.2f %-12s %-10s%n",
                    p.getPayrollId(),
                    p.getEmployeeId(),
                    p.getPayPeriodStart(),
                    p.getPayPeriodEnd(),
                    p.getBasicSalary(),
                    p.getBonus(),
                    p.getDeductions(),
                    p.getNetSalary(),
                    p.getPaymentDate() != null ? p.getPaymentDate().toString() : "N/A",
                    p.getPaymentStatus());
        }
        System.out.println("=".repeat(120));
    }
    
    /**
     * Display payroll summary for an employee
     */
    public void displayEmployeePayrollSummary(int employeeId) {
        Employee employee = employeeDAO.getEmployeeById(employeeId);
        
        if (employee == null) {
            System.out.println("Employee not found.");
            return;
        }
        
        List<Payroll> payrolls = getEmployeePayrollHistory(employeeId);
        
        System.out.println("\n===== Payroll Summary for " + employee.getFullName() + " =====");
        
        if (payrolls.isEmpty()) {
            System.out.println("No payroll records found for this employee.");
            return;
        }
        
        double totalPaid = 0;
        double totalPending = 0;
        
        for (Payroll p : payrolls) {
            if ("PAID".equals(p.getPaymentStatus())) {
                totalPaid += p.getNetSalary();
            } else {
                totalPending += p.getNetSalary();
            }
        }
        
        System.out.println("Total Payroll Records: " + payrolls.size());
        System.out.printf("Total Paid: $%.2f%n", totalPaid);
        System.out.printf("Total Pending: $%.2f%n", totalPending);
        System.out.println("=".repeat(80));
    }
}
