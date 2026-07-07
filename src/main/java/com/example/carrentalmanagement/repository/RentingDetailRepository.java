package com.example.carrentalmanagement.repository;

import com.example.carrentalmanagement.domain.entity.RentingDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentingDetailRepository extends JpaRepository<RentingDetail, Long> {
}
