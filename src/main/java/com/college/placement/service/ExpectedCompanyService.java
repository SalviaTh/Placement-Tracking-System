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
public class ExpectedCompanyService {
    
    @Autowired
    private ExpectedCompanyRepository expectedCompanyRepository;
    
    public List<ExpectedCompany> getAllExpectedCompanies() {
        return expectedCompanyRepository.findAll();
    }
    
    public Optional<ExpectedCompany> getExpectedCompanyById(Integer id) {
        return expectedCompanyRepository.findById(id);
    }
    
    public List<ExpectedCompany> getExpectedCompaniesByYear(Integer year) {
        return expectedCompanyRepository.findByExpectedYear(year);
    }
    
    public List<ExpectedCompany> getExpectedCompaniesByStatus(ExpectedCompany.VisitStatus status) {
        return expectedCompanyRepository.findByStatus(status);
    }
    
    public ExpectedCompany createExpectedCompany(ExpectedCompany expectedCompany) {
        return expectedCompanyRepository.save(expectedCompany);
    }
    
    public ExpectedCompany updateExpectedCompany(Integer id, ExpectedCompany companyDetails) {
        ExpectedCompany company = expectedCompanyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expected company not found"));
        
        company.setCompanyName(companyDetails.getCompanyName());
        company.setExpectedYear(companyDetails.getExpectedYear());
        company.setStatus(companyDetails.getStatus());
        company.setNotes(companyDetails.getNotes());
        
        return expectedCompanyRepository.save(company);
    }
    
    public void deleteExpectedCompany(Integer id) {
        expectedCompanyRepository.deleteById(id);
    }
}
