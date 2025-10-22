package com.pg91.insurance.repo;

import com.pg91.insurance.entity.UserPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserPolicyRepository extends JpaRepository<UserPolicy, Integer> {

    // Find all policies for a specific plan
    // CHANGED from findByPlanId
    List<UserPolicy> findByInsurancePlanId(Integer planId);

    // Count policies for a specific plan
    // CHANGED from countByPlanId
    long countByInsurancePlanId(Integer planId);

    // Find active policies for a specific plan
    // CHANGED from p.planId to p.insurancePlan.id
    @Query("SELECT p FROM UserPolicy p WHERE p.insurancePlan.id = :planId AND p.status = 'active'")
    List<UserPolicy> findActivePoliciesByPlanId(@Param("planId") Integer planId);

    // Count active policies for a specific plan
    // CHANGED from p.planId to p.insurancePlan.id
    @Query("SELECT COUNT(p) FROM UserPolicy p WHERE p.insurancePlan.id = :planId AND p.status = 'active'")
    long countActivePoliciesByPlanId(@Param("planId") Integer planId);
}