package com.pg91.web;

import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.sql.Date;

public class InsurancePlanForm {

    private Integer userId;
    private String planName;
    private String planCode;
    private String category;
    private String planDescription;
    private String planCoverDetail;
    private BigDecimal coverageAmount;
    private BigDecimal oneMonthPrice;
    private BigDecimal sixMonthsPrice;
    private BigDecimal oneYearPrice;
    private MultipartFile coverImage;
    private String status;
    private Boolean benefitEr;
    private Boolean benefitAmbulance;
    private Boolean benefitDental;
    private Boolean benefitVision;
    private Boolean benefitTravel;
    private Date validFrom;
    private Date validTo;

    // Getters & Setters
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getPlanName() { return planName; }
    public void setPlanName(String planName) { this.planName = planName; }

    public String getPlanCode() { return planCode; }
    public void setPlanCode(String planCode) { this.planCode = planCode; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getPlanDescription() { return planDescription; }
    public void setPlanDescription(String planDescription) { this.planDescription = planDescription; }

    public String getPlanCoverDetail() { return planCoverDetail; }
    public void setPlanCoverDetail(String planCoverDetail) { this.planCoverDetail = planCoverDetail; }

    public BigDecimal getCoverageAmount() { return coverageAmount; }
    public void setCoverageAmount(BigDecimal coverageAmount) { this.coverageAmount = coverageAmount; }

    public BigDecimal getOneMonthPrice() { return oneMonthPrice; }
    public void setOneMonthPrice(BigDecimal oneMonthPrice) { this.oneMonthPrice = oneMonthPrice; }

    public BigDecimal getSixMonthsPrice() { return sixMonthsPrice; }
    public void setSixMonthsPrice(BigDecimal sixMonthsPrice) { this.sixMonthsPrice = sixMonthsPrice; }

    public BigDecimal getOneYearPrice() { return oneYearPrice; }
    public void setOneYearPrice(BigDecimal oneYearPrice) { this.oneYearPrice = oneYearPrice; }

    public MultipartFile getCoverImage() { return coverImage; }
    public void setCoverImage(MultipartFile coverImage) { this.coverImage = coverImage; }

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

    public Date getValidFrom() { return validFrom; }
    public void setValidFrom(Date validFrom) { this.validFrom = validFrom; }

    public Date getValidTo() { return validTo; }
    public void setValidTo(Date validTo) { this.validTo = validTo; }
}