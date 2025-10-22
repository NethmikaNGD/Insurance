package com.pg91.insurance.repo;

import com.pg91.insurance.entity.AppUser;
import com.pg91.insurance.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Integer> {
    Optional<AppUser> findByEmail(String email);

    Optional<AppUser> findByName(String name);

    List<AppUser> findByRole(Role role);
}
