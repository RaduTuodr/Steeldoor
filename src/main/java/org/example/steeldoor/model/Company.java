package org.example.steeldoor.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.example.steeldoor.model.enums.CompanySize;

@Entity
@Table(
        name = "company",
        uniqueConstraints = @UniqueConstraint(name = "uq_company_name", columnNames = {"name"}),
        indexes = @Index(name = "idx_company_name", columnList = "name")
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, columnDefinition = "TEXT")
    @NotBlank
    private String name;

    @Column(name = "logo_url", columnDefinition = "TEXT")
    private String logoUrl;

    @Column(columnDefinition = "TEXT")
    private String website;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String industry;

    @Column(columnDefinition = "TEXT")
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(name = "company_size")
    private CompanySize size;
}
