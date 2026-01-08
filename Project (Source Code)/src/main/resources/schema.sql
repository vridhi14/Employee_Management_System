-- Employee Management and Payroll System Database Schema

-- Create database
CREATE DATABASE IF NOT EXISTS employee_payroll_db;
USE employee_payroll_db;

-- Drop existing tables if they exist
DROP TABLE IF EXISTS payroll;
DROP TABLE IF EXISTS employees;
DROP TABLE IF EXISTS departments;

-- Create departments table
CREATE TABLE departments (
    department_id INT PRIMARY KEY AUTO_INCREMENT,
    department_name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create employees table
CREATE TABLE employees (
    employee_id INT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20),
    hire_date DATE NOT NULL,
    job_title VARCHAR(100) NOT NULL,
    department_id INT,
    salary DECIMAL(10, 2) NOT NULL,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (department_id) REFERENCES departments(department_id)
);

-- Create payroll table
CREATE TABLE payroll (
    payroll_id INT PRIMARY KEY AUTO_INCREMENT,
    employee_id INT NOT NULL,
    pay_period_start DATE NOT NULL,
    pay_period_end DATE NOT NULL,
    basic_salary DECIMAL(10, 2) NOT NULL,
    bonus DECIMAL(10, 2) DEFAULT 0.00,
    deductions DECIMAL(10, 2) DEFAULT 0.00,
    net_salary DECIMAL(10, 2) NOT NULL,
    payment_date DATE,
    payment_status VARCHAR(20) DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (employee_id) REFERENCES employees(employee_id)
);

-- Insert sample departments
INSERT INTO departments (department_name, description) VALUES
('Human Resources', 'Manages employee relations and recruitment'),
('Engineering', 'Software development and technical operations'),
('Finance', 'Financial planning and accounting'),
('Marketing', 'Marketing and sales operations'),
('Operations', 'Day-to-day business operations');

-- Insert sample employees
INSERT INTO employees (first_name, last_name, email, phone, hire_date, job_title, department_id, salary, status) VALUES
('John', 'Doe', 'john.doe@company.com', '123-456-7890', '2023-01-15', 'Software Engineer', 2, 75000.00, 'ACTIVE'),
('Jane', 'Smith', 'jane.smith@company.com', '123-456-7891', '2023-02-20', 'HR Manager', 1, 65000.00, 'ACTIVE'),
('Bob', 'Johnson', 'bob.johnson@company.com', '123-456-7892', '2023-03-10', 'Financial Analyst', 3, 70000.00, 'ACTIVE'),
('Alice', 'Williams', 'alice.williams@company.com', '123-456-7893', '2023-04-05', 'Marketing Specialist', 4, 60000.00, 'ACTIVE'),
('Charlie', 'Brown', 'charlie.brown@company.com', '123-456-7894', '2023-05-12', 'Operations Manager', 5, 68000.00, 'ACTIVE');

-- Create indexes for better query performance
CREATE INDEX idx_employee_email ON employees(email);
CREATE INDEX idx_employee_department ON employees(department_id);
CREATE INDEX idx_payroll_employee ON payroll(employee_id);
CREATE INDEX idx_payroll_period ON payroll(pay_period_start, pay_period_end);
