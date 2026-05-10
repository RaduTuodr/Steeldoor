package org.example.steeldoor.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.example.steeldoor.model.keys.RoundQuestionId;

@Entity
@Table(
        name = "round_question",
        indexes = @Index(name = "idx_round_question_question_id", columnList = "question_id")
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoundQuestion {

    @EmbeddedId
    private RoundQuestionId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("roundId")
    @JoinColumn(
            name = "round_id",
            foreignKey = @ForeignKey(
                    name = "fk_round_question_round",
                    foreignKeyDefinition = "FOREIGN KEY (round_id) REFERENCES interview_round(id) ON DELETE CASCADE"
            )
    )
    @NotNull
    private InterviewRound round;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("questionId")
    @JoinColumn(
            name = "question_id",
            foreignKey = @ForeignKey(
                    name = "fk_round_question_question",
                    foreignKeyDefinition = "FOREIGN KEY (question_id) REFERENCES question(id) ON DELETE CASCADE"
            )
    )
    @NotNull
    private Question question;
}
