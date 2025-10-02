package com.pg91.insurance.controller;

import com.pg91.insurance.entity.Claim;
import com.pg91.insurance.entity.ClaimStatus;
import com.pg91.insurance.service.ClaimService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/claims")
public class ClaimController {

    @Autowired
    private ClaimService claimService;

    // Show claim submission form
    @GetMapping("/submit")
    public String showSubmitForm(Model model) {
        model.addAttribute("claim", new Claim());
        return "claim-form";
    }

    // Handle claim submission
    @PostMapping("/submit")
    public String submitClaim(@ModelAttribute Claim claim,
                              @RequestParam("documents") List<MultipartFile> documents,
                              @RequestParam("policyholderId") String policyholderId,
                              RedirectAttributes redirectAttributes) throws IOException {
        claim.setPolicyholderId(policyholderId);
        Claim savedClaim = claimService.submitClaim(claim, documents);

        // Add claim ID to redirect attributes to show on the submission detail page
        redirectAttributes.addAttribute("claimId", savedClaim.getId());
        return "redirect:/claims/submission/{claimId}";
    }

    // Show submission details
    @GetMapping("/submission/{claimId}")
    public String showSubmissionDetail(@PathVariable Long claimId, Model model) {
        Claim claim = claimService.getClaimById(claimId)
                .orElseThrow(() -> new RuntimeException("Claim not found"));
        model.addAttribute("claim", claim);
        return "submission_detail";
    }
    // Show submission details
//    @GetMapping("/submission/{claimId}")
//    public String showSubmissionDetail(@PathVariable Long claimId, Model model) {
//        Claim claim = claimService.getClaimById(claimId)
//                .orElseThrow(() -> new RuntimeException("Claim not found"));
//        model.addAttribute("claim", claim);
//        return "submission_detail";
//    }


//    @GetMapping("/submission/{claimId}")
//    public String showSubmissionDetail(@PathVariable Long claimId, Model model) {
//        Claim claim = claimService.getClaimById(claimId);
//        model.addAttribute("claim", claim);
//        return "submission_detail";
//    }

    @GetMapping("/track")
    public String trackClaims(Model model) {
        model.addAttribute("claims", new Claim());
        return "claim-list";
    }

    // Admin: Update status
    @PostMapping("/updateStatus")
    public String updateStatus(@RequestParam("id") Long id,
                               @RequestParam("status") String status) {
        claimService.updateClaimStatus(id, ClaimStatus.valueOf(status.toUpperCase()));
        return "redirect:/admin/claims";
    }

    // Modify pending claim - show modification form
    @GetMapping("/modify/{claimId}")
    public String showModifyForm(@PathVariable Long claimId, Model model) {
        Claim claim = claimService.getClaimById(claimId)
                .orElseThrow(() -> new RuntimeException("Claim not found"));
        model.addAttribute("claim", claim);
        return "modify-claim-form";
    }

//    @GetMapping("/modify/{claimId}")
//    public String showModifyForm(@PathVariable Long claimId, Model model) {
//        Claim claim = claimService.getClaimById(claimId);
//        model.addAttribute("claim", claim);
//        return "modify-claim-form";
//    }

    // Process claim modification
    @PostMapping("/modify")
    public String modifyClaim(@RequestParam("id") Long id,
                              @RequestParam("description") String description,
                              @RequestParam("documents") List<MultipartFile> documents,
                              RedirectAttributes redirectAttributes) throws IOException {
        Claim updatedClaim = claimService.modifyClaim(id, description, documents);
        redirectAttributes.addAttribute("claimId", updatedClaim.getId());
        return "redirect:/claims/submission/{claimId}";
    }

    // Withdraw pending claim
    @PostMapping("/withdraw")
    public String withdrawClaim(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        claimService.withdrawClaim(id);
        redirectAttributes.addFlashAttribute("message", "Claim has been withdrawn successfully");
        return "redirect:/claims/track";
    }

    // Delete claim
//    @PostMapping("/delete")
//    public String deleteClaim(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
//        claimService.deleteClaim(id);
//        redirectAttributes.addFlashAttribute("message", "Claim has been deleted successfully");
//        return "redirect:/claims/track";
//    }


//    @PostMapping("/delete")
//    public String deleteClaim(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
//        claimService.deleteClaim(id);
//        redirectAttributes.addFlashAttribute("message", "Claim has been deleted successfully");
//        return "redirect:/claims/track";
//    }
}



//
//package com.pg91.insurance.controller;
//
//import com.pg91.insurance.entity.Claim;
//import com.pg91.insurance.entity.ClaimStatus;
//import com.pg91.insurance.service.ClaimService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//        import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.util.List;
////localhost:8080/claims/track
//@Controller
//@RequestMapping("/claims")
//public class ClaimController {
//
//    @Autowired
//    private ClaimService claimService;
//
//    // Show claim submission form
//    @GetMapping("/submit")
//    public String showSubmitForm(Model model) {
//        model.addAttribute("claim", new Claim());
//        return "claim-form"; // Thymeleaf template: claim-form.html
//    }
//
//    // Handle claim submission
//    @PostMapping("/submit")
//    public String submitClaim(@ModelAttribute Claim claim,
//                              @RequestParam("documents") List<MultipartFile> documents,
//                              @RequestParam("policyholderId") String policyholderId) throws IOException {
//        claim.setPolicyholderId(policyholderId); // Assuming passed from session or form
//        claimService.submitClaim(claim, documents);
//        //return "redirect:/claims/track"; // Redirect to tracking page
//        return "redirect:"; // Redirect to tracking page
//
//    }
//
//    @GetMapping("/track")
//    public String trackClaims(Model model) {
//        //List<Claim> claims = claimService.getClaimsByPolicyholder(policyholderId);
//        model.addAttribute("claims", new Claim());
//        return "claim-list"; // Thymeleaf template: claim-list.html
//    }
//
//    // Track claims for a user
////    @GetMapping("/track")
////    public String trackClaims(@RequestParam("policyholderId") String policyholderId, Model model) {
////        List<Claim> claims = claimService.getClaimsByPolicyholder(policyholderId);
////        model.addAttribute("claims", claims);
////        return "claim-list"; // Thymeleaf template: claim-list.html
////    }
//
//
//
//
//    // Admin: Update status
//    @PostMapping("/updateStatus")
//    public String updateStatus(@RequestParam("id") Long id,
//                               @RequestParam("status") String status) {
//        claimService.updateClaimStatus(id, ClaimStatus.valueOf(status.toUpperCase()));
//        return "redirect:/admin/claims"; // Assuming an admin page
//    }
//
//    // Modify pending claim
//    @PostMapping("/modify")
//    public String modifyClaim(@RequestParam("id") Long id,
//                              @RequestParam("description") String description,
//                              @RequestParam("documents") List<MultipartFile> documents) throws IOException {
//        claimService.modifyClaim(id, description, documents);
//        return "redirect:/claims/track";
//    }
//
//    // Withdraw pending claim
//    @PostMapping("/withdraw")
//    public String withdrawClaim(@RequestParam("id") Long id) {
//        claimService.withdrawClaim(id);
//        return "redirect:/claims/track";
//    }
//}