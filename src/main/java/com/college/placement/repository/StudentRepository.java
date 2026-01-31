
package com.college.placement.repository;

import com.college.placement.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface StudentRepository extends JpaRepository<Student, String> {
    
    List<Student> findByDepartment(String department);
    
    List<Student> findByBatchYear(Integer batchYear);
    
    List<Student> findByPlacementStatus(Student.PlacementStatus status);
    
    List<Student> findByDepartmentAndBatchYear(String department, Integer batchYear);
    
    @Query("SELECT COUNT(s) FROM Student s WHERE s.placementStatus = 'PLACED'")
    Long countPlacedStudents();
    
    @Query("SELECT AVG(s.packageLpa) FROM Student s WHERE s.placementStatus = 'PLACED'")
    Double getAveragePackage();
    
    @Query("SELECT MAX(s.packageLpa) FROM Student s WHERE s.placementStatus = 'PLACED'")
    Double getHighestPackage();
    
    @Query("SELECT s.department, COUNT(s), " +
           "SUM(CASE WHEN s.placementStatus = 'PLACED' THEN 1 ELSE 0 END), " +
           "AVG(CASE WHEN s.placementStatus = 'PLACED' THEN s.packageLpa ELSE NULL END) " +
           "FROM Student s GROUP BY s.department")
    List<Object[]> getDepartmentStatistics();
    
    @Query("SELECT s.batchYear, COUNT(s), " +
           "SUM(CASE WHEN s.placementStatus = 'PLACED' THEN 1 ELSE 0 END), " +
           "AVG(CASE WHEN s.placementStatus = 'PLACED' THEN s.packageLpa ELSE NULL END) " +
           "FROM Student s GROUP BY s.batchYear ORDER BY s.batchYear DESC")
    List<Object[]> getYearStatistics();
}

// ============================================
// CompanyRepository.java
// ============================================


