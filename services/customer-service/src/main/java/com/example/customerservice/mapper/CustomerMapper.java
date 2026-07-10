package com.example.customerservice.mapper;

import com.example.customerservice.domain.entity.Customer;
import com.example.customerservice.dto.response.CustomerResponse;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

  public CustomerResponse toCustomerResponse(Customer customer) {
    return CustomerResponse.builder()
        .customerId(customer.getCustomerId())
        .customerName(customer.getCustomerName())
        .telephone(customer.getTelephone())
        .email(customer.getEmail())
        .customerBirthday(customer.getCustomerBirthday())
        .customerStatus(customer.getCustomerStatus().name())
        .build();
  }
}
