package com.example.rentingservice.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "renting_detail")
public class RentingDetail {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long rentingDetailId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "renting_transaction_id")
  private RentingTransaction rentingTransaction;

  private Long carId;

  private LocalDate startDate;

  private LocalDate endDate;

  private BigDecimal price;
}
