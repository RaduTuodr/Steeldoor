package org.example.steeldoor.model.keys;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class RoundProblemId implements Serializable {

    @Column(name = "round_id")
    private Integer roundId;

    @Column(name = "problem_id")
    private Integer problemId;
}
