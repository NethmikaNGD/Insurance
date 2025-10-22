package com.pg91.insurance.web;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.sql.Date;

@Setter
@Getter
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
}