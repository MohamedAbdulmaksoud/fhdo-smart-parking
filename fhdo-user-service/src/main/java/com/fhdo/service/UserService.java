package com.fhdo.service;

import com.fhdo.entity.UserEntity;
import com.fhdo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    /**
     * Register a new user.
     *
     * @param name     the user's name
     * @param email    the user's email
     * @param password the user's password
     * @return the newly created user entity
     */
    public UserEntity registerUser(String name, String email, String password) {
        logger.info("Attempting to register user with email: {}", email);

        // Check if user with the given email already exists
        if (userRepository.findByEmail(email).isPresent()) {
            logger.warn("User with email {} already exists.", email);
            throw new IllegalArgumentException("User with this email already exists.");
        }

        // Create and save the new user
        UserEntity userEntity = new UserEntity();
        userEntity.setName(name);
        userEntity.setEmail(email);
        userEntity.setPassword(passwordEncoder.encode(password));

        UserEntity savedUser = userRepository.save(userEntity);
        logger.info("User with email {} registered successfully.", email);
        return savedUser;
    }

    /**
     * Authenticate a user.
     *
     * @param email    the user's email
     * @param password the user's password
     */
    public UUID authenticateUser(String email, String password, AuthenticationManager authenticationManager) {
        logger.info("Attempting to authenticate user with email: {}", email);

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
            UserEntity userEntity = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            logger.info("User with email {} authenticated successfully.", email);
            return userEntity.getId();
        } catch (Exception ex) {
            logger.warn("Authentication failed for user with email: {}", email);
            throw new RuntimeException("Invalid email or password");
        }
    }

    /**
     * Load a user by email (for Spring Security).
     *
     * @param email the user's email
     * @return the user details
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.info("Loading user by email: {}", email);

        // Find user by email
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // Convert UserEntity to Spring Security UserDetails
        return User.builder()
                .username(userEntity.getEmail())
                .password(userEntity.getPassword())
                .roles("USER") // Assign default role; can be enhanced for role-based authorization
                .build();
    }
}
