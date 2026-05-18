package org.example.steeldoor.service;

import org.example.steeldoor.config.exception.InterviewRoundNotFoundException;
import org.example.steeldoor.config.exception.SubmissionNotFoundException;
import org.example.steeldoor.dto.CreateInterviewRoundDTO;
import org.example.steeldoor.model.InterviewRound;
import org.example.steeldoor.model.Submission;
import org.example.steeldoor.model.enums.RoundType;
import org.example.steeldoor.repository.InterviewRoundRepository;
import org.example.steeldoor.repository.SubmissionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InterviewRoundServiceTest {

    @Mock
    private InterviewRoundRepository interviewRoundRepository;

    @Mock
    private SubmissionRepository submissionRepository;

    @InjectMocks
    private InterviewRoundService interviewRoundService;

    private Submission submission;

    @BeforeEach
    void setUp() {
        submission = Submission.builder()
                .id(1)
                .build();
    }

    @Test
    void getInterviewRound_found() {
        InterviewRound round = InterviewRound.builder().id(10).submission(submission).build();
        when(interviewRoundRepository.findById(10)).thenReturn(Optional.of(round));

        InterviewRound result = interviewRoundService.getInterviewRound(10);

        assertNotNull(result);
        assertEquals(10, result.getId());
    }

    @Test
    void getInterviewRound_notFound_throws() {
        when(interviewRoundRepository.findById(5)).thenReturn(Optional.empty());

        assertThrows(InterviewRoundNotFoundException.class, () -> interviewRoundService.getInterviewRound(5));
    }

    @Test
    void createInterviewRound_success() {
        CreateInterviewRoundDTO dto = CreateInterviewRoundDTO.builder()
                .submissionId(1)
                .roundType(RoundType.OA.getDbValue())
                .title("Title")
                .description("Desc")
                .difficulty(3)
                .durationMinutes(60)
                .orderIndex(1)
                .build();

        when(submissionRepository.findById(1)).thenReturn(Optional.of(submission));

        InterviewRound saved = InterviewRound.builder()
                .id(42)
                .submission(submission)
                .type(RoundType.OA)
                .title("Title")
                .orderIndex(1)
                .build();

        when(interviewRoundRepository.save(any(InterviewRound.class))).thenReturn(saved);

        InterviewRound result = interviewRoundService.createInterviewRound(dto);

        assertNotNull(result);
        assertEquals(42, result.getId());
        // ensure the round was attached to the submission's rounds list
        assertEquals(1, submission.getRounds().size());
        verify(interviewRoundRepository).save(any(InterviewRound.class));
    }

    @Test
    void createInterviewRound_invalidRoundType_throws() {
        CreateInterviewRoundDTO dto = CreateInterviewRoundDTO.builder()
                .submissionId(1)
                .roundType("UNKNOWN")
                .orderIndex(1)
                .build();

        when(submissionRepository.findById(1)).thenReturn(Optional.of(submission));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> interviewRoundService.createInterviewRound(dto));

        assertTrue(ex.getMessage().contains("Invalid round type"));
        verify(interviewRoundRepository, never()).save(any());
    }

    @Test
    void createInterviewRound_submissionNotFound_throws() {
        CreateInterviewRoundDTO dto = CreateInterviewRoundDTO.builder()
                .submissionId(99)
                .roundType(RoundType.OA.getDbValue())
                .orderIndex(1)
                .build();

        when(submissionRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(SubmissionNotFoundException.class, () -> interviewRoundService.createInterviewRound(dto));
        verify(interviewRoundRepository, never()).save(any());
    }

    @Test
    void getInterviewRoundsBySubmissionId_success() {
        InterviewRound r1 = InterviewRound.builder().id(1).submission(submission).orderIndex(1).build();
        when(submissionRepository.findById(1)).thenReturn(Optional.of(submission));
        when(interviewRoundRepository.findAllBySubmissionIdOrderByOrderIndexAsc(1)).thenReturn(List.of(r1));

        var results = interviewRoundService.getInterviewRoundsBySubmissionId(1);

        assertNotNull(results);
        assertEquals(1, results.size());
    }

    @Test
    void getInterviewRoundsBySubmissionId_submissionNotFound_throws() {
        when(submissionRepository.findById(123)).thenReturn(Optional.empty());

        assertThrows(SubmissionNotFoundException.class, () -> interviewRoundService.getInterviewRoundsBySubmissionId(123));
    }

    @Test
    void updateOrderIndex_success() {
        InterviewRound round = InterviewRound.builder().id(7).submission(submission).orderIndex(1).build();
        when(interviewRoundRepository.findById(7)).thenReturn(Optional.of(round));
        when(interviewRoundRepository.save(any(InterviewRound.class))).thenAnswer(invocation -> invocation.getArgument(0));

        InterviewRound updated = interviewRoundService.updateOrderIndex(7, 5);

        assertEquals(5, updated.getOrderIndex());
        verify(interviewRoundRepository).save(round);
    }
}


