package org.example.steeldoor.dto;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class RegisterResponseDTO {
    private Integer id;
    private String username;
    private String email;
    private Integer roleId;
    private OffsetDateTime createdAt;
}
