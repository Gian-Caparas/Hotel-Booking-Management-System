package com.hotel.wildcat_hotel.service;

import java.util.List;

import com.hotel.wildcat_hotel.hotel.Room;
import com.hotel.wildcat_hotel.repository.RoomRepository;
import com.hotel.wildcat_hotel.validation.RoomValidator;

public class RoomService extends AbstractCrudService<Room> {

    private final RoomRepository repository;

    public RoomService(RoomRepository repository, RoomValidator validator) {
        super(repository, validator);
        this.repository = repository;
    }

    public List<Room> getAvailableRooms() {
        return repository.findAvailableRooms();
    }
}