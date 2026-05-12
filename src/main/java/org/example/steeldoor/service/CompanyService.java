package org.example.steeldoor.service;

import lombok.RequiredArgsConstructor;
import org.example.steeldoor.config.exception.CompanyNotFoundException;
import org.example.steeldoor.dto.CompanyFilterDTO;
import org.example.steeldoor.model.Company;
import org.example.steeldoor.repository.CompanyRepository;
import org.example.steeldoor.utils.SortStrategy;
import org.example.steeldoor.utils.strategy.SortByIndustry;
import org.example.steeldoor.utils.strategy.SortByLocation;
import org.example.steeldoor.utils.strategy.SortByName;
import org.example.steeldoor.utils.strategy.SortBySize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    public Company getCompanyBySlug(String slug) {
        return companyRepository.findBySlug(slug).orElseThrow(
                () -> new CompanyNotFoundException("Slug not found")
        );
    }

    public List<Company> filterCompanies(CompanyFilterDTO companyFilter) {
        if (companyFilter == null) {
            return new ArrayList<>(companyRepository.findAll());
        }

        String sortBy = normalizeForSearch(companyFilter.getSortBy());
        String sortDir = normalizeForSearch(companyFilter.getSortDir());

        List<Company> companies = new ArrayList<>(companyRepository.findAll());

        if (companyFilter.getQuery() != null) {
            String query = companyFilter.getQuery().toLowerCase();
            companies = companies.stream()
                    .filter(c -> {
                        String name        = c.getName()        != null ? c.getName().toLowerCase()        : "";
                        String description = c.getDescription() != null ? c.getDescription().toLowerCase() : "";
                        return name.contains(query) || description.contains(query);
                    })
                    .collect(Collectors.toCollection(ArrayList::new));
        }

        if (companyFilter.getIndustry() != null) {
            String targetIndustry = companyFilter.getIndustry();
            companies = companies.stream()
                    .filter(c -> c.getIndustry().equalsIgnoreCase(targetIndustry))
                    .collect(Collectors.toCollection(ArrayList::new));
        }

        if (companyFilter.getLocation() != null) {
            String targetLocation = companyFilter.getLocation();
            companies = companies.stream()
                    .filter(c -> c.getLocation().equalsIgnoreCase(targetLocation))
                    .collect(Collectors.toCollection(ArrayList::new));
        }

        if (companyFilter.getSize() != null && !companyFilter.getSize().equalsIgnoreCase("all")) {
            int targetWeight = companySizeStringToInt(companyFilter.getSize());
            companies = companies.stream()
                    .filter(c -> c.getSize() != null && c.getSize().getWeight() == targetWeight)
                    .collect(Collectors.toCollection(ArrayList::new));
        }

        if (sortBy != null && !sortBy.equalsIgnoreCase("all")) {
            SortStrategy<Company> sortStrategy = getSortStrategy(sortBy);
            sortStrategy.sort(companies);
        }

        if ("desc".equals(sortDir)) {
            Collections.reverse(companies);
        }

        return companies;
    }

    private int companySizeStringToInt(String size) {
        System.out.println(size);
        return switch (size.toLowerCase()) {
            case "startup"    -> 1;
            case "smb"        -> 2;
            case "mid_market" -> 3;
            default           -> 4;
        };
    }

    private SortStrategy<Company> getSortStrategy(String sortBy) {
        return switch (sortBy) {
            case "name"     -> new SortByName();
            case "industry" -> new SortByIndustry();
            case "location" -> new SortByLocation();
            case "size"     -> new SortBySize();
            default         -> new SortByName();
        };
    }

    private String normalizeForSearch(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value
                .toLowerCase(Locale.ROOT)
                .replace("-", " ")
                .trim();
    }
}