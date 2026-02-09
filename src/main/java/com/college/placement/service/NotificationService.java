package com.college.placement.service;

import com.college.placement.model.*;
import com.college.placement.repository.NotificationRepository;
import com.college.placement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {
    
    @Autowired
    private NotificationRepository notificationRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * Notify about new drive (async)
     */
    @Async
    public void notifyNewDrive(PlacementDrive drive) {
        try {
            // Get all active students
            List<User> students = userRepository.findByRole(User.UserRole.STUDENT);
            
            for (User user : students) {
                if (user.getIsActive()) {
                    Notification notification = Notification.builder()
                            .user(user)
                            .title("New Placement Drive")
                            .message(String.format("New opportunity: %s is hiring for %s position. Package: ₹%.2f LPA",
                                    drive.getCompanyName(), 
                                    drive.getJobRole(),
                                    drive.getPackageOffered()))
                            .notificationType(Notification.NotificationType.NEW_DRIVE)
                            .relatedDrive(drive)
                            .build();
                    
                    notificationRepository.save(notification);
                }
            }
        } catch (Exception e) {
            // Log error but don't fail the main operation
            System.err.println("Error sending notifications: " + e.getMessage());
        }
    }
    
    /**
     * Notify when drive is cancelled
     */
    @Async
    public void notifyDriveCancelled(PlacementDrive drive) {
        try {
            // Get all users who applied to this drive
            List<Application> applications = drive.getApplications();
            
            for (Application app : applications) {
                Student student = app.getStudent();
                if (student.getUser() != null) {
                    Notification notification = Notification.builder()
                            .user(student.getUser())
                            .title("Placement Drive Cancelled")
                            .message(String.format("The placement drive for %s - %s has been cancelled.",
                                    drive.getCompanyName(), 
                                    drive.getJobRole()))
                            .notificationType(Notification.NotificationType.SYSTEM)
                            .relatedDrive(drive)
                            .build();
                    
                    notificationRepository.save(notification);
                }
            }
        } catch (Exception e) {
            System.err.println("Error sending cancellation notifications: " + e.getMessage());
        }
    }
    
    /**
     * Get unread notifications for user
     */
    public List<Notification> getUnreadNotifications(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        return notificationRepository.findByUserAndIsReadFalse(user);
    }
    
    /**
     * Mark notification as read
     */
    public void markAsRead(Integer notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        
        notification.setIsRead(true);
        notification.setReadAt(LocalDateTime.now());
        notificationRepository.save(notification);
    }
}