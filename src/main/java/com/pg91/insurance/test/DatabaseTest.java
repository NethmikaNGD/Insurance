package com.pg91.insurance.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.pg91.insurance.repo.AppUserRepository;

@SpringBootApplication
public class DatabaseTest {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DatabaseTest.class, args);
        
        AppUserRepository userRepository = context.getBean(AppUserRepository.class);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        System.out.println("=== Database Connection Test ===");
        
        try {
            // Test database connection by counting users
            long userCount = userRepository.count();
            System.out.println("Total users in database: " + userCount);
            
            // List all users
            System.out.println("\n=== All Users in Database ===");
            userRepository.findAll().forEach(user -> {
                System.out.println("ID: " + user.getAppUserId());
                System.out.println("Name: " + user.getName());
                System.out.println("Email: " + user.getEmail());
                System.out.println("Role: " + user.getRole());
                System.out.println("Status: " + user.getStatus());
                System.out.println("Password Hash: " + user.getPassword());
                System.out.println("Created: " + user.getCreatedAt());
                System.out.println("---");
            });
            
            // Test password matching for "Dineth"
            System.out.println("\n=== Password Test for 'Dineth' ===");
            userRepository.findAll().forEach(user -> {
                if ("Dineth".equals(user.getName()) || user.getEmail().contains("dineth")) {
                    System.out.println("Testing user: " + user.getName() + " (" + user.getEmail() + ")");
                    boolean matches = encoder.matches("Dineth", user.getPassword());
                    System.out.println("Password 'Dineth' matches: " + matches);
                    
                    // Generate a new hash for comparison
                    String newHash = encoder.encode("Dineth");
                    System.out.println("New hash for 'Dineth': " + newHash);
                    boolean newMatches = encoder.matches("Dineth", newHash);
                    System.out.println("New hash matches: " + newMatches);
                }
            });
            
        } catch (Exception e) {
            System.err.println("Database connection failed: " + e.getMessage());
            e.printStackTrace();
        }
        
        context.close();
    }
}
