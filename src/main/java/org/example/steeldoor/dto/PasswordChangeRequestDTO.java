package org.example.steeldoor.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
