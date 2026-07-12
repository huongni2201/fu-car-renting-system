package com.example.rentingservice.repository;

import com.example.rentingservice.domain.entity.SagaCompensationFailure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SagaCompensationFailureRepository extends JpaRepository<SagaCompensationFailure, Long> {
}
