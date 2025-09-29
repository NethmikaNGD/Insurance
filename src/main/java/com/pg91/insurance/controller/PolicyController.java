package com.pg91.insurance.controller;

import com.pg91.insurance.entity.Policy;
import com.pg91.insurance.repository.PolicyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/policies")
public class PolicyController {

    @Autowired
    private PolicyRepository policyRepository;

    @GetMapping
    public List<Policy> getAllPolicies() {
        return policyRepository.findAll();
    }

    @PostMapping
    public Policy createPolicy(@RequestBody Policy policy) {
        return policyRepository.save(policy);
    }

    @GetMapping("/{id}")
    public Policy getPolicyById(@PathVariable Long id) {
        return policyRepository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Policy updatePolicy(@PathVariable Long id, @RequestBody Policy policyDetails) {
        Policy policy = policyRepository.findById(id).orElse(null);
        if (policy != null) {
            policy.setTitle(policyDetails.getTitle());
            policy.setPlanCode(policyDetails.getPlanCode());
            policy.setCategory(policyDetails.getCategory());
            policy.setDescription(policyDetails.getDescription());
            policy.setCoverageAmount(policyDetails.getCoverageAmount());
            policy.setValidFrom(policyDetails.getValidFrom());
            policy.setValidTo(policyDetails.getValidTo());
            policy.setStatus(policyDetails.getStatus());
            return policyRepository.save(policy);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void deletePolicy(@PathVariable Long id) {
        policyRepository.deleteById(id);
    }
}
