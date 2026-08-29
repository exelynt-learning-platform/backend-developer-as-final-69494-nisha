package com.example.resourcebooking.repository;

import com.example.resourcebooking.entity.Reservation;
import com.example.resourcebooking.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ReservationRepository
        extends JpaRepository<Reservation, Long>, JpaSpecificationExecutor<Reservation> {

    List<Reservation> findByUserUsername(String username);

    List<Reservation> findByStatus(ReservationStatus status);

    List<Reservation> findByUserUsernameAndStatus(
            String username,
            ReservationStatus status
    );
}