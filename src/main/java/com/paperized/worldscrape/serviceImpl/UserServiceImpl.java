package com.paperized.worldscrape.serviceImpl;

import com.paperized.worldscrape.dto.UserDTO;
import com.paperized.worldscrape.entity.User;
import com.paperized.worldscrape.exception.EntityNotFoundException;
import com.paperized.worldscrape.repository.RoleRepository;
import com.paperized.worldscrape.repository.UserRepository;
import com.paperized.worldscrape.security.util.SecurityUtils;
import com.paperized.worldscrape.service.UserService;
import com.paperized.worldscrape.util.MapperUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.paperized.worldscrape.security.AuthRole.ADMIN;

@Service
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;

  public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
  }

  @Override
  public UserDTO getUserByEmail(String email) {
    User user = userRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);
    return MapperUtil.mapTo(user, SecurityUtils.getCurrentRoles(), UserDTO::fullUser);
  }

  @Transactional
  @Override
  public UserDTO changeRole(Long id, String role) {
    User user = userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    user.getRoles().clear();
    user.getRoles().add(roleRepository.findByName(ADMIN).orElseThrow());
    return MapperUtil.mapTo(userRepository.save(user), null, UserDTO::fullUser);
  }
}
