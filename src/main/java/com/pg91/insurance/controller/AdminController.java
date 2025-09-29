package com.pg91.insurance.controller;

import com.pg91.insurance.service.UserService;
import com.pg91.insurance.service.PolicyService;
import com.pg91.insurance.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private PolicyService policyService;

    @Autowired
    private UserService userService;

    // --- Policy Management ---
    @GetMapping("/policies")
    public String listPolicies(Model model) {
        model.addAttribute("policies", policyService.getAllPolicies());
        return "policies"; // maps to templates/policies.html
    }

    @PostMapping("/policies/add")
    public String addPolicy(@ModelAttribute com.pg91.insurance.entity.Policy policy) {
        policyService.addPolicy(policy);
        return "redirect:/admin/policies";
    }

    @GetMapping("/policies/delete/{id}")
    public String deletePolicy(@PathVariable Long id) {
        policyService.deletePolicy(id);
        return "redirect:/admin/policies";
    }

    @GetMapping("/policies/activate/{id}")
    public String activatePolicy(@PathVariable Long id) {
        policyService.activatePolicy(id);
        return "redirect:/admin/policies";
    }

    @GetMapping("/policies/deactivate/{id}")
    public String deactivatePolicy(@PathVariable Long id) {
        policyService.deactivatePolicy(id);
        return "redirect:/admin/policies";
    }

    // --- User Management ---
    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "users"; // maps to templates/users.html
    }

    @GetMapping("/users/activate/{id}")
    public String activateUser(@PathVariable Long id) {
        userService.activateUser(id);
        return "redirect:/admin/users";
    }

    @GetMapping("/users/deactivate/{id}")
    public String deactivateUser(@PathVariable Long id) {
        userService.deactivateUser(id);
        return "redirect:/admin/users";
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/users";
    }
}
