package com.yourcompany.medicalsystem.controller;

import com.yourcompany.medicalsystem.entity.MedicalReport;
import com.yourcompany.medicalsystem.service.MedicalReportService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class MedicalReportController {

    private final MedicalReportService service;

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    public MedicalReportController(MedicalReportService service) {
        this.service = service;
    }

    // READ (List all reports)
    @GetMapping({"/", "/reports"})
    public String viewReports(Model model,
                              @RequestParam(value = "message", required = false) String message) {
        List<MedicalReport> list = service.findAll();
        model.addAttribute("reports", list);
        if (message != null) {
            model.addAttribute("message", message);
        }
        return "view-reports";
    }

    // CREATE (Show upload form)
    @GetMapping("/upload")
    public String uploadForm(Model model) {
        model.addAttribute("report", new MedicalReport());
        return "upload-report";
    }

    // CREATE (Handle upload)
    @PostMapping("/upload")
    public String handleUpload(@ModelAttribute MedicalReport report,
                               @RequestParam("file") MultipartFile file,
                               Model model) {
        String message = null;
        try {
            // Ensure upload directory exists
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Handle file upload if provided
            if (file != null && !file.isEmpty()) {
                String filename = StringUtils.cleanPath(file.getOriginalFilename());
                Path target = uploadPath.resolve(filename);
                Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
                report.setFileName(filename);
            } else {
                // Optional: Allow saving without a file if file is not required
                if (report.getFileName() == null) {
                    report.setFileName(""); // Default to empty if no file
                }
            }

            // Save the report
            service.save(report);
            message = "Report uploaded successfully!";
        } catch (Exception e) {
            message = "Error uploading report: " + e.getMessage();
        }

        model.addAttribute("message", message);
        model.addAttribute("report", report); // Use the submitted report to reflect saved data
        return "upload-report";
    }

    // READ (Download file)
    @GetMapping("/download/{id}")
    public ResponseEntity<?> downloadFile(@PathVariable Long id) {
        return service.findById(id)
                .map(r -> {
                    try {
                        if (r.getFileName() == null || r.getFileName().isEmpty()) {
                            return ResponseEntity.badRequest().body("No file associated with report ID: " + id);
                        }
                        Path file = Paths.get(uploadDir).resolve(r.getFileName()).normalize();
                        Resource resource = new UrlResource(file.toUri());
                        if (!resource.exists()) {
                            return ResponseEntity.notFound().build();
                        }
                        return ResponseEntity.ok()
                                .header(HttpHeaders.CONTENT_DISPOSITION,
                                        "attachment; filename=\"" + r.getFileName() + "\"")
                                .body(resource);
                    } catch (MalformedURLException e) {
                        return ResponseEntity.badRequest().body("File error for report ID: " + id);
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE (Handle deletion with POST)
    @PostMapping("/delete/{id}")
    public String deleteReport(@PathVariable Long id) {
        try {
            service.deleteById(id);
            return "redirect:/reports?message=Report+deleted+successfully";
        } catch (RuntimeException e) {
            return "redirect:/reports?message=Error+deleting+report:+" + e.getMessage().replace(" ", "+");
        }
    }

    // UPDATE (Show edit form)
    @GetMapping("/edit/{id}")
    public String editReport(@PathVariable Long id, Model model) {
        MedicalReport report = service.findById(id)
                .orElseThrow(() -> new RuntimeException("Report not found with ID: " + id));
        model.addAttribute("report", report);
        return "edit-report";
    }

    // UPDATE (Handle update with redirect on success)
    @PostMapping("/update/{id}")
    public String updateReport(@PathVariable Long id,
                               @ModelAttribute MedicalReport report,
                               @RequestParam(value = "file", required = false) MultipartFile file,
                               Model model) {
        try {
            if (file != null && !file.isEmpty()) {
                String filename = StringUtils.cleanPath(file.getOriginalFilename());
                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);
                Path target = uploadPath.resolve(filename);
                Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
                report.setFileName(filename);
            }
            service.update(id, report);
            return "redirect:/reports?message=Report+updated+successfully";
        } catch (Exception e) {
            model.addAttribute("message", "Error updating report: " + e.getMessage());
            MedicalReport updatedReport = service.findById(id)
                    .orElseThrow(() -> new RuntimeException("Report not found with ID: " + id));
            model.addAttribute("report", updatedReport);
            return "edit-report";
        }
    }

    // Success page for update (optional)
    @GetMapping("/update-success")
    public String updateSuccess(Model model) {
        model.addAttribute("message", "Report updated successfully!");
        return "update-success";
    }

    // History endpoint
    @GetMapping("/history/{patientName}")
    public String viewHistory(@PathVariable String patientName, Model model) {
        List<MedicalReport> reports = service.findByPatientName(patientName);
        model.addAttribute("reports", reports);
        model.addAttribute("patientName", patientName);
        return "history";
    }

    // Test endpoint
    @GetMapping("/test")
    public String test(Model model) {
        model.addAttribute("message", "Test message");
        model.addAttribute("serverTime", LocalDateTime.now());
        model.addAttribute("reportCount", service.findAll().size());
        model.addAttribute("reports", service.findAll());
        return "test";
    }
}