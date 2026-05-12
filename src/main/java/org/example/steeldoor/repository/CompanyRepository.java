package org.example.steeldoor.repository;

import org.example.steeldoor.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Integer> {

    @Query("SELECT c FROM Company c WHERE LOWER(REPLACE(c.name, ' ', '-')) = :slug")
    Optional<Company> findBySlug(@Param("slug") String slug);
}
