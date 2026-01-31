package com.college.placement.controller;

import com.college.placement.model.*;
import com.college.placement.dto.*;
import com.college.placement.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/expected-companies")
@CrossOrigin(origins = "*")
public class ExpectedCompanyController {
    
    @Autowired
    private ExpectedCompanyService expectedCompanyService;
    
    // GET all expected companies
    @GetMapping
    public ResponseEntity<ApiResponse<List<ExpectedCompany>>> getAllExpectedCompanies() {
        try {
            List<ExpectedCompany> companies = expectedCompanyService.getAllExpectedCompanies();
            return ResponseEntity.ok(ApiResponse.success("Expected companies retrieved", companies));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Debug Error: " + e.getMessage()));
        }
    }
    
    // GET expected company by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ExpectedCompany>> getExpectedCompanyById(
            @PathVariable Integer id) {
        return expectedCompanyService.getExpectedCompanyById(id)
                .map(company -> ResponseEntity.ok(
                        ApiResponse.success("Expected company found", company)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Expected company not found")));
    }
    
    // GET expected companies by year
    @GetMapping("/year/{year}")
    public ResponseEntity<ApiResponse<List<ExpectedCompany>>> getExpectedCompaniesByYear(
            @PathVariable Integer year) {
        List<ExpectedCompany> companies = expectedCompanyService.getExpectedCompaniesByYear(year);
        return ResponseEntity.ok(ApiResponse.success("Expected companies retrieved", companies));
    }
    
    // POST create expected company
    @PostMapping
    public ResponseEntity<ApiResponse<ExpectedCompany>> createExpectedCompany(
            @Valid @RequestBody ExpectedCompany expectedCompany) {
        ExpectedCompany created = expectedCompanyService.createExpectedCompany(expectedCompany);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Expected company created", created));
    }
    
    // PUT update expected company
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ExpectedCompany>> updateExpectedCompany(
            @PathVariable Integer id, @Valid @RequestBody ExpectedCompany company) {
        try {
            ExpectedCompany updated = expectedCompanyService.updateExpectedCompany(id, company);
            return ResponseEntity.ok(
                    ApiResponse.success("Expected company updated", updated));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    // DELETE expected company
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteExpectedCompany(@PathVariable Integer id) {
        expectedCompanyService.deleteExpectedCompany(id);
        return ResponseEntity.ok(ApiResponse.success("Expected company deleted", null));
    }
}
