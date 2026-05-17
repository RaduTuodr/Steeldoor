package org.example.steeldoor.controller;

import lombok.RequiredArgsConstructor;
import org.example.steeldoor.dto.UpvoteDTO;
import org.example.steeldoor.service.VoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/vote")
@RequiredArgsConstructor
public class VoteController {

    private final VoteService voteService;

    @PostMapping("/{userId}/{submissionId}")
    public ResponseEntity<UpvoteDTO> getUpvote(@PathVariable Integer userId, @PathVariable Integer submissionId) {
        UpvoteDTO result = voteService.vote(userId, submissionId);
        return ResponseEntity.ok(result);
    }
}
