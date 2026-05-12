package org.example.steeldoor.controller;

import lombok.RequiredArgsConstructor;
import org.example.steeldoor.dto.CompanyFilterDTO;
import org.example.steeldoor.model.Company;
import org.example.steeldoor.service.CompanyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/company")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

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
}
