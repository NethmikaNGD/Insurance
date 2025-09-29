package com.pg91.service;

import com.pg91.entity.InsurancePlan;
import com.pg91.repo.InsurancePlanRepository;
import com.pg91.web.InsurancePlanForm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class InsurancePlanService {

    private final InsurancePlanRepository repository;

    @Value("${file.upload-dir:uploads}")
    private String uploadRoot;

    public InsurancePlanService(InsurancePlanRepository repository) {
        this.repository = repository;
    }

    public InsurancePlan savePlan(InsurancePlanForm form) throws IOException {
        System.out.println("Saving new plan for userId=" + form.getUserId());

        String imageWebPath = null;

        // Handle File Upload
        MultipartFile file = form.getCoverImage();
        if (file != null && !file.isEmpty()) {
            String cleanName = StringUtils.cleanPath(file.getOriginalFilename());
            String newFileName = UUID.randomUUID() + "_" + cleanName;

            Path dir = Paths.get(uploadRoot, "cover-images");
            Files.createDirectories(dir);

            Path target = dir.resolve(newFileName);
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

            imageWebPath = "/cover-images/" + newFileName;
        }

        // Map Form -> Entity
        InsurancePlan plan = new InsurancePlan();
        plan.setUserId(form.getUserId());
        plan.setTitle(form.getPlanName());
        plan.setPlanCode(form.getPlanCode());
        plan.setCategory(form.getCategory());
        plan.setDescription(form.getPlanDescription());
        plan.setCoverDetail(form.getPlanCoverDetail());
        plan.setCoverageAmount(form.getCoverageAmount());
        plan.setPriceMonth(form.getOneMonthPrice());
        plan.setPrice6m(form.getSixMonthsPrice());
        plan.setPriceYear(form.getOneYearPrice());
        plan.setCoverImageUrl(imageWebPath);
        plan.setStatus(form.getStatus());
        plan.setBenefitEr(form.getBenefitEr() != null ? form.getBenefitEr() : false);
        plan.setBenefitAmbulance(form.getBenefitAmbulance() != null ? form.getBenefitAmbulance() : false);
        plan.setBenefitDental(form.getBenefitDental() != null ? form.getBenefitDental() : false);
        plan.setBenefitVision(form.getBenefitVision() != null ? form.getBenefitVision() : false);
        plan.setBenefitTravel(form.getBenefitTravel() != null ? form.getBenefitTravel() : false);
        plan.setValidFrom(form.getValidFrom());
        plan.setValidTo(form.getValidTo());

        return repository.save(plan);
    }

    // Get all plans (for admin use)
    public List<InsurancePlan> getAllPlans() {
        return repository.findAll();
    }

    // NEW: Get only active plans (for customer-facing pages)
    public List<InsurancePlan> getActivePlans() {
        return repository.findByStatus("active");
    }

    public InsurancePlan getPlanById(Integer id) {
        Optional<InsurancePlan> plan = repository.findById(id);
        return plan.orElse(null);
    }

    // Get plan by ID only if it's active
    public InsurancePlan getActivePlanById(Integer id) {
        InsurancePlan plan = getPlanById(id);
        if (plan != null && "active".equals(plan.getStatus())) {
            return plan;
        }
        return null;
    }

    public long getTotalPlansCount() {
        return repository.countAllPlans();
    }

    public long getActivePlansCount() {
        return repository.countActivePlans();
    }

    public long getDraftPlansCount() {
        return repository.countDraftPlans();
    }

    public InsurancePlanForm mapEntityToForm(InsurancePlan p) {
        InsurancePlanForm f = new InsurancePlanForm();
        f.setUserId(p.getUserId());
        f.setPlanName(p.getTitle());
        f.setPlanCode(p.getPlanCode());
        f.setCategory(p.getCategory());
        f.setPlanDescription(p.getDescription());
        f.setPlanCoverDetail(p.getCoverDetail());
        f.setCoverageAmount(p.getCoverageAmount());
        f.setOneMonthPrice(p.getPriceMonth());
        f.setSixMonthsPrice(p.getPrice6m());
        f.setOneYearPrice(p.getPriceYear());
        f.setStatus(p.getStatus());
        f.setBenefitEr(p.getBenefitEr());
        f.setBenefitAmbulance(p.getBenefitAmbulance());
        f.setBenefitDental(p.getBenefitDental());
        f.setBenefitVision(p.getBenefitVision());
        f.setBenefitTravel(p.getBenefitTravel());
        f.setValidFrom(p.getValidFrom());
        f.setValidTo(p.getValidTo());
        return f;
    }

    public InsurancePlan updatePlan(Integer id, InsurancePlanForm form) throws IOException {
        InsurancePlan plan = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plan not found"));

        plan.setUserId(form.getUserId());
        plan.setTitle(form.getPlanName());
        plan.setPlanCode(form.getPlanCode());
        plan.setCategory(form.getCategory());
        plan.setDescription(form.getPlanDescription());
        plan.setCoverDetail(form.getPlanCoverDetail());
        plan.setCoverageAmount(form.getCoverageAmount());
        plan.setPriceMonth(form.getOneMonthPrice());
        plan.setPrice6m(form.getSixMonthsPrice());
        plan.setPriceYear(form.getOneYearPrice());
        plan.setStatus(form.getStatus());
        plan.setBenefitEr(Boolean.TRUE.equals(form.getBenefitEr()));
        plan.setBenefitAmbulance(Boolean.TRUE.equals(form.getBenefitAmbulance()));
        plan.setBenefitDental(Boolean.TRUE.equals(form.getBenefitDental()));
        plan.setBenefitVision(Boolean.TRUE.equals(form.getBenefitVision()));
        plan.setBenefitTravel(Boolean.TRUE.equals(form.getBenefitTravel()));
        plan.setValidFrom(form.getValidFrom());
        plan.setValidTo(form.getValidTo());

        // replace image only if a new file is uploaded
        MultipartFile newImage = form.getCoverImage();
        if (newImage != null && !newImage.isEmpty()) {
            String clean = StringUtils.cleanPath(newImage.getOriginalFilename());
            String newName = UUID.randomUUID() + "_" + clean;
            Path dir = Paths.get(uploadRoot, "cover-images");
            Files.createDirectories(dir);
            Files.copy(newImage.getInputStream(), dir.resolve(newName), StandardCopyOption.REPLACE_EXISTING);
            plan.setCoverImageUrl("/cover-images/" + newName);
        }

        return repository.save(plan);
    }

    public void deletePlan(Integer id) {
        repository.deleteById(id);
    }


}