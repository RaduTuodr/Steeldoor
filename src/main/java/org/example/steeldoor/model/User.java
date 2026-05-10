package org.example.steeldoor.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.OffsetDateTime;

@Entity
@Table(
        name = "users",
        indexes = {
                @Index(name = "idx_user_email", columnList = "email"),
                @Index(name = "idx_user_role_id", columnList = "role_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, columnDefinition = "TEXT")
    @NotBlank
    private String username;

    @Column(nullable = false, unique = true, columnDefinition = "TEXT")
    @NotBlank
    @Email
    private String email;

    @Column(name = "password_hash", nullable = false, columnDefinition = "TEXT")
    @NotBlank
    private String passwordHash;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "role_id",
            foreignKey = @ForeignKey(
                    name = "fk_user_role",
                    foreignKeyDefinition = "FOREIGN KEY (role_id) REFERENCES role(id) ON DELETE SET NULL"
            )
    )
    private Role role;

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMPTZ DEFAULT now()")
    @NotNull
    private OffsetDateTime createdAt;
}
