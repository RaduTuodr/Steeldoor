package org.example.steeldoor.service;

import lombok.RequiredArgsConstructor;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;
    private final UserRepository userRepository;
    private final SubmissionRepository submissionRepository;

    @Transactional
    public UpvoteDTO vote(Integer userId, Integer submissionId) {
        Optional<Vote> vote = voteRepository.findByUserIdAndSubmissionId(userId, submissionId);

        if (vote.isPresent()) {
            voteRepository.deleteByUserIdAndSubmissionId(userId, submissionId);

            Integer noVotes = voteRepository.countBySubmissionId(submissionId);

            return UpvoteDTO.builder()
                    .totalVotes(noVotes)
                    .hasUpvoted(false)
                    .build();
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id: " + userId + " not found"));
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new SubmissionNotFoundException("Submission with id: " + submissionId + " not found"));

        VoteId voteId = VoteId.builder()
                .userId(userId)
                .submissionId(submissionId)
                .build();
        Vote newVote = Vote.builder()
                .id(voteId)
                .user(user)
                .submission(submission)
                .build();

        voteRepository.save(newVote);

        Integer noVotes = voteRepository.countBySubmissionId(submissionId);

        return UpvoteDTO.builder()
                .totalVotes(noVotes)
                .hasUpvoted(true)
                .build();
    }
}
