package org.example.steeldoor.service;

import org.example.steeldoor.config.exception.SubmissionNotFoundException;
import org.example.steeldoor.config.exception.UserNotFoundException;
import org.example.steeldoor.dto.UpvoteDTO;
import org.example.steeldoor.model.Submission;
import org.example.steeldoor.model.User;
import org.example.steeldoor.model.Vote;
import org.example.steeldoor.model.keys.VoteId;
import org.example.steeldoor.repository.SubmissionRepository;
import org.example.steeldoor.repository.UserRepository;
import org.example.steeldoor.repository.VoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VoteServiceTest {

    @Mock
    private VoteRepository voteRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SubmissionRepository submissionRepository;

    @InjectMocks
    private VoteService voteService;

    private User user;
    private Submission submission;

    @BeforeEach
    void setUp() {
        user = User.builder().id(1).build();
        submission = Submission.builder().id(2).build();
    }

    @Test
    void vote_whenAlreadyPresent_removesVoteAndReturnsFalse() {
        Vote existing = Vote.builder().id(VoteId.builder().userId(1).submissionId(2).build()).build();
        when(voteRepository.findByUserIdAndSubmissionId(1, 2)).thenReturn(Optional.of(existing));
        when(voteRepository.countBySubmissionId(2)).thenReturn(3);

        UpvoteDTO result = voteService.vote(1, 2);

        assertFalse(result.isHasUpvoted());
        assertEquals(3, result.getTotalVotes());
        verify(voteRepository).deleteByUserIdAndSubmissionId(1, 2);
    }

    @Test
    void vote_userNotFound_throws() {
        when(voteRepository.findByUserIdAndSubmissionId(1, 2)).thenReturn(Optional.empty());
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> voteService.vote(1, 2));
    }

    @Test
    void vote_submissionNotFound_throws() {
        when(voteRepository.findByUserIdAndSubmissionId(1, 2)).thenReturn(Optional.empty());
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(submissionRepository.findById(2)).thenReturn(Optional.empty());

        assertThrows(SubmissionNotFoundException.class, () -> voteService.vote(1, 2));
    }

    @Test
    void vote_success_createsVoteAndReturnsTrue() {
        when(voteRepository.findByUserIdAndSubmissionId(1, 2)).thenReturn(Optional.empty());
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(submissionRepository.findById(2)).thenReturn(Optional.of(submission));
        when(voteRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(voteRepository.countBySubmissionId(2)).thenReturn(5);

        var result = voteService.vote(1, 2);

        assertTrue(result.isHasUpvoted());
        assertEquals(5, result.getTotalVotes());
        verify(voteRepository).save(any());
    }
}


