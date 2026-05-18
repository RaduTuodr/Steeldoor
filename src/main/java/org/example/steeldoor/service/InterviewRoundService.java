package org.example.steeldoor.service;

import lombok.RequiredArgsConstructor;
import org.example.steeldoor.config.exception.InterviewRoundNotFoundException;
import org.example.steeldoor.config.exception.SubmissionNotFoundException;
import org.example.steeldoor.dto.CreateInterviewRoundDTO;
import org.example.steeldoor.model.InterviewRound;
import org.example.steeldoor.model.Submission;
import org.example.steeldoor.model.enums.RoundType;
import org.example.steeldoor.repository.InterviewRoundRepository;
import org.example.steeldoor.repository.SubmissionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InterviewRoundService {

    private final InterviewRoundRepository interviewRoundRepository;
    private final SubmissionRepository submissionRepository;

    public InterviewRound getInterviewRound(Integer id) {
        return interviewRoundRepository.findById(id)
                .orElseThrow(() -> new InterviewRoundNotFoundException("Interview round not found"));
    }

    @Transactional
    public InterviewRound createInterviewRound(CreateInterviewRoundDTO dto) {
        Submission submission = submissionRepository.findById(dto.getSubmissionId()).orElseThrow(
                () -> new SubmissionNotFoundException("Submission not found")
        );

        RoundType type;
        try {
            type = RoundType.fromDbValue(dto.getRoundType());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid round type: " + dto.getRoundType());
        }

        InterviewRound round = InterviewRound.builder()
                .submission(submission)
                .type(type)
                .title(dto.getTitle())
                .description(dto.getDescription())
                .difficulty(dto.getDifficulty())
                .durationMinutes(dto.getDurationMinutes())
                .orderIndex(dto.getOrderIndex())
                .build();

        submission.getRounds().add(round);

        return interviewRoundRepository.save(round);
    }

    public List<InterviewRound> getInterviewRoundsBySubmissionId(Integer submissionId) {
        submissionRepository.findById(submissionId).orElseThrow(
                () -> new SubmissionNotFoundException("Submission not found")
        );

        return interviewRoundRepository.findAllBySubmissionIdOrderByOrderIndexAsc(submissionId);
    }

    public InterviewRound updateOrderIndex(Integer roundId, Integer orderIndex) {
        InterviewRound interviewRound = getInterviewRound(roundId);
        interviewRound.setOrderIndex(orderIndex);

        return interviewRoundRepository.save(interviewRound);
    }
}
