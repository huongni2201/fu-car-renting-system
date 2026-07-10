package com.example.customerservice.service;

import com.example.customerservice.domain.entity.Customer;
import com.example.customerservice.domain.enums.CustomerStatus;
import com.example.customerservice.dto.request.LoginRequest;
import com.example.customerservice.dto.response.LoginResponse;
import com.example.customerservice.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

  private final CustomerRepository customerRepository;
  private final PasswordEncoder passwordEncoder;

  public LoginResponse loginCustomer(LoginRequest request) {
    Customer customer = customerRepository.findByEmail(request.email())
        .orElseThrow(() -> new ResponseStatusException(UNAUTHORIZED, "Invalid email or password"));

    if (customer.getCustomerStatus() != CustomerStatus.ACTIVE
        || !passwordEncoder.matches(request.password(), customer.getPassword())) {
      throw new ResponseStatusException(UNAUTHORIZED, "Invalid email or password");
    }

    return LoginResponse.builder()
        .userId(customer.getCustomerId())
        .email(customer.getEmail())
        .name(customer.getCustomerName())
        .role("CUSTOMER")
        .build();
  }
}
