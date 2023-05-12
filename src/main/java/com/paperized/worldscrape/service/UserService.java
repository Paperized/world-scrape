package com.paperized.worldscrape.service;

import com.paperized.worldscrape.dto.UserDTO;

public interface UserService {
  UserDTO getUserByEmail(String email);
  UserDTO changeRole(Long id, String role);
}
