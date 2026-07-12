package com.example.rentingservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "customer-service")
public interface CustomerServiceFeignClient {

  @GetMapping("/internal/customers/{id}/active")
  void ensureActiveCustomer(
      @PathVariable("id") Long customerId,
      @RequestHeader("X-Internal-Secret") String internalSecret
  );
}
