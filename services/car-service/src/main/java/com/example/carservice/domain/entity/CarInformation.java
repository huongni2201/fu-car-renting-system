package com.example.carservice.domain.entity;

import com.example.carservice.domain.enums.CarStatus;
import com.example.carservice.domain.enums.FuelType;
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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
  private int numberOfDoors;

  private int seatingCapacity;

  @Enumerated(EnumType.STRING)
  private FuelType fuelType;

  @Column(name = "manufacturing_year")
  private int year;

  @Enumerated(EnumType.STRING)
  private CarStatus carStatus;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "manufacturer_id")
  private Manufacturer manufacturer;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "supplier_id")
  private Supplier supplier;

}
