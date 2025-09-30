package com.pg91.insurance.entity;

import jakarta.persistence.*;
<<<<<<< Updated upstream
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
=======

import java.time.LocalDate;
import java.util.Date;
>>>>>>> Stashed changes

@Entity
@Table(name = "Claims")
public class Claim {
<<<<<<< Updated upstream

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "claim_id")
    private Long claimId;

    @Column(name = "policy_id", nullable = false)
    private Long policyId;

    @Column(name = "hospital_name", length = 150)
    private String hospitalName;

    @Column(name = "treatment_date")
    private LocalDate treatmentDate;

    @Column(name = "amount_requested", precision = 12, scale = 2)
    private BigDecimal amountRequested;

    @Column(name = "status", length = 20, nullable = false)
    private String status = "pending";

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "policy_id", insertable = false, updatable = false)
    private UserPolicy userPolicy;

    // Constructors
    public Claim() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Claim(Long policyId, String hospitalName, LocalDate treatmentDate, BigDecimal amountRequested) {
        this();
        this.policyId = policyId;
        this.hospitalName = hospitalName;
        this.treatmentDate = treatmentDate;
        this.amountRequested = amountRequested;
    }

    // Lifecycle callbacks
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getClaimId() { return claimId; }
    public void setClaimId(Long claimId) { this.claimId = claimId; }

    public Long getPolicyId() { return policyId; }
    public void setPolicyId(Long policyId) { this.policyId = policyId; }

    public String getHospitalName() { return hospitalName; }
    public void setHospitalName(String hospitalName) { this.hospitalName = hospitalName; }

    public LocalDate getTreatmentDate() { return treatmentDate; }
    public void setTreatmentDate(LocalDate treatmentDate) { this.treatmentDate = treatmentDate; }

    public BigDecimal getAmountRequested() { return amountRequested; }
    public void setAmountRequested(BigDecimal amountRequested) { this.amountRequested = amountRequested; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public UserPolicy getUserPolicy() { return userPolicy; }
    public void setUserPolicy(UserPolicy userPolicy) { this.userPolicy = userPolicy; }
}
=======
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long claimId;

    @ManyToOne
    @JoinColumn(name = "policy_id")
    private UserPolicy policy;

    private String hospitalName;
    private LocalDate treatmentDate;
    private Double amountRequested;

    private String status = "pending"; // pending, approved, rejected

    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date createdAt = new java.util.Date();

    public Long getClaimId() {
        return claimId;
    }

    public void setClaimId(Long claimId) {
        this.claimId = claimId;
    }

    public UserPolicy getPolicy() {
        return policy;
    }

    public void setPolicy(UserPolicy policy) {
        this.policy = policy;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public LocalDate getTreatmentDate() {
        return treatmentDate;
    }

    public void setTreatmentDate(LocalDate treatmentDate) {
        this.treatmentDate = treatmentDate;
    }

    public Double getAmountRequested() {
        return amountRequested;
    }

    public void setAmountRequested(Double amountRequested) {
        this.amountRequested = amountRequested;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

// getters + setters
}


>>>>>>> Stashed changes
