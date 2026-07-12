package com.example.rentingservice.service;

import com.example.rentingservice.domain.entity.SagaCompensationFailure;
import com.example.rentingservice.repository.SagaCompensationFailureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class SagaCompensationFailureService {

  private final SagaCompensationFailureRepository repository;

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void recordReleaseFailure(Long carId, String reservationToken, String reason) {
    repository.save(SagaCompensationFailure.builder()
        .carId(carId)
        .reservationToken(reservationToken)
        .operation("RELEASE_CAR")
        .reason(reason)
        .createdAt(Instant.now())
        .build());
  }
}
