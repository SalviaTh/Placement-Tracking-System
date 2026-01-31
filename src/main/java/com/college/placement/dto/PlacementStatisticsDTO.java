package com.college.placement.dto;

import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlacementStatisticsDTO {
    private Long totalStudents;
    private Long studentsPlaced;
    private BigDecimal placementPercentage;
    private BigDecimal averagePackage;
    private BigDecimal highestPackage;
    private Long companiesVisited;
    private Long expectedCompanies;
}
