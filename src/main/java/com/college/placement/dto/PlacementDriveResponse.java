package com.college.placement.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


// Placement Drive Response
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlacementDriveResponse {
    private Integer driveId;
    private String companyName;
    private String jobRole;
    private String jobDescription;
    private BigDecimal packageOffered;
    
    // Eligibility
    private BigDecimal minCgpa;
    private Integer maxBacklogs;
    private List<String> eligibleDepartments;
    private List<Integer> eligibleBatches;
    
    // Drive Details
    private LocalDate driveDate;
    private LocalDateTime applicationDeadline;
    private String venue;
    private String status;
    private Integer totalPositions;
    
    // Metadata
    private Integer totalApplications;
    private Boolean hasApplied; // For student view
    private String applicationStatus; // For student view
    private LocalDateTime createdAt;
}
