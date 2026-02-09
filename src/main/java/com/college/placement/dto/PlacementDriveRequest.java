
package com.college.placement.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

// Placement Drive Request
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlacementDriveRequest {
    
    @NotBlank(message = "Company name is required")
    private String companyName;
    
    @NotBlank(message = "Job role is required")
    private String jobRole;
    
    private String jobDescription;
    
    @NotNull(message = "Package offered is required")
    @DecimalMin(value = "0.0", message = "Package must be positive")
    private BigDecimal packageOffered;
    
    // Eligibility Criteria
    @NotNull
    @DecimalMin(value = "0.0")
    @DecimalMax(value = "10.0")
    private BigDecimal minCgpa;
    
    @Min(value = 0, message = "Max backlogs must be non-negative")
    private Integer maxBacklogs;
    
    @NotEmpty(message = "At least one department must be eligible")
    private List<String> eligibleDepartments;
    
    @NotEmpty(message = "At least one batch must be eligible")
    private List<Integer> eligibleBatches;
    
    // Drive Details
    @NotNull(message = "Drive date is required")
    @Future(message = "Drive date must be in future")
    private LocalDate driveDate;
    
    @NotNull(message = "Application deadline is required")
    private LocalDateTime applicationDeadline;
    
    private String venue;
    
    @NotNull
    @Min(value = 1)
    private Integer totalPositions;
}
