package com.paperized.worldscrape.controller;

import com.paperized.worldscrape.dto.auth.LoginRequestDTO;
import com.paperized.worldscrape.dto.auth.LoginResponseDTO;
import com.paperized.worldscrape.dto.auth.RegisterRequestDTO;
import com.paperized.worldscrape.dto.auth.RegisterResponseDTO;
import com.paperized.worldscrape.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
  Logger logger = LoggerFactory.getLogger(AuthController.class);
  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/login")
  public LoginResponseDTO login(@RequestBody LoginRequestDTO loginRequestDTO) {
    logger.info("[REQ] login: {}", loginRequestDTO);
    return authService.login(loginRequestDTO);
  }

  @PostMapping("/register")
  public RegisterResponseDTO register(@RequestBody RegisterRequestDTO registerRequestDTO) {
    logger.info("[REQ] register: {}", registerRequestDTO);
    return authService.register(registerRequestDTO);
  }
}















