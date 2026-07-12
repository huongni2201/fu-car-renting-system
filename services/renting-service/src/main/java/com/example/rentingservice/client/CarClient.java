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
public class CarClient {

  private final CarServiceFeignClient carServiceFeignClient;

  @Value("${app.internal-secret}")
  private String internalSecret;

  public CarClient(CarServiceFeignClient carServiceFeignClient) {
    this.carServiceFeignClient = carServiceFeignClient;
  }

  public void reserve(Long carId, String reservationToken) {
    try {
      carServiceFeignClient.reserve(carId, internalSecret, new ReservationRequest(reservationToken));
    } catch (RuntimeException exception) {
      throw mapException(exception, "Car is not available");
    }
  }

  public void release(Long carId, String reservationToken) {
    try {
      carServiceFeignClient.release(carId, internalSecret, new ReservationRequest(reservationToken));
    } catch (RuntimeException exception) {
      throw mapException(exception, "Car reservation cannot be released");
    }
  }

  private ResponseStatusException mapException(RuntimeException exception, String clientErrorMessage) {
    if (exception instanceof RetryableException) {
      return new ResponseStatusException(GATEWAY_TIMEOUT, "Car service request timed out", exception);
    }
    if (exception instanceof FeignException feignException) {
      int status = feignException.status();
      if (status >= 400 && status < 500) {
        return new ResponseStatusException(valueOf(status), clientErrorMessage, exception);
      }
      return new ResponseStatusException(BAD_GATEWAY, "Car service is unavailable", exception);
    }

    return new ResponseStatusException(BAD_GATEWAY, "Car service request failed", exception);
  }
}
