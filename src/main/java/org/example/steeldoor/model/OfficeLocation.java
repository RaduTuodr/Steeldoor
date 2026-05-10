package org.example.steeldoor.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(
        name = "office_location",
        uniqueConstraints = @UniqueConstraint(name = "uq_office_location_company_city_country", columnNames = {"company_id", "city", "country"}),
        indexes = @Index(name = "idx_office_location_company_id", columnList = "company_id")
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OfficeLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "company_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_office_location_company",
                    foreignKeyDefinition = "FOREIGN KEY (company_id) REFERENCES company(id) ON DELETE CASCADE"
            )
    )
    @NotNull
    private Company company;

    @Column(nullable = false, columnDefinition = "TEXT")
    @NotBlank
    private String city;

    @Column(nullable = false, columnDefinition = "TEXT")
    @NotBlank
    private String country;
}
