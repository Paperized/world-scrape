package com.paperized.worldscrape.security;

import com.paperized.worldscrape.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class JwtService {
  private final static String SECRET_KEY = "3s6v9y$B&E)H@MbQeThWmZq4t7w!z%C*F-JaNdRfUjXn2r5u8x/A?D(G+KbPeShV";

  public String generateToken(User user) {
    Map<String, Object> map = new HashMap<>();
    map.put("roles", user.getAuthorities().stream()
      .map(GrantedAuthority::getAuthority).collect(Collectors.joining(",")));
    return generateToken(map, user);
  }

  public String generateToken(Map<String, Object> claims, User user) {
    return Jwts.builder()
      .setClaims(claims)
      .setSubject(user.getEmail())
      .setId(user.getId().toString())
      .setIssuedAt(new Date(System.currentTimeMillis()))
      .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
      .signWith(getSignKey(), SignatureAlgorithm.HS512)
      .compact();
  }

  public Claims extractAllClaims(String token) {
    return Jwts.parserBuilder()
      .setSigningKey(getSignKey())
      .build()
      .parseClaimsJws(token).getBody();
  }

  private Key getSignKey() {
    byte[] keyBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
