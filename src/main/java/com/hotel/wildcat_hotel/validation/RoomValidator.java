package com.hotel.wildcat_hotel.validation;

import com.hotel.wildcat_hotel.core.Validator;
import com.hotel.wildcat_hotel.hotel.Room;

public class RoomValidator implements Validator<Room> {

    @Override
    public void validate(Room room) {
        if (room == null) {
            throw new IllegalArgumentException("Room must not be null.");
        }
        if (room.getRoomType() == null || room.getRoomType().trim().isEmpty()) {
            throw new IllegalArgumentException("Room type must not be blank.");
        }
        if (room.getRoomCapacity() == null || room.getRoomCapacity().trim().isEmpty()) {
            throw new IllegalArgumentException("Room capacity must not be blank.");
        }
        if (room.getRoomRate() < 0) {
            throw new IllegalArgumentException("Room rate must not be negative.");
        }
        if (room.getStatus() == null
                || (!"AVAILABLE".equalsIgnoreCase(room.getStatus())
                && !"OCCUPIED".equalsIgnoreCase(room.getStatus()))) {
            throw new IllegalArgumentException("Room status must be AVAILABLE or OCCUPIED.");
        }
    }
}