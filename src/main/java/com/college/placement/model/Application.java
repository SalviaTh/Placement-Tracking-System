package com.college.placement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;


@Entity
@Table(name = "applications",
       uniqueConstraints = @UniqueConstraint(columnNames = {"drive_id", "student_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Application {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "application_id")
    private Integer applicationId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "drive_id", nullable = false)
    private PlacementDrive drive;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus status = ApplicationStatus.APPLIED;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id")
    private StudentResume resume;
    
    @Column(name = "cover_letter", columnDefinition = "TEXT")
    private String coverLetter;
    
    // Status Timestamps
    @Column(name = "applied_at", updatable = false)
    private LocalDateTime appliedAt;
    
    @Column(name = "shortlisted_at")
    private LocalDateTime shortlistedAt;
    
    @Column(name = "interviewed_at")
    private LocalDateTime interviewedAt;
    
    @Column(name = "offered_at")
    private LocalDateTime offeredAt;
    
    @Column(name = "final_status_at")
    private LocalDateTime finalStatusAt;
    
    // Admin Feedback
    @Column(name = "admin_remarks", columnDefinition = "TEXT")
    private String adminRemarks;
    
    @Column(name = "interview_feedback", columnDefinition = "TEXT")
    private String interviewFeedback;
    
    @OneToOne(mappedBy = "application", cascade = CascadeType.ALL)
    @JsonIgnore
    private OfferLetter offerLetter;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        appliedAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    public enum ApplicationStatus {
        APPLIED,
        SHORTLISTED,
        INTERVIEWED,
        OFFERED,
        ACCEPTED,
        REJECTED,
        WITHDRAWN
    }
}