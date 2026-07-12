package com.example.rentingservice.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "saga_compensation_failure")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SagaCompensationFailure {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long carId;

  private String reservationToken;

  private String operation;

  private String reason;

  private Instant createdAt;
}
