package com.hotel.wildcat_hotel.service;

import java.util.Optional;

import com.hotel.wildcat_hotel.hotel.Reservation;
import com.hotel.wildcat_hotel.repository.ReservationRepository;
import com.hotel.wildcat_hotel.validation.ReservationValidator;

public class ReservationService extends AbstractCrudService<Reservation> {

    private final ReservationRepository repository;

    public ReservationService(ReservationRepository repository, ReservationValidator validator) {
        super(repository, validator);
        this.repository = repository;
    }

    public Optional<Reservation> findLatestByRoomId(int roomId) {
        return repository.findLatestByRoomId(roomId);
    }

    public Optional<Reservation> findLatestByGuestId(int guestId) {
        return repository.findLatestByGuestId(guestId);
    }

    public Optional<Reservation> findLatestByUserId(int userId) {
        return repository.findLatestByUserId(userId);
    }

    public Optional<Reservation> findLatestByRoomIdAndUserId(int roomId, int userId) {
        return repository.findLatestByRoomIdAndUserId(roomId, userId);
    }

    public boolean cancelById(int reservationId) {
        return repository.markCancelledById(reservationId);
    }

    public boolean cancelByIdAndUserId(int reservationId, int userId) {
        return repository.markCancelledByIdAndUserId(reservationId, userId);
    }
}