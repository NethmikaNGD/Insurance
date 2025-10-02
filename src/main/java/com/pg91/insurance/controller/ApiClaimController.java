package com.pg91.insurance.controller;

import com.pg91.insurance.dto.ClaimDTO;
import com.pg91.insurance.entity.Claim;
import com.pg91.insurance.service.ClaimService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/claims")
public class ApiClaimController {

    @Autowired
    private ClaimService claimService;

    @PostMapping
    public ResponseEntity<Claim> create(@Valid @RequestBody ClaimDTO claimDTO) {
        Claim saved = claimService.createClaim(claimDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Claim> update(@PathVariable Long id, @Valid @RequestBody ClaimDTO claimDTO) {
        Claim updated = claimService.updateClaim(id, claimDTO);
        return ResponseEntity.ok(updated);
    }
}


