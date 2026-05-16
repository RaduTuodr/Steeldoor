package org.example.steeldoor.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.steeldoor.dto.*;
import org.example.steeldoor.model.User;
import org.example.steeldoor.service.AuthService;
import org.example.steeldoor.service.PasswordResetService;
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
    private final PasswordResetService passwordResetService;

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

    @PostMapping("/password/request")
    public void requestPasswordChange(@RequestBody @Valid PasswordChangeRequestDTO passwordChangeRequestDTO) {
        System.out.println(passwordChangeRequestDTO);
        passwordResetService.passwordChangeRequest(passwordChangeRequestDTO);
        System.out.println(passwordChangeRequestDTO.getPhoneNumber());
    }

    @PostMapping("/password/confirm")
    public void confirmPasswordChange(@RequestBody @Valid PasswordChangeConfirmDTO passwordChangeConfirmDTO) {
        passwordResetService.passwordChangeConfirm(passwordChangeConfirmDTO);
    }
}
