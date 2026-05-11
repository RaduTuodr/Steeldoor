package org.example.steeldoor.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.steeldoor.dto.LoginRequestDTO;
import org.example.steeldoor.dto.LoginResponseDTO;
import org.example.steeldoor.dto.RegisterRequestDTO;
import org.example.steeldoor.dto.RegisterResponseDTO;
import org.example.steeldoor.model.User;
import org.example.steeldoor.service.AuthService;
import org.example.steeldoor.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(@RequestBody @Valid RegisterRequestDTO registerRequestDTO) {
        RegisterResponseDTO response = authService.register(registerRequestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO loginRequestDTO) {
        LoginResponseDTO response = authService.login(loginRequestDTO);
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(@RequestBody @Valid Integer userId) {
        User user = userService.findById(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
