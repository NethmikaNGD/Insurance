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
import java.nio.file.*;
import java.util.List;

@Controller
public class MedicalReportController {

    private final MedicalReportService service;

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    public MedicalReportController(MedicalReportService service) {
        this.service = service;
    }

    // ✅ READ (List all reports)
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

    // ✅ CREATE (Show upload form)
    @GetMapping("/upload")
    public String uploadForm(Model model) {
        model.addAttribute("report", new MedicalReport());
        return "upload-report";
    }

    // ✅ CREATE (Handle upload)
    @PostMapping("/upload")
    public String handleUpload(@ModelAttribute MedicalReport report,
                               @RequestParam("file") MultipartFile file,
                               Model model) throws Exception {
        if (file != null && !file.isEmpty()) {
            String filename = StringUtils.cleanPath(file.getOriginalFilename());
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);
            Path target = uploadPath.resolve(filename);
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
            report.setFileName(filename);
        }
        service.save(report);

        // ✅ Show success message on same page
        model.addAttribute("message", "✅ Report uploaded successfully!");
        model.addAttribute("report", new MedicalReport());
        return "upload-report";
    }

    // ✅ READ (Download file)
    @GetMapping("/download/{id}")
    public ResponseEntity<?> downloadFile(@PathVariable Long id) {
        return service.findById(id)
                .map(r -> {
                    try {
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
                        return ResponseEntity.badRequest().body("File error");
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ DELETE (with success message)
    @GetMapping("/delete/{id}")
    public String deleteReport(@PathVariable Long id) {
        service.findById(id).ifPresent(r -> {
            if (r.getFileName() != null) {
                try {
                    Path file = Paths.get(uploadDir).resolve(r.getFileName()).normalize();
                    Files.deleteIfExists(file);
                } catch (Exception ignored) {}
            }
        });
        service.deleteById(id);
        return "redirect:/reports?message=✅ Report deleted successfully!";
    }

    // ✅ UPDATE (Show edit form)
    @GetMapping("/edit/{id}")
    public String editReport(@PathVariable Long id, Model model) {
        MedicalReport report = service.findById(id)
                .orElseThrow(() -> new RuntimeException("Report not found"));
        model.addAttribute("report", report);
        return "edit-report";
    }

    // ✅ UPDATE (Handle update with message)
    @PostMapping("/update/{id}")
    public String updateReport(@PathVariable Long id,
                               @ModelAttribute MedicalReport report,
                               @RequestParam(value = "file", required = false) MultipartFile file) throws Exception {
        MedicalReport existing = service.findById(id)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        existing.setPatientName(report.getPatientName());
        existing.setReportType(report.getReportType());
        existing.setDescription(report.getDescription());

        if (file != null && !file.isEmpty()) {
            String filename = StringUtils.cleanPath(file.getOriginalFilename());
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);
            Path target = uploadPath.resolve(filename);
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
            existing.setFileName(filename);
        }



        service.save(existing);
        return "redirect:/reports?message=✅ Report updated successfully!";
    }
}
