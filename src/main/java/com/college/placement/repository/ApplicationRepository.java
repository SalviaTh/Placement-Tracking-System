package com.college.placement.repository;

import com.college.placement.model.Application;
import com.college.placement.model.PlacementDrive;
import com.college.placement.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Integer> {
    List<Application> findByStudent(Student student);
    List<Application> findByDrive(PlacementDrive drive);
    Optional<Application> findByDriveAndStudent(PlacementDrive drive, Student student);
    List<Application> findByStatus(Application.ApplicationStatus status);
}