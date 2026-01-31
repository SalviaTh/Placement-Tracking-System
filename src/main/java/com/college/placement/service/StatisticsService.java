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
import java.util.stream.Collectors;
@Service
public class StatisticsService {
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private CompanyRepository companyRepository;
    
    @Autowired
    private ExpectedCompanyRepository expectedCompanyRepository;
    
    @Autowired
    private PlacementRepository placementRepository;
    
    public PlacementStatisticsDTO getOverallStatistics() {
        Long totalStudents = studentRepository.count();
        Long studentsPlaced = studentRepository.countPlacedStudents();
        
        BigDecimal placementPercentage = BigDecimal.ZERO;
        if (totalStudents > 0) {
            placementPercentage = BigDecimal.valueOf(studentsPlaced)
                    .multiply(BigDecimal.valueOf(100))
                    .divide(BigDecimal.valueOf(totalStudents), 2, RoundingMode.HALF_UP);
        }
        
        Double avgPackage = studentRepository.getAveragePackage();
        Double highestPackage = studentRepository.getHighestPackage();
        
        Integer currentYear = Year.now().getValue();
        Long companiesVisited = companyRepository.countCompaniesByYear(currentYear);
        Long expectedCompanies = expectedCompanyRepository
                .countExpectedCompaniesByYear(currentYear);
        
        return PlacementStatisticsDTO.builder()
                .totalStudents(totalStudents)
                .studentsPlaced(studentsPlaced)
                .placementPercentage(placementPercentage)
                .averagePackage(avgPackage != null ? 
                        BigDecimal.valueOf(avgPackage).setScale(2, RoundingMode.HALF_UP) : null)
                .highestPackage(highestPackage != null ? 
                        BigDecimal.valueOf(highestPackage).setScale(2, RoundingMode.HALF_UP) : null)
                .companiesVisited(companiesVisited)
                .expectedCompanies(expectedCompanies)
                .build();
    }
    
    public List<DepartmentStatisticsDTO> getDepartmentStatistics() {
    List<Object[]> results = studentRepository.getDepartmentStatistics();
    
    return results.stream()
            .map(row -> {
                String department = (String) row[0];
                Long total = ((Number) row[1]).longValue();
                Long placed = ((Number) row[2]).longValue();
                
                BigDecimal percentage = BigDecimal.ZERO;
                if (total > 0) {
                    percentage = BigDecimal.valueOf(placed)
                            .multiply(BigDecimal.valueOf(100))
                            .divide(BigDecimal.valueOf(total), 2, RoundingMode.HALF_UP);
                }
                
                BigDecimal avgPkg = null;
                if (row[3] != null) {
                    avgPkg = BigDecimal.valueOf(((Number) row[3]).doubleValue())
                            .setScale(2, RoundingMode.HALF_UP);
                }
                
                return DepartmentStatisticsDTO.builder()
                        .department(department)
                        .totalStudents(total)
                        .studentsPlaced(placed)
                        .placementPercentage(percentage)
                        .averagePackage(avgPkg)
                        .build();
            })
            .collect(Collectors.toList());
}
    
    public List<YearStatisticsDTO> getYearStatistics() {
    List<Object[]> results = studentRepository.getYearStatistics();
    
    return results.stream()
            .map(row -> {
                Integer year = (Integer) row[0];
                Long total = ((Number) row[1]).longValue();
                Long placed = ((Number) row[2]).longValue();
                
                BigDecimal percentage = BigDecimal.ZERO;
                if (total > 0) {
                    percentage = BigDecimal.valueOf(placed)
                            .multiply(BigDecimal.valueOf(100))
                            .divide(BigDecimal.valueOf(total), 2, RoundingMode.HALF_UP);
                }
                
                BigDecimal avgPkg = null;
                if (row[3] != null) {
                    avgPkg = BigDecimal.valueOf(((Number) row[3]).doubleValue())
                            .setScale(2, RoundingMode.HALF_UP);
                }
                
                return YearStatisticsDTO.builder()
                        .year(year)
                        .totalStudents(total)
                        .studentsPlaced(placed)
                        .placementPercentage(percentage)
                        .averagePackage(avgPkg)
                        .build();
            })
            .collect(Collectors.toList());
}
    
    @Transactional
    public void updatePlacement(PlacementRequest request) {
        // Update student
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found"));
        
        student.setPlacementStatus(Student.PlacementStatus.PLACED);
        student.setCompanyPlaced(request.getCompanyName());
        student.setPackageLpa(request.getPackageLpa());
        studentRepository.save(student);
        
        // Get or create company
        Integer currentYear = Year.now().getValue();
        Company company = companyRepository
                .findByCompanyNameAndVisitYear(request.getCompanyName(), currentYear)
                .orElseGet(() -> {
                    Company newCompany = new Company();
                    newCompany.setCompanyName(request.getCompanyName());
                    newCompany.setVisitYear(currentYear);
                    newCompany.setStudentsPlaced(0);
                    newCompany.setPackageOffered(request.getPackageLpa());
                    return companyRepository.save(newCompany);
                });
        
        // Update company statistics
        company.setStudentsPlaced(company.getStudentsPlaced() + 1);
        if (company.getPackageOffered() == null || 
            request.getPackageLpa().compareTo(company.getPackageOffered()) > 0) {
            company.setPackageOffered(request.getPackageLpa());
        }
        companyRepository.save(company);
        
        // Create placement record
        Placement placement = new Placement();
        placement.setStudent(student);
        placement.setCompany(company);
        placement.setPlacementDate(LocalDate.now());
        placement.setPackageLpa(request.getPackageLpa());
        placement.setRole(request.getRole());
        placementRepository.save(placement);
    }
}

