package com.fhdo.service;

import com.fhdo.entity.User;
import com.fhdo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser_Success() {
        String name = "John Doe";
        String email = "john@example.com";
        String password = "password123";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.registerUser(name, email, password);

        assertNotNull(result);
        assertEquals(name, result.getName());
        assertEquals(email, result.getEmail());
        assertEquals("encodedPassword", result.getPassword());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRegisterUser_DuplicateEmail() {
        String email = "john@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(new User()));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.registerUser("John Doe", email, "password123"));

        assertEquals("User with this email already exists.", exception.getMessage());
    }

    @Test
    void testAuthenticateUser_Success() {
        String email = "john@example.com";
        String password = "password123";
        User user = new User();
        user.setEmail(email);
        user.setPassword("encodedPassword");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, "encodedPassword")).thenReturn(true);

        Optional<User> result = userService.authenticateUser(email, password);

        assertTrue(result.isPresent());
        assertEquals(email, result.get().getEmail());
    }

    @Test
    void testAuthenticateUser_InvalidPassword() {
        String email = "john@example.com";
        String password = "wrongPassword";
        User user = new User();
        user.setEmail(email);
        user.setPassword("encodedPassword");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, "encodedPassword")).thenReturn(false);

        Optional<User> result = userService.authenticateUser(email, password);

        assertTrue(result.isEmpty());
    }

    @Test
    void testAuthenticateUser_EmailNotFound() {
        String email = "unknown@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        Optional<User> result = userService.authenticateUser(email, "password123");

        assertTrue(result.isEmpty());
    }
}