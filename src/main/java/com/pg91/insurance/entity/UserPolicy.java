package com.pg91.insurance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "User_Policies")
public class UserPolicy {

    // Getters & Setters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "policy_id")
    private Integer policyId;

    @Column(name = "app_user_id")
    private Integer appUserId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    private InsurancePlan insurancePlan;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "status")
    private String status = "active";

    @OneToMany(mappedBy = "userPolicy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Claim> claims;

    // Constructors
    public UserPolicy() {
    }
}