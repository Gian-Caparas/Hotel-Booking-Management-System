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
                            "from Reservation r where r.roomID = :roomId and r.status = :status order by r.entityId desc",
                            Reservation.class)
                    .setParameter("roomId", roomId)
                    .setParameter("status", Reservation.STATUS_ACTIVE)
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
                            "from Reservation r where r.guestID = :guestId and r.status = :status order by r.entityId desc",
                            Reservation.class)
                    .setParameter("guestId", guestId)
                    .setParameter("status", Reservation.STATUS_ACTIVE)
                    .setMaxResults(1)
                    .uniqueResult();
            session.getTransaction().commit();
            return Optional.ofNullable(reservation);
        }
    }

    public Optional<Reservation> findLatestByUserId(int userId) {
        try (Session session = getSessionFactory().openSession()) {
            session.beginTransaction();
            Reservation reservation = session.createQuery(
                            "from Reservation r where r.userID = :userId and r.status = :status order by r.entityId desc",
                            Reservation.class)
                    .setParameter("userId", userId)
                    .setParameter("status", Reservation.STATUS_ACTIVE)
                    .setMaxResults(1)
                    .uniqueResult();
            session.getTransaction().commit();
            return Optional.ofNullable(reservation);
        }
    }

    public Optional<Reservation> findLatestByRoomIdAndUserId(int roomId, int userId) {
        try (Session session = getSessionFactory().openSession()) {
            session.beginTransaction();
            Reservation reservation = session.createQuery(
                            "from Reservation r where r.roomID = :roomId and r.userID = :userId and r.status = :status order by r.entityId desc",
                            Reservation.class)
                    .setParameter("roomId", roomId)
                    .setParameter("userId", userId)
                    .setParameter("status", Reservation.STATUS_ACTIVE)
                    .setMaxResults(1)
                    .uniqueResult();
            session.getTransaction().commit();
            return Optional.ofNullable(reservation);
        }
    }

    public boolean markCancelledById(int reservationId) {
        try (Session session = getSessionFactory().openSession()) {
            session.beginTransaction();
            int updated = session.createMutationQuery(
                            "update Reservation r set r.status = :cancelledStatus where r.entityId = :id and r.status = :activeStatus")
                    .setParameter("cancelledStatus", Reservation.STATUS_CANCELLED)
                    .setParameter("id", reservationId)
                    .setParameter("activeStatus", Reservation.STATUS_ACTIVE)
                    .executeUpdate();
            session.getTransaction().commit();
            return updated > 0;
        }
    }

    public boolean markCancelledByIdAndUserId(int reservationId, int userId) {
        try (Session session = getSessionFactory().openSession()) {
            session.beginTransaction();
            int updated = session.createMutationQuery(
                            "update Reservation r set r.status = :cancelledStatus where r.entityId = :id and r.userID = :userId and r.status = :activeStatus")
                    .setParameter("cancelledStatus", Reservation.STATUS_CANCELLED)
                    .setParameter("id", reservationId)
                    .setParameter("userId", userId)
                    .setParameter("activeStatus", Reservation.STATUS_ACTIVE)
                    .executeUpdate();
            session.getTransaction().commit();
            return updated > 0;
        }
    }
}