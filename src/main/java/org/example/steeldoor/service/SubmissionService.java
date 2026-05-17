package org.example.steeldoor.service;

import lombok.AllArgsConstructor;
import org.example.steeldoor.config.exception.InvalidCompanySlugException;
import org.example.steeldoor.config.exception.UserNotFoundException;
import org.example.steeldoor.dto.CompanySubmissionFilterDTO;
import org.example.steeldoor.dto.SubmissionCreateDTO;
import org.example.steeldoor.dto.SubmissionResponseDTO;
import org.example.steeldoor.model.Company;
import org.example.steeldoor.model.Submission;
import org.example.steeldoor.model.User;
import org.example.steeldoor.repository.CompanyRepository;
import org.example.steeldoor.repository.SubmissionRepository;
import org.example.steeldoor.repository.UserRepository;
import org.example.steeldoor.repository.VoteRepository;
import org.example.steeldoor.repository.specification.SubmissionSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class SubmissionService {

    private SubmissionRepository submissionRepository;

    private CompanyRepository companyRepository;

    private UserRepository userRepository;

    private VoteRepository voteRepository;

    public Submission createSubmission(SubmissionCreateDTO dto, String companySlug) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User " + dto.getUserId() + " not found"));

        Company company = companyRepository.findBySlug(companySlug)
                .orElseThrow(() -> new InvalidCompanySlugException("Company " + companySlug + " not found"));

        Submission submission = new Submission();
        submission.setCompany(company);
        submission.setUser(user);
        submission.setPosition(dto.getPosition());
        submission.setOverallDifficulty(dto.getOverallDifficulty());
        submission.setOfferReceived(dto.getOfferReceived());
        submission.setCreatedAt(dto.getCreatedAt() != null ? dto.getCreatedAt() : OffsetDateTime.now());

        return submissionRepository.save(submission);
    }

    public Page<SubmissionResponseDTO> getCompanySubmissions(
            String slug,
            CompanySubmissionFilterDTO filter
    ) {
        Company company = companyRepository.findBySlug(slug)
                .orElseThrow(() ->
                        new InvalidCompanySlugException("Company " + slug + " not found"));

        Sort sort = buildSort(filter);

        Pageable pageable = PageRequest.of(
                filter.getPage(),
                filter.getPageSize(),
                sort
        );

        Specification<Submission> spec = SubmissionSpecification
                .hasCompany(company);

        if (filter.getOfferReceived() != null && filter.getOfferReceived()) {
            spec = spec.and(SubmissionSpecification.offerReceived(true));
        } else if (filter.getOfferReceived() != null) {
            spec = spec.and(SubmissionSpecification.offerReceived(false));
        }

        if (filter.getPosition() != null) {
            spec = spec.and(
                    SubmissionSpecification.positionContains(filter.getPosition())
            );
        }

        Page<Submission> submissionsPage = submissionRepository.findAll(spec, pageable);

        return submissionsPage.map(submission -> {
            boolean hasUpvoted = false;
            Integer userId = filter.getUserId();
            if (userId != null) {
                hasUpvoted = voteRepository.existsBySubmissionIdAndUserId(submission.getId(), userId);
            }
            long numberOfVotes = voteRepository.countBySubmissionId(submission.getId());

            return new SubmissionResponseDTO(submission, numberOfVotes, hasUpvoted);
        });
    }

    private Sort buildSort(CompanySubmissionFilterDTO filter) {
        String sortBy = filter.getSortBy();
        String sortDir = filter.getSortDir();

        Sort.Direction direction =
                "desc".equalsIgnoreCase(sortDir)
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC;

        return switch (sortBy) {
            case "votes" ->
                    Sort.by(direction, "voteCount");

            default ->
                    Sort.by(direction, "createdAt");
        };
    }

    public List<Submission> getUserSubmissions(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User " + userId + " not found"));

        return submissionRepository.findAllByUser(user);
    }
}
