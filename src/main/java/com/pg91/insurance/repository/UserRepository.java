package com.pg91.insurance.repository;

import com.pg91.insurance.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByStatus(String status);  // Example: find active/inactive users
}
