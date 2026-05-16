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
@Builder
public class VoteId implements Serializable {

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "submission_id")
    private Integer submissionId;
}
