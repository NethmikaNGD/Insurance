package com.pg91.repo;

import com.pg91.entity.InsurancePlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InsurancePlanRepository extends JpaRepository<InsurancePlan, Integer> {

    // Existing methods
    List<InsurancePlan> findByStatus(String status);

    @Query("SELECT COUNT(p) FROM InsurancePlan p")
    long countAllPlans();

    @Query("SELECT COUNT(p) FROM InsurancePlan p WHERE p.status = 'active'")
    long countActivePlans();

    @Query("SELECT COUNT(p) FROM InsurancePlan p WHERE p.status = 'draft'")
    long countDraftPlans();

    // NEW: Find active plans only (for customer pages)
    @Query("SELECT p FROM InsurancePlan p WHERE p.status = 'active' " +
            "AND p.validFrom <= CURRENT_DATE " +
            "AND (p.validTo IS NULL OR p.validTo >= CURRENT_DATE)")
    List<InsurancePlan> findActivePlans();

    // NEW: Find active plan by ID
    @Query("SELECT p FROM InsurancePlan p WHERE p.id = :id " +
            "AND p.status = 'active' " +
            "AND p.validFrom <= CURRENT_DATE " +
            "AND (p.validTo IS NULL OR p.validTo >= CURRENT_DATE)")
    InsurancePlan findActivePlanById(@Param("id") Integer id);
}