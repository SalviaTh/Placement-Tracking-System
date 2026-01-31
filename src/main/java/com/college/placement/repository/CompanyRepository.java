
package com.college.placement.repository;

import com.college.placement.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {
    
    List<Company> findByVisitYear(Integer visitYear);
    
    Optional<Company> findByCompanyNameAndVisitYear(String companyName, Integer visitYear);
    
    @Query("SELECT COUNT(DISTINCT c.companyName) FROM Company c WHERE c.visitYear = :year")
    Long countCompaniesByYear(@Param("year") Integer year);
    
    @Query("SELECT c FROM Company c WHERE c.visitYear = :year ORDER BY c.studentsPlaced DESC")
    List<Company> getTopCompaniesByYear(@Param("year") Integer year);
}