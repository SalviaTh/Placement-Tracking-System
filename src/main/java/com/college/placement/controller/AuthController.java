
package com.college.placement.controller;

import com.college.placement.dto.*;
import com.college.placement.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    /**
     * User Login
     * POST /api/auth/login
     * Public endpoint
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        try {
            AuthResponse response = authService.authenticateUser(request);
            return ResponseEntity.ok(ApiResponse.success("Login successful", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Invalid credentials: " + e.getMessage()));
        }
    }
    
    /**
     * User Registration (Student)
     * POST /api/auth/register
     * Public endpoint
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        try {
            AuthResponse response = authService.registerStudent(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Registration successful", response));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * Admin Registration (Restricted)
     * POST /api/auth/register/admin
     * Requires existing admin authentication
     */
    @PostMapping("/register/admin")
    public ResponseEntity<ApiResponse<AuthResponse>> registerAdmin(@Valid @RequestBody RegisterRequest request) {
        try {
            AuthResponse response = authService.registerAdmin(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Admin registered successfully", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * Get Current User Profile
     * GET /api/auth/me
     * Requires authentication
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        try {
            UserProfileResponse profile = authService.getUserProfile(username);
            return ResponseEntity.ok(ApiResponse.success("Profile retrieved", profile));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("User not found"));
        }
    }
    
    /**
     * Refresh Token
     * POST /api/auth/refresh
     */
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<TokenResponse>> refreshToken(@RequestBody TokenRefreshRequest request) {
        try {
            TokenResponse response = authService.refreshToken(request.getRefreshToken());
            return ResponseEntity.ok(ApiResponse.success("Token refreshed", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Invalid refresh token"));
        }
    }
    
    /**
     * Logout
     * POST /api/auth/logout
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout() {
        // In stateless JWT, logout is handled on client side by removing token
        // Optionally implement token blacklist here
        return ResponseEntity.ok(ApiResponse.success("Logged out successfully", null));
    }
}