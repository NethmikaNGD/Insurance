package com.pg91.insurance.dto;

import com.pg91.insurance.entity.Claim;
import com.pg91.insurance.entity.ClaimDocument;
import com.pg91.insurance.entity.AppUser;

import java.util.List;

/**
 * ClaimDTO is a Data Transfer Object that bundles together:
 * - A Claim (the claim details)
 * - An AppUser (who submitted the claim)
 * - The list of ClaimDocuments (proofs like bills, reports, etc.)
 *
 * Instead of the frontend having to fetch Claim + User + Documents separately,
 * we group them in one object and send them at once.
 */
public class ClaimDTO {

    private Claim claim;
    private AppUser user;
    private List<ClaimDocument> documents;

    // Constructor
    public ClaimDTO(Claim claim, AppUser user, List<ClaimDocument> documents) {
        this.claim = claim;
        this.user = user;
        this.documents = documents;
    }

    // Getters
    public Claim getClaim() { return claim; }
    public AppUser getUser() { return user; }
    public List<ClaimDocument> getDocuments() { return documents; }
}

