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
@Table(name = "placement_drives")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlacementDrive {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "drive_id")
    private Integer driveId;
    
    @NotBlank
    @Column(name = "company_name", nullable = false, length = 100)
    private String companyName;
    
    @NotBlank
    @Column(name = "job_role", nullable = false, length = 100)
    private String jobRole;
    
    @Column(name = "job_description", columnDefinition = "TEXT")
    private String jobDescription;
    
    @Column(name = "package_offered", precision = 10, scale = 2)
    private BigDecimal packageOffered;
    
    // Eligibility Criteria
    @Column(name = "min_cgpa", precision = 3, scale = 2)
    @Builder.Default
    private BigDecimal minCgpa = BigDecimal.ZERO;
    
    @Column(name = "max_backlogs")
    @Builder.Default
    private Integer maxBacklogs = 0;
    
    @Column(name = "eligible_departments", columnDefinition = "JSON")
    private String eligibleDepartments; // JSON: ["CS", "EC"]
    
    @Column(name = "eligible_batches", columnDefinition = "JSON")
    private String eligibleBatches; // JSON: [2025, 2026]
    
    // Drive Details
    @Column(name = "drive_date")
    private LocalDate driveDate;
    
    @Column(name = "application_deadline")
    private LocalDateTime applicationDeadline;
    
    @Column(length = 200)
    private String venue;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private DriveStatus status = DriveStatus.UPCOMING;
    
    @Column(name = "total_positions")
    @Builder.Default
    private Integer totalPositions = 1;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;
    
    @OneToMany(mappedBy = "drive", cascade = CascadeType.ALL)
    @JsonIgnore
    @Builder.Default
    private List<Application> applications = new ArrayList<>();
    
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    public enum DriveStatus {
        UPCOMING, ONGOING, COMPLETED, CANCELLED
    }
    
    // Helper methods
    @Transient
    public boolean isApplicationOpen() {
        return status == DriveStatus.UPCOMING || status == DriveStatus.ONGOING;
    }
    
    @Transient
    public boolean isDeadlinePassed() {
        return applicationDeadline != null && LocalDateTime.now().isAfter(applicationDeadline);
    }
}