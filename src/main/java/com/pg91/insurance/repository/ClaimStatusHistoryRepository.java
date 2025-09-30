package com.pg91.insurance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.pg91.insurance.entity.ClaimStatusHistory;

public interface ClaimStatusHistoryRepository extends JpaRepository<ClaimStatusHistory, Long> {
}
