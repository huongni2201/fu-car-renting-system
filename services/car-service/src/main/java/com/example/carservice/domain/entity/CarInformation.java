package com.example.carservice.domain.entity;

import com.example.carservice.domain.enums.CarStatus;
import com.example.carservice.domain.enums.FuelType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
  private int NumberOfDoors;

  private int seatingCapacity;

  @Enumerated(EnumType.STRING)
  private FuelType fuelType;

  private int year;

  private CarStatus carStatus;

}
