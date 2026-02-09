
package com.college.placement.dto;

import jakarta.validation.constraints.*;
import lombok.*;

// User Profile Response
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileResponse {
    private Integer userId;
    private String username;
    private String email;
    private String role;
    private Boolean isActive;
    
    // Student-specific fields
    private String studentId;
    private String name;
    private String department;
    private Integer batchYear;
    private String placementStatus;
}