package com.pg91.insurance.repository;

import com.pg91.insurance.entity.Claim;
import org.springframework.data.jpa.repository.JpaRepository;
<<<<<<< Updated upstream
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
=======
>>>>>>> Stashed changes
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClaimRepository extends JpaRepository<Claim, Long> {
<<<<<<< Updated upstream

    List<Claim> findByStatus(String status);

    List<Claim> findByPolicyId(Long policyId);

    // Temporarily comment out this query until you have data to test with
    // @Query("SELECT c FROM Claim c WHERE c.userPolicy.appUserId = :userId")
    // List<Claim> findByUserId(@Param("userId") Long userId);

    @Query("SELECT c FROM Claim c WHERE c.status = :status ORDER BY c.createdAt DESC")
    List<Claim> findByStatusOrderByCreatedAtDesc(@Param("status") String status);
}
=======
    List<Claim> findByStatus(String status); // get pending claims
}
>>>>>>> Stashed changes
