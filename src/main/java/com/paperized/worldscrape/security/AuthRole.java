package com.paperized.worldscrape.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class AuthRole {
  public static final String USER = "ROLE_USER";
  public static final String ADMIN = "ROLE_ADMIN";

  public static final String AUTH_ROLE_USER = "hasRole('USER')";
  public static final String AUTH_ROLE_ADMIN = "hasRole('ADMIN')";

  public static SimpleGrantedAuthority SIMPLE_ROLE_USER = new SimpleGrantedAuthority(USER);
  public static SimpleGrantedAuthority SIMPLE_ROLE_ADMIN = new SimpleGrantedAuthority(ADMIN);
}
