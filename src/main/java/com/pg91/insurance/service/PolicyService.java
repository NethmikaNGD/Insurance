package com.pg91.insurance.service;

import com.pg91.insurance.entity.Policy;
import com.pg91.insurance.repository.PolicyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PolicyService {

    @Autowired
    private PolicyRepository repo;

    // Get all policies
    public List<Policy> getAllPolicies() {
        return repo.findAll();
    }

    // Add new policy
    public Policy addPolicy(Policy policy) {
        return repo.save(policy);
    }

    // Delete a policy by id
    public void deletePolicy(Long id) {
        repo.deleteById(id);
    }

    // Activate a policy
    public Policy activatePolicy(Long id) {
        Policy policy = repo.findById(id).orElseThrow(() -> new RuntimeException("Policy not found"));
        policy.setActive(true);
        return repo.save(policy);
    }

    // Deactivate a policy
    public Policy deactivatePolicy(Long id) {
        Policy policy = repo.findById(id).orElseThrow(() -> new RuntimeException("Policy not found"));
        policy.setActive(false);
        return repo.save(policy);
    }
}
