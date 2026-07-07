package com.example.carrentalmanagement.repository;

import com.example.carrentalmanagement.domain.entity.RentingTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentingTransactionRepository extends JpaRepository<RentingTransaction, Long> {
}
