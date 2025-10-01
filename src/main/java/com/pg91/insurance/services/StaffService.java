package com.pg91.insurance.services;

import com.pg91.insurance.entities.AppUser;
import com.pg91.insurance.repositories.AppUserRepository;
import com.pg91.insurance.util.SimplePasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StaffService {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private SimplePasswordEncoder passwordEncoder;

    // Get all staff members (admin and doctor)
    public List<AppUser> getAllStaff() {
        return appUserRepository.findAllStaff();
    }

    // Get staff by ID
    public Optional<AppUser> getStaffById(Integer id) {
        return appUserRepository.findById(id);
    }

    // Create new staff member
    public AppUser createStaff(AppUser staff) {
        // Check if email already exists
        if (appUserRepository.existsByEmail(staff.getEmail())) {
            throw new RuntimeException("Email already exists: " + staff.getEmail());
        }

        // Validate role
        if (!isValidStaffRole(staff.getRole())) {
            throw new RuntimeException("Invalid role for staff: " + staff.getRole());
        }

        // Hash password before saving (using simple encoder)
        if (staff.getPasswordHash() != null && !staff.getPasswordHash().isEmpty()) {
            staff.setPasswordHash(passwordEncoder.encode(staff.getPasswordHash()));
        } else {
            staff.setPasswordHash(passwordEncoder.encode("default123")); // Default password
        }

        return appUserRepository.save(staff);
    }

    // Update staff member
    public AppUser updateStaff(Integer id, AppUser staffDetails) {
        Optional<AppUser> optionalStaff = appUserRepository.findById(id);

        if (optionalStaff.isPresent()) {
            AppUser staff = optionalStaff.get();

            // Update fields
            staff.setName(staffDetails.getName());
            staff.setEmail(staffDetails.getEmail());
            staff.setRole(staffDetails.getRole());
            staff.setStatus(staffDetails.getStatus());

            // Only update password if provided
            if (staffDetails.getPasswordHash() != null && !staffDetails.getPasswordHash().isEmpty()) {
                staff.setPasswordHash(passwordEncoder.encode(staffDetails.getPasswordHash()));
            }

            return appUserRepository.save(staff);
        } else {
            throw new RuntimeException("Staff member not found with id: " + id);
        }
    }

    // Delete staff member (soft delete by setting status to inactive)
    public void deleteStaff(Integer id) {
        Optional<AppUser> optionalStaff = appUserRepository.findById(id);

        if (optionalStaff.isPresent()) {
            AppUser staff = optionalStaff.get();
            staff.setStatus("inactive");
            appUserRepository.save(staff);
        } else {
            throw new RuntimeException("Staff member not found with id: " + id);
        }
    }

    // Validate staff role
    private boolean isValidStaffRole(String role) {
        return "admin".equals(role) || "doctor".equals(role);
    }
}