package com.paperized.worldscrape.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.paperized.worldscrape.dto.utils.Dto;
import com.paperized.worldscrape.entity.Role;
import com.paperized.worldscrape.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.sql.Timestamp;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO implements Dto<User> {
  private Long id;
  private String username;
  private String email;
  private String[] authorities;
  private Timestamp creationTime;
  private Boolean isEnabled;
  private String firstName;
  private String lastName;

  public static UserDTO fullUser(User user, Set<? extends GrantedAuthority> roles) {
    return new UserDTO(
      user.getId(),
      user.getUsername(),
      user.getEmail(),
      user.getRoles().stream().map(Role::getName).toArray(String[]::new),
      user.getCreationTime(),
      user.isEnabled(),
      user.getFirstName(),
      user.getLastName()
    );
  }

  @Override
  public User toEntity() {
    return new User(
      id,
      username,
      email,
      null,
      isEnabled,
      creationTime,
      firstName,
      lastName,
      null
    );
  }
}
