package com.example.rentingservice.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.BAD_GATEWAY;
import static org.springframework.http.HttpStatus.GATEWAY_TIMEOUT;

@Component
public class CarClient {

  private final RestClient restClient;

  @Value("${app.internal-secret}")
  private String internalSecret;

  public CarClient(RestClient.Builder restClientBuilder) {
    this.restClient = restClientBuilder.baseUrl("http://car-service").build();
  }

  public void reserve(Long carId, String reservationToken) {
    postInternal("/internal/cars/{id}/reserve", carId, reservationToken, "Car is not available");
  }

  public void release(Long carId, String reservationToken) {
    postInternal("/internal/cars/{id}/release", carId, reservationToken, "Car reservation cannot be released");
  }

  private void postInternal(String uri, Long carId, String reservationToken, String clientErrorMessage) {
    try {
      restClient.post()
          .uri(uri, carId)
          .header("X-Internal-Secret", internalSecret)
          .body(new ReservationRequest(reservationToken))
          .retrieve()
          .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
            throw new ResponseStatusException(response.getStatusCode(), clientErrorMessage);
          })
          .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
            throw new ResponseStatusException(BAD_GATEWAY, "Car service is unavailable");
          })
          .toBodilessEntity();
    } catch (ResourceAccessException exception) {
      throw new ResponseStatusException(GATEWAY_TIMEOUT, "Car service request timed out", exception);
    } catch (RestClientException exception) {
      throw new ResponseStatusException(BAD_GATEWAY, "Car service request failed", exception);
    }
  }

  private record ReservationRequest(String reservationToken) {
  }
}
