package com.example.rentingservice.client;

import feign.FeignException;
import feign.RetryableException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.BAD_GATEWAY;
import static org.springframework.http.HttpStatus.GATEWAY_TIMEOUT;
import static org.springframework.http.HttpStatusCode.valueOf;

@Component
public class CustomerClient {

  private final CustomerServiceFeignClient customerServiceFeignClient;

  @Value("${app.internal-secret}")
  private String internalSecret;

  public CustomerClient(CustomerServiceFeignClient customerServiceFeignClient) {
    this.customerServiceFeignClient = customerServiceFeignClient;
  }

  public void ensureActiveCustomer(Long customerId) {
    try {
      customerServiceFeignClient.ensureActiveCustomer(customerId, internalSecret);
    } catch (RuntimeException exception) {
      throw mapException(exception);
    }
  }

  private ResponseStatusException mapException(RuntimeException exception) {
    if (exception instanceof RetryableException) {
      return new ResponseStatusException(GATEWAY_TIMEOUT, "Customer service request timed out", exception);
    }
    if (exception instanceof FeignException feignException) {
      int status = feignException.status();
      if (status >= 400 && status < 500) {
        return new ResponseStatusException(valueOf(status), "Customer is not eligible to rent", exception);
      }
      return new ResponseStatusException(BAD_GATEWAY, "Customer service is unavailable", exception);
    }

    return new ResponseStatusException(BAD_GATEWAY, "Customer service request failed", exception);
  }
}
