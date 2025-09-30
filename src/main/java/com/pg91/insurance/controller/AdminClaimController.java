package com.pg91.insurance.controller;

import org.springframework.ui.Model;
import com.pg91.insurance.service.AdminClaimService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/claims")
public class AdminClaimController {

    @Autowired
    private AdminClaimService adminService;

    @GetMapping
    public String listPendingClaims(Model model) {
        model.addAttribute("claims", adminService.getPendingClaims());
        return "admin_claims"; // loads admin_claims.html
    }

    @PostMapping("/{claimId}/approve")
    public String approveClaim(@PathVariable Long claimId, @RequestParam Long adminId) {
        adminService.updateClaimStatus(claimId, "approved");
        return "redirect:/admin/claims";
    }

    @PostMapping("/{claimId}/reject")
    public String rejectClaim(@PathVariable Long claimId, @RequestParam Long adminId) {
        adminService.updateClaimStatus(claimId, "rejected");
        return "redirect:/admin/claims";
    }
}

