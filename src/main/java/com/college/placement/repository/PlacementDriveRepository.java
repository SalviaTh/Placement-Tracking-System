package com.college.placement.repository;

import com.college.placement.model.PlacementDrive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PlacementDriveRepository extends JpaRepository<PlacementDrive, Integer> {
    List<PlacementDrive> findByStatus(PlacementDrive.DriveStatus status);
    List<PlacementDrive> findByCompanyNameContaining(String companyName);
    List<PlacementDrive> findByStatusAndCompanyNameContaining(
        PlacementDrive.DriveStatus status, String companyName);
    List<PlacementDrive> findByApplicationDeadlineBetween(
        LocalDateTime start, LocalDateTime end);
}