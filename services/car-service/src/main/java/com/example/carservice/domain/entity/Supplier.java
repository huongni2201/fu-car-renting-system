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
@Table(name = "suppliers")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Supplier {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "supplier_id")
  private Long supplierId;

  @Column(name = "supplier_name", nullable = false)
  private String supplierName;

  @Column(name = "supplier_description")
  private String supplierDescription;

  @Column(name = "supplier_address")
  private String supplierAddress;

  @OneToMany(mappedBy = "supplier")
  private List<CarInformation> cars = new ArrayList<>();
}
