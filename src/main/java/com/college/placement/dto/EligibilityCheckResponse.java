package com.college.placement.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EligibilityCheckResponse {
    private Boolean isEligible;
    private String message;
    private List<String> failedCriteria;
    
    // Student's current values
    private BigDecimal studentCgpa;
    private Integer studentBacklogs;
    private String studentDepartment;
    private Integer studentBatch;
    
    // Drive requirements
    private BigDecimal requiredCgpa;
    private Integer maxAllowedBacklogs;
    private List<String> eligibleDepartments;
    private List<Integer> eligibleBatches;
}