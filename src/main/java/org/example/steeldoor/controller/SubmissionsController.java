package org.example.steeldoor.controller;

import lombok.RequiredArgsConstructor;
import org.example.steeldoor.model.Submission;
import org.example.steeldoor.service.SubmissionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/submission")
@RequiredArgsConstructor
public class SubmissionsController {

    private final SubmissionService submissionService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<Submission>> getUserSubmissions(@PathVariable Integer userId) {
        List<Submission> response = submissionService.getUserSubmissions(userId);
        return ResponseEntity.ok(response);
    }
}
