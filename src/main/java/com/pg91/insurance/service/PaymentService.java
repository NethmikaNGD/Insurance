package com.pg91.insurance.service;

import com.pg91.insurance.entity.Payment;
import com.pg91.insurance.entity.InsurancePlan;
import com.pg91.insurance.entity.UserPolicy;
import com.pg91.insurance.repo.PaymentRepository;
import com.pg91.insurance.repo.InsurancePlanRepository;
import com.pg91.insurance.repo.UserPolicyRepository;
import com.pg91.insurance.strategy.PaymentContext;
import com.pg91.insurance.strategy.PaymentMethodType;
import com.pg91.insurance.strategy.PaymentResult;
import com.pg91.insurance.strategy.PaymentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentContext paymentContext;
    private final InsurancePlanRepository insurancePlanRepository;
    private final UserPolicyRepository userPolicyRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository,
                          PaymentContext paymentContext,
                          InsurancePlanRepository insurancePlanRepository,
                          UserPolicyRepository userPolicyRepository) {
        this.paymentRepository = paymentRepository;
        this.paymentContext = paymentContext;
        this.insurancePlanRepository = insurancePlanRepository;
        this.userPolicyRepository = userPolicyRepository;
    }

    public PaymentResult processPayment(Payment payment, PaymentMethodType paymentMethod) throws PaymentException {
        // Validate payment details
        if (!paymentContext.validatePayment(payment, paymentMethod)) {
            throw new PaymentException("Invalid payment details for " + paymentMethod.getDisplayName());
        }

        // Validate plan exists and is active
        Optional<InsurancePlan> planOpt = insurancePlanRepository.findById(payment.getPlanId());
        if (planOpt.isEmpty()) {
            throw new PaymentException("Insurance plan not found");
        }

        InsurancePlan plan = planOpt.get();
        if (!"active".equalsIgnoreCase(plan.getStatus())) {
            throw new PaymentException("Insurance plan is not active");
        }

        PaymentResult result = paymentContext.processPayment(payment, paymentMethod);

        if (result.isSuccess()) {
            // Let auditing handle dates
            paymentRepository.save(payment);

            // Create user policy if payment is successful (transactional—rolls back on failure)
            createUserPolicy(payment);
        }

        return result;
    }

    private void createUserPolicy(Payment payment) {
        UserPolicy userPolicy = new UserPolicy();
        userPolicy.setAppUserId(payment.getAppUserId());
        userPolicy.setStartDate(Date.valueOf(LocalDate.now()));

        // Set end date based on plan validity
        Optional<InsurancePlan> planOpt = insurancePlanRepository.findById(payment.getPlanId());
        if (planOpt.isPresent()) {
            InsurancePlan plan = planOpt.get();
            userPolicy.setInsurancePlan(plan);

            if (plan.getValidTo() != null) {
                userPolicy.setEndDate(plan.getValidTo());
            } else {
                // Default to 1 year from start date
                userPolicy.setEndDate(Date.valueOf(LocalDate.now().plusYears(1)));
            }
        }

        userPolicy.setStatus("active");

        UserPolicy savedPolicy = userPolicyRepository.save(userPolicy);

        payment.setPolicyId(savedPolicy.getPolicyId());
        paymentRepository.save(payment);

        System.out.println("User policy created for user: " + payment.getAppUserId() +
                ", plan: " + payment.getPlanId() + ", policy ID: " + savedPolicy.getPolicyId());
    }


    public List<Payment> getPaymentsByUserId(Integer appUserId) {
        return paymentRepository.findByAppUserId(appUserId);
    }

    public List<Payment> getPaymentsByPlanId(Integer planId) {
        return paymentRepository.findByPlanId(planId);
    }
    public Optional<Payment> getPaymentById(Integer payId) {
        return paymentRepository.findById(payId);
    }


    public BigDecimal getTotalAmountPaidByUser(Integer appUserId) {
        BigDecimal total = paymentRepository.getTotalAmountPaidByUser(appUserId);
        return total != null ? total : BigDecimal.ZERO;
    }


    public BigDecimal getTotalAmountReceivedForPlan(Integer planId) {
        BigDecimal total = paymentRepository.getTotalAmountReceivedForPlan(planId);
        return total != null ? total : BigDecimal.ZERO;
    }

    public PaymentMethodType[] getAvailablePaymentMethods() {
        return paymentContext.getAvailablePaymentMethods();
    }

    public boolean isPaymentMethodSupported(PaymentMethodType paymentMethod) {
        return paymentContext.isPaymentMethodSupported(paymentMethod);
    }
}