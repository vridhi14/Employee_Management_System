package com.employee.management.dao;

import com.employee.management.model.Payroll;
import com.employee.management.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Payroll operations
 */
public class PayrollDAO {
    
    /**
     * Add a new payroll record
     */
    public boolean addPayroll(Payroll payroll) {
        String sql = "INSERT INTO payroll (employee_id, pay_period_start, pay_period_end, " +
                     "basic_salary, bonus, deductions, net_salary, payment_date, payment_status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, payroll.getEmployeeId());
            pstmt.setDate(2, Date.valueOf(payroll.getPayPeriodStart()));
            pstmt.setDate(3, Date.valueOf(payroll.getPayPeriodEnd()));
            pstmt.setDouble(4, payroll.getBasicSalary());
            pstmt.setDouble(5, payroll.getBonus());
            pstmt.setDouble(6, payroll.getDeductions());
            pstmt.setDouble(7, payroll.getNetSalary());
            
            if (payroll.getPaymentDate() != null) {
                pstmt.setDate(8, Date.valueOf(payroll.getPaymentDate()));
            } else {
                pstmt.setNull(8, Types.DATE);
            }
            
            pstmt.setString(9, payroll.getPaymentStatus());
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    payroll.setPayrollId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error adding payroll: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Get payroll by ID
     */
    public Payroll getPayrollById(int payrollId) {
        String sql = "SELECT * FROM payroll WHERE payroll_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, payrollId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractPayrollFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error getting payroll: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Get all payroll records
     */
    public List<Payroll> getAllPayrolls() {
        List<Payroll> payrolls = new ArrayList<>();
        String sql = "SELECT * FROM payroll ORDER BY payroll_id DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                payrolls.add(extractPayrollFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all payrolls: " + e.getMessage());
            e.printStackTrace();
        }
        return payrolls;
    }
    
    /**
     * Get payroll records for a specific employee
     */
    public List<Payroll> getPayrollsByEmployeeId(int employeeId) {
        List<Payroll> payrolls = new ArrayList<>();
        String sql = "SELECT * FROM payroll WHERE employee_id = ? ORDER BY pay_period_start DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, employeeId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                payrolls.add(extractPayrollFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting payrolls for employee: " + e.getMessage());
            e.printStackTrace();
        }
        return payrolls;
    }
    
    /**
     * Get payroll records within a date range
     */
    public List<Payroll> getPayrollsByDateRange(LocalDate startDate, LocalDate endDate) {
        List<Payroll> payrolls = new ArrayList<>();
        String sql = "SELECT * FROM payroll WHERE pay_period_start >= ? AND pay_period_end <= ? " +
                     "ORDER BY pay_period_start DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDate(1, Date.valueOf(startDate));
            pstmt.setDate(2, Date.valueOf(endDate));
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                payrolls.add(extractPayrollFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting payrolls by date range: " + e.getMessage());
            e.printStackTrace();
        }
        return payrolls;
    }
    
    /**
     * Update payroll record
     */
    public boolean updatePayroll(Payroll payroll) {
        String sql = "UPDATE payroll SET employee_id = ?, pay_period_start = ?, pay_period_end = ?, " +
                     "basic_salary = ?, bonus = ?, deductions = ?, net_salary = ?, " +
                     "payment_date = ?, payment_status = ? WHERE payroll_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, payroll.getEmployeeId());
            pstmt.setDate(2, Date.valueOf(payroll.getPayPeriodStart()));
            pstmt.setDate(3, Date.valueOf(payroll.getPayPeriodEnd()));
            pstmt.setDouble(4, payroll.getBasicSalary());
            pstmt.setDouble(5, payroll.getBonus());
            pstmt.setDouble(6, payroll.getDeductions());
            pstmt.setDouble(7, payroll.getNetSalary());
            
            if (payroll.getPaymentDate() != null) {
                pstmt.setDate(8, Date.valueOf(payroll.getPaymentDate()));
            } else {
                pstmt.setNull(8, Types.DATE);
            }
            
            pstmt.setString(9, payroll.getPaymentStatus());
            pstmt.setInt(10, payroll.getPayrollId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating payroll: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Delete payroll record
     */
    public boolean deletePayroll(int payrollId) {
        String sql = "DELETE FROM payroll WHERE payroll_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, payrollId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting payroll: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Mark payroll as paid
     */
    public boolean markPayrollAsPaid(int payrollId, LocalDate paymentDate) {
        String sql = "UPDATE payroll SET payment_status = 'PAID', payment_date = ? WHERE payroll_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDate(1, Date.valueOf(paymentDate));
            pstmt.setInt(2, payrollId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error marking payroll as paid: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Extract Payroll object from ResultSet
     */
    private Payroll extractPayrollFromResultSet(ResultSet rs) throws SQLException {
        Payroll payroll = new Payroll();
        payroll.setPayrollId(rs.getInt("payroll_id"));
        payroll.setEmployeeId(rs.getInt("employee_id"));
        
        Date payPeriodStart = rs.getDate("pay_period_start");
        if (payPeriodStart != null) {
            payroll.setPayPeriodStart(payPeriodStart.toLocalDate());
        }
        
        Date payPeriodEnd = rs.getDate("pay_period_end");
        if (payPeriodEnd != null) {
            payroll.setPayPeriodEnd(payPeriodEnd.toLocalDate());
        }
        
        payroll.setBasicSalary(rs.getDouble("basic_salary"));
        payroll.setBonus(rs.getDouble("bonus"));
        payroll.setDeductions(rs.getDouble("deductions"));
        payroll.setNetSalary(rs.getDouble("net_salary"));
        
        Date paymentDate = rs.getDate("payment_date");
        if (paymentDate != null) {
            payroll.setPaymentDate(paymentDate.toLocalDate());
        }
        
        payroll.setPaymentStatus(rs.getString("payment_status"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            payroll.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        return payroll;
    }
}
