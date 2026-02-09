-- ============================================
-- PLACEMENT TRACKING SYSTEM - DATABASE SCHEMA
-- ============================================

-- Drop existing tables if they exist
DROP TABLE IF EXISTS placements;
DROP TABLE IF EXISTS expected_companies;
DROP TABLE IF EXISTS companies;
DROP TABLE IF EXISTS students;

-- ============================================
-- STUDENTS TABLE
-- ============================================
CREATE TABLE students (
    student_id VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    department VARCHAR(50) NOT NULL,
    batch_year INT NOT NULL,
    placement_status ENUM('Placed', 'Not Placed') DEFAULT 'Not Placed',
    company_placed VARCHAR(100),
    package_lpa DECIMAL(10, 2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_department (department),
    INDEX idx_batch_year (batch_year),
    INDEX idx_placement_status (placement_status)
);

-- ============================================
-- COMPANIES TABLE
-- ============================================
CREATE TABLE companies (
    company_id INT AUTO_INCREMENT PRIMARY KEY,
    company_name VARCHAR(100) NOT NULL,
    visit_year INT NOT NULL,
    students_placed INT DEFAULT 0,
    package_offered DECIMAL(10, 2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY unique_company_year (company_name, visit_year),
    INDEX idx_visit_year (visit_year)
);

-- ============================================
-- EXPECTED COMPANIES TABLE
-- ============================================
CREATE TABLE expected_companies (
    expected_company_id INT AUTO_INCREMENT PRIMARY KEY,
    company_name VARCHAR(100) NOT NULL,
    expected_year INT NOT NULL,
    status ENUM('Confirmed', 'Expected', 'Cancelled') DEFAULT 'Expected',
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_expected_year (expected_year),
    INDEX idx_status (status)
);

-- ============================================
-- PLACEMENTS TABLE (Junction table for many-to-many)
-- ============================================
CREATE TABLE placements (
    placement_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id VARCHAR(20) NOT NULL,
    company_id INT NOT NULL,
    placement_date DATE,
    package_lpa DECIMAL(10, 2),
    role VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
    FOREIGN KEY (company_id) REFERENCES companies(company_id) ON DELETE CASCADE,
    INDEX idx_placement_date (placement_date)
);

-- ============================================
-- SAMPLE DATA
-- ============================================

-- Insert sample students
INSERT INTO students (student_id, name, department, batch_year, placement_status, company_placed, package_lpa) VALUES
('CS2021001', 'Rajesh Kumar', 'Computer Science', 2025, 'Placed', 'Google', 18.50),
('CS2021002', 'Priya Sharma', 'Computer Science', 2025, 'Placed', 'Microsoft', 16.00),
('CS2021003', 'Amit Patel', 'Computer Science', 2025, 'Not Placed', NULL, NULL),
('EC2021001', 'Sneha Reddy', 'Electronics', 2025, 'Placed', 'Intel', 12.50),
('EC2021002', 'Vikram Singh', 'Electronics', 2025, 'Placed', 'Qualcomm', 14.00),
('ME2021001', 'Anjali Gupta', 'Mechanical', 2025, 'Not Placed', NULL, NULL),
('CS2020001', 'Rahul Verma', 'Computer Science', 2024, 'Placed', 'Amazon', 20.00),
('CS2020002', 'Kavya Nair', 'Computer Science', 2024, 'Placed', 'Flipkart', 15.50),
('EC2020001', 'Arjun Mehta', 'Electronics', 2024, 'Placed', 'Samsung', 13.00),
('ME2020001', 'Divya Iyer', 'Mechanical', 2024, 'Placed', 'Tata Motors', 8.50),
('CS2022001', 'Aditya Rao', 'Computer Science', 2026, 'Not Placed', NULL, NULL),
('EC2022001', 'Pooja Das', 'Electronics', 2026, 'Not Placed', NULL, NULL);

-- Insert sample companies
INSERT INTO companies (company_name, visit_year, students_placed, package_offered) VALUES
('Google', 2025, 1, 18.50),
('Microsoft', 2025, 1, 16.00),
('Intel', 2025, 1, 12.50),
('Qualcomm', 2025, 1, 14.00),
('Amazon', 2024, 1, 20.00),
('Flipkart', 2024, 1, 15.50),
('Samsung', 2024, 1, 13.00),
('Tata Motors', 2024, 1, 8.50),
('Infosys', 2023, 5, 7.50),
('TCS', 2023, 8, 6.50),
('Wipro', 2023, 6, 7.00);

-- Insert sample expected companies
INSERT INTO expected_companies (company_name, expected_year, status, notes) VALUES
('Apple', 2026, 'Confirmed', 'Campus drive scheduled for March 2026'),
('Netflix', 2026, 'Expected', 'In talks with placement cell'),
('Tesla', 2026, 'Expected', 'Interested in Mechanical and Electronics students'),
('Adobe', 2026, 'Confirmed', 'Pre-placement talk scheduled'),
('Oracle', 2025, 'Cancelled', 'Company postponed campus recruitment');

-- Insert sample placements (linking students to companies)
INSERT INTO placements (student_id, company_id, placement_date, package_lpa, role) VALUES
('CS2021001', 1, '2024-11-15', 18.50, 'Software Engineer'),
('CS2021002', 2, '2024-10-20', 16.00, 'Software Developer'),
('EC2021001', 3, '2024-09-10', 12.50, 'Hardware Engineer'),
('EC2021002', 4, '2024-10-05', 14.00, 'Chip Design Engineer'),
('CS2020001', 5, '2023-11-20', 20.00, 'SDE-1'),
('CS2020002', 6, '2023-10-15', 15.50, 'Backend Developer'),
('EC2020001', 7, '2023-09-25', 13.00, 'Electronics Engineer'),
('ME2020001', 8, '2023-08-30', 8.50, 'Design Engineer');

-- ============================================
-- VIEWS FOR STATISTICS
-- ============================================

-- View: Overall placement statistics
CREATE OR REPLACE VIEW placement_statistics AS
SELECT 
    COUNT(*) as total_students,
    SUM(CASE WHEN placement_status = 'Placed' THEN 1 ELSE 0 END) as students_placed,
    ROUND((SUM(CASE WHEN placement_status = 'Placed' THEN 1 ELSE 0 END) * 100.0 / COUNT(*)), 2) as placement_percentage,
    ROUND(AVG(CASE WHEN placement_status = 'Placed' THEN package_lpa END), 2) as avg_package,
    MAX(package_lpa) as highest_package
FROM students;

-- View: Department-wise statistics
CREATE OR REPLACE VIEW department_statistics AS
SELECT 
    department,
    COUNT(*) as total_students,
    SUM(CASE WHEN placement_status = 'Placed' THEN 1 ELSE 0 END) as students_placed,
    ROUND((SUM(CASE WHEN placement_status = 'Placed' THEN 1 ELSE 0 END) * 100.0 / COUNT(*)), 2) as placement_percentage,
    ROUND(AVG(CASE WHEN placement_status = 'Placed' THEN package_lpa END), 2) as avg_package
FROM students
GROUP BY department;

-- View: Year-wise statistics
CREATE OR REPLACE VIEW year_statistics AS
SELECT 
    batch_year,
    COUNT(*) as total_students,
    SUM(CASE WHEN placement_status = 'Placed' THEN 1 ELSE 0 END) as students_placed,
    ROUND((SUM(CASE WHEN placement_status = 'Placed' THEN 1 ELSE 0 END) * 100.0 / COUNT(*)), 2) as placement_percentage,
    ROUND(AVG(CASE WHEN placement_status = 'Placed' THEN package_lpa END), 2) as avg_package
FROM students
GROUP BY batch_year
ORDER BY batch_year DESC;

-- ============================================
-- STORED PROCEDURES
-- ============================================

-- Procedure to update student placement
DELIMITER //
CREATE PROCEDURE update_student_placement(
    IN p_student_id VARCHAR(20),
    IN p_company_name VARCHAR(100),
    IN p_package DECIMAL(10, 2),
    IN p_role VARCHAR(100)
)
BEGIN
    DECLARE v_company_id INT;
    DECLARE v_visit_year INT;
    
    -- Get current year
    SET v_visit_year = YEAR(CURRENT_DATE);
    
    -- Get or create company
    SELECT company_id INTO v_company_id 
    FROM companies 
    WHERE company_name = p_company_name AND visit_year = v_visit_year;
    
    IF v_company_id IS NULL THEN
        INSERT INTO companies (company_name, visit_year, students_placed, package_offered)
        VALUES (p_company_name, v_visit_year, 1, p_package);
        SET v_company_id = LAST_INSERT_ID();
    ELSE
        UPDATE companies 
        SET students_placed = students_placed + 1,
            package_offered = GREATEST(package_offered, p_package)
        WHERE company_id = v_company_id;
    END IF;
    
    -- Update student
    UPDATE students 
    SET placement_status = 'Placed',
        company_placed = p_company_name,
        package_lpa = p_package
    WHERE student_id = p_student_id;
    
    -- Create placement record
    INSERT INTO placements (student_id, company_id, placement_date, package_lpa, role)
    VALUES (p_student_id, v_company_id, CURRENT_DATE, p_package, p_role);
END //
DELIMITER ;
