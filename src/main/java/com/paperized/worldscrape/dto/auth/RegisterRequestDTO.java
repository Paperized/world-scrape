package com.paperized.worldscrape.dto.auth;

import com.paperized.worldscrape.entity.User;
import lombok.Data;

import java.sql.Timestamp;
import java.time.Instant;

@Data
public class RegisterRequestDTO {
    private String username;
    private String email;
    private String password;

    public static User toUser(RegisterRequestDTO registerRequestDTO) {
        User user = new User();
        user.setUsername(registerRequestDTO.getUsername());
        user.setEmail(registerRequestDTO.getEmail());
        user.setPassword(registerRequestDTO.getPassword());
        user.setCreationTime(Timestamp.from(Instant.now()));
        return user;
    }
}
