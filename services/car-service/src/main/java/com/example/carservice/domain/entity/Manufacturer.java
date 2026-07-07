package com.example.carservice.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "manufacturers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Manufacturer {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "manufacturer_id")
  private Long manufacturerId;

  @Column(name = "manufacturer_name", nullable = false)
  private String manufacturerName;

  @Column(name = "description")
  private String description;

  @Column(name = "manufacturer_country")
  private String manufacturerCountry;

  @OneToMany(mappedBy = "manufacturer")
  private List<CarInformation> cars = new ArrayList<>();

}
