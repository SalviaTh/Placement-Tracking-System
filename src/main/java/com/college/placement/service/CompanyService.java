package com.college.placement.service;

import com.college.placement.model.*;
import com.college.placement.dto.*;
import com.college.placement.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Year;
import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {
    
    @Autowired
    private CompanyRepository companyRepository;
    
    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }
    
    public Optional<Company> getCompanyById(Integer companyId) {
        return companyRepository.findById(companyId);
    }
    
    public List<Company> getCompaniesByYear(Integer year) {
        return companyRepository.findByVisitYear(year);
    }
    
    public Company createCompany(Company company) {
        return companyRepository.save(company);
    }
    
    public Company updateCompany(Integer companyId, Company companyDetails) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));
        
        company.setCompanyName(companyDetails.getCompanyName());
        company.setVisitYear(companyDetails.getVisitYear());
        company.setStudentsPlaced(companyDetails.getStudentsPlaced());
        company.setPackageOffered(companyDetails.getPackageOffered());
        
        return companyRepository.save(company);
    }
    
    public void deleteCompany(Integer companyId) {
        companyRepository.deleteById(companyId);
    }
}
