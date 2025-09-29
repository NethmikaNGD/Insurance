package com.pg91.insurance.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "Insurance_Plans")
public class Policy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_id")
    private Long id;

    @Column(name = "app_user_id", nullable = false)
    private Long appUserId;

    @Column(name = "title", nullable = false, length = 150)
    private String title;

    @Column(name = "plan_code", nullable = false, length = 50, unique = true)
    private String planCode;

    @Column(name = "category", nullable = false, length = 50)
    private String category;

    @Column(name = "description", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String description;

    @Column(name = "cover_detail", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String coverDetail;

    @Column(name = "coverage_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal coverageAmount;

    @Column(name = "valid_from", nullable = false)
    private LocalDate validFrom;

    @Column(name = "valid_to")
    private LocalDate validTo;

    @Column(name = "price_month", nullable = false, precision = 10, scale = 2)
    private BigDecimal priceMonth;

    @Column(name = "price_6m", precision = 10, scale = 2)
    private BigDecimal price6Month;

    @Column(name = "price_year", precision = 10, scale = 2)
    private BigDecimal priceYear;

    @Column(name = "cover_image_url", length = 255)
    private String coverImageUrl;

    @Column(name = "status", length = 20, nullable = false)
    private String status = "active";

    @Column(name = "benefit_er", nullable = false)
    private Boolean benefitER = false;

    @Column(name = "benefit_ambulance", nullable = false)
    private Boolean benefitAmbulance = false;

    @Column(name = "benefit_dental", nullable = false)
    private Boolean benefitDental = false;

    @Column(name = "benefit_vision", nullable = false)
    private Boolean benefitVision = false;

    @Column(name = "benefit_travel", nullable = false)
    private Boolean benefitTravel = false;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Constructors
    public Policy() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Policy(String title, String planCode, String category, String description, BigDecimal coverageAmount, LocalDate validFrom, LocalDate validTo, String status) {
        this();
        this.title = title;
        this.planCode = planCode;
        this.category = category;
        this.description = description;
        this.coverageAmount = coverageAmount;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.status = status;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getAppUserId() { return appUserId; }
    public void setAppUserId(Long appUserId) { this.appUserId = appUserId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getPlanCode() { return planCode; }
    public void setPlanCode(String planCode) { this.planCode = planCode; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCoverDetail() { return coverDetail; }
    public void setCoverDetail(String coverDetail) { this.coverDetail = coverDetail; }

    public BigDecimal getCoverageAmount() { return coverageAmount; }
    public void setCoverageAmount(BigDecimal coverageAmount) { this.coverageAmount = coverageAmount; }

    public LocalDate getValidFrom() { return validFrom; }
    public void setValidFrom(LocalDate validFrom) { this.validFrom = validFrom; }

    public LocalDate getValidTo() { return validTo; }
    public void setValidTo(LocalDate validTo) { this.validTo = validTo; }

    public BigDecimal getPriceMonth() { return priceMonth; }
    public void setPriceMonth(BigDecimal priceMonth) { this.priceMonth = priceMonth; }

    public BigDecimal getPrice6Month() { return price6Month; }
    public void setPrice6Month(BigDecimal price6Month) { this.price6Month = price6Month; }

    public BigDecimal getPriceYear() { return priceYear; }
    public void setPriceYear(BigDecimal priceYear) { this.priceYear = priceYear; }

    public String getCoverImageUrl() { return coverImageUrl; }
    public void setCoverImageUrl(String coverImageUrl) { this.coverImageUrl = coverImageUrl; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Boolean getBenefitER() { return benefitER; }
    public void setBenefitER(Boolean benefitER) { this.benefitER = benefitER; }

    public Boolean getBenefitAmbulance() { return benefitAmbulance; }
    public void setBenefitAmbulance(Boolean benefitAmbulance) { this.benefitAmbulance = benefitAmbulance; }

    public Boolean getBenefitDental() { return benefitDental; }
    public void setBenefitDental(Boolean benefitDental) { this.benefitDental = benefitDental; }

    public Boolean getBenefitVision() { return benefitVision; }
    public void setBenefitVision(Boolean benefitVision) { this.benefitVision = benefitVision; }

    public Boolean getBenefitTravel() { return benefitTravel; }
    public void setBenefitTravel(Boolean benefitTravel) { this.benefitTravel = benefitTravel; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // Helper methods for backward compatibility
    public boolean isActive() {
        return "active".equals(this.status);
    }

    public void setActive(boolean active) {
        this.status = active ? "active" : "draft";
    }
}