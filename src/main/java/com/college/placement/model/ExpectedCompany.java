package com.college.placement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "expected_companies")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpectedCompany {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "expected_company_id")
    private Integer expectedCompanyId;
    
    @NotBlank(message = "Company name is required")
    @Column(name = "company_name", nullable = false, length = 100)
    private String companyName;
    
    @NotNull(message = "Expected year is required")
    @Column(name = "expected_year", nullable = false)
    private Integer expectedYear;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VisitStatus status = VisitStatus.EXPECTED;
    
    @Column(columnDefinition = "TEXT")
    private String notes;
    
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
    
    public enum VisitStatus {
        CONFIRMED("Confirmed"),
        EXPECTED("Expected"),
        CANCELLED("Cancelled");
        
        private final String displayName;
        
        VisitStatus(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
}