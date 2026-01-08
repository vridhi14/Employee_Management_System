package com.employee.management.model;

import java.time.LocalDateTime;

/**
 * Department Entity Model
 */
public class Department {
    private int departmentId;
    private String departmentName;
    private String description;
    private LocalDateTime createdAt;
    
    // Constructors
    public Department() {}
    
    public Department(String departmentName, String description) {
        this.departmentName = departmentName;
        this.description = description;
    }
    
    // Getters and Setters
    public int getDepartmentId() {
        return departmentId;
    }
    
    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }
    
    public String getDepartmentName() {
        return departmentName;
    }
    
    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    @Override
    public String toString() {
        return "Department{" +
                "departmentId=" + departmentId +
                ", departmentName='" + departmentName + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
