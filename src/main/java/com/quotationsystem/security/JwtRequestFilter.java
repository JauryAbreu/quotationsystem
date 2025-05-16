package com.quotationsystem.security;

import com.quotationsystem.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

  private static final Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);

  @Autowired
  private JwtUtil jwtUtil;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
          throws ServletException, IOException {
    final String authorizationHeader = request.getHeader("Authorization");
    String username = null;
    String jwt = null;

    logger.debug("Authorization Header: {}", authorizationHeader);

    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
      jwt = authorizationHeader.substring(7);
      try {
        username = jwtUtil.extractUsername(jwt);
        logger.debug("Extracted Username: {}", username);
      } catch (Exception e) {
        logger.error("Failed to extract username from JWT: {}", e.getMessage());
      }
    } else {
      logger.debug("No Bearer token found in Authorization header");
    }

    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      if (jwtUtil.validateToken(jwt, username)) {
        String role = jwtUtil.extractRole(jwt);
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                username, null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role)));
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
        logger.debug("Authentication set for user: {}", username);
      } else {
        logger.error("JWT token validation failed for user: {}", username);
      }
    } else if (username == null) {
      logger.debug("No username extracted, proceeding with anonymous authentication");
    }

    chain.doFilter(request, response);
  }
}