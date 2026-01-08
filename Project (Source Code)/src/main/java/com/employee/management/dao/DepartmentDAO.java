package com.employee.management.dao;

import com.employee.management.model.Department;
import com.employee.management.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Department operations
 */
public class DepartmentDAO {
    
    /**
     * Add a new department
     */
    public boolean addDepartment(Department department) {
        String sql = "INSERT INTO departments (department_name, description) VALUES (?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, department.getDepartmentName());
            pstmt.setString(2, department.getDescription());
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    department.setDepartmentId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error adding department: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Get department by ID
     */
    public Department getDepartmentById(int departmentId) {
        String sql = "SELECT * FROM departments WHERE department_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, departmentId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractDepartmentFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error getting department: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Get all departments
     */
    public List<Department> getAllDepartments() {
        List<Department> departments = new ArrayList<>();
        String sql = "SELECT * FROM departments ORDER BY department_id";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                departments.add(extractDepartmentFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all departments: " + e.getMessage());
            e.printStackTrace();
        }
        return departments;
    }
    
    /**
     * Update department
     */
    public boolean updateDepartment(Department department) {
        String sql = "UPDATE departments SET department_name = ?, description = ? WHERE department_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, department.getDepartmentName());
            pstmt.setString(2, department.getDescription());
            pstmt.setInt(3, department.getDepartmentId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating department: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Delete department
     */
    public boolean deleteDepartment(int departmentId) {
        String sql = "DELETE FROM departments WHERE department_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, departmentId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting department: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Extract Department object from ResultSet
     */
    private Department extractDepartmentFromResultSet(ResultSet rs) throws SQLException {
        Department department = new Department();
        department.setDepartmentId(rs.getInt("department_id"));
        department.setDepartmentName(rs.getString("department_name"));
        department.setDescription(rs.getString("description"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            department.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        return department;
    }
}
