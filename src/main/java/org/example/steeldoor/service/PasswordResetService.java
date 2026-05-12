package org.example.steeldoor.service;

import lombok.AllArgsConstructor;
import org.example.steeldoor.config.exception.CodeExpiredException;
import org.example.steeldoor.config.exception.InvalidCodeException;
import org.example.steeldoor.config.exception.UserNotFoundException;
import org.example.steeldoor.dto.PasswordChangeConfirmDTO;
import org.example.steeldoor.dto.PasswordChangeRequestDTO;
import org.example.steeldoor.model.Code;
import org.example.steeldoor.model.User;
import org.example.steeldoor.repository.CodeRepository;
import org.example.steeldoor.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Date;

@Service
@AllArgsConstructor
public class PasswordResetService {

    private final MessageService messageService;
    private final UserRepository userRepository;
    private final CodeRepository codeRepository;

    private final PasswordEncoder passwordEncoder;
    private final SecureRandom random = new SecureRandom();

    private final int MINUTE = 60 * 1000;

    public void passwordChangeRequest(PasswordChangeRequestDTO request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User with ID " + request.getUserId() + " not found"));

        Code code = new Code();
        code.setCode(generateCode());
        code.setUser(user);
        code.setExpirationDate(new Date(System.currentTimeMillis() + 5 * MINUTE));
        codeRepository.save(code);

        messageService.sendMessage(request.getPhoneNumber(), code.getCode());
    }

    public void passwordChangeConfirm(PasswordChangeConfirmDTO request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User with ID " + request.getUserId() + " not found"));

        Code code = codeRepository.findTopByUserOrderByExpirationDateDesc(user)
                .orElseThrow(() -> new IllegalStateException("No code found for user with ID " + request.getUserId()));

        if (!code.getCode().equals(request.getCode())) { throw new InvalidCodeException("Invalid code"); }
        if (code.getExpirationDate().before(new Date())) { throw new CodeExpiredException("Code has expired"); }

        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        codeRepository.delete(code);
    }

    private String generateCode() {
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }
}
