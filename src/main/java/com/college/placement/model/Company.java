package com.college.placement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "companies", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"company_name", "visit_year"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Company {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_id")
    private Integer companyId;
    
    @NotBlank(message = "Company name is required")
    @Column(name = "company_name", nullable = false, length = 100)
    private String companyName;
    
    @NotNull(message = "Visit year is required")
    @Column(name = "visit_year", nullable = false)
    private Integer visitYear;
    
    @Column(name = "students_placed")
    private Integer studentsPlaced = 0;
    
    @Column(name = "package_offered", precision = 10, scale = 2)
    private BigDecimal packageOffered;
    
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
}