package com.pg91.insurance.web;

import com.pg91.insurance.entity.AppUser;
import com.pg91.insurance.entity.Role;
import com.pg91.insurance.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Show registration form
     */
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new AppUser());
        return "register";
    }

    /**
     * Process registration form
     */
    @PostMapping("/register")
    public String processRegistration(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            @RequestParam(defaultValue = "CUSTOMER") String role,
            RedirectAttributes redirectAttributes) {

        try {
            // Validate input
            if (name == null || name.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Name is required");
                return "redirect:/user/register";
            }

            if (email == null || email.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Email is required");
                return "redirect:/user/register";
            }

            if (password == null || password.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Password is required");
                return "redirect:/user/register";
            }

            if (!password.equals(confirmPassword)) {
                redirectAttributes.addFlashAttribute("error", "Passwords do not match");
                return "redirect:/user/register";
            }

            if (password.length() < 4) {
                redirectAttributes.addFlashAttribute("error", "Password must be at least 4 characters long");
                return "redirect:/user/register";
            }

            // Check if email already exists
            if (userService.emailExists(email)) {
                redirectAttributes.addFlashAttribute("error", "Email already registered");
                return "redirect:/user/register";
            }

            // Parse role
            Role userRole;
            try {
                userRole = Role.valueOf(role.toUpperCase());
            } catch (IllegalArgumentException e) {
                userRole = Role.CUSTOMER; // Default to customer
            }

            // Register user
            AppUser newUser = userService.registerUser(name, email, password, userRole);
            
            redirectAttributes.addFlashAttribute("success", 
                "Registration successful! Welcome " + newUser.getName() + ". You can now login.");
            
            return "redirect:/login";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Registration failed: " + e.getMessage());
            return "redirect:/user/register";
        }
    }

    /**
     * Show login form
     */
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        return "login";
    }

    /**
     * Show user profile
     */
    @GetMapping("/profile")
    public String showProfile(Model model) {
        // This would typically get the current user from security context
        // For now, we'll just show the profile page
        return "profile";
    }

    /**
     * API endpoint to check if email exists (for AJAX validation)
     */
    @GetMapping("/check-email")
    @ResponseBody
    public boolean checkEmail(@RequestParam String email) {
        return userService.emailExists(email);
    }

    /**
     * API endpoint to get user by email (for testing)
     */
    @GetMapping("/get-by-email")
    @ResponseBody
    public AppUser getUserByEmail(@RequestParam String email) {
        Optional<AppUser> user = userService.findByEmail(email);
        return user.orElse(null);
    }
}
