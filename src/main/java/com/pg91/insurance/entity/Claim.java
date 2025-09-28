package com.pg91.insurance.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "claims")
public class Claim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String policyholderId; // Reference to user/policyholder

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDateTime submissionDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClaimStatus status = ClaimStatus.PENDING; // Enum for status

    @ElementCollection
    private List<String> documentPaths; // List of uploaded document paths

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPolicyholderId() {
        return policyholderId;
    }

    public void setPolicyholderId(String policyholderId) {
        this.policyholderId = policyholderId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(LocalDateTime submissionDate) {
        this.submissionDate = submissionDate;
    }

    public ClaimStatus getStatus() {
        return status;
    }

    public void setStatus(ClaimStatus status) {
        this.status = status;
    }

    public List<String> getDocumentPaths() {
            return documentPaths;
    }
    public void setDocumentPaths(List<String> documentPaths) {
        this.documentPaths = documentPaths;
    }
}
