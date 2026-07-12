package com.example.customerservice.service;

import com.example.customerservice.domain.entity.Customer;
import com.example.customerservice.domain.enums.CustomerStatus;
import com.example.customerservice.dto.request.CreateCustomerRequest;
import com.example.customerservice.dto.request.UpdateCustomerRequest;
import com.example.customerservice.dto.response.CustomerResponse;
import com.example.customerservice.dto.response.PageResponse;
import com.example.customerservice.exception.CustomerNotFoundException;
import com.example.customerservice.exception.EmailExistedException;
import com.example.customerservice.mapper.CustomerMapper;
import com.example.customerservice.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@Service
@RequiredArgsConstructor
public class CustomerService {

  private final PasswordEncoder passwordEncoder;
  private final CustomerMapper customerMapper;
  private final CustomerRepository customerRepository;

  public CustomerResponse createCustomer(CreateCustomerRequest request) {
    validateCreateRequest(request);

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

    return customerMapper.toCustomerResponse(savedCustomer);
  }

  public CustomerResponse registerCustomer(CreateCustomerRequest request) {
    validateCreateRequest(request);

    return createCustomer(CreateCustomerRequest.builder()
        .customerName(request.customerName())
        .telephone(request.telephone())
        .email(request.email())
        .customerBirthday(request.customerBirthday())
        .customerStatus(CustomerStatus.ACTIVE)
        .password(request.password())
        .build());
  }

  public PageResponse<CustomerResponse> getCustomers(int page, int size) {
    validatePageRequest(page, size);

    Pageable pageable = PageRequest.of(page, size, Sort.unsorted());

    Page<Customer> customerPage = customerRepository.findAll(pageable);

    List<CustomerResponse> content = customerPage.getContent()
        .stream()
        .map(customerMapper::toCustomerResponse)
        .toList();

    return PageResponse.<CustomerResponse>builder()
        .size(size)
        .page(page)
        .content(content)
        .totalElements(customerPage.getTotalElements())
        .totalPages(customerPage.getTotalPages())
        .first(customerPage.isFirst())
        .last(customerPage.isLast())
        .build();
  }

  public CustomerResponse getCustomerById(Long customerId) {
    Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new CustomerNotFoundException("Customer not found!"));

    return customerMapper.toCustomerResponse(customer);
  }

  public CustomerResponse update(Long customerId, UpdateCustomerRequest request) {
    validateUpdateRequest(request);

    Customer customer = customerRepository.findById(customerId).orElseThrow(
        () -> new CustomerNotFoundException("Customer not found!")
    );

    if (request.customerName() != null) {
      customer.setCustomerName(request.customerName());
    }
    if (request.customerBirthday() != null) {
      customer.setCustomerBirthday(request.customerBirthday());
    }
    if (request.telephone() != null) {
      customer.setTelephone(request.telephone());
    }
    if (request.customerStatus() != null) {
      customer.setCustomerStatus(request.customerStatus());
    }

    Customer savedCustomer = this.customerRepository.save(customer);

    return customerMapper.toCustomerResponse(savedCustomer);
  }

  public CustomerResponse updateProfile(Long customerId, UpdateCustomerRequest request) {
    validateUpdateRequest(request);

    Customer customer = customerRepository.findById(customerId).orElseThrow(
        () -> new CustomerNotFoundException("Customer not found!")
    );

    if (request.customerName() != null) {
      customer.setCustomerName(request.customerName());
    }
    if (request.customerBirthday() != null) {
      customer.setCustomerBirthday(request.customerBirthday());
    }
    if (request.telephone() != null) {
      customer.setTelephone(request.telephone());
    }

    return customerMapper.toCustomerResponse(customerRepository.save(customer));
  }

  public void ensureActiveCustomer(Long customerId) {
    Customer customer = customerRepository.findById(customerId).orElseThrow(
        () -> new CustomerNotFoundException("Customer not found!")
    );

    if (customer.getCustomerStatus() != CustomerStatus.ACTIVE) {
      throw new ResponseStatusException(FORBIDDEN, "Customer is not active");
    }
  }


  public void deleteCustomer(Long customerId) {
    Customer customer = customerRepository.findById(customerId).orElseThrow(
        () -> new CustomerNotFoundException("Customer not found!")
    );

    customerRepository.delete(customer);
  }

  private void validateCreateRequest(CreateCustomerRequest request) {
    if (request == null
        || isBlank(request.customerName())
        || isBlank(request.email())
        || isBlank(request.password())) {
      throw new ResponseStatusException(BAD_REQUEST, "customerName, email and password are required");
    }
  }

  private void validateUpdateRequest(UpdateCustomerRequest request) {
    if (request == null) {
      throw new ResponseStatusException(BAD_REQUEST, "Request body is required");
    }
  }

  private void validatePageRequest(int page, int size) {
    if (page < 0 || size <= 0 || size > 100) {
      throw new ResponseStatusException(BAD_REQUEST, "page must be >= 0 and size must be between 1 and 100");
    }
  }

  private boolean isBlank(String value) {
    return value == null || value.isBlank();
  }
}
