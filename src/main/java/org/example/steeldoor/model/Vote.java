package org.example.steeldoor.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.example.steeldoor.model.keys.VoteId;

@Entity
@Table(
        name = "vote",
        indexes = {
                @Index(name = "idx_vote_submission_id", columnList = "submission_id"),
                @Index(name = "idx_vote_user_id", columnList = "user_id")
        }
)
@org.hibernate.annotations.Check(constraints = "value IN (1, -1)")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vote {

    @EmbeddedId
    private VoteId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(
            name = "user_id",
            foreignKey = @ForeignKey(
                    name = "fk_vote_user",
                    foreignKeyDefinition = "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE"
            )
    )
    @NotNull
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("submissionId")
    @JoinColumn(
            name = "submission_id",
            foreignKey = @ForeignKey(
                    name = "fk_vote_submission",
                    foreignKeyDefinition = "FOREIGN KEY (submission_id) REFERENCES submission(id) ON DELETE CASCADE"
            )
    )
    @NotNull
    private Submission submission;

    @Column(nullable = false, columnDefinition = "SMALLINT")
    @NotNull
    private Short value;
}
