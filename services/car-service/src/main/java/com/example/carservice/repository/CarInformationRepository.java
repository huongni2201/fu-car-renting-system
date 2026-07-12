package com.example.carservice.repository;

import com.example.carservice.domain.entity.CarInformation;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarInformationRepository extends JpaRepository<CarInformation, Long> {

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("select car from CarInformation car where car.carId = :id")
  Optional<CarInformation> findByIdForUpdate(@Param("id") Long id);
}
