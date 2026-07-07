package com.example.customerservice.service;

import com.example.customerservice.domain.entity.Customer;
import com.example.customerservice.domain.enums.CustomerStatus;
import com.example.customerservice.dto.request.CreateCustomerRequest;
import com.example.customerservice.dto.response.CreateCustomerResponse;
import com.example.customerservice.dto.response.CustomerListResponse;
import com.example.customerservice.dto.response.CustomerResponse;
import com.example.customerservice.exception.CustomerNotFoundException;
import com.example.customerservice.exception.EmailExistedException;
import com.example.customerservice.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

  private final PasswordEncoder passwordEncoder;

  private final CustomerRepository customerRepository;

  public CreateCustomerResponse createCustomer(CreateCustomerRequest request) {
    if (customerRepository.existsByEmail(request.email())) {
      throw new EmailExistedException("Email already exists");
    }

    Customer customer = Customer.builder()
        .customerName(request.customerName())
        .telephone(request.telephone())
        .email(request.email())
        .customerBirthday(request.customerBirthday())
        .customerStatus(
            request.customerStatus() != null
                ? request.customerStatus()
                : CustomerStatus.ACTIVE
        )
        .password(passwordEncoder.encode(request.password()))
        .build();

    Customer savedCustomer = this.customerRepository.save(customer);

    return CreateCustomerResponse.builder().build();
  }

  public List<CustomerListResponse> getAllCustomers() {
    return new ArrayList<>();
  }

  public CustomerResponse getCustomerById(Long customerId) {
    Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new CustomerNotFoundException("Customer not found!"));

    return CustomerResponse.builder()
        .customerName(customer.getCustomerName())
        .telephone(customer.getTelephone())
        .email(customer.getEmail())
        .customerBirthday(customer.getCustomerBirthday())
        .customerStatus(customer.getCustomerStatus().name())
        .build();
  }


  public void deleteCustomer(Long customerId) {
    Customer customer = customerRepository.getReferenceById(customerId);

    customerRepository.delete(customer);
  }
}
