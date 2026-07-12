package com.example.customerservice.controller;

import com.example.customerservice.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.FORBIDDEN;

@RestController
@RequestMapping("/internal/customers")
@RequiredArgsConstructor
public class InternalCustomerController {

  private final CustomerService customerService;

  @Value("${app.internal-secret}")
  private String internalSecret;

  @GetMapping("/{id}/active")
  public ResponseEntity<Void> ensureActiveCustomer(
      @PathVariable Long id,
      @RequestHeader(value = "X-Internal-Secret", required = false) String providedSecret
  ) {
    if (!internalSecret.equals(providedSecret)) {
      throw new ResponseStatusException(FORBIDDEN, "Internal gateway secret is invalid");
    }

    customerService.ensureActiveCustomer(id);
    return ResponseEntity.noContent().build();
  }
}
