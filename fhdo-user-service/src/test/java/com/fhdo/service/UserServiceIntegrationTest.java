package com.fhdo.service;

import com.fhdo.entity.User;
import com.fhdo.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void testRegisterUser() {
        User result = userService.registerUser("John Doe", "john@example.com", "password123");

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals("john@example.com", result.getEmail());
        assertTrue(passwordEncoder.matches("password123", result.getPassword()));
    }

    @Test
    void testAuthenticateUser_Success() {
        userService.registerUser("John Doe", "john@example.com", "password123");

        Optional<User> result = userService.authenticateUser("john@example.com", "password123");

        assertTrue(result.isPresent());
        assertEquals("john@example.com", result.get().getEmail());
    }

    @Test
    void testAuthenticateUser_Failure() {
        userService.registerUser("John Doe", "john@example.com", "password123");

        Optional<User> result = userService.authenticateUser("john@example.com", "wrongPassword");

        assertTrue(result.isEmpty());
    }
}
