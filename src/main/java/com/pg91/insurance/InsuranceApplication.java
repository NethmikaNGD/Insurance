package com.pg91.insurance;

import com.pg91.insurance.entity.AppUser;
import com.pg91.insurance.entity.Role;
import com.pg91.insurance.repo.AppUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@SpringBootApplication
@EnableScheduling
public class InsuranceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InsuranceApplication.class, args);
    }

    @Bean
    CommandLineRunner run(AppUserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            System.out.println("=== Checking existing users in database ===");
            
            // Check if your existing users are in the database
            userRepository.findByEmail("Micheal@gmail.com").ifPresentOrElse(
                user -> System.out.println("✓ Found existing customer: " + user.getName()),
                () -> System.out.println("✗ Customer user not found: Micheal@gmail.com")
            );
            
            userRepository.findByEmail("Oliver@admin.com").ifPresentOrElse(
                user -> System.out.println("✓ Found existing admin: " + user.getName()),
                () -> System.out.println("✗ Admin user not found: Oliver@admin.com")
            );
            
            // Only create additional test users if they don't exist
            if (userRepository.findByEmail("admin@email.com").isEmpty()) {
                System.out.println("Creating additional admin user...");
                AppUser adminUser = new AppUser();
                adminUser.setName("Test Admin");
                adminUser.setPassword(passwordEncoder.encode("1234"));
                adminUser.setEmail("admin@email.com");
                adminUser.setRole(Role.ADMIN);
                adminUser.setStatus("active");
                adminUser.setCreatedAt(LocalDateTime.now());
                userRepository.save(adminUser);
                System.out.println("✓ Additional admin user created");
            }

            // Create your specific customer user
            if (userRepository.findByEmail("customer@email.com").isEmpty()) {
                System.out.println("Creating your customer user...");
                AppUser customerUser = new AppUser();
                customerUser.setName("Your Customer");
                customerUser.setPassword(passwordEncoder.encode("1234"));
                customerUser.setEmail("customer@email.com");
                customerUser.setRole(Role.CUSTOMER);
                customerUser.setStatus("active");
                customerUser.setCreatedAt(LocalDateTime.now());
                userRepository.save(customerUser);
                System.out.println("✓ Your customer user created with email: customer@email.com");
            } else {
                System.out.println("✓ Your customer user already exists: customer@email.com");
            }
            
            System.out.println("=== User initialization completed ===");
        };
    }
}