package org.example.steeldoor.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordChangeConfirmDTO {
    @NotNull(message = "userId is required")
    private Integer userId;

    @NotBlank(message = "code is required")
    private String code;

    @NotBlank(message = "newPassword is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String newPassword;
}
