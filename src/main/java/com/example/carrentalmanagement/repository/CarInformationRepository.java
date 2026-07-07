package com.example.carrentalmanagement.repository;

import com.example.carrentalmanagement.domain.entity.CarInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarInformationRepository extends JpaRepository<CarInformation, Long> {
}
