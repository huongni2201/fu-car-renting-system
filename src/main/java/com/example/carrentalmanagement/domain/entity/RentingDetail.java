package com.example.carrentalmanagement.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RentingDetail {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long rentingDetailId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "renting_transaction_id", nullable = false)
  private RentingTransaction rentingTransaction;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "car_id", nullable = false)
  private CarInformation carInformation;

  private LocalDate startDate;

  private LocalDate endDate;

  @Column(name = "price", precision = 18, scale = 2, nullable = false)
  private BigDecimal price;
}
