package com.fhdo.entity;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidUser() {
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password123");

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertTrue(violations.isEmpty(), "Expected no validation errors for a valid user");
    }

    @Test
    void testBlankName() {
        User user = new User();
        user.setName("   ");
        user.setEmail("john.doe@example.com");
        user.setPassword("password123");

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(1, violations.size());
        assertEquals("Name cannot be blank", violations.iterator().next().getMessage());
    }

    @Test
    void testNameTooShort() {
        User user = new User();
        user.setName("J");
        user.setEmail("john.doe@example.com");
        user.setPassword("password123");

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(1, violations.size());
        assertEquals("Name must be between 2 and 50 characters", violations.iterator().next().getMessage());
    }

    @Test
    void testInvalidEmail() {
        User user = new User();
        user.setName("John Doe");
        user.setEmail("invalid-email");
        user.setPassword("password123");

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(1, violations.size());
        assertEquals("Invalid email format", violations.iterator().next().getMessage());
    }

    @Test
    void testBlankPassword() {
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("         ");

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(1, violations.size());
        assertEquals("Password cannot be blank", violations.iterator().next().getMessage());
    }

    @Test
    void testPasswordTooShort() {
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("short");

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(1, violations.size());
        assertEquals("Password must be at least 8 characters long", violations.iterator().next().getMessage());
    }

}