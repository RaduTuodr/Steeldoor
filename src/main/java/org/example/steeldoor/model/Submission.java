package org.example.steeldoor.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.OffsetDateTime;

@Entity
@Table(
        name = "submission",
        indexes = {
                @Index(name = "idx_submission_user_id", columnList = "user_id"),
                @Index(name = "idx_submission_company_id", columnList = "company_id"),
                @Index(name = "idx_submission_created_at", columnList = "created_at")
        }
)
@org.hibernate.annotations.Check(constraints = "overall_difficulty IS NULL OR (overall_difficulty BETWEEN 1 AND 5)")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_submission_user",
                    foreignKeyDefinition = "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE"
            )
    )
    @NotNull
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "company_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_submission_company",
                    foreignKeyDefinition = "FOREIGN KEY (company_id) REFERENCES company(id) ON DELETE CASCADE"
            )
    )
    @NotNull
    private Company company;

    @Column(nullable = false)
    @NotBlank
    @Size(max = 255)
    private String position;

    @Column(name = "overall_difficulty")
    @Min(1)
    @Max(5)
    private Integer overallDifficulty;

    @Column(name = "offer_received")
    private Boolean offerReceived;

    @Column(nullable = false, columnDefinition = "TIMESTAMPTZ DEFAULT now()")
    @NotNull
    private OffsetDateTime createdAt;
}
