
package com.college.placement.repository;

import com.college.placement.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface PlacementRepository extends JpaRepository<Placement, Integer> {
    
    List<Placement> findByStudent_StudentId(String studentId);
    
    List<Placement> findByCompany_CompanyId(Integer companyId);
    
    @Query("SELECT p FROM Placement p WHERE YEAR(p.placementDate) = :year")
    List<Placement> findByPlacementYear(@Param("year") Integer year);
}