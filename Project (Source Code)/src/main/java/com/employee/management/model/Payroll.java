package com.employee.management.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Payroll Entity Model
 */
public class Payroll {
    private int payrollId;
    private int employeeId;
    private LocalDate payPeriodStart;
    private LocalDate payPeriodEnd;
    private double basicSalary;
    private double bonus;
    private double deductions;
    private double netSalary;
    private LocalDate paymentDate;
    private String paymentStatus;
    private LocalDateTime createdAt;
    
    // Constructors
    public Payroll() {}
    
    public Payroll(int employeeId, LocalDate payPeriodStart, LocalDate payPeriodEnd,
                   double basicSalary, double bonus, double deductions) {
        this.employeeId = employeeId;
        this.payPeriodStart = payPeriodStart;
        this.payPeriodEnd = payPeriodEnd;
        this.basicSalary = basicSalary;
        this.bonus = bonus;
        this.deductions = deductions;
        this.netSalary = basicSalary + bonus - deductions;
        this.paymentStatus = "PENDING";
    }
    
    // Getters and Setters
    public int getPayrollId() {
        return payrollId;
    }
    
    public void setPayrollId(int payrollId) {
        this.payrollId = payrollId;
    }
    
    public int getEmployeeId() {
        return employeeId;
    }
    
    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }
    
    public LocalDate getPayPeriodStart() {
        return payPeriodStart;
    }
    
    public void setPayPeriodStart(LocalDate payPeriodStart) {
        this.payPeriodStart = payPeriodStart;
    }
    
    public LocalDate getPayPeriodEnd() {
        return payPeriodEnd;
    }
    
    public void setPayPeriodEnd(LocalDate payPeriodEnd) {
        this.payPeriodEnd = payPeriodEnd;
    }
    
    public double getBasicSalary() {
        return basicSalary;
    }
    
    public void setBasicSalary(double basicSalary) {
        this.basicSalary = basicSalary;
    }
    
    public double getBonus() {
        return bonus;
    }
    
    public void setBonus(double bonus) {
        this.bonus = bonus;
    }
    
    public double getDeductions() {
        return deductions;
    }
    
    public void setDeductions(double deductions) {
        this.deductions = deductions;
    }
    
    public double getNetSalary() {
        return netSalary;
    }
    
    public void setNetSalary(double netSalary) {
        this.netSalary = netSalary;
    }
    
    public LocalDate getPaymentDate() {
        return paymentDate;
    }
    
    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }
    
    public String getPaymentStatus() {
        return paymentStatus;
    }
    
    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public void calculateNetSalary() {
        this.netSalary = this.basicSalary + this.bonus - this.deductions;
    }
    
    @Override
    public String toString() {
        return "Payroll{" +
                "payrollId=" + payrollId +
                ", employeeId=" + employeeId +
                ", payPeriodStart=" + payPeriodStart +
                ", payPeriodEnd=" + payPeriodEnd +
                ", basicSalary=" + basicSalary +
                ", bonus=" + bonus +
                ", deductions=" + deductions +
                ", netSalary=" + netSalary +
                ", paymentDate=" + paymentDate +
                ", paymentStatus='" + paymentStatus + '\'' +
                '}';
    }
}
