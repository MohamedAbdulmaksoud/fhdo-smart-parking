package com.fhdo.controller;

import com.fhdo.dto.UserDto;
import com.fhdo.entity.UserEntity;
import com.fhdo.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    /**
     * Register a new user.
     */
    @PostMapping("/register")
    public ResponseEntity<UUID> registerUser(@RequestBody @Valid UserDto userDto) {
        UUID userId = userService.registerUser(userDto.getName(), userDto.getEmail(), userDto.getPassword()).getId();
        return ResponseEntity.ok(userId);
    }

    /**
     * Authenticate a user and create a session.
     */
    @PostMapping("/login")
    public ResponseEntity<UUID> loginUser(@RequestBody @Valid UserDto userDto) {
        UUID userId = userService.authenticateUser(userDto.getEmail(), userDto.getPassword(), authenticationManager);
        return ResponseEntity.ok(userId);
    }

    /**
     * Get the currently authenticated user.
     */
    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if the user is authenticated
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            return ResponseEntity.status(401).build();
        }

        // Extract user details
        Set<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
        String name = null;

        if (authentication.getPrincipal() instanceof UserEntity) {
            name = ((UserEntity) authentication.getPrincipal()).getUsername();
        }

        UserDto userDto = new UserDto();
        userDto.setEmail(name);
        userDto.setRoles(roles);

        return ResponseEntity.ok(userDto);
    }
}
