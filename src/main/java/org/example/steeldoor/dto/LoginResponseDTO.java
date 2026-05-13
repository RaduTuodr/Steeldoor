package org.example.steeldoor.dto;

import lombok.Builder;
import lombok.Data;
import org.example.steeldoor.model.User;

@Data
@Builder
public class LoginResponseDTO {
    private Integer id;
    private String token;
    private User user;
}
