package org.example.steeldoor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
@AllArgsConstructor
public class SubmissionCreateDTO {
    private Integer userId;
    private String position;
    private Integer rating;
    private Boolean offerReceived;
    private OffsetDateTime createdAt;
}
