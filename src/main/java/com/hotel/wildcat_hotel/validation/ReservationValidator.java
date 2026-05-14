package com.hotel.wildcat_hotel.validation;

import com.hotel.wildcat_hotel.core.Validator;
import com.hotel.wildcat_hotel.hotel.Reservation;

public class ReservationValidator implements Validator<Reservation> {

    @Override
    public void validate(Reservation reservation) {
        if (reservation == null) {
            throw new IllegalArgumentException("Reservation must not be null.");
        }
        if (reservation.getGuestID() <= 0 || reservation.getRoomID() <= 0) {
            throw new IllegalArgumentException("Reservation must reference valid guest and room IDs.");
        }
        if (reservation.getUserID() <= 0) {
            throw new IllegalArgumentException("Reservation must reference the user who created it.");
        }
        if (reservation.getCheckInDate() == null || reservation.getCheckOutDate() == null) {
            throw new IllegalArgumentException("Reservation dates must not be null.");
        }
        if (reservation.getNumberOfDays() <= 0) {
            throw new IllegalArgumentException("Reservation days must be greater than zero.");
        }
        if (reservation.getTotalCost() < 0) {
            throw new IllegalArgumentException("Reservation total cost must not be negative.");
        }
    }
}