package com.pg91.insurance.repository;

import com.pg91.insurance.entity.Claim;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClaimRepository extends JpaRepository<Claim, Long> {
    List<Claim> findByPolicyholderId(String policyholderId); // For tracking claims by user


}