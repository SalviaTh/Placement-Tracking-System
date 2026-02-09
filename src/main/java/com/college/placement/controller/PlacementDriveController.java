
package com.college.placement.controller;

import com.college.placement.dto.*;
import com.college.placement.service.PlacementDriveService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drives")
@CrossOrigin(origins = "*")
public class PlacementDriveController {
    
    @Autowired
    private PlacementDriveService driveService;
    
    /**
     * Create new placement drive (ADMIN ONLY)
     * POST /api/drives
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PlacementDriveResponse>> createDrive(
            @Valid @RequestBody PlacementDriveRequest request,
            Authentication auth) {
        try {
            PlacementDriveResponse drive = driveService.createDrive(request, auth.getName());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Drive created successfully", drive));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * Update placement drive (ADMIN ONLY)
     * PUT /api/drives/{id}
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PlacementDriveResponse>> updateDrive(
            @PathVariable Integer id,
            @Valid @RequestBody PlacementDriveRequest request) {
        try {
            PlacementDriveResponse drive = driveService.updateDrive(id, request);
            return ResponseEntity.ok(ApiResponse.success("Drive updated successfully", drive));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * Get all drives (with optional filters)
     * GET /api/drives
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<PlacementDriveResponse>>> getAllDrives(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String company) {
        List<PlacementDriveResponse> drives = driveService.getAllDrives(status, company);
        return ResponseEntity.ok(ApiResponse.success("Drives retrieved", drives));
    }
    
    /**
     * Get drive by ID
     * GET /api/drives/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PlacementDriveResponse>> getDriveById(@PathVariable Integer id) {
        try {
            PlacementDriveResponse drive = driveService.getDriveById(id);
            return ResponseEntity.ok(ApiResponse.success("Drive found", drive));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Drive not found"));
        }
    }
    
    /**
     * Get drives eligible for current student (STUDENT ONLY)
     * GET /api/drives/eligible
     */
    @GetMapping("/eligible")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<List<PlacementDriveResponse>>> getEligibleDrives(
            Authentication auth) {
        try {
            List<PlacementDriveResponse> drives = driveService.getEligibleDrivesForStudent(auth.getName());
            return ResponseEntity.ok(ApiResponse.success("Eligible drives retrieved", drives));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * Check eligibility for specific drive (STUDENT ONLY)
     * GET /api/drives/{id}/check-eligibility
     */
    @GetMapping("/{id}/check-eligibility")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<EligibilityCheckResponse>> checkEligibility(
            @PathVariable Integer id,
            Authentication auth) {
        try {
            EligibilityCheckResponse response = driveService.checkEligibility(id, auth.getName());
            return ResponseEntity.ok(ApiResponse.success("Eligibility checked", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * Cancel/Close drive (ADMIN ONLY)
     * POST /api/drives/{id}/cancel
     */
    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> cancelDrive(@PathVariable Integer id) {
        try {
            driveService.cancelDrive(id);
            return ResponseEntity.ok(ApiResponse.success("Drive cancelled", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * Get drive statistics (ADMIN ONLY)
     * GET /api/drives/{id}/statistics
     */
    @GetMapping("/{id}/statistics")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<DriveStatisticsResponse>> getDriveStatistics(@PathVariable Integer id) {
        try {
            DriveStatisticsResponse stats = driveService.getDriveStatistics(id);
            return ResponseEntity.ok(ApiResponse.success("Statistics retrieved", stats));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}
