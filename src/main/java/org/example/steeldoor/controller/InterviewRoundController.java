package org.example.steeldoor.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.steeldoor.dto.CreateInterviewRoundDTO;
import org.example.steeldoor.dto.InterviewRoundResponseDTO;
import org.example.steeldoor.model.InterviewRound;
import org.example.steeldoor.service.InterviewRoundService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/round")
@RequiredArgsConstructor
public class InterviewRoundController {

    private final InterviewRoundService interviewRoundService;

    @GetMapping("/{id}")
    public ResponseEntity<InterviewRoundResponseDTO> getInterviewRound(@PathVariable Integer id) {
        InterviewRound result = interviewRoundService.getInterviewRound(id);
        return ResponseEntity.ok(mapToDto(result));
    }

    @PostMapping
    public ResponseEntity<InterviewRoundResponseDTO> createInterviewRound(@Valid @RequestBody CreateInterviewRoundDTO interviewRoundDTO) {
        InterviewRound result = interviewRoundService.createInterviewRound(interviewRoundDTO);
        URI location = URI.create("/api/round/" + result.getId());
        return ResponseEntity.created(location).body(mapToDto(result));
    }

    @GetMapping
    public ResponseEntity<List<InterviewRoundResponseDTO>> getAllInterviewRoundsBySubmissionId(@RequestParam("submissionId") Integer submissionId) {
        List<InterviewRound> results = interviewRoundService.getInterviewRoundsBySubmissionId(submissionId);
        List<InterviewRoundResponseDTO> dtos = results.stream()
                .map(this::mapToDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    private InterviewRoundResponseDTO mapToDto(InterviewRound round) {
        return InterviewRoundResponseDTO.builder()
                .id(round.getId())
                .submissionId(round.getSubmission().getId())
                .roundType(round.getType().getDbValue())
                .title(round.getTitle())
                .description(round.getDescription())
                .difficulty(round.getDifficulty())
                .durationMinutes(round.getDurationMinutes())
                .orderIndex(round.getOrderIndex())
                .build();
    }
}
