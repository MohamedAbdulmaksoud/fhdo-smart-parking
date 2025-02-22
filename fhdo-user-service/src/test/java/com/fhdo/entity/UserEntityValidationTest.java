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

class UserEntityValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidUser() {
        UserEntity userEntity = new UserEntity();
        userEntity.setName("John Doe");
        userEntity.setEmail("john.doe@example.com");
        userEntity.setPassword("password123");

        Set<ConstraintViolation<UserEntity>> violations = validator.validate(userEntity);

        assertTrue(violations.isEmpty(), "Expected no validation errors for a valid user");
    }

    @Test
    void testBlankName() {
        UserEntity userEntity = new UserEntity();
        userEntity.setName("   ");
        userEntity.setEmail("john.doe@example.com");
        userEntity.setPassword("password123");

        Set<ConstraintViolation<UserEntity>> violations = validator.validate(userEntity);

        assertEquals(1, violations.size());
        assertEquals("Name cannot be blank", violations.iterator().next().getMessage());
    }

    @Test
    void testNameTooShort() {
        UserEntity userEntity = new UserEntity();
        userEntity.setName("J");
        userEntity.setEmail("john.doe@example.com");
        userEntity.setPassword("password123");

        Set<ConstraintViolation<UserEntity>> violations = validator.validate(userEntity);

        assertEquals(1, violations.size());
        assertEquals("Name must be between 2 and 50 characters", violations.iterator().next().getMessage());
    }

    @Test
    void testInvalidEmail() {
        UserEntity userEntity = new UserEntity();
        userEntity.setName("John Doe");
        userEntity.setEmail("invalid-email");
        userEntity.setPassword("password123");

        Set<ConstraintViolation<UserEntity>> violations = validator.validate(userEntity);

        assertEquals(1, violations.size());
        assertEquals("Invalid email format", violations.iterator().next().getMessage());
    }

    @Test
    void testBlankPassword() {
        UserEntity userEntity = new UserEntity();
        userEntity.setName("John Doe");
        userEntity.setEmail("john.doe@example.com");
        userEntity.setPassword("         ");

        Set<ConstraintViolation<UserEntity>> violations = validator.validate(userEntity);

        assertEquals(1, violations.size());
        assertEquals("Password cannot be blank", violations.iterator().next().getMessage());
    }

    @Test
    void testPasswordTooShort() {
        UserEntity userEntity = new UserEntity();
        userEntity.setName("John Doe");
        userEntity.setEmail("john.doe@example.com");
        userEntity.setPassword("short");

        Set<ConstraintViolation<UserEntity>> violations = validator.validate(userEntity);

        assertEquals(1, violations.size());
        assertEquals("Password must be at least 8 characters long", violations.iterator().next().getMessage());
    }

}