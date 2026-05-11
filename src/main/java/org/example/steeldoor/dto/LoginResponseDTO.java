package org.example.steeldoor.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponseDTO {
    private Integer id;
    private String username;
    private String email;
    private String token;
}
