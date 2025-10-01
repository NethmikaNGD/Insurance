package com.pg91.web;

import com.pg91.insurance.dto.StaffDTO;
import com.pg91.insurance.entities.AppUser;
import com.pg91.insurance.services.StaffService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/staff")
public class StaffController {

    @Autowired
    private StaffService staffService;

    // Display all staff members
    @GetMapping
    public String viewStaffList(Model model) {
        List<AppUser> staffList = staffService.getAllStaff();
        model.addAttribute("staffList", staffList);
        return "admin/staff-list";
    }

    // Show form to create new staff
    @GetMapping("/new")
    public String showCreateStaffForm(Model model) {
        model.addAttribute("staffDTO", new StaffDTO());
        return "admin/staff-form";
    }

    // Create new staff member
    @PostMapping("/create")
    public String createStaff(@Valid @ModelAttribute StaffDTO staffDTO,
                              BindingResult result,
                              Model model,
                              RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "admin/staff-form";
        }

        try {
            AppUser staff = new AppUser();
            staff.setName(staffDTO.getName());
            staff.setEmail(staffDTO.getEmail());
            staff.setPasswordHash(staffDTO.getPasswordHash()); // Will be hashed in service
            staff.setRole(staffDTO.getRole());
            staff.setStatus(staffDTO.getStatus());

            staffService.createStaff(staff);
            redirectAttributes.addFlashAttribute("successMessage", "Staff member created successfully!");
            return "redirect:/admin/staff";

        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error creating staff: " + e.getMessage());
            return "admin/staff-form";
        }
    }

    // Show form to edit staff
    @GetMapping("/edit/{id}")
    public String showEditStaffForm(@PathVariable Integer id, Model model) {
        try {
            AppUser staff = staffService.getStaffById(id)
                    .orElseThrow(() -> new RuntimeException("Staff member not found"));

            StaffDTO staffDTO = new StaffDTO();
            staffDTO.setAppUserId(staff.getAppUserId());
            staffDTO.setName(staff.getName());
            staffDTO.setEmail(staff.getEmail());
            staffDTO.setRole(staff.getRole());
            staffDTO.setStatus(staff.getStatus());

            model.addAttribute("staffDTO", staffDTO);
            return "admin/staff-form";

        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/admin/staff";
        }
    }

    // Update staff member
    @PostMapping("/update/{id}")
    public String updateStaff(@PathVariable Integer id,
                              @Valid @ModelAttribute StaffDTO staffDTO,
                              BindingResult result,
                              Model model,
                              RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "admin/staff-form";
        }

        try {
            AppUser staffDetails = new AppUser();
            staffDetails.setName(staffDTO.getName());
            staffDetails.setEmail(staffDTO.getEmail());
            staffDetails.setPasswordHash(staffDTO.getPasswordHash()); // Will handle empty in service
            staffDetails.setRole(staffDTO.getRole());
            staffDetails.setStatus(staffDTO.getStatus());

            staffService.updateStaff(id, staffDetails);
            redirectAttributes.addFlashAttribute("successMessage", "Staff member updated successfully!");
            return "redirect:/admin/staff";

        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error updating staff: " + e.getMessage());
            return "admin/staff-form";
        }
    }

    // Delete staff member
    @GetMapping("/delete/{id}")
    public String deleteStaff(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            staffService.deleteStaff(id);
            redirectAttributes.addFlashAttribute("successMessage", "Staff member deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting staff: " + e.getMessage());
        }
        return "redirect:/admin/staff";
    }
}