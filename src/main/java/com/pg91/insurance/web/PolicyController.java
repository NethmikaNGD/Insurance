package com.pg91.insurance.web;

import com.pg91.insurance.dto.PolicyDTO;
import com.pg91.insurance.entities.Policy;
import com.pg91.insurance.services.PolicyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/policies")
public class PolicyController {

    @Autowired
    private PolicyService policyService;

    // Display all policies
    @GetMapping
    public String viewPolicyList(Model model) {
        List<Policy> policyList = policyService.getAllPolicies();
        model.addAttribute("policyList", policyList);
        return "admin/policy-list";
    }

    // Show form to create new policy
    @GetMapping("/new")
    public String showCreatePolicyForm(Model model) {
        model.addAttribute("policyDTO", new PolicyDTO());
        return "admin/policy-form";
    }

    // Create new policy
    @PostMapping("/create")
    public String createPolicy(@Valid @ModelAttribute PolicyDTO policyDTO,
                               BindingResult result,
                               Model model,
                               RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "admin/policy-form";
        }

        try {
            Policy policy = new Policy();
            policy.setTitle(policyDTO.getTitle());
            policy.setContent(policyDTO.getContent());
            policy.setVersion(policyDTO.getVersion());
            policy.setIsActive(policyDTO.getIsActive());
            policy.setRequiredForUsers(policyDTO.getRequiredForUsers());

            policyService.createPolicy(policy);
            redirectAttributes.addFlashAttribute("successMessage", "Policy created successfully!");
            return "redirect:/admin/policies";

        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error creating policy: " + e.getMessage());
            return "admin/policy-form";
        }
    }

    // Show form to edit policy
    @GetMapping("/edit/{id}")
    public String showEditPolicyForm(@PathVariable Integer id, Model model) {
        try {
            Policy policy = policyService.getPolicyById(id)
                    .orElseThrow(() -> new RuntimeException("Policy not found"));

            PolicyDTO policyDTO = new PolicyDTO();
            policyDTO.setPolicyId(policy.getPolicyId());
            policyDTO.setTitle(policy.getTitle());
            policyDTO.setContent(policy.getContent());
            policyDTO.setVersion(policy.getVersion());
            policyDTO.setIsActive(policy.getIsActive());
            policyDTO.setRequiredForUsers(policy.getRequiredForUsers());

            model.addAttribute("policyDTO", policyDTO);
            return "admin/policy-form";

        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/admin/policies";
        }
    }

    // Update policy
    @PostMapping("/update/{id}")
    public String updatePolicy(@PathVariable Integer id,
                               @Valid @ModelAttribute PolicyDTO policyDTO,
                               BindingResult result,
                               Model model,
                               RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "admin/policy-form";
        }

        try {
            Policy policyDetails = new Policy();
            policyDetails.setTitle(policyDTO.getTitle());
            policyDetails.setContent(policyDTO.getContent());
            policyDetails.setVersion(policyDTO.getVersion());
            policyDetails.setIsActive(policyDTO.getIsActive());
            policyDetails.setRequiredForUsers(policyDTO.getRequiredForUsers());

            policyService.updatePolicy(id, policyDetails);
            redirectAttributes.addFlashAttribute("successMessage", "Policy updated successfully!");
            return "redirect:/admin/policies";

        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error updating policy: " + e.getMessage());
            return "admin/policy-form";
        }
    }

    // Delete policy
    @GetMapping("/delete/{id}")
    public String deletePolicy(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            policyService.deletePolicy(id);
            redirectAttributes.addFlashAttribute("successMessage", "Policy deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting policy: " + e.getMessage());
        }
        return "redirect:/admin/policies";
    }

    // Toggle policy status
    @GetMapping("/toggle/{id}")
    public String togglePolicyStatus(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            policyService.togglePolicyStatus(id);
            redirectAttributes.addFlashAttribute("successMessage", "Policy status updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating policy status: " + e.getMessage());
        }
        return "redirect:/admin/policies";
    }

    // View policy details
    @GetMapping("/view/{id}")
    public String viewPolicy(@PathVariable Integer id, Model model) {
        try {
            Policy policy = policyService.getPolicyById(id)
                    .orElseThrow(() -> new RuntimeException("Policy not found"));
            model.addAttribute("policy", policy);
            return "admin/policy-view";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/admin/policies";
        }
    }
}