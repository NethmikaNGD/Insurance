package com.pg91.insurance.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "AppUsers")
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appUserId;

    private String name;
    private String email;
    private String passwordHash;
    private String role;
    private String status;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();

    // getters and setters
}

