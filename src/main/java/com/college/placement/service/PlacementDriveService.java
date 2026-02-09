
package com.college.placement.service;

import com.college.placement.dto.*;
import com.college.placement.model.*;
import com.college.placement.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PlacementDriveService {
    
    @Autowired
    private PlacementDriveRepository driveRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private ApplicationRepository applicationRepository;
    
    @Autowired
    private NotificationService notificationService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    /**
     * Create new placement drive
     */
    @Transactional
    public PlacementDriveResponse createDrive(PlacementDriveRequest request, String adminUsername) {
        User admin = userRepository.findByUsername(adminUsername)
                .orElseThrow(() -> new RuntimeException("Admin not found"));
        
        try {
            PlacementDrive drive = PlacementDrive.builder()
                    .companyName(request.getCompanyName())
                    .jobRole(request.getJobRole())
                    .jobDescription(request.getJobDescription())
                    .packageOffered(request.getPackageOffered())
                    .minCgpa(request.getMinCgpa())
                    .maxBacklogs(request.getMaxBacklogs())
                    .eligibleDepartments(objectMapper.writeValueAsString(request.getEligibleDepartments()))
                    .eligibleBatches(objectMapper.writeValueAsString(request.getEligibleBatches()))
                    .driveDate(request.getDriveDate())
                    .applicationDeadline(request.getApplicationDeadline())
                    .venue(request.getVenue())
                    .status(PlacementDrive.DriveStatus.UPCOMING)
                    .totalPositions(request.getTotalPositions())
                    .createdBy(admin)
                    .build();
            
            drive = driveRepository.save(drive);
            
            // Send notifications to eligible students (asynchronous)
            notificationService.notifyNewDrive(drive);
            
            return mapToResponse(drive, null);
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to create drive: " + e.getMessage());
        }
    }
    
    /**
     * Update existing drive
     */
    @Transactional
    public PlacementDriveResponse updateDrive(Integer driveId, PlacementDriveRequest request) {
        PlacementDrive drive = driveRepository.findById(driveId)
                .orElseThrow(() -> new RuntimeException("Drive not found"));
        
        try {
            drive.setCompanyName(request.getCompanyName());
            drive.setJobRole(request.getJobRole());
            drive.setJobDescription(request.getJobDescription());
            drive.setPackageOffered(request.getPackageOffered());
            drive.setMinCgpa(request.getMinCgpa());
            drive.setMaxBacklogs(request.getMaxBacklogs());
            drive.setEligibleDepartments(objectMapper.writeValueAsString(request.getEligibleDepartments()));
            drive.setEligibleBatches(objectMapper.writeValueAsString(request.getEligibleBatches()));
            drive.setDriveDate(request.getDriveDate());
            drive.setApplicationDeadline(request.getApplicationDeadline());
            drive.setVenue(request.getVenue());
            drive.setTotalPositions(request.getTotalPositions());
            
            drive = driveRepository.save(drive);
            return mapToResponse(drive, null);
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to update drive: " + e.getMessage());
        }
    }
    
    /**
     * Get all drives with filters
     */
    public List<PlacementDriveResponse> getAllDrives(String status, String company) {
        List<PlacementDrive> drives;
        
        if (status != null && company != null) {
            drives = driveRepository.findByStatusAndCompanyNameContaining(
                    PlacementDrive.DriveStatus.valueOf(status.toUpperCase()), company);
        } else if (status != null) {
            drives = driveRepository.findByStatus(PlacementDrive.DriveStatus.valueOf(status.toUpperCase()));
        } else if (company != null) {
            drives = driveRepository.findByCompanyNameContaining(company);
        } else {
            drives = driveRepository.findAll();
        }
        
        return drives.stream()
                .map(drive -> mapToResponse(drive, null))
                .collect(Collectors.toList());
    }
    
    /**
     * Get drive by ID
     */
    public PlacementDriveResponse getDriveById(Integer driveId) {
        PlacementDrive drive = driveRepository.findById(driveId)
                .orElseThrow(() -> new RuntimeException("Drive not found"));
        return mapToResponse(drive, null);
    }
    
    /**
     * Get eligible drives for student
     */
    public List<PlacementDriveResponse> getEligibleDrivesForStudent(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (user.getStudent() == null) {
            throw new RuntimeException("Student record not found");
        }
        
        Student student = user.getStudent();
        
        // Get all active drives
        List<PlacementDrive> activeDrives = driveRepository.findByStatus(PlacementDrive.DriveStatus.UPCOMING);
        activeDrives.addAll(driveRepository.findByStatus(PlacementDrive.DriveStatus.ONGOING));
        
        // Filter eligible drives
        return activeDrives.stream()
                .filter(drive -> isStudentEligible(student, drive))
                .filter(drive -> !drive.isDeadlinePassed())
                .map(drive -> {
                    // Check if already applied
                    Optional<Application> existingApp = applicationRepository
                            .findByDriveAndStudent(drive, student);
                    return mapToResponse(drive, existingApp.orElse(null));
                })
                .collect(Collectors.toList());
    }
    
    /**
     * Check eligibility for specific drive
     */
    public EligibilityCheckResponse checkEligibility(Integer driveId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Student student = user.getStudent();
        if (student == null) {
            throw new RuntimeException("Student record not found");
        }
        
        PlacementDrive drive = driveRepository.findById(driveId)
                .orElseThrow(() -> new RuntimeException("Drive not found"));
        
        return performEligibilityCheck(student, drive);
    }
    
    /**
     * Cancel drive
     */
    @Transactional
    public void cancelDrive(Integer driveId) {
        PlacementDrive drive = driveRepository.findById(driveId)
                .orElseThrow(() -> new RuntimeException("Drive not found"));
        
        drive.setStatus(PlacementDrive.DriveStatus.CANCELLED);
        driveRepository.save(drive);
        
        // Notify all applicants
        notificationService.notifyDriveCancelled(drive);
    }
    
    /**
     * Get drive statistics
     */
    public DriveStatisticsResponse getDriveStatistics(Integer driveId) {
        PlacementDrive drive = driveRepository.findById(driveId)
                .orElseThrow(() -> new RuntimeException("Drive not found"));
        
        List<Application> applications = applicationRepository.findByDrive(drive);
        
        Map<String, Integer> deptWise = new HashMap<>();
        for (Application app : applications) {
            String dept = app.getStudent().getDepartment();
            deptWise.put(dept, deptWise.getOrDefault(dept, 0) + 1);
        }
        
        return DriveStatisticsResponse.builder()
                .driveId(drive.getDriveId())
                .companyName(drive.getCompanyName())
                .totalApplications(applications.size())
                .shortlistedCount((int) applications.stream().filter(a -> a.getStatus() == Application.ApplicationStatus.SHORTLISTED).count())
                .interviewedCount((int) applications.stream().filter(a -> a.getStatus() == Application.ApplicationStatus.INTERVIEWED).count())
                .offeredCount((int) applications.stream().filter(a -> a.getStatus() == Application.ApplicationStatus.OFFERED).count())
                .acceptedCount((int) applications.stream().filter(a -> a.getStatus() == Application.ApplicationStatus.ACCEPTED).count())
                .rejectedCount((int) applications.stream().filter(a -> a.getStatus() == Application.ApplicationStatus.REJECTED).count())
                .withdrawnCount((int) applications.stream().filter(a -> a.getStatus() == Application.ApplicationStatus.WITHDRAWN).count())
                .departmentWiseApplications(deptWise)
                .build();
    }
    
    // ============================================
    // ELIGIBILITY ENGINE - CORE LOGIC
    // ============================================
    
    private boolean isStudentEligible(Student student, PlacementDrive drive) {
        try {
            List<String> depts = objectMapper.readValue(drive.getEligibleDepartments(), List.class);
            List<Integer> batches = objectMapper.readValue(drive.getEligibleBatches(), List.class);
            
            // CGPA check
            if (student.getCgpa().compareTo(drive.getMinCgpa()) < 0) {
                return false;
            }
            
            // Backlogs check
            if (student.getBacklogs() > drive.getMaxBacklogs()) {
                return false;
            }
            
            // Department check
            if (!depts.contains(student.getDepartment())) {
                return false;
            }
            
            // Batch check
            if (!batches.contains(student.getBatchYear())) {
                return false;
            }
            
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    private EligibilityCheckResponse performEligibilityCheck(Student student, PlacementDrive drive) {
        List<String> failedCriteria = new ArrayList<>();
        boolean isEligible = true;
        
        try {
            List<String> depts = objectMapper.readValue(drive.getEligibleDepartments(), List.class);
            List<Integer> batches = objectMapper.readValue(drive.getEligibleBatches(), List.class);
            
            // CGPA check
            if (student.getCgpa().compareTo(drive.getMinCgpa()) < 0) {
                isEligible = false;
                failedCriteria.add(String.format("CGPA requirement not met. Required: %.2f, Your CGPA: %.2f", 
                        drive.getMinCgpa(), student.getCgpa()));
            }
            
            // Backlogs check
            if (student.getBacklogs() > drive.getMaxBacklogs()) {
                isEligible = false;
                failedCriteria.add(String.format("Too many backlogs. Maximum allowed: %d, You have: %d", 
                        drive.getMaxBacklogs(), student.getBacklogs()));
            }
            
            // Department check
            if (!depts.contains(student.getDepartment())) {
                isEligible = false;
                failedCriteria.add(String.format("Your department (%s) is not eligible for this drive", 
                        student.getDepartment()));
            }
            
            // Batch check
            if (!batches.contains(student.getBatchYear())) {
                isEligible = false;
                failedCriteria.add(String.format("Your batch (%d) is not eligible for this drive", 
                        student.getBatchYear()));
            }
            
            String message = isEligible ? 
                    "You are eligible to apply for this drive!" : 
                    "You are not eligible for this drive based on the following criteria:";
            
            return EligibilityCheckResponse.builder()
                    .isEligible(isEligible)
                    .message(message)
                    .failedCriteria(failedCriteria)
                    .studentCgpa(student.getCgpa())
                    .studentBacklogs(student.getBacklogs())
                    .studentDepartment(student.getDepartment())
                    .studentBatch(student.getBatchYear())
                    .requiredCgpa(drive.getMinCgpa())
                    .maxAllowedBacklogs(drive.getMaxBacklogs())
                    .eligibleDepartments(depts)
                    .eligibleBatches(batches)
                    .build();
                    
        } catch (Exception e) {
            throw new RuntimeException("Error checking eligibility: " + e.getMessage());
        }
    }
    
    private PlacementDriveResponse mapToResponse(PlacementDrive drive, Application application) {
        try {
            List<String> depts = objectMapper.readValue(drive.getEligibleDepartments(), List.class);
            List<Integer> batches = objectMapper.readValue(drive.getEligibleBatches(), List.class);
            
            return PlacementDriveResponse.builder()
                    .driveId(drive.getDriveId())
                    .companyName(drive.getCompanyName())
                    .jobRole(drive.getJobRole())
                    .jobDescription(drive.getJobDescription())
                    .packageOffered(drive.getPackageOffered())
                    .minCgpa(drive.getMinCgpa())
                    .maxBacklogs(drive.getMaxBacklogs())
                    .eligibleDepartments(depts)
                    .eligibleBatches(batches)
                    .driveDate(drive.getDriveDate())
                    .applicationDeadline(drive.getApplicationDeadline())
                    .venue(drive.getVenue())
                    .status(drive.getStatus().name())
                    .totalPositions(drive.getTotalPositions())
                    .totalApplications(drive.getApplications().size())
                    .hasApplied(application != null)
                    .applicationStatus(application != null ? application.getStatus().name() : null)
                    .createdAt(drive.getCreatedAt())
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Error mapping drive to response: " + e.getMessage());
        }
    }
}