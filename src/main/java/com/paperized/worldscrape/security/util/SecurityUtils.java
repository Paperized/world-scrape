package com.paperized.worldscrape.security.util;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class SecurityUtils {
  public static void setCurrentAuthentication(AuthenticatedUser authenticatedUser, HttpServletRequest request) {
    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
      authenticatedUser,
      null,
      authenticatedUser.getAuthorities()
    );

    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    SecurityContextHolder.getContext().setAuthentication(authToken);
  }

  public static void setCurrentAuthentication(Long id, String email, String[] roles, HttpServletRequest request) {
    Set<GrantedAuthority> mappedRoles = Arrays.stream(roles)
      .map(SimpleGrantedAuthority::new)
      .collect(Collectors.toSet());
    AuthenticatedUser authenticatedUser = new AuthenticatedUser(id, email, mappedRoles);
    SecurityUtils.setCurrentAuthentication(authenticatedUser, request);
  }

  public static AuthenticatedUser getCurrentUser() {
    Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
    return currentAuth != null ? (AuthenticatedUser) currentAuth.getPrincipal() : null;
  }

  public static Set<? extends GrantedAuthority> getCurrentRoles() {
    Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
    return currentAuth != null ? (Set<? extends GrantedAuthority>) ((AuthenticatedUser) currentAuth.getPrincipal()).getAuthorities() : Set.of();
  }
}
