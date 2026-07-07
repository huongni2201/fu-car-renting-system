package com.example.customerservice.domain.entity;

import com.example.customerservice.domain.enums.CustomerStatus;
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
public class Customer {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long customerId;

  private String customerName;

  private String telephone;

  private String email;

  private String customerBirthday;

  @Enumerated(EnumType.STRING)
  private CustomerStatus customerStatus;

  private String password;
}
