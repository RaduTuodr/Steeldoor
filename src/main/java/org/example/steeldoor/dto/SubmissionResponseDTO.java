package org.example.steeldoor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.example.steeldoor.model.Submission;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@Builder
public class SubmissionResponseDTO {
    private Submission submission;
    private long numberOfVotes;
    private boolean hasUpvoted;
}
