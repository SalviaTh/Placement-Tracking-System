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
@RequestMapping("/api/companies")
@CrossOrigin(origins = "*")
public class CompanyController {
    
    @Autowired
    private CompanyService companyService;
    
    // GET all companies
    @GetMapping
    public ResponseEntity<ApiResponse<List<Company>>> getAllCompanies() {
        List<Company> companies = companyService.getAllCompanies();
        return ResponseEntity.ok(ApiResponse.success("Companies retrieved", companies));
    }
    
    // GET company by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Company>> getCompanyById(@PathVariable Integer id) {
        return companyService.getCompanyById(id)
                .map(company -> ResponseEntity.ok(
                        ApiResponse.success("Company found", company)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Company not found")));
    }
    
    // GET companies by year
    @GetMapping("/year/{year}")
    public ResponseEntity<ApiResponse<List<Company>>> getCompaniesByYear(
            @PathVariable Integer year) {
        List<Company> companies = companyService.getCompaniesByYear(year);
        return ResponseEntity.ok(ApiResponse.success("Companies retrieved", companies));
    }
    
    // POST create company
    @PostMapping
    public ResponseEntity<ApiResponse<Company>> createCompany(
            @Valid @RequestBody Company company) {
        Company createdCompany = companyService.createCompany(company);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Company created successfully", createdCompany));
    }
    
    // PUT update company
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Company>> updateCompany(
            @PathVariable Integer id, @Valid @RequestBody Company company) {
        try {
            Company updatedCompany = companyService.updateCompany(id, company);
            return ResponseEntity.ok(
                    ApiResponse.success("Company updated successfully", updatedCompany));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    // DELETE company
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCompany(@PathVariable Integer id) {
        companyService.deleteCompany(id);
        return ResponseEntity.ok(ApiResponse.success("Company deleted successfully", null));
    }
}
