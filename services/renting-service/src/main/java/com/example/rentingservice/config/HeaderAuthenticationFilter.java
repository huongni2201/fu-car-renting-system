package com.example.rentingservice.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@Component
public class HeaderAuthenticationFilter extends OncePerRequestFilter {

  private static final String USER_ID_HEADER = "X-User-Id";
  private static final String USER_EMAIL_HEADER = "X-User-Email";
  private static final String USER_ROLE_HEADER = "X-User-Role";

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain
  ) throws ServletException, IOException {
    String role = request.getHeader(USER_ROLE_HEADER);

    if (role != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      String userId = request.getHeader(USER_ID_HEADER);
      String email = request.getHeader(USER_EMAIL_HEADER);
      String principal = userId != null && !"0".equals(userId) ? userId : email;

      UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
          principal,
          null,
          List.of(new SimpleGrantedAuthority(role.toUpperCase(Locale.ROOT)))
      );
      authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
      SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    try {
      filterChain.doFilter(request, response);
    } finally {
      SecurityContextHolder.clearContext();
    }
  }
}
