package com.example.rentingservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "car-service")
public interface CarServiceFeignClient {

  @PostMapping("/internal/cars/{id}/reserve")
  void reserve(
      @PathVariable("id") Long carId,
      @RequestHeader("X-Internal-Secret") String internalSecret,
      @RequestBody ReservationRequest request
  );

  @PostMapping("/internal/cars/{id}/release")
  void release(
      @PathVariable("id") Long carId,
      @RequestHeader("X-Internal-Secret") String internalSecret,
      @RequestBody ReservationRequest request
  );
}
