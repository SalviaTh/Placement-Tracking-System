
package com.college.placement.dto;

import lombok.*;
import java.math.BigDecimal;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentStatisticsDTO {
    private String department;
    private Long totalStudents;
    private Long studentsPlaced;
    private BigDecimal placementPercentage;
    private BigDecimal averagePackage;
}