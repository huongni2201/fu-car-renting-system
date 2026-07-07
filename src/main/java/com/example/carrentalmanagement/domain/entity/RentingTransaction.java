package com.example.carrentalmanagement.domain.entity;

import com.example.carrentalmanagement.domain.enums.RentingStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RentingTransaction {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long rentingTransactionId;

  private LocalDate rentingDate;

  @Column(name = "total_price", precision = 18, scale = 2, nullable = false)
  private BigDecimal totalPrice;

  @ManyToOne
  @JoinColumn(name = "customer_id")
  private Customer customer;

  @Enumerated(EnumType.STRING)
  private RentingStatus rentingStatus;

  private List<RentingDetail> rentingDetails = new ArrayList<>();

}
