package com.yourcompany.medicalsystem.repository;

import com.yourcompany.medicalsystem.entity.MedicalReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalReportRepository extends JpaRepository<MedicalReport, Long> {
    // add custom queries here if needed (e.g., findByPatientName)
}
