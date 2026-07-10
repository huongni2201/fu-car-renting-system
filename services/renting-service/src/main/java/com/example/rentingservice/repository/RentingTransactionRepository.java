package com.example.rentingservice.repository;

import com.example.rentingservice.domain.entity.RentingTransaction;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RentingTransactionRepository extends JpaRepository<RentingTransaction, Long> {

  List<RentingTransaction> findByCustomerId(Long customerId, Sort sort);

  List<RentingTransaction> findByRentingDateBetween(LocalDate startDate, LocalDate endDate, Sort sort);
}
