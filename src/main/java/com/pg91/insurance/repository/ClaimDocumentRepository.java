package com.pg91.insurance.repository;

import com.pg91.insurance.entity.Claim;
import com.pg91.insurance.entity.ClaimDocument;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClaimDocumentRepository extends JpaRepository<ClaimDocument, Long> {
    List<ClaimDocument> findByClaim(Claim claim);
}
