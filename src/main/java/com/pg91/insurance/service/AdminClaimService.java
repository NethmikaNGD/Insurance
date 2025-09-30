package com.pg91.insurance.service;

import com.pg91.insurance.dto.ClaimDTO;
import com.pg91.insurance.entity.AppUser;
import com.pg91.insurance.entity.Claim;
import com.pg91.insurance.entity.ClaimDocument;
import com.pg91.insurance.repository.ClaimDocumentRepository;
import com.pg91.insurance.repository.ClaimRepository;
import com.pg91.insurance.repository.ClaimStatusHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminClaimService {

    @Autowired
    private ClaimRepository claimRepo;

    @Autowired private ClaimDocumentRepository docRepo;

    public List<Claim> getPendingClaims() {
        return claimRepo.findByStatus("pending");
    }

    public ClaimDTO getClaimDetails(Long claimId) {
        Claim claim = claimRepo.findById(claimId).orElseThrow();
        AppUser user = claim.getPolicy().getAppUser();
        List<ClaimDocument> docs = docRepo.findByClaim(claim);
        return new ClaimDTO(claim, user, docs);
    }

    public void updateClaimStatus(Long claimId, String newStatus) {
        Claim claim = claimRepo.findById(claimId).orElseThrow();
        claim.setStatus(newStatus);
        claimRepo.save(claim);
    }
}

