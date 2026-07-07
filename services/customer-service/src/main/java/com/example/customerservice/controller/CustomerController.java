package com.example.customerservice.controller;

import com.example.customerservice.dto.request.CreateCustomerRequest;
import com.example.customerservice.dto.request.UpdateCustomerRequest;
import com.example.customerservice.dto.response.ApiResponse;
import com.example.customerservice.dto.response.CustomerResponse;
import com.example.customerservice.dto.response.PageResponse;
import com.example.customerservice.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

  private final CustomerService customerService;

  @GetMapping("/{id}")
  public ResponseEntity<CustomerResponse> getById(@PathVariable("id") Long id) {

    return ResponseEntity.ok(customerService.getCustomerById(id));
  }

  @GetMapping()
  public ResponseEntity<PageResponse<CustomerResponse>> getList(
      @RequestParam("page") int page,
      @RequestParam("size") int size
  ) {
    return ResponseEntity.ok(customerService.getCustomers(page, size));
  }

  @PutMapping("/{id}")
  public ResponseEntity<CustomerResponse> update(
      @RequestParam("id") Long id,
      @RequestBody UpdateCustomerRequest request
  ) {
    return ResponseEntity.ok(customerService.update(id, request));
  }

  @PostMapping()
  public ResponseEntity<CustomerResponse> create(
      @RequestBody CreateCustomerRequest request
  ) {

    return ResponseEntity.ok(customerService.createCustomer(request));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse<Void>> delete(
      @RequestParam("id") Long id
  ) {
    this.customerService.deleteCustomer(id);

    return ResponseEntity.ok(ApiResponse.success(null));
  }


}
