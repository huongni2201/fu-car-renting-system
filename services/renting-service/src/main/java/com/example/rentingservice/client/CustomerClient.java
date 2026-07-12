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
public class CustomerClient {

  private final RestClient restClient;

  @Value("${app.internal-secret}")
  private String internalSecret;

  public CustomerClient(RestClient.Builder restClientBuilder) {
    this.restClient = restClientBuilder.baseUrl("http://customer-service").build();
  }

  public void ensureActiveCustomer(Long customerId) {
    try {
      restClient.get()
          .uri("/internal/customers/{id}/active", customerId)
          .header("X-Internal-Secret", internalSecret)
          .retrieve()
          .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
            throw new ResponseStatusException(response.getStatusCode(), "Customer is not eligible to rent");
          })
          .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
            throw new ResponseStatusException(BAD_GATEWAY, "Customer service is unavailable");
          })
          .toBodilessEntity();
    } catch (ResourceAccessException exception) {
      throw new ResponseStatusException(GATEWAY_TIMEOUT, "Customer service request timed out", exception);
    } catch (RestClientException exception) {
      throw new ResponseStatusException(BAD_GATEWAY, "Customer service request failed", exception);
    }
  }
}
