package com.pg91.insurance.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Claim_Documents")
public class ClaimDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long docId;

    @ManyToOne
    @JoinColumn(name = "claim_id")
    private Claim claim;

    @ManyToOne
    @JoinColumn(name = "uploaded_by")
    private AppUser uploadedBy;

    private String filePath;

    @Temporal(TemporalType.TIMESTAMP)
    private Date uploadedAt = new Date();

    // getters and setters
}

