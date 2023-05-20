package com.paperized.worldscrape.controller;

import com.paperized.worldscrape.dto.UserDTO;
import com.paperized.worldscrape.security.util.IsAuthenticated;
import com.paperized.worldscrape.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/users")
public class UserController {
  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @IsAuthenticated
  @GetMapping("/current")
  public UserDTO getCurrentUser(Authentication authentication) {
    return userService.getUserByEmail(authentication.getName());
  }

  @PostMapping("/test-api/{id}/change-role")
  public UserDTO changeRole(@PathVariable Long id, ChangeRoleDTO changeRoleDTO) {
    return userService.changeRole(id, changeRoleDTO.role());
  }

  private record ChangeRoleDTO(String role) { }
}
