package com.pg91.insurance.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents a payment record in the application, mapping to the 'payment_table' table in the database.
 * This entity handles payment transactions for insurance plans.
 */
@Setter
@Getter
@Entity
@Table(name = "payment_table")
@EntityListeners(AuditingEntityListener.class)
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pay_id")
    private Integer payId;

    @Column(name = "app_user_id", nullable = false)
    @NotNull(message = "User ID is required")
    private Integer appUserId;

    @Column(name = "plan_id", nullable = false)
    @NotNull(message = "Plan ID is required")
    private Integer planId;

    @Column(name = "policy_id")
    private Integer policyId;

    @Column(name = "card_holder_name", nullable = false, length = 100)
    @NotBlank(message = "Card holder name is required")
    @Size(max = 100, message = "Card holder name must not exceed 100 characters")
    private String cardHolderName;

    @Column(name = "card_no", nullable = false, length = 20)
    @NotBlank(message = "Card number is required")
    @Size(max = 20, message = "Card number must not exceed 20 characters")
    private String cardNo;

    @Column(name = "valid_date", nullable = false)
    @NotNull(message = "Valid date is required")
    private java.sql.Date validDate;

    @Column(name = "security_code", nullable = false)
    @NotNull(message = "Security code is required")
    @Min(value = 0, message = "Security code must be between 0 and 9999")
    @Max(value = 9999, message = "Security code must be between 0 and 9999")
    private Short securityCode;

    @Column(name = "amount_paid", nullable = false, precision = 10, scale = 2)
    @NotNull(message = "Amount paid is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amountPaid;

    @Column(name = "payment_date")
    @CreatedDate
    private LocalDateTime paymentDate;

    @Column(name = "updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_user_id", insertable = false, updatable = false)
    private AppUser appUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", insertable = false, updatable = false)
    private InsurancePlan insurancePlan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "policy_id", insertable = false, updatable = false)
    private UserPolicy userPolicy;

    // Constructors
    public Payment() {
    }

    public Payment(Integer appUserId, Integer planId, String cardHolderName,
                   String cardNo, java.sql.Date validDate, Short securityCode,
                   BigDecimal amountPaid) {
        this.appUserId = appUserId;
        this.planId = planId;
        this.cardHolderName = cardHolderName;
        this.cardNo = cardNo;
        this.validDate = validDate;
        this.securityCode = securityCode;
        this.amountPaid = amountPaid;
    }

    @PrePersist
    protected void onCreate() {
        if (paymentDate == null) {
            paymentDate = LocalDateTime.now();
        }
        if (updatedAt == null) {
            updatedAt = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}