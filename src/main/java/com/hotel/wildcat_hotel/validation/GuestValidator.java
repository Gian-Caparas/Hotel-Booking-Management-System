package com.hotel.wildcat_hotel.validation;

import com.hotel.wildcat_hotel.core.Validator;
import com.hotel.wildcat_hotel.hotel.Guest;

public class GuestValidator implements Validator<Guest> {

    @Override
    public void validate(Guest guest) {
        if (guest == null) {
            throw new IllegalArgumentException("Guest must not be null.");
        }
        if (guest.getRoomID() <= 0) {
            throw new IllegalArgumentException("Guest must be assigned to a valid room.");
        }
        if (guest.getFirstName() == null || guest.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("Guest first name must not be blank.");
        }
        if (guest.getLastName() == null || guest.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Guest last name must not be blank.");
        }
        if (guest.getEmail() == null || guest.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Guest email must not be blank.");
        }
    }
}