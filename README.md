# Placement Tracking System

A comprehensive full-stack web application for colleges to manage and analyze student placement data with real-time statistics, interactive dashboards, and department-wise analytics.
---

## Features

### Dashboard Analytics
- **Real-time Statistics Cards** - Total students, placement rate, average package, etc.
- **Interactive Charts** - Department-wise, year-wise, and status-based visualizations
- **Responsive Design** - Modern dark theme with glassmorphism effects

### Student Management
- Add, update, and view student records
- Track placement status and company details
- Filter by department and batch year
- View individual student package details

### Company Management
- Track companies that visited campus
- Monitor students placed per company
- Record package offerings
- Year-wise company visit history

### Expected Companies
- Maintain list of upcoming company visits
- Track visit status (Confirmed/Expected/Cancelled)
- Add notes and scheduling information

### Statistics & Reporting
- Overall placement percentage
- Department-wise performance comparison
- Year-over-year placement trends
- Average and highest package tracking
- Companies visited count

---

## Architecture

### Three-Tier Architecture

```
┌─────────────────────────────────────┐
│   PRESENTATION LAYER                │
│   React/HTML + CSS + JavaScript     │
│   - Interactive Dashboard           │
│   - Charts & Visualizations         │
└─────────────────────────────────────┘
              ↓ REST API
┌─────────────────────────────────────┐
│   APPLICATION LAYER                 │
│   Spring Boot REST API              │
│   - Controllers                     │
│   - Services (Business Logic)       │
│   - DTOs                            │
└─────────────────────────────────────┘
              ↓ JDBC/JPA
┌─────────────────────────────────────┐
│   DATA LAYER                        │
│   MySQL Database                    │
│   - Students, Companies             │
│   - Placements, Expected Companies  │
└─────────────────────────────────────┘
```

---

## Tech Stack

### Backend
- **Java 17** - Programming language
- **Spring Boot 3.2.0** - Application framework
- **Spring Data JPA** - ORM and database operations
- **MySQL 8.0+** - Relational database
- **Maven** - Build and dependency management
- **Lombok** - Code generation and reduction

### Frontend)
- **HTML5/CSS3/JavaScript** - Vanilla web technologies
- **Chart.js** - Data visualization library
- **Google Fonts** - Manrope & Space Mono typography


---


### Step 1: Database Setup

```bash
# Login to MySQL
mysql -u root -p

# Create database
CREATE DATABASE placement_tracking;
USE placement_tracking;

# Import schema and sample data
source database_schema.sql;
```

### Step 2: Backend Setup

```bash
# Clone/download the project files

# Update application.properties with your MySQL credentials
# File location: src/main/resources/application.properties
spring.datasource.username=your_mysql_username
spring.datasource.password=your_mysql_password

# Build the project
mvn clean install

# Run the Spring Boot application
mvn spring-boot:run

# Backend will start on http://localhost:8080
```

### Step 3: Frontend Setup
simple open with live server in the Dashboard.html
Then navigate to http://localhost:8080/dashboard.html
```

---

##  Statistics Calculation

### Overall Placement Percentage
```
Placement % = (Students Placed / Total Students) × 100
```

### Department-wise Statistics
```sql
GROUP BY department
- Count total students per department
- Count placed students per department
- Calculate percentage and average package
```

### Year-wise Statistics
```sql
GROUP BY batch_year
- Count total students per batch
- Count placed students per batch
- Calculate trends over years
```

### Placement Update Process
1. Update student record (status, company, package)
2. Create/update company record for current year
3. Increment company's placement count
4. Update company's highest package if needed
5. Create placement link in junction table

All operations wrapped in a transaction for data integrity.

---

## Usage Guide

### Adding a New Student

**Via API:**
```bash
curl -X POST http://localhost:8080/api/students \
  -H "Content-Type: application/json" \
  -d '{
    "studentId": "CS2025001",
    "name": "John Doe",
    "department": "Computer Science",
    "batchYear": 2025
  }'
```

### Recording a Placement

**Via API:**
```bash
curl -X POST http://localhost:8080/api/statistics/placement \
  -H "Content-Type: application/json" \
  -d '{
    "studentId": "CS2025001",
    "companyName": "Google",
    "packageLpa": 18.50,
    "role": "Software Engineer"
  }'
```

### Viewing Statistics

Simply navigate to the dashboard in your browser and select the appropriate tab:
- **Dashboard** - Overall statistics and charts
- **Students** - Complete student records table
- **Companies** - Company visit history
- **Expected** - Upcoming company visits

---

## 🔧 Customization

### Changing Database Credentials
Edit `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/placement_tracking
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
```

---

## 📄 License

This project is created for educational purposes. Feel free to use and modify as needed.

---

## 🤝 Contributing

Contributions are welcome! To contribute:
1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Submit a pull request


---

**Version:** 1.0.0  
**Last Updated:** January 2025  
**Author:** Thingbaijam Salvia

---

Made with ❤️ for colleges and placement officers

