
package com.pg91.insurance.service;

import com.pg91.insurance.entity.Claim;
import com.pg91.insurance.entity.ClaimStatus;
import com.pg91.insurance.repository.ClaimRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ClaimService {

    @Autowired
    private ClaimRepository claimRepository;

    private final String UPLOAD_DIR = "uploads/";

    public Claim submitClaim(Claim claim, List<MultipartFile> documents) throws IOException {
        claim.setSubmissionDate(LocalDateTime.now());
        claim.setStatus(ClaimStatus.PENDING);

        List<String> documentPaths = new ArrayList<>();
        for (MultipartFile file : documents) {
            if (!file.isEmpty()) {
                String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                File dest = new File(UPLOAD_DIR + fileName);
                file.transferTo(dest);
                documentPaths.add(fileName);
            }
        }
        claim.setDocumentPaths(documentPaths);

        return claimRepository.save(claim);
    }

    public List<Claim> getClaimsByPolicyholder(String policyholderId) {
        return claimRepository.findByPolicyholderId(policyholderId);
    }

    public Optional<Claim> getClaimById(Long id) {
        return claimRepository.findById(id);
    }

    // Admin function to update status
    public Claim updateClaimStatus(Long id, ClaimStatus newStatus) {
        Optional<Claim> optionalClaim = claimRepository.findById(id);
        if (optionalClaim.isPresent()) {
            Claim claim = optionalClaim.get();
            claim.setStatus(newStatus);
            return claimRepository.save(claim);
        }
        throw new RuntimeException("Claim not found");
    }

    // For modification (only if pending)
    public Claim modifyClaim(Long id, String newDescription, List<MultipartFile> newDocuments) throws IOException {
        Optional<Claim> optionalClaim = claimRepository.findById(id);
        if (optionalClaim.isPresent()) {
            Claim claim = optionalClaim.get();
            if (claim.getStatus() != ClaimStatus.PENDING) {
                throw new RuntimeException("Can only modify pending claims");
            }
            claim.setDescription(newDescription);

            if (newDocuments != null && !newDocuments.isEmpty()) {
                List<String> documentPaths = claim.getDocumentPaths() != null ? claim.getDocumentPaths() : new ArrayList<>();
                for (MultipartFile file : newDocuments) {
                    if (!file.isEmpty()) {
                        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                        File dest = new File(UPLOAD_DIR + fileName);
                        file.transferTo(dest);
                        documentPaths.add(fileName);
                    }
                }
                claim.setDocumentPaths(documentPaths);
            }

            return claimRepository.save(claim);
        }
        throw new RuntimeException("Claim not found");
    }

    // For withdrawal (only if pending)
    public void withdrawClaim(Long id) {
        Optional<Claim> optionalClaim = claimRepository.findById(id);
        if (optionalClaim.isPresent()) {
            Claim claim = optionalClaim.get();
            if (claim.getStatus() != ClaimStatus.PENDING) {
                throw new RuntimeException("Can only withdraw pending claims");
            }
            claimRepository.delete(claim);
        } else {
            throw new RuntimeException("Claim not found");
        }
    }
}