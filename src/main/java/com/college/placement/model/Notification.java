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
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Integer notificationId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @NotBlank
    @Column(nullable = false, length = 200)
    private String title;
    
    @NotBlank
    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type", nullable = false)
    private NotificationType notificationType;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "related_drive_id")
    private PlacementDrive relatedDrive;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "related_application_id")
    private Application relatedApplication;
    
    @Column(name = "is_read")
    private Boolean isRead = false;
    
    @Column(name = "read_at")
    private LocalDateTime readAt;
    
    @Column(name = "email_sent")
    private Boolean emailSent = false;
    
    @Column(name = "email_sent_at")
    private LocalDateTime emailSentAt;
    
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    
    public enum NotificationType {
        NEW_DRIVE,
        STATUS_UPDATE,
        DEADLINE_REMINDER,
        OFFER_RECEIVED,
        SYSTEM
    }
}