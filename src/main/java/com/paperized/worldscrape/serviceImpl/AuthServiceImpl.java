package com.paperized.worldscrape.serviceImpl;

import com.paperized.worldscrape.dto.auth.LoginRequestDTO;
import com.paperized.worldscrape.dto.auth.LoginResponseDTO;
import com.paperized.worldscrape.dto.auth.RegisterRequestDTO;
import com.paperized.worldscrape.dto.auth.RegisterResponseDTO;
import com.paperized.worldscrape.entity.Role;
import com.paperized.worldscrape.entity.User;
import com.paperized.worldscrape.repository.RoleRepository;
import com.paperized.worldscrape.repository.UserRepository;
import com.paperized.worldscrape.security.AuthRole;
import com.paperized.worldscrape.security.JwtService;
import com.paperized.worldscrape.service.AuthService;
import com.paperized.worldscrape.util.MapperUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
  private Logger logging = LoggerFactory.getLogger(AuthServiceImpl.class);
  private final AuthenticationManager authenticationManager;
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;

  public AuthServiceImpl(AuthenticationManager authenticationManager, UserRepository userRepository,
                         RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
    this.authenticationManager = authenticationManager;
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtService = jwtService;
  }

  @Override
  public RegisterResponseDTO register(RegisterRequestDTO registerRequestDTO) {
    User user = MapperUtil.mapTo(registerRequestDTO, RegisterRequestDTO::toUser);
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    user.setEnabled(true);

    Role role = roleRepository.findByName(AuthRole.USER).orElseThrow(() -> new RuntimeException("Unexpected error, base user role not found"));
    user.getRoles().add(role);

    RegisterResponseDTO registerResponseDTO = new RegisterResponseDTO(userRepository.save(user).getId());
    logging.info("[ACTION] userRegistered: {}", registerResponseDTO.getId());
    return registerResponseDTO;
  }

  @Override
  public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
    Authentication authentication = authenticationManager.authenticate(
      new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getPassword()));

    User user = (User) authentication.getPrincipal();
    LoginResponseDTO loginResponseDTO = new LoginResponseDTO(jwtService.generateToken(user));
    logging.info("[ACTION] userLogin: {}", user.getId());
    return loginResponseDTO;
  }
}
