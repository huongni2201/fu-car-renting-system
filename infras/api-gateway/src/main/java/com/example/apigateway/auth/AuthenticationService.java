package com.example.apigateway.auth;

import com.example.apigateway.auth.dto.AuthenticatedUser;
import com.example.apigateway.auth.dto.LoginRequest;
import com.example.apigateway.auth.dto.LoginResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Service
public class AuthenticationService {

  private final WebClient.Builder webClientBuilder;
  private final JwtService jwtService;

  @Value("${app.admin.email}")
  private String adminEmail;

  @Value("${app.admin.password}")
  private String adminPassword;

  @Value("${app.internal-secret}")
  private String internalSecret;

  public AuthenticationService(WebClient.Builder webClientBuilder, JwtService jwtService) {
    this.webClientBuilder = webClientBuilder;
    this.jwtService = jwtService;
  }

  public Mono<LoginResponse> login(LoginRequest request) {
    if (adminEmail.equalsIgnoreCase(request.email()) && adminPassword.equals(request.password())) {
      AuthenticatedUser admin = new AuthenticatedUser(1L, adminEmail, "System Administrator", "ADMIN");
      return Mono.just(toLoginResponse(admin));
    }

    return webClientBuilder.build()
        .post()
        .uri("http://customer-service/internal/auth/customers/login")
        .header("X-Internal-Secret", internalSecret)
        .bodyValue(request)
        .retrieve()
        .onStatus(HttpStatusCode::isError, response -> Mono.error(
            new ResponseStatusException(UNAUTHORIZED, "Invalid email or password")
        ))
        .bodyToMono(AuthenticatedUser.class)
        .map(this::toLoginResponse);
  }

  private LoginResponse toLoginResponse(AuthenticatedUser user) {
    return new LoginResponse(
        "Bearer",
        jwtService.generateToken(user),
        jwtService.getExpirationSeconds(),
        user.userId(),
        user.email(),
        user.name(),
        user.role()
    );
  }
}
