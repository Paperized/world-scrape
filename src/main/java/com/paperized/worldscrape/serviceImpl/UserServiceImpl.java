package com.paperized.worldscrape.serviceImpl;

import com.paperized.worldscrape.dto.UserDTO;
import com.paperized.worldscrape.entity.User;
import com.paperized.worldscrape.exception.EntityNotFoundException;
import com.paperized.worldscrape.repository.RoleRepository;
import com.paperized.worldscrape.repository.UserRepository;
import com.paperized.worldscrape.security.util.SecurityUtils;
import com.paperized.worldscrape.service.UserService;
import com.paperized.worldscrape.util.MapperUtil;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.paperized.worldscrape.security.AuthRole.ADMIN;

@Service
public class UserServiceImpl implements UserService {
  private final Logger logging = org.slf4j.LoggerFactory.getLogger(UserServiceImpl.class);
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;

  public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
  }

  @Override
  public UserDTO getUserByEmail(String email) {
    User user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("User"));
    logging.info("[ACTION] getUserByEmail: {}", user.getEmail());
    return MapperUtil.mapTo(user, SecurityUtils.getCurrentRoles(), UserDTO::fullUser);
  }

  @Transactional
  @Override
  public UserDTO changeRole(Long id, String role) {
    User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User"));
    String prevRole = user.getRoles().listIterator().next().getName();
    user.getRoles().clear();
    user.getRoles().add(roleRepository.findByName(role).orElseThrow(() -> new EntityNotFoundException("Role")));

    user = userRepository.save(user);
    logging.info("[ACTION] changeRole: {}|{}->{}", user.getId(), prevRole, role);
    return MapperUtil.mapTo(user, null, UserDTO::fullUser);
  }
}
