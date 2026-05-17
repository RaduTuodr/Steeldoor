package org.example.steeldoor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterviewRoundResponseDTO {
    private Integer id;
    private Integer submissionId;
    private String roundType;
    private String title;
    private String description;
    private Integer difficulty;
    private Integer durationMinutes;
    private Integer orderIndex;
}

