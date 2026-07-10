package com.example.apigateway.auth;

import com.example.apigateway.auth.dto.AuthenticatedUser;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationGlobalFilter implements GlobalFilter, Ordered {

  private static final String BEARER_PREFIX = "Bearer ";
  private static final String USER_ID_HEADER = "X-User-Id";
  private static final String USER_EMAIL_HEADER = "X-User-Email";
  private static final String USER_ROLE_HEADER = "X-User-Role";

  private final JwtService jwtService;

  public JwtAuthenticationGlobalFilter(JwtService jwtService) {
    this.jwtService = jwtService;
  }

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    ServerHttpRequest request = stripUserHeaders(exchange.getRequest());

    if (isPublicEndpoint(request)) {
      return chain.filter(exchange.mutate().request(request).build());
    }

    String authorization = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
    if (authorization == null || !authorization.startsWith(BEARER_PREFIX)) {
      exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
      return exchange.getResponse().setComplete();
    }

    try {
      AuthenticatedUser user = jwtService.parseToken(authorization.substring(BEARER_PREFIX.length()));
      ServerHttpRequest authenticatedRequest = request.mutate()
          .headers(headers -> {
            headers.set(USER_ID_HEADER, user.userId() != null ? String.valueOf(user.userId()) : "0");
            headers.set(USER_EMAIL_HEADER, user.email());
            headers.set(USER_ROLE_HEADER, user.role());
          })
          .build();

      return chain.filter(exchange.mutate().request(authenticatedRequest).build());
    } catch (RuntimeException exception) {
      exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
      return exchange.getResponse().setComplete();
    }
  }

  @Override
  public int getOrder() {
    return Ordered.HIGHEST_PRECEDENCE;
  }

  private boolean isPublicEndpoint(ServerHttpRequest request) {
    String path = request.getURI().getPath();
    HttpMethod method = request.getMethod();

    return (HttpMethod.POST.equals(method) && "/api/v1/auth/login".equals(path))
        || (HttpMethod.POST.equals(method) && "/api/v1/customers/register".equals(path))
        || (HttpMethod.GET.equals(method) && path.startsWith("/api/v1/cars"));
  }

  private ServerHttpRequest stripUserHeaders(ServerHttpRequest request) {
    return request.mutate()
        .headers(headers -> {
          headers.remove(USER_ID_HEADER);
          headers.remove(USER_EMAIL_HEADER);
          headers.remove(USER_ROLE_HEADER);
        })
        .build();
  }
}
