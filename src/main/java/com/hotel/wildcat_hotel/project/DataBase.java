package com.hotel.wildcat_hotel.project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.hotel.wildcat_hotel.hotel.Guest;
import com.hotel.wildcat_hotel.hotel.Reservation;
import com.hotel.wildcat_hotel.hotel.Room;

public class DataBase {

    // ── Connection info ───────────────────────────────────────────────────────

    private static final String DB_URL =
            "jdbc:mysql://localhost:3306/hotelDB"
            + "?useSSL=false"
            + "&serverTimezone=UTC"
            + "&allowPublicKeyRetrieval=true";

    private static final String DB_USER     = "root";
    private static final String DB_PASSWORD = "";

    // ── Hibernate SessionFactory ──────────────────────────────────────────────

    private static SessionFactory sessionFactory;

    private static SessionFactory getFactory() {
        if (sessionFactory == null || sessionFactory.isClosed()) {
            sessionFactory = new Configuration()
                    .configure("hibernate.cfg.xml")
                    .addAnnotatedClass(Room.class)
                    .addAnnotatedClass(Guest.class)
                    .addAnnotatedClass(Reservation.class)   // ← was missing
                    .addAnnotatedClass(User.class)
                    .buildSessionFactory();
        }
        return sessionFactory;
    }

