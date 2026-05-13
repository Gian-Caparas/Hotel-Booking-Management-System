package com.hotel.wildcat_hotel.service;

import java.util.List;

import com.hotel.wildcat_hotel.hotel.Guest;
import com.hotel.wildcat_hotel.repository.GuestRepository;
import com.hotel.wildcat_hotel.validation.GuestValidator;

public class GuestService extends AbstractCrudService<Guest> {

    private final GuestRepository repository;

    public GuestService(GuestRepository repository, GuestValidator validator) {
        super(repository, validator);
        this.repository = repository;
    }

    public List<Guest> getActiveGuests() {
        return repository.findActiveGuests();
    }
}