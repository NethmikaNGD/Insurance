package com.pg91.insurance.factory;

import com.pg91.insurance.entities.AppUser;
import org.springframework.stereotype.Component;

@Component
public class UserFactory {

    public AppUser createUser(String role, String name, String email, String password) {
        AppUser user = new AppUser();
        user.setName(name);
        user.setEmail(email);
        user.setPasswordHash(password);
        user.setRole(role);

        // Set role-specific properties
        switch (role.toLowerCase()) {
            case "admin":
                return createAdminUser(user);
            case "doctor":
                return createDoctorUser(user);
            case "receptionist":
                return createReceptionistUser(user);
            default:
                return createDefaultUser(user);
        }
    }

    private AppUser createAdminUser(AppUser user) {
        user.setStatus("active");
        // Admin specific initializations can go here
        // For example: set special permissions, default settings, etc.
        return user;
    }

    private AppUser createDoctorUser(AppUser user) {
        user.setStatus("active");
        // Doctor specific initializations
        // For example: set medical license info, specialization, etc.
        return user;
    }

    private AppUser createReceptionistUser(AppUser user) {
        user.setStatus("active");
        // Receptionist specific initializations
        // For example: set front desk permissions, etc.
        return user;
    }

    private AppUser createDefaultUser(AppUser user) {
        user.setStatus("pending");
        user.setRole("staff"); // Default role
        return user;
    }

    // Factory method for creating user with detailed parameters
    public AppUser createUserWithDetails(String role, String name, String email,
                                         String password, String status, String department) {
        AppUser user = createUser(role, name, email, password);
        user.setStatus(status);

        // You could add department-specific logic here
        if (department != null) {
            // Set department-specific properties
        }

        return user;
    }
}