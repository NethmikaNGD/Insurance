package com.pg91.insurance.service;

import com.pg91.insurance.config.CustomUserDetails;
import com.pg91.insurance.entity.AppUser;
import com.pg91.insurance.repo.AppUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    private final AppUserRepository usersRepository;

    // Constructor injection: Removed PasswordEncoder to avoid circular dependency
    public CustomUserDetailsService(AppUserRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Normalize email to lowercase for case-insensitive lookup
        String normalizedEmail = email.toLowerCase().trim();
        System.out.println("Attempting to load user with normalized email: " + normalizedEmail);

        Optional<AppUser> userOptional = usersRepository.findByEmail(normalizedEmail);
        if (userOptional.isPresent()) {
            AppUser user = userOptional.get();
            System.out.println("User found: " + user.getName() + " with role: " + user.getRole() + " and status: " + user.getStatus());

            // Check if user is active
            if (!"active".equals(user.getStatus())) {
                System.out.println("User account is inactive: " + normalizedEmail);
                throw new UsernameNotFoundException("User account is inactive");
            }

            return new CustomUserDetails(user);
        } else {
            System.out.println("User not found with email: " + normalizedEmail);
            throw new UsernameNotFoundException("User not found with email: " + normalizedEmail);
        }
    }

    // Removed testPassword method to avoid circular dependency with PasswordEncoder
    // Password validation is now handled by Spring Security's authentication mechanism
}