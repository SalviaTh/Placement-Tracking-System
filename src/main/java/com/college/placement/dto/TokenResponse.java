
package com.college.placement.dto;

import jakarta.validation.constraints.*;
import lombok.*;

// Token Response
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenResponse {
    private String accessToken;
    private String tokenType = "Bearer";
    private Long expiresIn;
}