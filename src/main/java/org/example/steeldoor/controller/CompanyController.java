package org.example.steeldoor.controller;

import lombok.RequiredArgsConstructor;
import org.example.steeldoor.dto.CompanyFilterDTO;
import org.example.steeldoor.dto.SubmissionCreateDTO;
import org.example.steeldoor.model.Company;
import org.example.steeldoor.model.Submission;
import org.example.steeldoor.service.CompanyService;
import org.example.steeldoor.service.SubmissionService;
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
}
