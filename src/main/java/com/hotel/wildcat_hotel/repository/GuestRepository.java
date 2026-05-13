package com.hotel.wildcat_hotel.repository;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.hotel.wildcat_hotel.hotel.Guest;

public class GuestRepository extends AbstractHibernateRepository<Guest> {

    public GuestRepository(SessionFactory sessionFactory) {
        super(sessionFactory, Guest.class);
    }

    public List<Guest> findActiveGuests() {
        try (Session session = getSessionFactory().openSession()) {
            session.beginTransaction();
            List<Guest> guests = session.createQuery(
                    "select distinct g from Guest g join Reservation r on r.guestID = g.entityId",
                    Guest.class).list();
            session.getTransaction().commit();
            return guests;
        }
    }
}