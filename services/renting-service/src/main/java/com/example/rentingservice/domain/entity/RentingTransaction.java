package com.example.rentingservice.domain.entity;

import com.example.rentingservice.domain.enums.RentingStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
@Table(name = "renting_transaction")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RentingTransaction {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long rentingTransactionId;

  private LocalDate rentingDate;

  private BigDecimal totalPrice;

  private Long customerId;

  @Enumerated(EnumType.STRING)
  private RentingStatus rentingStatus;

  @OneToMany(
      mappedBy = "rentingTransaction",
      cascade = CascadeType.ALL,
      orphanRemoval = true
  )
  @Builder.Default
  private List<RentingDetail> rentingDetails = new ArrayList<>();

  public void addRentingDetail(RentingDetail rentingDetail) {
    rentingDetails.add(rentingDetail);
    rentingDetail.setRentingTransaction(this);
  }
}
