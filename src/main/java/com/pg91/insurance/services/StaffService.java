package com.pg91.insurance.services;

import com.pg91.insurance.entities.AppUser;
import com.pg91.insurance.factory.UserFactory;
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

    @Autowired
    private UserFactory userFactory;


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

        // Hash password before saving
        String encodedPassword = passwordEncoder.encode(
                staff.getPasswordHash() != null && !staff.getPasswordHash().isEmpty()
                        ? staff.getPasswordHash()
                        : "default123"
        );

        // Use factory to create user with proper role-specific settings
        AppUser newStaff = userFactory.createUser(
                staff.getRole(),
                staff.getName(),
                staff.getEmail(),
                encodedPassword
        );

        return appUserRepository.save(staff);
    }
    // Create staff with detailed DTO using Factory
    public AppUser createStaffFromDTO(com.pg91.insurance.dto.StaffDTO staffDTO) {
        // Check if email already exists
        if (appUserRepository.existsByEmail(staffDTO.getEmail())) {
            throw new RuntimeException("Email already exists: " + staffDTO.getEmail());
        }

        // Validate role
        if (!isValidStaffRole(staffDTO.getRole())) {
            throw new RuntimeException("Invalid role for staff: " + staffDTO.getRole());
        }

        // Hash password
        String encodedPassword = passwordEncoder.encode(
                staffDTO.getPasswordHash() != null && !staffDTO.getPasswordHash().isEmpty()
                        ? staffDTO.getPasswordHash()
                        : "default123"
        );

        // Use factory to create user
        AppUser newStaff = userFactory.createUserWithDetails(
                staffDTO.getRole(),
                staffDTO.getName(),
                staffDTO.getEmail(),
                encodedPassword,
                staffDTO.getStatus(),
                staffDTO.getDepartment()
        );

        // Set role-specific properties
        setRoleSpecificProperties(newStaff, staffDTO);

        return appUserRepository.save(newStaff);
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

    // Get staff by role using factory-like filtering
    public List<AppUser> getStaffByRole(String role) {
        if (!isValidStaffRole(role)) {
            throw new RuntimeException("Invalid role: " + role);
        }
        return appUserRepository.findByRole(role);
    }

    // Validate staff role
    private boolean isValidStaffRole(String role) {
        return "admin".equals(role) || "doctor".equals(role) || "receptionist".equals(role);
    }

    // Set role-specific properties
    private void setRoleSpecificProperties(AppUser user, com.pg91.insurance.dto.StaffDTO dto) {
        // You could store these in a separate table or as JSON in a properties field
        // For demo, we'll just set them conceptually
        switch (user.getRole().toLowerCase()) {
            case "doctor":
                // Set doctor-specific properties
                // user.setProperties(createDoctorProperties(dto));
                break;
            case "admin":
                // Set admin-specific properties
                // user.setProperties(createAdminProperties(dto));
                break;
            case "receptionist":
                // Set receptionist-specific properties
                // user.setProperties(createReceptionistProperties(dto));
                break;
        }
    }
}