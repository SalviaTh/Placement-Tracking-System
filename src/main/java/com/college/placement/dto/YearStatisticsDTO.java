package com.college.placement.dto;

import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class YearStatisticsDTO {
    private Integer year;
    private Long totalStudents;
    private Long studentsPlaced;
    private BigDecimal placementPercentage;
    private BigDecimal averagePackage;
}
