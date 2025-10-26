package com.pg91.insurance.service;

import com.pg91.insurance.entity.AppUser;
import com.pg91.insurance.entity.Role;
import com.pg91.insurance.repo.AppUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private AppUserRepository appUserRepository;

    @InjectMocks
    private UserService userService;

    private AppUser testUser;

    @BeforeEach
    void setUp() {
        testUser = new AppUser();
        testUser.setAppUserId(1);
        testUser.setName("John Doe");
        testUser.setEmail("john@example.com");
        testUser.setPassword("password123");
        testUser.setRole(Role.CUSTOMER);
        testUser.setStatus("active");
    }

    @Test
    void registerUser_Success() {
        // Given
        when(appUserRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(appUserRepository.save(any(AppUser.class))).thenReturn(testUser);

        // When
        AppUser result = userService.registerUser("John Doe", "john@example.com", "password123", Role.CUSTOMER);

        // Then
        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals("john@example.com", result.getEmail());
        assertEquals("password123", result.getPassword()); // Plain text password
        assertEquals(Role.CUSTOMER, result.getRole());
        assertEquals("active", result.getStatus());

        verify(appUserRepository).findByEmail("john@example.com");
        verify(appUserRepository).save(any(AppUser.class));
    }

    @Test
    void registerUser_EmailAlreadyExists_ThrowsException() {
        // Given
        when(appUserRepository.findByEmail(anyString())).thenReturn(Optional.of(testUser));

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser("John Doe", "john@example.com", "password123", Role.CUSTOMER);
        });

        assertEquals("User with email john@example.com already exists", exception.getMessage());
        verify(appUserRepository).findByEmail("john@example.com");
        verify(appUserRepository, never()).save(any(AppUser.class));
    }

    @Test
    void registerCustomer_Success() {
        // Given
        when(appUserRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(appUserRepository.save(any(AppUser.class))).thenReturn(testUser);

        // When
        AppUser result = userService.registerCustomer("John Doe", "john@example.com", "password123");

        // Then
        assertNotNull(result);
        assertEquals(Role.CUSTOMER, result.getRole());
        verify(appUserRepository).save(any(AppUser.class));
    }

    @Test
    void emailExists_ReturnsTrue() {
        // Given
        when(appUserRepository.findByEmail(anyString())).thenReturn(Optional.of(testUser));

        // When
        boolean exists = userService.emailExists("john@example.com");

        // Then
        assertTrue(exists);
        verify(appUserRepository).findByEmail("john@example.com");
    }

    @Test
    void emailExists_ReturnsFalse() {
        // Given
        when(appUserRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // When
        boolean exists = userService.emailExists("nonexistent@example.com");

        // Then
        assertFalse(exists);
        verify(appUserRepository).findByEmail("nonexistent@example.com");
    }

    @Test
    void verifyPassword_ReturnsTrue() {
        // When
        boolean isValid = userService.verifyPassword(testUser, "password123");

        // Then
        assertTrue(isValid);
    }

    @Test
    void verifyPassword_ReturnsFalse() {
        // When
        boolean isValid = userService.verifyPassword(testUser, "wrongPassword");

        // Then
        assertFalse(isValid);
    }
}
