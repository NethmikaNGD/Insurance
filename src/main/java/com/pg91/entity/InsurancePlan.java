package com.pg91.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDateTime;

@Entity
@Table(name = "Insurance_Plans")  // <-- FIXED: Match your exact table name
@EntityListeners(AuditingEntityListener.class)
public class InsurancePlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_id")  // <-- FIXED: Match your database primary key
    private Integer id;

    @Column(name = "app_user_id")
    @NotNull  // <-- REQUIRED by database
    private Integer appUserId;  // <-- FIXED: Match database column name

//    @ManyToOne
//    @JoinColumn(name = "app_user_id", insertable = false, updatable = false)
//    private AppUser createdBy;

    @Column(name = "title")
    @NotNull  // <-- REQUIRED by database
    private String title;

    @Column(name = "plan_code")
    @NotNull  // <-- REQUIRED by database
    private String planCode;

    @Column(name = "category")
    @NotNull  // <-- REQUIRED by database
    private String category;

    @Column(name = "description")
    @NotNull  // <-- REQUIRED by database
    private String description;

    @Column(name = "cover_detail")
    @NotNull  // <-- REQUIRED by database
    private String coverDetail;

    @Column(name = "coverage_amount")
    @NotNull  // <-- REQUIRED by database
    private BigDecimal coverageAmount;

    @Column(name = "valid_from")
    @NotNull  // <-- REQUIRED by database
    private Date validFrom;

    @Column(name = "valid_to")
    private Date validTo;  // Can be NULL

    @Column(name = "price_month")
    @NotNull  // <-- REQUIRED by database
    private BigDecimal priceMonth;

    @Column(name = "price_6m")
    private BigDecimal price6m;  // Can be NULL

    @Column(name = "price_year")
    private BigDecimal priceYear;  // Can be NULL

    @Column(name = "cover_image_url")
    private String coverImageUrl;  // Can be NULL

    @Column(name = "status")
    private String status = "active";  // Default value

    @Column(name = "benefit_er")
    private Boolean benefitEr = false;

    @Column(name = "benefit_ambulance")
    private Boolean benefitAmbulance = false;

    @Column(name = "benefit_dental")
    private Boolean benefitDental = false;

    @Column(name = "benefit_vision")
    private Boolean benefitVision = false;

    @Column(name = "benefit_travel")
    private Boolean benefitTravel = false;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructors
    public InsurancePlan() {}

    // Getters & Setters - UPDATED to match database
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    // FIXED: Use appUserId to match database column
    public Integer getAppUserId() { return appUserId; }
    public void setAppUserId(Integer appUserId) { this.appUserId = appUserId; }

    // Keep the userId method for backward compatibility with your service
    public Integer getUserId() { return appUserId; }
    public void setUserId(Integer userId) { this.appUserId = userId; }

//    public AppUser getCreatedBy() { return createdBy; }
//    public void setCreatedBy(AppUser createdBy) { this.createdBy = createdBy; }

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

    public Date getValidFrom() { return validFrom; }
    public void setValidFrom(Date validFrom) { this.validFrom = validFrom; }

    public Date getValidTo() { return validTo; }
    public void setValidTo(Date validTo) { this.validTo = validTo; }

    public BigDecimal getPriceMonth() { return priceMonth; }
    public void setPriceMonth(BigDecimal priceMonth) { this.priceMonth = priceMonth; }

    public BigDecimal getPrice6m() { return price6m; }
    public void setPrice6m(BigDecimal price6m) { this.price6m = price6m; }

    public BigDecimal getPriceYear() { return priceYear; }
    public void setPriceYear(BigDecimal priceYear) { this.priceYear = priceYear; }

    public String getCoverImageUrl() { return coverImageUrl; }
    public void setCoverImageUrl(String coverImageUrl) { this.coverImageUrl = coverImageUrl; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Boolean getBenefitEr() { return benefitEr; }
    public void setBenefitEr(Boolean benefitEr) { this.benefitEr = benefitEr; }

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
}