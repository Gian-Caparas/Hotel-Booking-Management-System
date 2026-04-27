package com.hotel.wildcat_hotel.project;

import com.hotel.wildcat_hotel.hotel.Guest;
import com.hotel.wildcat_hotel.hotel.Room;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class DataBase {

    private static SessionFactory sessionFactory;

    private static SessionFactory getFactory() {
        if (sessionFactory == null || sessionFactory.isClosed()) {
            sessionFactory = new Configuration()
                    .configure("hibernate.cfg.xml")
                    .addAnnotatedClass(Room.class)
                    .addAnnotatedClass(Guest.class)
                    .addAnnotatedClass(User.class)
                    .buildSessionFactory();
        }
        return sessionFactory;
    }

    public static void checkConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3307/hotelDB?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
        Connection con = DriverManager.getConnection(url, "root", "");
        System.out.println("Connection success!");
        con.close();
    }

    // ===================== ROOMS =====================

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
                    "from Room r where r.isEmpty = true", Room.class).list();
            session.getTransaction().commit();
            return rooms;
        }
    }

    public static void saveRoom(Room room) {
        try (Session session = getFactory().openSession()) {
            session.beginTransaction();
            session.persist(room);
            session.getTransaction().commit();
        }
    }

    // ===================== GUESTS =====================

    public static List<Guest> getGuests() {
        try (Session session = getFactory().openSession()) {
            session.beginTransaction();
            List<Guest> guests = session.createQuery("from Guest", Guest.class).list();
            session.getTransaction().commit();
            return guests;
        }
    }

    // ===================== USERS =====================

    public static List<User> getUsers() {
        try (Session session = getFactory().openSession()) {
            session.beginTransaction();
            List<User> users = session.createQuery("from User", User.class).list();
            session.getTransaction().commit();
            return users;
        }
    }

    public static boolean saveUser(User user) {
        List<User> users = getUsers();
        for (User u : users) {
            if (u.getUsername().equals(user.getUsername())) return false;
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

    // ===================== DB TEST (run this to verify connection) =====================

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║   WildCat Hotel — DB Connection Test ║");
        System.out.println("╚══════════════════════════════════════╝");

        // Test 1: Raw JDBC
        try {
            checkConnection();
            System.out.println("[PASS] JDBC connection to XAMPP MySQL: SUCCESS");
        } catch (Exception e) {
            System.out.println("[FAIL] JDBC connection FAILED: " + e.getMessage());
            System.out.println("       → Make sure XAMPP MySQL is running on port 3307");
            return;
        }

        // Test 2: Load rooms via Hibernate
        try {
            List<Room> rooms = getRooms();
            System.out.println("[PASS] Rooms loaded: " + rooms.size() + " total");
            rooms.stream().limit(5).forEach(r ->
                System.out.println("       Room #" + r.getRoomID()
                    + " | " + r.getRoomType()
                    + " | " + r.getRoomCapacity()
                    + " | Empty=" + r.isEmpty()));
            if (rooms.size() > 5) System.out.println("       ... and " + (rooms.size()-5) + " more");
        } catch (Exception e) {
            System.out.println("[FAIL] Rooms query FAILED: " + e.getMessage());
        }

        // Test 3: Load users via Hibernate
        try {
            List<User> users = getUsers();
            System.out.println("[PASS] Users loaded: " + users.size() + " total");
            users.forEach(u ->
                System.out.println("       User=" + u.getUsername() + " | Admin=" + u.isAdmin()));
        } catch (Exception e) {
            System.out.println("[FAIL] Users query FAILED: " + e.getMessage());
        }

        // Test 4: Login validation
        try {
            User testUser = new User("admin", "admin123", false);
            boolean valid   = User.isUserValid(testUser);
            boolean isAdmin = User.isUserAdmin(testUser);
            System.out.println("[PASS] Login 'admin/admin123': valid=" + valid + " | isAdmin=" + isAdmin);
        } catch (Exception e) {
            System.out.println("[FAIL] Login validation FAILED: " + e.getMessage());
        }

        System.out.println("══════════════════════════════════════════");
        System.out.println("  All tests complete!");
        System.out.println("══════════════════════════════════════════");
    }
}
