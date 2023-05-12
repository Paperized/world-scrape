package com.paperized.worldscrape.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ConstraintNamesMapper {

  @Bean(name = "constraintsTranslator")
  public Map<String, String> getConstraintNamesTranslator() {
    Map<String, String> map = new HashMap<>();
    // User
    map.put("users.uc_user_username", "uc_user_username");
    map.put("users.uc_user_email", "uc_user_email");

    // Role
    map.put("roles.uc_role_name", "uc_role_name");

    // User-Role
    map.put("user_roles.fk_userroles_user_id", "fk_user_id");
    map.put("user_roles.fk_userroles_role_id", "fk_role_id");

    // Product

    return map;
  }
}
