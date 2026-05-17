package org.example.steeldoor.controller;

import lombok.RequiredArgsConstructor;
import org.example.steeldoor.dto.CompanyFilterDTO;
import org.example.steeldoor.dto.CompanySubmissionFilterDTO;
import org.example.steeldoor.dto.SubmissionCreateDTO;
import org.example.steeldoor.dto.SubmissionResponseDTO;
import org.example.steeldoor.model.Company;
import org.example.steeldoor.model.Submission;
import org.example.steeldoor.service.CompanyService;
import org.example.steeldoor.service.SubmissionService;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/company")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    private final SubmissionService submissionService;

    @GetMapping("/{slug}")
    public ResponseEntity<Company> getCompany(@PathVariable String slug) {
        Company company = companyService.getCompanyBySlug(slug);
        return ResponseEntity.ok(company);
    }

    @PostMapping("/filter")
    public ResponseEntity<List<Company>> getAllCompanies(@RequestBody CompanyFilterDTO companyFilter) {
        List<Company> companies = companyService.filterCompanies(companyFilter);
        return ResponseEntity.ok(companies);
    }

    @PostMapping("/{slug}/submissions")
    public ResponseEntity<Submission> createSubmission(@PathVariable String slug, @RequestBody SubmissionCreateDTO submissionCreateDTO) {
        Submission submission = submissionService.createSubmission(submissionCreateDTO, slug);
        return ResponseEntity.ok(submission);
    }

    @PostMapping("/{slug}/submissions/filter")
    public ResponseEntity<PagedModel<SubmissionResponseDTO>> getAllSubmissions(@PathVariable String slug, @RequestBody CompanySubmissionFilterDTO companySubmissionFilterDTO) {
        Page<SubmissionResponseDTO> submissions = submissionService.getCompanySubmissions(slug, companySubmissionFilterDTO);
        PagedModel<SubmissionResponseDTO> response = new PagedModel<>(submissions);
        return ResponseEntity.ok(response);
    }
}
