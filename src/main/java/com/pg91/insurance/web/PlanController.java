package com.pg91.insurance.web;

import com.pg91.insurance.entity.InsurancePlan;
import com.pg91.insurance.repo.InsurancePlanRepository;
import com.pg91.insurance.service.AuditLogService;
import com.pg91.insurance.service.InsurancePlanService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class PlanController {

    private final InsurancePlanService service;
    private final InsurancePlanRepository insurancePlanRepository;
    private final AuditLogService auditLogService;

    public PlanController(InsurancePlanService service, InsurancePlanRepository insurancePlanRepository, AuditLogService auditLogService) {
        this.service = service;
        this.insurancePlanRepository = insurancePlanRepository;
        this.auditLogService = auditLogService;
    }

    @PostMapping("/createPlan")
    public String createPlan(@Valid @ModelAttribute InsurancePlanForm form,
                             BindingResult result, RedirectAttributes ra,
                             HttpSession session) {
        if (result.hasErrors()) {
            ra.addFlashAttribute("error", "Please correct the form errors.");
            return "redirect:/newPlan";
        }

        try {
            // Automatically set user ID from session
            Integer userId = (Integer) session.getAttribute("CURRENT_USER_ID");
            if (userId != null) {
                form.setUserId(userId);
            }

            System.out.println("Create Plan button clicked! User ID: " + userId);
            service.savePlan(form);
            ra.addFlashAttribute("message", "Plan created successfully!");
            return "redirect:/viewPlans";
        } catch (Exception e) {
            System.err.println("Error creating plan: " + e.getMessage());
            e.printStackTrace();
            ra.addFlashAttribute("error", "Failed to create plan: " + e.getMessage());
            return "redirect:/newPlan";
        }
    }
    @GetMapping("/")
    public String root() {
        return "home";
    }

    @GetMapping("/newPlan")
    public String createPlan(Model model) {
        model.addAttribute("insurancePlanForm", new InsurancePlanForm());
        return "/adminDashboard/insurancePlanCreate";
    }

    @GetMapping("/plans")
    public String getCustomerPlans(Model model) {
        List<InsurancePlan> activePlans = service.getActivePlans();
        System.out.println("Active plans fetched for customers = " + activePlans.size());
        model.addAttribute("plans", activePlans);
        return "plans";
    }


    @GetMapping("/viewPlans")
    public String viewPlans(Model model) {
        System.out.println("View Plans button clicked!");
        List<InsurancePlan> plans = service.getAllPlans();

        long totalPlans = service.getTotalPlansCount();
        long activePlans = service.getActivePlansCount();
        long draftPlans = service.getDraftPlansCount();

        System.out.println("Plans loaded - Total: " + totalPlans + ", Active: " + activePlans + ", Draft: " + draftPlans);

        model.addAttribute("plans", plans);
        model.addAttribute("totalPlans", totalPlans);
        model.addAttribute("activePlans", activePlans);
        model.addAttribute("draftPlans", draftPlans);

        return "adminDashboard/viewPlans";
    }


    @GetMapping("/planProfile")
    public String viewPlanProfile(@RequestParam("planId") Integer planId, Model model) {
        try {
            InsurancePlan plan = service.getActivePlanById(planId);

            if (plan != null) {
                System.out.println("Active plan found: " + plan.getTitle() + " (ID: " + planId + ")");

                model.addAttribute("plan", plan);

                List<InsurancePlan> otherActivePlans = service.getActivePlans();
                otherActivePlans.removeIf(p -> p.getId().equals(planId));
                model.addAttribute("otherPlans", otherActivePlans);

                return "planProfile";
            } else {
                System.out.println("Active plan not found for ID: " + planId);
                model.addAttribute("error", "Plan not found or not active!");
                return "redirect:/plans";
            }
        } catch (Exception e) {
            System.out.println("Error fetching plan: " + e.getMessage());
            model.addAttribute("error", "Error loading plan details!");
            return "redirect:/plans";
        }
    }

    @GetMapping("/editPlan")
    public String showEditForm(@RequestParam("planId") Integer planId, Model model) {
        InsurancePlan plan = service.getPlanById(planId);
        if (plan == null) {
            return "redirect:adminDashboard/viewPlans";
        }

        InsurancePlanForm form = service.mapEntityToForm(plan);
        model.addAttribute("insurancePlanForm", form);
        model.addAttribute("planId", planId);
        return "adminDashboard/insurancePlanEdit";
    }

    @PostMapping("/updatePlan/{planId}")
    public String updatePlan(@PathVariable Integer planId,
                             @ModelAttribute InsurancePlanForm form,
                             BindingResult result,
                             RedirectAttributes ra) {
        if (result.hasErrors()) {
            ra.addFlashAttribute("error", "Please correct the errors.");
            return "redirect:/editPlan?planId=" + planId;
        }
        try {
            service.updatePlan(planId, form);
            ra.addFlashAttribute("message", "Plan updated successfully!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Update failed: " + e.getMessage());
        }
        return "redirect:/viewPlans";
    }

//    @GetMapping("/deletePlan")
//    public String showDeleteConfirm(@RequestParam("planId") Integer planId, Model model) {
//        InsurancePlan plan = service.getPlanById(planId);
//        if (plan == null) {
//            return "redirect:/viewPlans";
//        }
//        model.addAttribute("plan", plan);
//        return "adminDashboard/deleteConfirm";
//    }

    @GetMapping("/deletePlan")
    public String showDeleteConfirm(@RequestParam("planId") Integer planId, Model model) {
        InsurancePlan plan = service.getPlanById(planId);
        if (plan == null) {
            return "redirect:/viewPlans";
        }

        boolean isDeletable = true;

        model.addAttribute("plan", plan);
        model.addAttribute("canDelete", isDeletable);

        return "adminDashboard/deleteConfirm";
    }

//    @PostMapping("/confirmDelete")
//    public String confirmDelete(@RequestParam("planId") Integer planId,
//                                @RequestParam("confirmation") String confirmation,
//                                RedirectAttributes ra) {
//        if ("CONFIRM".equalsIgnoreCase(confirmation)) {
//            InsurancePlan plan = service.getPlanById(planId);
//            if (plan != null) {
//                Integer userId = 1; // userID ,
//                service.deletePlan(planId);
//                auditLogService.logAction(userId, "DELETE_PLAN", "user deleted the planid" + planId);
//                ra.addFlashAttribute("message", "Plan deleted successfully!");
//            } else {
//                ra.addFlashAttribute("error", "Plan not found!");
//            }
//        } else {
//            ra.addFlashAttribute("error", "Confirmation failed. Plan not deleted.");
//        }
//        return "redirect:viewPlans";
//    }

    @PostMapping("/confirmDelete")
    public String confirmDelete(@RequestParam("planId") Integer planId,
                                @RequestParam("confirmation") String confirmation,
                                RedirectAttributes ra,
                                HttpSession session ) {

        if ("CONFIRM".equalsIgnoreCase(confirmation)) {
            InsurancePlan plan = service.getPlanById(planId);
            if (plan != null) {
                Integer userId = (Integer) session.getAttribute("CURRENT_USER_ID");
                service.deletePlan(planId);
                auditLogService.logAction(userId, "DELETE_PLAN", "user deleted the planid" + planId);
                ra.addFlashAttribute("message", "Plan deleted successfully!");
            } else {
                ra.addFlashAttribute("error", "Plan not found!");
            }
        } else {
            ra.addFlashAttribute("error", "Confirmation failed. Plan not deleted.");
        }

        return "redirect:/viewPlans"; // Changed from "redirect:viewPlans"
    }


    @GetMapping("/dashboard")
    public String viewDashboard(Model model) {
        return  "adminDashboard/dashboard";
    }


    @GetMapping("/login")
    public String userLogin(Model model) {
        return  "login";
    }

    // Removed: POST /login hardcoded handler. Spring Security now handles authentication.
}