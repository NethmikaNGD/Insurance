package com.pg91.insurance.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "User_Policies")
public class UserPolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "policy_id")
    private Long policyId;

    @Column(name = "app_user_id", nullable = false)
    private Long appUserId;

    @Column(name = "plan_id", nullable = false)
    private Long planId;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "status", length = 20, nullable = false)
    private String status = "active";

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_user_id", insertable = false, updatable = false)
    private User appUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", insertable = false, updatable = false)
    private Policy insurancePlan;

    // Constructors
    public UserPolicy() {}

    public UserPolicy(Long appUserId, Long planId, LocalDate startDate, LocalDate endDate, String status) {
        this.appUserId = appUserId;
        this.planId = planId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    // Getters and Setters
    public Long getPolicyId() { return policyId; }
    public void setPolicyId(Long policyId) { this.policyId = policyId; }

    public Long getAppUserId() { return appUserId; }
    public void setAppUserId(Long appUserId) { this.appUserId = appUserId; }

    public Long getPlanId() { return planId; }
    public void setPlanId(Long planId) { this.planId = planId; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public User getAppUser() { return appUser; }
    public void setAppUser(User appUser) { this.appUser = appUser; }

    public Policy getInsurancePlan() { return insurancePlan; }
    public void setInsurancePlan(Policy insurancePlan) { this.insurancePlan = insurancePlan; }
}