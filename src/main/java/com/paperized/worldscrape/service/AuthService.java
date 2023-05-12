package com.paperized.worldscrape.service;

import com.paperized.worldscrape.dto.auth.LoginRequestDTO;
import com.paperized.worldscrape.dto.auth.LoginResponseDTO;
import com.paperized.worldscrape.dto.auth.RegisterRequestDTO;
import com.paperized.worldscrape.dto.auth.RegisterResponseDTO;

public interface AuthService {
    RegisterResponseDTO register(RegisterRequestDTO registerRequestDTO);
    LoginResponseDTO login(LoginRequestDTO loginRequestDTO);
}
