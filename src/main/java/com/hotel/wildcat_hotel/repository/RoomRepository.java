package com.hotel.wildcat_hotel.repository;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.hotel.wildcat_hotel.hotel.Room;

public class RoomRepository extends AbstractHibernateRepository<Room> {

    public RoomRepository(SessionFactory sessionFactory) {
        super(sessionFactory, Room.class);
    }

    public List<Room> findAvailableRooms() {
        try (Session session = getSessionFactory().openSession()) {
            return session.createQuery("from Room r where r.status = 'AVAILABLE'", Room.class).list();
        }
    }
}