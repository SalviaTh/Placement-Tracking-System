
package com.college.placement.dto;

import jakarta.validation.constraints.*;
import lombok.*;

// Auth Response
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {
    private String accessToken;
    private String tokenType = "Bearer";
    private Long expiresIn;
    private String username;
    private String email;
    private String role;
    private UserProfileResponse profile;
}