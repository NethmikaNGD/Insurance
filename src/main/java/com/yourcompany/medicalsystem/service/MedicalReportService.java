package com.yourcompany.medicalsystem.service;

import com.yourcompany.medicalsystem.entity.MedicalReport;
import com.yourcompany.medicalsystem.repository.MedicalReportRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicalReportService {

    private final MedicalReportRepository repo;

    public MedicalReportService(MedicalReportRepository repo) {
        this.repo = repo;
    }

    public MedicalReport save(MedicalReport report) {
        return repo.save(report);
    }

    public List<MedicalReport> findAll() {
        return repo.findAll();
    }

    public Optional<MedicalReport> findById(Long id) {
        return repo.findById(id);
    }

    public MedicalReport update(Long id, MedicalReport updated) {
        return repo.findById(id).map(existing -> {
            existing.setPatientName(updated.getPatientName());
            existing.setReportType(updated.getReportType());
            existing.setDescription(updated.getDescription());
            if (updated.getFileName() != null) existing.setFileName(updated.getFileName());
            return repo.save(existing);
        }).orElseThrow(() -> new RuntimeException("Report not found: " + id));
    }



    public void deleteById(Long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
        } else {
            throw new RuntimeException("Report not found: " + id);
        }
    }
}
