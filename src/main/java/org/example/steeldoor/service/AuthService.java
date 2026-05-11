package org.example.steeldoor.service;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.example.steeldoor.config.JwtUtils;
import org.example.steeldoor.config.exception.UserNotFoundException;
import org.example.steeldoor.dto.LoginRequestDTO;
import org.example.steeldoor.dto.LoginResponseDTO;
import org.example.steeldoor.dto.RegisterRequestDTO;
import org.example.steeldoor.dto.RegisterResponseDTO;
import org.example.steeldoor.model.Role;
import org.example.steeldoor.model.User;
import org.example.steeldoor.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public RegisterResponseDTO register(RegisterRequestDTO request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .createdAt(OffsetDateTime.now())
                .role(Role.builder().id(1).build())
                .build();

        User savedUser = userRepository.save(user);

        return RegisterResponseDTO.builder()
                .id(savedUser.getId())
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .roleId(savedUser.getRole() != null ? savedUser.getRole().getId() : 1)
                .createdAt(savedUser.getCreatedAt())
                .build();
    }

    public LoginResponseDTO login(LoginRequestDTO request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + request.getEmail()));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new ValidationException("Invalid password");
        }

        String token = jwtUtils.generateToken(org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPasswordHash())
                .authorities(user.getRole() != null ? user.getRole().getName() : "ROLE_MEMBER")
                .build());

        return LoginResponseDTO.builder()
                .token(token)
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }
}