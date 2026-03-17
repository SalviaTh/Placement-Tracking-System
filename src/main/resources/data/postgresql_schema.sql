-- Users table
CREATE TABLE IF NOT EXISTS users (
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'STUDENT',
    is_active BOOLEAN DEFAULT TRUE,
    last_login TIMESTAMP,
    student_id VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Students table (update existing)
ALTER TABLE students 
ADD COLUMN IF NOT EXISTS cgpa DECIMAL(3, 2) DEFAULT 0.00,
ADD COLUMN IF NOT EXISTS backlogs INT DEFAULT 0,
ADD COLUMN IF NOT EXISTS skills TEXT,
ADD COLUMN IF NOT EXISTS is_eligible_for_placement BOOLEAN DEFAULT TRUE;

-- Placement Drives
CREATE TABLE IF NOT EXISTS placement_drives (
    drive_id SERIAL PRIMARY KEY,
    company_name VARCHAR(100) NOT NULL,
    job_role VARCHAR(100) NOT NULL,
    job_description TEXT,
    package_offered DECIMAL(10, 2),
    min_cgpa DECIMAL(3, 2) DEFAULT 0.00,
    max_backlogs INT DEFAULT 0,
    eligible_departments TEXT,
    eligible_batches TEXT,
    drive_date DATE,
    application_deadline TIMESTAMP,
    venue VARCHAR(200),
    status VARCHAR(20) DEFAULT 'UPCOMING',
    total_positions INT DEFAULT 1,
    created_by INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Applications
CREATE TABLE IF NOT EXISTS applications (
    application_id SERIAL PRIMARY KEY,
    drive_id INT NOT NULL,
    student_id VARCHAR(20) NOT NULL,
    status VARCHAR(20) DEFAULT 'APPLIED',
    cover_letter TEXT,
    applied_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    shortlisted_at TIMESTAMP,
    interviewed_at TIMESTAMP,
    offered_at TIMESTAMP,
    final_status_at TIMESTAMP,
    admin_remarks TEXT,
    interview_feedback TEXT,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(drive_id, student_id)
);

-- Notifications
CREATE TABLE IF NOT EXISTS notifications (
    notification_id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    title VARCHAR(200) NOT NULL,
    message TEXT NOT NULL,
    notification_type VARCHAR(50) NOT NULL,
    related_drive_id INT,
    related_application_id INT,
    is_read BOOLEAN DEFAULT FALSE,
    read_at TIMESTAMP,
    email_sent BOOLEAN DEFAULT FALSE,
    email_sent_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Offer Letters
CREATE TABLE IF NOT EXISTS offer_letters (
    offer_id SERIAL PRIMARY KEY,
    application_id INT NOT NULL,
    package_offered DECIMAL(10, 2) NOT NULL,
    joining_date DATE,
    offer_letter_path VARCHAR(500),
    acceptance_status VARCHAR(20) DEFAULT 'PENDING',
    accepted_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert admin user
INSERT INTO users (username, email, password_hash, role)
VALUES ('admin', 'admin@college.edu', '$2a$10$N9qo8uLOickgx2ZMRZoMye5POv2Jm0sQdg3DfPPLbQLaU8OqhKQ8u', 'ADMIN')
ON CONFLICT (username) DO NOTHING;