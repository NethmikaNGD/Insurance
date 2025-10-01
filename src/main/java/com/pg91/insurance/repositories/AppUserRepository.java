package com.pg91.insurance.repositories;

import com.pg91.insurance.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Integer> {

    Optional<AppUser> findByEmail(String email);

    // Use the entity name "AppUser" not the table name
    @Query("SELECT u FROM AppUser u WHERE u.role IN ('admin', 'doctor')")
    List<AppUser> findAllStaff();

    // Find by role
    List<AppUser> findByRole(String role);

    boolean existsByEmail(String email);
}