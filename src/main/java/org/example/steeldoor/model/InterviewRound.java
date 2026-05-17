package org.example.steeldoor.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.example.steeldoor.model.enums.RoundType;

@Entity
@Table(
        name = "interview_round",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_interview_round_submission_order",
                columnNames = {"submission_id", "order_index"}
        ),
        indexes = @Index(name = "idx_interview_round_submission_id", columnList = "submission_id")
)
@org.hibernate.annotations.Check(
        constraints = "(difficulty IS NULL OR difficulty BETWEEN 1 AND 5) AND (duration_minutes IS NULL OR duration_minutes BETWEEN 1 AND 480) AND order_index BETWEEN 1 AND 20"
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterviewRound {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "submission_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_interview_round_submission",
                    foreignKeyDefinition = "FOREIGN KEY (submission_id) REFERENCES submission(id) ON DELETE CASCADE"
            )
    )
    @NotNull
    private Submission submission;

    @Column(nullable = false, columnDefinition = "TEXT")
    @NotNull
    @Convert(converter = RoundTypeConverter.class)
    private RoundType type;

    @Column(columnDefinition = "TEXT")
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column
    @Min(1)
    @Max(5)
    private Integer difficulty;

    @Column(name = "duration_minutes")
    @Min(1)
    @Max(480)
    private Integer durationMinutes;

    @Column(name = "order_index", nullable = false)
    @NotNull
    @Min(1)
    @Max(20)
    private Integer orderIndex;
}
