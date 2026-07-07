package com.example.carrentalmanagement.domain.entity;

import com.example.carrentalmanagement.domain.enums.CarStatus;
import com.example.carrentalmanagement.domain.enums.FuelType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CarInformation {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long carId;

  private String carName;
  private String carDescription;
  private int NumberOfDoors;

  private int seatingCapacity;

  @Enumerated(EnumType.STRING)
  private FuelType fuelType;

  private int year;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "manufacturer_id", nullable = false)
  private Manufacturer manufacturer;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "supplier_id", nullable = false)
  private Supplier supplier;

  private CarStatus carStatus;

  @Column(name = "car_renting_price_per_day", precision = 18, scale = 2)
  private BigDecimal carRentingPricePerDay;

  @OneToMany(mappedBy = "carInformation")
  private List<RentingDetail> rentingDetails = new ArrayList<>();

}
