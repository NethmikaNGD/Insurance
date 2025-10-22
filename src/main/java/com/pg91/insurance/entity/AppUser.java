package com.pg91.insurance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Represents a user in the application, mapping to the 'AppUsers' table in the database.
 */
@Setter
@Getter
@Entity
@Table(name = "\"AppUsers\"") // Correctly maps this entity to the "AppUsers" database table with exact case
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "app_user_id")
    private Integer appUserId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(name = "status", length = 20)
    private String status; // e.g., 'active', 'inactive'

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * This JPA callback automatically runs before a new user is saved.
     * It sets the creation timestamp and a default status.
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (status == null) {
            status = "active"; // Ensures all new users are active by default
        }
    }

    // --- Constructors ---

    /**
     * Default constructor required by JPA.
     */
    public AppUser() {
    }

    /**
     * Convenience constructor for creating new users.
     */
    public AppUser(String name, String email, String password, Role role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }
}