package com.pg91.insurance.services;

import com.pg91.insurance.entities.Policy;
import com.pg91.insurance.repositories.PolicyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PolicyService {

    @Autowired
    private PolicyRepository policyRepository;

    // Get all policies
    public List<Policy> getAllPolicies() {
        return policyRepository.findAllOrderByCreatedAtDesc();
    }

    // Get active policies
    public List<Policy> getActivePolicies() {
        return policyRepository.findByIsActiveTrue();
    }

    // Get policies required for users
    public List<Policy> getUserRequiredPolicies() {
        return policyRepository.findByIsActiveTrueAndRequiredForUsersTrue();
    }

    // Get policy by ID
    public Optional<Policy> getPolicyById(Integer id) {
        return policyRepository.findById(id);
    }

    // Create new policy
    public Policy createPolicy(Policy policy) {
        // Check if title already exists
        if (policyRepository.findByTitle(policy.getTitle()).isPresent()) {
            throw new RuntimeException("Policy with title '" + policy.getTitle() + "' already exists");
        }

        return policyRepository.save(policy);
    }

    // Update policy
    public Policy updatePolicy(Integer id, Policy policyDetails) {
        Optional<Policy> optionalPolicy = policyRepository.findById(id);

        if (optionalPolicy.isPresent()) {
            Policy policy = optionalPolicy.get();

            // Update fields
            policy.setTitle(policyDetails.getTitle());
            policy.setContent(policyDetails.getContent());
            policy.setVersion(policyDetails.getVersion());
            policy.setIsActive(policyDetails.getIsActive());
            policy.setRequiredForUsers(policyDetails.getRequiredForUsers());
            policy.setUpdatedAt(java.time.LocalDateTime.now());

            return policyRepository.save(policy);
        } else {
            throw new RuntimeException("Policy not found with id: " + id);
        }
    }

    // Delete policy (soft delete by setting inactive)
    public void deletePolicy(Integer id) {
        Optional<Policy> optionalPolicy = policyRepository.findById(id);

        if (optionalPolicy.isPresent()) {
            Policy policy = optionalPolicy.get();
            policy.setIsActive(false);
            policy.setUpdatedAt(java.time.LocalDateTime.now());
            policyRepository.save(policy);
        } else {
            throw new RuntimeException("Policy not found with id: " + id);
        }
    }

    // Toggle policy status
    public Policy togglePolicyStatus(Integer id) {
        Optional<Policy> optionalPolicy = policyRepository.findById(id);

        if (optionalPolicy.isPresent()) {
            Policy policy = optionalPolicy.get();
            policy.setIsActive(!policy.getIsActive());
            policy.setUpdatedAt(java.time.LocalDateTime.now());
            return policyRepository.save(policy);
        } else {
            throw new RuntimeException("Policy not found with id: " + id);
        }
    }
}