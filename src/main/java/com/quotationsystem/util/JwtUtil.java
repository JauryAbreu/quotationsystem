package com.quotationsystem.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

  private static final String SECRET_KEY = "mY5f8Zp3rT9xL2qV0sNd7BhJwKmCeYXA";
  private static final long EXPIRATION_TIME = 3600000; // 1 hour in milliseconds
  private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

  public String generateToken(String username, String role) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("role", role);
    String token =
        Jwts.builder()
            .setClaims(claims)
            .setSubject(username)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
            .compact();
    logger.debug("Generated JWT for user: {}", username);
    return token;
  }

  public Claims extractClaims(String token) {
    try {
      Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
      logger.debug("Extracted claims: {}", claims);
      return claims;
    } catch (Exception e) {
      logger.error("Failed to extract claims from token: {}", e.getMessage());
      throw e;
    }
  }

  public String extractUsername(String token) {
    String username = extractClaims(token).getSubject();
    logger.debug("Extracted username: {}", username);
    return username;
  }

  public String extractRole(String token) {
    String role = (String) extractClaims(token).get("role");
    logger.debug("Extracted role: {}", role);
    return role;
  }

  public boolean isTokenExpired(String token) {
    boolean expired = extractClaims(token).getExpiration().before(new Date());
    logger.debug("Token expired: {}", expired);
    return expired;
  }

  public boolean validateToken(String token, String username) {
    try {
      boolean valid = username.equals(extractUsername(token)) && !isTokenExpired(token);
      logger.debug("Token validation for user {}: {}", username, valid);
      return valid;
    } catch (Exception e) {
      logger.error("Token validation failed: {}", e.getMessage());
      return false;
    }
  }
}
