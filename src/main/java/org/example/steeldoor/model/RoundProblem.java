package org.example.steeldoor.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.example.steeldoor.model.keys.RoundProblemId;

@Entity
@Table(
        name = "round_problem",
        indexes = @Index(name = "idx_round_problem_problem_id", columnList = "problem_id")
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoundProblem {

    @EmbeddedId
    private RoundProblemId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("roundId")
    @JoinColumn(
            name = "round_id",
            foreignKey = @ForeignKey(
                    name = "fk_round_problem_round",
                    foreignKeyDefinition = "FOREIGN KEY (round_id) REFERENCES interview_round(id) ON DELETE CASCADE"
            )
    )
    @NotNull
    private InterviewRound round;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("problemId")
    @JoinColumn(
            name = "problem_id",
            foreignKey = @ForeignKey(
                    name = "fk_round_problem_problem",
                    foreignKeyDefinition = "FOREIGN KEY (problem_id) REFERENCES problem(id) ON DELETE CASCADE"
            )
    )
    @NotNull
    private Problem problem;
}
