
package com.college.placement.dto;

import jakarta.validation.constraints.*;
import lombok.*;

// Token Refresh Request
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenRefreshRequest {
    @NotBlank
    private String refreshToken;
}

