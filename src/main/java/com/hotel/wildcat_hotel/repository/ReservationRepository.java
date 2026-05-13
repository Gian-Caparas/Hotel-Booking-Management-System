package com.hotel.wildcat_hotel.repository;

import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.hotel.wildcat_hotel.hotel.Reservation;

public class ReservationRepository extends AbstractHibernateRepository<Reservation> {

    public ReservationRepository(SessionFactory sessionFactory) {
        super(sessionFactory, Reservation.class);
    }

    public Optional<Reservation> findLatestByRoomId(int roomId) {
        try (Session session = getSessionFactory().openSession()) {
            session.beginTransaction();
            Reservation reservation = session.createQuery(
                            "from Reservation r where r.roomID = :roomId order by r.entityId desc",
                            Reservation.class)
                    .setParameter("roomId", roomId)
                    .setMaxResults(1)
                    .uniqueResult();
            session.getTransaction().commit();
            return Optional.ofNullable(reservation);
        }
    }

    public Optional<Reservation> findLatestByGuestId(int guestId) {
        try (Session session = getSessionFactory().openSession()) {
            session.beginTransaction();
            Reservation reservation = session.createQuery(
                            "from Reservation r where r.guestID = :guestId order by r.entityId desc",
                            Reservation.class)
                    .setParameter("guestId", guestId)
                    .setMaxResults(1)
                    .uniqueResult();
            session.getTransaction().commit();
            return Optional.ofNullable(reservation);
        }
    }
}