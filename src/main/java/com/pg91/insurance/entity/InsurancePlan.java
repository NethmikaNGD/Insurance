package com.pg91.insurance.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "Insurance_Plans")
@EntityListeners(AuditingEntityListener.class)
public class InsurancePlan {

    // Getters & Setters - UPDATED to match database
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_id")
    private Integer id;

    // Use appUserId to match database column
    @Column(name = "app_user_id")
    @NotNull
    private Integer appUserId;


    @Column(name = "title")
    @NotNull
    private String title;

    @Column(name = "plan_code")
    @NotNull
    private String planCode;

    @Column(name = "category")
    @NotNull
    private String category;

    @Column(name = "description")
    @NotNull
    private String description;

    @Column(name = "cover_detail")
    @NotNull
    private String coverDetail;

    @Column(name = "coverage_amount")
    @NotNull
    private BigDecimal coverageAmount;

    @Column(name = "valid_from")
    @NotNull
    private Date validFrom;

    @Column(name = "valid_to")
    private Date validTo;

    @Column(name = "price_month")
    @NotNull
    private BigDecimal priceMonth;

    @Column(name = "price_6m")
    private BigDecimal price6m;

    @Column(name = "price_year")
    private BigDecimal priceYear;

    @Column(name = "cover_image_url")
    private String coverImageUrl;

    @Column(name = "status")
    private String status = "active";

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

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @OneToMany(mappedBy = "insurancePlan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserPolicy> userPolicies;

    // Constructors
    public InsurancePlan() {
    }
}