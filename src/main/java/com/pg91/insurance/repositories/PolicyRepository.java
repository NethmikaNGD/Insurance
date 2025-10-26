package com.pg91.insurance.repositories;

import com.pg91.insurance.entities.Policy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PolicyRepository extends JpaRepository<Policy, Integer> {

    // Find active policies
    List<Policy> findByIsActiveTrue();

    // Find policies required for users
    List<Policy> findByIsActiveTrueAndRequiredForUsersTrue();

    // Find by title (for checking duplicates)
    Optional<Policy> findByTitle(String title);

    // Find all policies ordered by creation date
    @Query("SELECT p FROM Policy p ORDER BY p.createdAt DESC")
    List<Policy> findAllOrderByCreatedAtDesc();
}