    public static void closeFactory() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
    }

    // ── JDBC connection ───────────────────────────────────────────────────────

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public static void checkConnection() throws SQLException {
        try (Connection con = getConnection()) {
            System.out.println("JDBC connection SUCCESS");
        }
    }

    // ── ROOMS ─────────────────────────────────────────────────────────────────

    public static List<Room> getRooms() {
        try (Session session = getFactory().openSession()) {
            session.beginTransaction();
            List<Room> rooms = session.createQuery("from Room", Room.class).list();
            session.getTransaction().commit();
            return rooms;
        }
    }

    public static List<Room> getAvailableRooms() {
        try (Session session = getFactory().openSession()) {
            session.beginTransaction();
            List<Room> rooms = session.createQuery(
                    "from Room r where r.status = 'AVAILABLE'", Room.class).list();
            session.getTransaction().commit();
            return rooms;
        }
    }

    public static Room getRoomById(int roomID) {
        try (Session session = getFactory().openSession()) {
            return session.get(Room.class, roomID);
        }
    }

    public static void saveRoom(Room room) {
        try (Session session = getFactory().openSession()) {
            session.beginTransaction();
            session.persist(room);
            session.getTransaction().commit();
        }
    }

    public static void updateRoom(Room room) {
        try (Session session = getFactory().openSession()) {
            session.beginTransaction();
            session.merge(room);
            session.getTransaction().commit();
        }
    }

    // ── GUESTS ────────────────────────────────────────────────────────────────

    public static List<Guest> getGuests() {
        try (Session session = getFactory().openSession()) {
            session.beginTransaction();
            List<Guest> guests = session.createQuery(
                    "select distinct g from Guest g join Reservation r on r.guestID = g.guestID",
                    Guest.class).list();
            session.getTransaction().commit();
            return guests;
        }
    }

    public static Guest getGuestById(int guestID) {
        try (Session session = getFactory().openSession()) {
            return session.get(Guest.class, guestID);
        }
    }

    public static void saveGuest(Guest guest) {
        try (Session session = getFactory().openSession()) {
            session.beginTransaction();
            session.persist(guest);
            session.getTransaction().commit();
        }
    }

    public static void updateGuest(Guest guest) {
        try (Session session = getFactory().openSession()) {
            session.beginTransaction();
            session.merge(guest);
            session.getTransaction().commit();
        }
    }

    // ── RESERVATIONS ─────────────────────────────────────────────────────────

    public static List<Reservation> getReservations() {
        try (Session session = getFactory().openSession()) {
            session.beginTransaction();
            List<Reservation> list = session
                    .createQuery("from Reservation", Reservation.class).list();
            session.getTransaction().commit();
            return list;
        }
    }

    public static Reservation getReservationById(int reservationID) {
        try (Session session = getFactory().openSession()) {
            return session.get(Reservation.class, reservationID);
        }
    }

    public static void saveReservation(Reservation reservation) {
        try (Session session = getFactory().openSession()) {
            session.beginTransaction();
            session.persist(reservation);
            session.getTransaction().commit();
        }
    }

    public static void updateReservation(Reservation reservation) {
        try (Session session = getFactory().openSession()) {
            session.beginTransaction();
            session.merge(reservation);
            session.getTransaction().commit();
        }
    }

    public static void deleteReservation(int reservationID) {
        try (Session session = getFactory().openSession()) {
            session.beginTransaction();
            session.createMutationQuery(
                    "delete from Reservation where reservationID = :id")
                    .setParameter("id", reservationID)
                    .executeUpdate();
            session.getTransaction().commit();
        }
    }

    // ── USERS ─────────────────────────────────────────────────────────────────

    public static List<User> getUsers() {
        try (Session session = getFactory().openSession()) {
            session.beginTransaction();
            List<User> users = session.createQuery("from User", User.class).list();
            session.getTransaction().commit();
            return users;
        }
    }

    /**
     * Returns the matching User or null if credentials are invalid.
     * Uses the correct HQL field names: u.username and u.password.
     */
    public static User validateLogin(String username, String password) {
        try (Session session = getFactory().openSession()) {
            return session.createQuery(
                    "from User u where u.username = :u and u.password = :p",
                    User.class)
                    .setParameter("u", username)
                    .setParameter("p", password)
                    .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean saveUser(User user) {
        // Reject duplicate usernames
        for (User u : getUsers()) {
            if (u.getUsername().equalsIgnoreCase(user.getUsername())) {
                return false;
            }
        }
        try (Session session = getFactory().openSession()) {
            session.beginTransaction();
            session.persist(user);
            session.getTransaction().commit();
        }
        return true;
    }

    public static boolean deleteUser(String username) {
        try (Session session = getFactory().openSession()) {
            session.beginTransaction();
            int deleted = session.createMutationQuery(
                    "delete from User where username = :u")
                    .setParameter("u", username)
                    .executeUpdate();
            session.getTransaction().commit();
            return deleted > 0;
        }
    }

    // ── Main connection test ──────────────────────────────────────────────────

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║   WildCat Hotel — DB Connection Test ║");
        System.out.println("╚══════════════════════════════════════╝");

        // JDBC test
        try {
            checkConnection();
            System.out.println("[PASS] JDBC connection SUCCESS");
        } catch (Exception e) {
            System.out.println("[FAIL] JDBC connection FAILED: " + e.getMessage());
            return;
        }

        // Rooms test
        try {
            List<Room> rooms = getRooms();
            System.out.println("[PASS] Rooms loaded: " + rooms.size());
            rooms.stream().limit(5).forEach(r ->
                    System.out.println("  " + r));
        } catch (Exception e) {
            System.out.println("[FAIL] Rooms query FAILED: " + e.getMessage());
        }

        // Users test
        try {
            List<User> users = getUsers();
            System.out.println("[PASS] Users loaded: " + users.size());
            users.forEach(u -> System.out.println("  " + u));
        } catch (Exception e) {
            System.out.println("[FAIL] Users query FAILED: " + e.getMessage());
        }

        // Login test
        try {
            User user = validateLogin("admin", "admin123");
            if (user != null) {
                System.out.println("[PASS] Login SUCCESS — Welcome " + user.getUsername());
            } else {
                System.out.println("[FAIL] Invalid credentials");
            }
        } catch (Exception e) {
            System.out.println("[FAIL] Login test FAILED: " + e.getMessage());
        }

        System.out.println("══════════════════════════════════════════");
        System.out.println("All tests complete!");
        System.out.println("══════════════════════════════════════════");

        closeFactory();
    }
}