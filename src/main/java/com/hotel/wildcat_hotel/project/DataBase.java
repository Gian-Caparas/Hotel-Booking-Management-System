package com.hotel.wildcat_hotel.project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.hotel.wildcat_hotel.hotel.Guest;
import com.hotel.wildcat_hotel.hotel.Room;

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

    // ================= JDBC CONNECTION TEST =================

    public static void checkConnection() throws SQLException {

        String url =
                "jdbc:mysql://localhost:3306/hotelDB" +
                "?useSSL=false" +
                "&serverTimezone=UTC" +
                "&allowPublicKeyRetrieval=true";

        Connection con =
                DriverManager.getConnection(url, "root", "");

        System.out.println("Connection success!");

        con.close();
    }

    // ===================== ROOMS =====================

    public static List<Room> getRooms() {

        try (Session session = getFactory().openSession()) {

            session.beginTransaction();

            List<Room> rooms =
                    session.createQuery("from Room", Room.class)
                            .list();

            session.getTransaction().commit();

            return rooms;
        }
    }

    public static List<Room> getAvailableRooms() {

        try (Session session = getFactory().openSession()) {

            session.beginTransaction();

            List<Room> rooms =
                    session.createQuery(
                            "from Room r where r.isEmpty = true",
                            Room.class)
                            .list();

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

            List<Guest> guests =
                    session.createQuery("from Guest", Guest.class)
                            .list();

            session.getTransaction().commit();

            return guests;
        }
    }

    // ===================== USERS =====================

    public static List<User> getUsers() {

        try (Session session = getFactory().openSession()) {

            session.beginTransaction();

            List<User> users =
                    session.createQuery("from User", User.class)
                            .list();

            session.getTransaction().commit();

            return users;
        }
    }

    public static boolean saveUser(User user) {

        List<User> users = getUsers();

        for (User u : users) {

            if (u.getUsername()
                    .equalsIgnoreCase(user.getUsername())) {

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

            int deleted =
                    session.createMutationQuery(
                                    "delete from User where username = :u")
                            .setParameter("u", username)
                            .executeUpdate();

            session.getTransaction().commit();

            return deleted > 0;
        }
    }

    // ===================== MAIN TEST =====================

    public static void main(String[] args) {

        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║   WildCat Hotel — DB Connection Test ║");
        System.out.println("╚══════════════════════════════════════╝");

        // ================= JDBC TEST =================

        try {

            checkConnection();

            System.out.println(
                    "[PASS] JDBC connection SUCCESS");

        } catch (Exception e) {

            System.out.println(
                    "[FAIL] JDBC connection FAILED");

            System.out.println(e.getMessage());

            return;
        }

        // ================= ROOM TEST =================

        try {

            List<Room> rooms = getRooms();

            System.out.println(
                    "[PASS] Rooms loaded: "
                            + rooms.size());

            rooms.stream()
                    .limit(5)
                    .forEach(r ->

                            System.out.println(
                                    "Room #" + r.getRoomID()
                                            + " | " + r.getRoomType()
                                            + " | Capacity="
                                            + r.getRoomCapacity()
                                            + " | Empty="
                                            + r.isEmpty()));

        } catch (Exception e) {

            System.out.println(
                    "[FAIL] Rooms query FAILED");

            System.out.println(e.getMessage());
        }

        // ================= USER TEST =================

        try {

            List<User> users = getUsers();

            System.out.println(
                    "[PASS] Users loaded: "
                            + users.size());

            users.forEach(u ->

                    System.out.println(
                            "User=" + u.getUsername()
                                    + " | Role="
                                    + u.getRole()
                                    + " | Admin="
                                    + u.isAdmin()));

        } catch (Exception e) {

            System.out.println(
                    "[FAIL] Users query FAILED");

            System.out.println(e.getMessage());
        }

        // ================= LOGIN TEST =================

        try {

            User testUser =
                    new User(
                            "admin",
                            "admin123",
                            "Admin"
                    );

            boolean valid =
                    User.isUserValid(testUser);

            boolean isAdmin =
                    User.isUserAdmin(testUser);

            System.out.println(
                    "[PASS] Login Test → valid="
                            + valid
                            + " | isAdmin="
                            + isAdmin);

        } catch (Exception e) {

            System.out.println(
                    "[FAIL] Login validation FAILED");

            System.out.println(e.getMessage());
        }

        System.out.println(
                "══════════════════════════════════════════");

        System.out.println(
                "All tests complete!");

        System.out.println(
                "══════════════════════════════════════════");
    }
}