package com.pg91.insurance.service;

import com.pg91.insurance.entity.AppUser;
import com.pg91.insurance.entity.Role;
import com.pg91.insurance.repo.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private AppUserRepository appUserRepository;

    /**
     * Register a new user
     * @param name User's full name
     * @param email User's email address
     * @param password Plain text password (will be hashed)
     * @param role User's role (CUSTOMER, ADMIN, DOCTOR)
     * @return The created AppUser object
     * @throws IllegalArgumentException if email already exists
     */
    public AppUser registerUser(String name, String email, String password, Role role) {
        // Check if user already exists
        Optional<AppUser> existingUser = appUserRepository.findByEmail(email.toLowerCase().trim());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("User with email " + email + " already exists");
        }

        // Create new user
        AppUser newUser = new AppUser();
        newUser.setName(name);
        newUser.setEmail(email.toLowerCase().trim());
        newUser.setPassword(password); // Store plain text password
        newUser.setRole(role);
        newUser.setStatus("active"); // Set default status

        return appUserRepository.save(newUser);
    }

    /**
     * Register a new customer (convenience method)
     * @param name User's full name
     * @param email User's email address
     * @param password Plain text password
     * @return The created AppUser object
     */
    public AppUser registerCustomer(String name, String email, String password) {
        return registerUser(name, email, password, Role.CUSTOMER);
    }

    /**
     * Find user by email
     * @param email User's email address
     * @return Optional containing the user if found
     */
    public Optional<AppUser> findByEmail(String email) {
        return appUserRepository.findByEmail(email.toLowerCase().trim());
    }

    /**
     * Find user by ID
     * @param id User's ID
     * @return Optional containing the user if found
     */
    public Optional<AppUser> findById(Integer id) {
        return appUserRepository.findById(id);
    }

    /**
     * Check if email is already registered
     * @param email Email to check
     * @return true if email exists, false otherwise
     */
    public boolean emailExists(String email) {
        return appUserRepository.findByEmail(email.toLowerCase().trim()).isPresent();
    }

    /**
     * Update user information
     * @param user User to update
     * @return Updated user
     */
    public AppUser updateUser(AppUser user) {
        return appUserRepository.save(user);
    }

    /**
     * Change user password
     * @param user User whose password to change
     * @param newPassword New plain text password
     * @return Updated user
     */
    public AppUser changePassword(AppUser user, String newPassword) {
        user.setPassword(newPassword); // Store plain text password
        return appUserRepository.save(user);
    }

    /**
     * Verify password for a user (for login validation)
     * @param user User to verify password for
     * @param rawPassword Plain text password to verify
     * @return true if password matches, false otherwise
     */
    public boolean verifyPassword(AppUser user, String rawPassword) {
        return rawPassword.equals(user.getPassword()); // Simple string comparison
    }
}
