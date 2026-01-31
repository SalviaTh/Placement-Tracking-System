package com.college.placement.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "placements")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Placement {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "placement_id")
    private Integer placementId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;
    
    @Column(name = "placement_date")
    private LocalDate placementDate;
    
    @Column(name = "package_lpa", precision = 10, scale = 2)
    private BigDecimal packageLpa;
    
    @Column(length = 100)
    private String role;
    
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
