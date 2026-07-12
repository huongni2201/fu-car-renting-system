package com.example.carservice.controller;

import com.example.carservice.dto.request.CarReservationRequest;
import com.example.carservice.service.CarInformationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.FORBIDDEN;

@RestController
@RequestMapping("/internal/cars")
@RequiredArgsConstructor
public class InternalCarController {

  private final CarInformationService carInformationService;

  @Value("${app.internal-secret}")
  private String internalSecret;

  @PostMapping("/{id}/reserve")
  public ResponseEntity<Void> reserve(
      @PathVariable Long id,
      @RequestBody CarReservationRequest request,
      @RequestHeader(value = "X-Internal-Secret", required = false) String providedSecret
  ) {
    validateInternalSecret(providedSecret);
    carInformationService.reserve(id, request);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/{id}/release")
  public ResponseEntity<Void> release(
      @PathVariable Long id,
      @RequestBody CarReservationRequest request,
      @RequestHeader(value = "X-Internal-Secret", required = false) String providedSecret
  ) {
    validateInternalSecret(providedSecret);
    carInformationService.release(id, request);
    return ResponseEntity.noContent().build();
  }

  private void validateInternalSecret(String providedSecret) {
    if (!internalSecret.equals(providedSecret)) {
      throw new ResponseStatusException(FORBIDDEN, "Internal gateway secret is invalid");
    }
  }
}
