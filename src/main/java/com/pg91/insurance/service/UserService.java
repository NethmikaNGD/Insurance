package com.pg91.insurance.service;

import com.pg91.insurance.entity.User;
import com.pg91.insurance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository repo;

    // Get all users
    public List<User> getAllUsers() {
        return repo.findAll();
    }

    // Deactivate user account - CORRECTED
    public User deactivateUser(Long id) {
        User user = repo.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setStatus("inactive");  // Changed from setActive(false)
        return repo.save(user);
    }

    // Activate user account - CORRECTED
    public User activateUser(Long id) {
        User user = repo.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setStatus("active");    // Changed from setActive(true)
        return repo.save(user);
    }

    // Delete user
    public void deleteUser(Long id) {
        repo.deleteById(id);
    }

    // Additional helper methods
    public List<User> getActiveUsers() {
        return repo.findByStatus("active");
    }

    public List<User> getInactiveUsers() {
        return repo.findByStatus("inactive");
    }
}