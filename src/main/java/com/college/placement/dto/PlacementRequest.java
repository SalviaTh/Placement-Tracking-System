package com.college.placement.dto;

import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlacementRequest {
    private String studentId;
    private String companyName;
    private BigDecimal packageLpa;
    private String role;
}
