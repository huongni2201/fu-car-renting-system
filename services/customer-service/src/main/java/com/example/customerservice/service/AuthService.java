package com.example.customerservice.service;

import com.example.customerservice.domain.entity.Customer;
import com.example.customerservice.dto.response.CustomerResponse;
import com.example.customerservice.exception.CustomerNotFoundException;
import com.example.customerservice.mapper.CustomerMapper;
import com.example.customerservice.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final PasswordEncoder passwordEncoder;
  private final CustomerRepository customerRepository;
  private final CustomerMapper customerMapper;

  public CustomerResponse login(String email, String rawPassword) {
    Customer customer = customerRepository.findByEmail(email)
        .orElseThrow(() -> new CustomerNotFoundException("Invalid email or password"));

    boolean matched = passwordEncoder.matches(rawPassword, customer.getPassword());

    if (!matched) {
      throw new CustomerNotFoundException("Invalid email or password");
    }

    return customerMapper.toCustomerResponse(customer);
  }
}
