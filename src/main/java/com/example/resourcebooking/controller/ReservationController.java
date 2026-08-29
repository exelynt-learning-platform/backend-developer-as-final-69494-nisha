package com.example.resourcebooking.controller;

import com.example.resourcebooking.dto.ReservationRequest;
import com.example.resourcebooking.entity.Reservation;
import com.example.resourcebooking.enums.ReservationStatus;
import com.example.resourcebooking.service.ReservationService;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(
            ReservationService reservationService) {

        this.reservationService = reservationService;
    }

    // =========================================================
    // CREATE RESERVATION
    // =========================================================

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Reservation> createReservation(

            @Valid @RequestBody ReservationRequest request,

            Authentication authentication) {

        String username = authentication.getName();

        Reservation reservation =
                reservationService.createReservation(
                        request,
                        username
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(reservation);
    }

    // =========================================================
    // GET ALL RESERVATIONS
    // =========================================================

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Page<Reservation>> getReservations(

            @RequestParam(required = false)
            ReservationStatus status,

            @RequestParam(required = false)
            BigDecimal minPrice,

            @RequestParam(required = false)
            BigDecimal maxPrice,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size,

            Authentication authentication) {

        String username = authentication.getName();

        boolean isAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(authority ->
                        authority.getAuthority()
                                .equals("ROLE_ADMIN"));

        // Create Pageable manually.
        // This prevents Swagger from sending
        // sort=["createdAt,desc"].
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(
                        Sort.Direction.DESC,
                        "createdAt"
                )
        );

        Page<Reservation> reservations =
                reservationService.getReservations(
                        username,
                        isAdmin,
                        status,
                        minPrice,
                        maxPrice,
                        pageable
                );

        return ResponseEntity.ok(reservations);
    }

    // =========================================================
    // GET RESERVATION BY ID
    // =========================================================

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Reservation> getReservationById(

            @PathVariable Long id,

            Authentication authentication) {

        String username = authentication.getName();

        boolean isAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(authority ->
                        authority.getAuthority()
                                .equals("ROLE_ADMIN"));

        return ResponseEntity.ok(
                reservationService.getReservationById(
                        id,
                        username,
                        isAdmin
                )
        );
    }

    // =========================================================
    // UPDATE RESERVATION
    // =========================================================

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Reservation> updateReservation(

            @PathVariable Long id,

            @Valid @RequestBody ReservationRequest request,

            Authentication authentication) {

        String username = authentication.getName();

        boolean isAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(authority ->
                        authority.getAuthority()
                                .equals("ROLE_ADMIN"));

        return ResponseEntity.ok(
                reservationService.updateReservation(
                        id,
                        request,
                        username,
                        isAdmin
                )
        );
    }

    // =========================================================
    // DELETE RESERVATION
    // =========================================================

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Void> deleteReservation(

            @PathVariable Long id,

            Authentication authentication) {

        String username = authentication.getName();

        boolean isAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(authority ->
                        authority.getAuthority()
                                .equals("ROLE_ADMIN"));

        reservationService.deleteReservation(
                id,
                username,
                isAdmin
        );

        return ResponseEntity
                .noContent()
                .build();
    }

    // =========================================================
    // UPDATE RESERVATION STATUS
    // =========================================================

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Reservation> updateStatus(

            @PathVariable Long id,

            @RequestParam ReservationStatus status) {

        return ResponseEntity.ok(
                reservationService.updateStatus(
                        id,
                        status
                )
        );
    }
}