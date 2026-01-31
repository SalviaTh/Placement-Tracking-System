package com.college.placement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "students")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    
    @Id
    @Column(name = "student_id", length = 20)
    private String studentId;
    
    @NotBlank(message = "Name is required")
    @Column(nullable = false, length = 100)
    private String name;
    
    @NotBlank(message = "Department is required")
    @Column(nullable = false, length = 50)
    private String department;
    
    @NotNull(message = "Batch year is required")
    @Column(name = "batch_year", nullable = false)
    private Integer batchYear;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "placement_status", nullable = false)
    private PlacementStatus placementStatus = PlacementStatus.NOT_PLACED;
    
    @Column(name = "company_placed", length = 100)
    private String companyPlaced;
    
    @Column(name = "package_lpa", precision = 10, scale = 2)
    private BigDecimal packageLpa;
    
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
    
    public enum PlacementStatus {
        PLACED("Placed"),
        NOT_PLACED("Not Placed");
        
        private final String displayName;
        
        PlacementStatus(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
}