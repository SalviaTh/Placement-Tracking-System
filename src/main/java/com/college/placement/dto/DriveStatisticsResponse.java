
package com.college.placement.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DriveStatisticsResponse {
    private Integer driveId;
    private String companyName;
    private Integer totalApplications;
    private Integer shortlistedCount;
    private Integer interviewedCount;
    private Integer offeredCount;
    private Integer acceptedCount;
    private Integer rejectedCount;
    private Integer withdrawnCount;
    
    // Department-wise breakdown
    private Map<String, Integer> departmentWiseApplications;
}