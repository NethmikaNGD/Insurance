package com.pg91.insurance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "Claims")
public class Claim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "claim_id")
    private Integer claimId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "policy_id", nullable = false)
    private UserPolicy userPolicy;

    @Column(name = "claim_amount")
    private BigDecimal claimAmount;

    @Column(name = "claim_date")
    private LocalDateTime claimDate;

    @Column(name = "status")
    private String status;

    public Claim() {
    }
}