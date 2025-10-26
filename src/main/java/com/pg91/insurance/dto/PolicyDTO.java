package com.pg91.insurance.dto;

import jakarta.validation.constraints.NotBlank;

public class PolicyDTO {

    private Integer policyId;

    @NotBlank(message = "Policy title is required")
    private String title;

    @NotBlank(message = "Policy content is required")
    private String content;

    private String version = "1.0";

    private Boolean isActive = true;

    private Boolean requiredForUsers = true;

    // Constructors
    public PolicyDTO() {}

    public PolicyDTO(String title, String content, String version) {
        this.title = title;
        this.content = content;
        this.version = version;
    }

    // Getters and Setters
    public Integer getPolicyId() {
        return policyId;
    }

    public void setPolicyId(Integer policyId) {
        this.policyId = policyId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getRequiredForUsers() {
        return requiredForUsers;
    }

    public void setRequiredForUsers(Boolean requiredForUsers) {
        this.requiredForUsers = requiredForUsers;
    }
}