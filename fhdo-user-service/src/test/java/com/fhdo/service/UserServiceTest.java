package com.fhdo.service;

import com.fhdo.entity.UserEntity;
import com.fhdo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

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
        when(userRepository.save(any(UserEntity.class))).thenAnswer(invocation -> {
            UserEntity savedUser = invocation.getArgument(0);
            savedUser.setId(UUID.randomUUID());
            return savedUser;
        });

        UserEntity result = userService.registerUser(name, email, password);

        assertNotNull(result);
        assertEquals(name, result.getName());
        assertEquals(email, result.getEmail());
        assertEquals("encodedPassword", result.getPassword());
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void testRegisterUser_DuplicateEmail() {
        String email = "john@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(new UserEntity()));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.registerUser("John Doe", email, "password123"));

        assertEquals("User with this email already exists.", exception.getMessage());
    }

    @Test
    void testAuthenticateUser_Success() {
        String email = "john@example.com";
        String password = "password123";
        UUID userId = UUID.randomUUID();

        UserEntity userEntity = new UserEntity();
        userEntity.setId(userId);
        userEntity.setEmail(email);
        userEntity.setPassword("encodedPassword");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(userEntity));
        when(passwordEncoder.matches(password, "encodedPassword")).thenReturn(true);
        doNothing().when(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));

        UUID result = userService.authenticateUser(email, password, authenticationManager);

        assertNotNull(result);
        assertEquals(userId, result);
        verify(authenticationManager, times(1))
                .authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void testAuthenticateUser_InvalidPassword() {
        String email = "john@example.com";
        String password = "wrongPassword";

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(email);
        userEntity.setPassword("encodedPassword");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(userEntity));
        when(passwordEncoder.matches(password, "encodedPassword")).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.authenticateUser(email, password, authenticationManager));

        assertEquals("Invalid email or password", exception.getMessage());
    }

    @Test
    void testAuthenticateUser_EmailNotFound() {
        String email = "unknown@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.authenticateUser(email, "password123", authenticationManager));

        assertEquals("Invalid email or password", exception.getMessage());
    }
}
