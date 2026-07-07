package com.example.carservice.repository;

import com.example.carservice.domain.entity.CarInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarInformationRepository extends JpaRepository<CarInformation, Long> {
}
