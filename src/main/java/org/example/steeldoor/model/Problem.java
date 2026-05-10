package org.example.steeldoor.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.example.steeldoor.model.enums.ProblemDifficulty;

@Entity
@Table(
        name = "problem",
        uniqueConstraints = @UniqueConstraint(name = "uq_problem_name_platform", columnNames = {"name", "platform"}),
        indexes = @Index(name = "idx_problem_platform", columnList = "platform")
)
@org.hibernate.annotations.Check(constraints = "difficulty IS NULL OR difficulty IN ('Easy', 'Medium', 'Hard')")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Problem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Column(nullable = false, columnDefinition = "TEXT")
    private String name;

    @Column(columnDefinition = "TEXT")
    private String platform;

    @Column(columnDefinition = "TEXT")
    private String link;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "TEXT")
    private ProblemDifficulty difficulty;
}
