// ============================================
// CONTROLLERS - REST API Endpoints
// ============================================

package com.college.placement.controller;

import com.college.placement.dto.*;
import com.college.placement.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/api/statistics")
@CrossOrigin(origins = "*")
public class StatisticsController {
    
    @Autowired
    private StatisticsService statisticsService;
    
    // GET overall placement statistics
    @GetMapping("/overall")
    public ResponseEntity<ApiResponse<PlacementStatisticsDTO>> getOverallStatistics() {
        PlacementStatisticsDTO stats = statisticsService.getOverallStatistics();
        return ResponseEntity.ok(ApiResponse.success("Overall statistics retrieved", stats));
    }
    
    // GET department-wise statistics
    @GetMapping("/department")
    public ResponseEntity<ApiResponse<List<DepartmentStatisticsDTO>>> getDepartmentStatistics() {
        List<DepartmentStatisticsDTO> stats = statisticsService.getDepartmentStatistics();
        return ResponseEntity.ok(ApiResponse.success("Department statistics retrieved", stats));
    }
    
    // GET year-wise statistics
    @GetMapping("/year")
    public ResponseEntity<ApiResponse<List<YearStatisticsDTO>>> getYearStatistics() {
        List<YearStatisticsDTO> stats = statisticsService.getYearStatistics();
        return ResponseEntity.ok(ApiResponse.success("Year statistics retrieved", stats));
    }
    
    // POST update placement
    @PostMapping("/placement")
    public ResponseEntity<ApiResponse<Void>> updatePlacement(
            @Valid @RequestBody PlacementRequest request) {
        try {
            statisticsService.updatePlacement(request);
            return ResponseEntity.ok(ApiResponse.success("Placement updated successfully", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}
