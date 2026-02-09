package com.college.placement.model;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;




@Entity
@Table(name = "student_resumes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentResume {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resume_id")
    private Integer resumeId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
    
    // Extracted Information
    @Column(name = "extracted_name", length = 100)
    private String extractedName;
    
    @Column(name = "extracted_email", length = 100)
    private String extractedEmail;
    
    @Column(name = "extracted_skills", columnDefinition = "JSON")
    private String extractedSkills; // JSON: ["Java", "Python"]
    
    @Column(name = "extracted_cgpa", precision = 3, scale = 2)
    private BigDecimal extractedCgpa;
    
    // File Information
    @Column(name = "file_name", nullable = false)
    private String fileName;
    
    @Column(name = "file_path", nullable = false, length = 500)
    private String filePath;
    
    @Column(name = "file_size")
    private Integer fileSize;
    
    @Column(name = "uploaded_at", updatable = false)
    private LocalDateTime uploadedAt;
    
    @PrePersist
    protected void onCreate() {
        uploadedAt = LocalDateTime.now();
    }
}