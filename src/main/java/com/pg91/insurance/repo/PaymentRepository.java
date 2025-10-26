package com.pg91.insurance.repo;

import com.pg91.insurance.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Payment entity operations.
 */
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    /**
     * Find all payments by user ID.
     *
     * @param appUserId The user ID
     * @return List of payments for the user
     */
    List<Payment> findByAppUserId(Integer appUserId);

    /**
     * Find all payments by plan ID.
     *
     * @param planId The plan ID
     * @return List of payments for the plan
     */
    List<Payment> findByPlanId(Integer planId);

    /**
     * Find payments by user ID and plan ID.
     *
     * @param appUserId The user ID
     * @param planId The plan ID
     * @return List of payments for the user and plan
     */
    List<Payment> findByAppUserIdAndPlanId(Integer appUserId, Integer planId);

    /**
     * Find payments by policy ID.
     *
     * @param policyId The policy ID
     * @return List of payments for the policy
     */
    List<Payment> findByPolicyId(Integer policyId);

    /**
     * Count total payments by user.
     *
     * @param appUserId The user ID
     * @return Total count of payments for the user
     */
    long countByAppUserId(Integer appUserId);

    /**
     * Get total amount paid by user.
     *
     * @param appUserId The user ID
     * @return Total amount paid by the user
     */
    @Query("SELECT SUM(p.amountPaid) FROM Payment p WHERE p.appUserId = :appUserId")
    java.math.BigDecimal getTotalAmountPaidByUser(@Param("appUserId") Integer appUserId);

    /**
     * Get total amount received for a plan.
     *
     * @param planId The plan ID
     * @return Total amount received for the plan
     */
    @Query("SELECT SUM(p.amountPaid) FROM Payment p WHERE p.planId = :planId")
    java.math.BigDecimal getTotalAmountReceivedForPlan(@Param("planId") Integer planId);
}