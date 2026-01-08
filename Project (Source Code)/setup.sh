#!/bin/bash

# Employee Management System - Quick Setup Script

echo "=================================="
echo "Employee Management System Setup"
echo "=================================="
echo ""

# Check if MySQL is installed
if ! command -v mysql &> /dev/null; then
    echo "❌ MySQL is not installed. Please install MySQL 8.0 or higher."
    exit 1
fi

echo "✓ MySQL is installed"

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "❌ Maven is not installed. Please install Apache Maven 3.6 or higher."
    exit 1
fi

echo "✓ Maven is installed"

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "❌ Java is not installed. Please install JDK 17 or higher."
    exit 1
fi

echo "✓ Java is installed"
echo ""

# Database setup
echo "Setting up database..."
echo "Please enter your MySQL root password when prompted:"
mysql -u root -p -e "source src/main/resources/schema.sql"

if [ $? -eq 0 ]; then
    echo "✓ Database setup successful!"
else
    echo "❌ Database setup failed. Please check your MySQL credentials."
    exit 1
fi

echo ""

# Build the project
echo "Building the project..."
mvn clean package -DskipTests

if [ $? -eq 0 ]; then
    echo "✓ Project built successfully!"
else
    echo "❌ Build failed. Please check the error messages above."
    exit 1
fi

echo ""
echo "=================================="
echo "Setup Complete!"
echo "=================================="
echo ""
echo "To run the application:"
echo "  1. Update src/main/resources/db.properties with your MySQL password"
echo "  2. Run: mvn exec:java -Dexec.mainClass=\"com.employee.management.EmployeeManagementApp\""
echo "  OR"
echo "  3. Run: java -jar target/employee-management-payroll-1.0-SNAPSHOT.jar"
echo ""
