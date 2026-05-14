package org.example.steeldoor.service;

import lombok.AllArgsConstructor;
import org.example.steeldoor.config.exception.InvalidCompanySlugException;
import org.example.steeldoor.config.exception.UserNotFoundException;
import org.example.steeldoor.dto.SubmissionCreateDTO;
import org.example.steeldoor.model.Company;
import org.example.steeldoor.model.Submission;
import org.example.steeldoor.model.User;
import org.example.steeldoor.repository.CompanyRepository;
import org.example.steeldoor.repository.SubmissionRepository;
import org.example.steeldoor.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
@AllArgsConstructor
public class SubmissionService {

    private SubmissionRepository submissionRepository;

    private CompanyRepository companyRepository;

    private UserRepository userRepository;

    public Submission createSubmission(SubmissionCreateDTO dto, String companySlug) {
        System.out.println("User " + dto.getUserId() + " created");
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User " + dto.getUserId() + " not found"));

        System.out.println("User " + dto.getUserId() + " created");

        Company company = companyRepository.findBySlug(companySlug)
                .orElseThrow(() -> new InvalidCompanySlugException("Company " + companySlug + " not found"));

        System.out.println("Creating new submission");

        Submission submission = new Submission();
        submission.setCompany(company);
        submission.setUser(user);
        submission.setPosition(dto.getPosition());
        submission.setOverallDifficulty(dto.getOverallDifficulty());
        submission.setOfferReceived(dto.getOfferReceived());
        submission.setCreatedAt(dto.getCreatedAt() != null ? dto.getCreatedAt() : OffsetDateTime.now());

        return submissionRepository.save(submission);
    }
}
