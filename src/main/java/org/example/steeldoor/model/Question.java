package org.example.steeldoor.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(
        name = "question",
        indexes = @Index(name = "idx_question_category", columnList = "category")
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Column(nullable = false, unique = true, columnDefinition = "TEXT")
    private String content;

    @Column(columnDefinition = "TEXT")
    private String category;
}
