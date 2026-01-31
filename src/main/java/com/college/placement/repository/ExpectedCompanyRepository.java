
package com.college.placement.repository;

import com.college.placement.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ExpectedCompanyRepository extends JpaRepository<ExpectedCompany, Integer> {
    
    List<ExpectedCompany> findByExpectedYear(Integer expectedYear);
    
    List<ExpectedCompany> findByStatus(ExpectedCompany.VisitStatus status);
    
    List<ExpectedCompany> findByExpectedYearAndStatus(Integer expectedYear, 
                                                       ExpectedCompany.VisitStatus status);
    
    @Query("SELECT COUNT(e) FROM ExpectedCompany e WHERE e.expectedYear = :year " +
           "AND (e.status = com.college.placement.model.ExpectedCompany$VisitStatus.CONFIRMED OR e.status = com.college.placement.model.ExpectedCompany$VisitStatus.EXPECTED)")
    Long countExpectedCompaniesByYear(@Param("year") Integer year);
}