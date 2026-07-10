package com.example.customerservice.controller;

import com.example.customerservice.dto.request.LoginRequest;
import com.example.customerservice.dto.response.LoginResponse;
import com.example.customerservice.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.FORBIDDEN;

@RestController
@RequestMapping("/internal/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService authenticationService;

  @Value("${app.internal-secret}")
  private String internalSecret;

  @PostMapping("/customers/login")
  public ResponseEntity<LoginResponse> login(
      @RequestBody LoginRequest request,
      @RequestHeader(value = "X-Internal-Secret", required = false) String providedSecret
  ) {
    if (!internalSecret.equals(providedSecret)) {
      throw new ResponseStatusException(FORBIDDEN, "Internal gateway secret is invalid");
    }

    return ResponseEntity.ok(authenticationService.loginCustomer(request));
  }
}
