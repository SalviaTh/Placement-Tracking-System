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
@Table(name = "offer_letters")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OfferLetter {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "offer_id")
    private Integer offerId;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;
    
    @NotNull
    @Column(name = "package_offered", nullable = false, precision = 10, scale = 2)
    private BigDecimal packageOffered;
    
    @Column(name = "joining_date")
    private LocalDate joiningDate;
    
    @Column(name = "offer_letter_path", length = 500)
    private String offerLetterPath;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "acceptance_status")
    private AcceptanceStatus acceptanceStatus = AcceptanceStatus.PENDING;
    
    @Column(name = "accepted_at")
    private LocalDateTime acceptedAt;
    
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
    
    public enum AcceptanceStatus {
        PENDING, ACCEPTED, DECLINED
    }
}