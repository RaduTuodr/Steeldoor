package org.example.steeldoor.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.steeldoor.dto.LoginRequestDTO;
import org.example.steeldoor.dto.LoginResponseDTO;
import org.example.steeldoor.dto.RegisterRequestDTO;
import org.example.steeldoor.dto.RegisterResponseDTO;
import org.example.steeldoor.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public RegisterResponseDTO register(@RequestBody @Valid RegisterRequestDTO registerRequestDTO) {
        RegisterResponseDTO response = authService.register(registerRequestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED).getBody();
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody @Valid LoginRequestDTO loginRequestDTO) {
        LoginResponseDTO response = authService.login(loginRequestDTO);
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED).getBody();
    }
}
