# 🎓 College Placement Tracking System

A complete web application for colleges to manage student placements, track applications, and generate analytics reports.

---

## **What Does This System Do?**

This system helps colleges manage their entire placement process:

- **Students** can view job opportunities, check eligibility, apply to companies, and track application status
- **Admins** (Placement Officers) can create placement drives, manage applications, send offers, and generate reports
- **Automated** eligibility checking, email notifications, and deadline reminders

---

## **Key Features**

### For Students:
-  Register and login with secure authentication
-  View placement drives they're eligible for
-  Apply to companies with resume upload
-  Track application status in real-time
-  Receive notifications for new drives and updates
-  Accept or decline job offers

### For Admins (Placement Officers):
-  Create and manage placement drives
-  Set eligibility criteria (CGPA, backlogs, department, batch)
-  Review applications and update status
-  Upload offer letters
-  View analytics and statistics
-  Export reports in Excel or PDF format

### Automated Features:
-  Eligibility checking based on student profile
-  Email notifications for all events
-  Daily deadline reminders
-  Resume parsing to extract information
-  Automatic placement status updates

---

##  **Technology Stack**

| Component | Technology |
|-----------|-----------|
| **Frontend** | HTML, CSS, JavaScript (or React) |
| **Backend** | Java 17 + Spring Boot 3.2 |
| **Database** | MySQL 8.0+ |
| **Authentication** | JWT (JSON Web Tokens) |
| **Email** | Spring Mail (SMTP) |
| **File Processing** | Apache PDFBox (resume parsing) |
| **Reports** | Apache POI (Excel), iText (PDF) |
| **Security** | Spring Security + BCrypt |

---


---

## **Quick Start Guide**

### Prerequisites (Install These First):

1. **Java 17** - [Download here](https://www.oracle.com/java/technologies/downloads/#java17)
2. **MySQL 8.0+** - [Download here](https://dev.mysql.com/downloads/mysql/)
3. **Maven** - [Download here](https://maven.apache.org/download.cgi)
4. **Any IDE** - IntelliJ IDEA (recommended) or VS Code

---

### Step 1: Database Setup

Open MySQL Command Line (or MySQL Workbench) and run:

```sql
-- Create database
CREATE DATABASE placement_tracking;
USE placement_tracking;

-- Import enhanced schema
source /path/to/enhanced_database_schema.sql;
```

Verify it worked:
```sql
SHOW TABLES;
-- Should show 10 tables: users, students, companies, placement_drives, applications, etc.
```

---

### Step 2: Backend Setup

**1. Create Project Folder**
```bash
mkdir placement-system
cd placement-system
```


**3. Copy `pom.xml`** (from project-structure.txt file)

**4. Configure Database** - Edit `src/main/resources/application.properties`:
```properties
# Database
spring.datasource.url=jdbc:mysql://localhost:3306/placement_tracking
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD

# JWT Secret (Change this!)
jwt.secret=YourVerySecretKey256BitsOrMore_ChangeInProduction
jwt.expiration=86400000

# Email (Optional - for notifications)
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
```

**5. Build & Run**
```bash
# Build project (downloads dependencies - first time takes 5-10 minutes)
mvn clean install

# Run application
mvn spring-boot:run
```

 **Success!** You should see: `Started PlacementTrackingApplication in X seconds`

Backend is now running at: **http://localhost:8080**

---

### Step 3: Frontend Setup

**Option A: Simple HTML (Easiest)**

1. Just open `dashboard.html` in any browser
2. Or use a simple server:
```bash
npx http-server
```

 **Success!** Frontend opens at: **http://localhost:3000**

---

### 2. Test Frontend

1. Open `dashboard.html` in browser
2. You should see:
   - Login page
   - Statistics cards (total students, placement rate, etc.)
   - Charts showing department-wise and year-wise data
   - Tables with student records

---


##  **Authentication Flow**

1. **Register** → System creates user account
2. **Login** → System returns JWT token
3. **Use Token** → Include in `Authorization: Bearer <token>` header
4. **Token Expires** → Login again (24 hours default)

---

##  **User Workflows**

### Student Journey:

1. **Register** → Create account with student ID
2. **View Drives** → See eligible placement opportunities
3. **Check Eligibility** → System checks CGPA, backlogs, department
4. **Apply** → Upload resume and cover letter
5. **Track Status** → See application progress (Shortlisted → Interviewed → Offered)
6. **Receive Offer** → Get email notification
7. **Accept/Decline** → Respond to offer

### Admin Journey:

1. **Login** → Access admin dashboard
2. **Create Drive** → Set company details and eligibility criteria
3. **Review Applications** → See who applied
4. **Shortlist Candidates** → Update application status
5. **Create Offers** → Upload offer letters with package details
6. **Generate Reports** → Export placement data
7. **View Analytics** → Track placement statistics


---

Login credentials:
- Admin: `admin` / `123456`
- Student: `rajesh.kumar` / `123456`

---

##  **You're All Set!**

Your placement tracking system is now ready to use. Start by:

1.  Login as admin
2.  Create a new placement drive
3.  Login as student  
4.  Apply to eligible drives
5.  Update application status as admin
6.  Generate reports

**Version:** 2.0 (Enhanced)  
**Last Updated:** January 2025  
**Built with:** Spring Boot + HTML + MySQL

---

