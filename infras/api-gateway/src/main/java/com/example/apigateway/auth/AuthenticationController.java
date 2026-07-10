package com.example.apigateway.auth;

import com.example.apigateway.auth.dto.LoginRequest;
import com.example.apigateway.auth.dto.LoginResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

  private final AuthenticationService authenticationService;

  public AuthenticationController(AuthenticationService authenticationService) {
    this.authenticationService = authenticationService;
  }

  @PostMapping("/login")
  public Mono<ResponseEntity<LoginResponse>> login(@RequestBody LoginRequest request) {
    return authenticationService.login(request).map(ResponseEntity::ok);
  }
}
