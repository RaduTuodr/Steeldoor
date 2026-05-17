package org.example.steeldoor.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateInterviewRoundDTO {
    @NotNull(message = "Submission ID cannot be null")
    private Integer submissionId;

    @NotBlank(message = "Round type cannot be blank")
    private String roundType;

    @Size(max = 255, message = "Title cannot exceed 255 characters")
    private String title;

    private String description;

    @Min(value = 1, message = "Difficulty must be at least 1")
    @Max(value = 5, message = "Difficulty cannot exceed 5")
    private Integer difficulty;

    @Min(value = 1, message = "Duration must be at least 1 minute")
    @Max(value = 480, message = "Duration cannot exceed 480 minutes")
    private Integer durationMinutes;

    @NotNull(message = "Order index cannot be null")
    @Min(value = 1, message = "Order index must be at least 1")
    @Max(value = 20, message = "Order index cannot exceed 20")
    private Integer orderIndex;
}
