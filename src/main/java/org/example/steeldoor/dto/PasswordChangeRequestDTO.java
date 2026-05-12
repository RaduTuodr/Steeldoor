package org.example.steeldoor.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordChangeRequestDTO {
    @NotNull(message = "userId is required")
    private Integer userId;

    @NotBlank(message = "phoneNumber is required")
    private String phoneNumber;
}
