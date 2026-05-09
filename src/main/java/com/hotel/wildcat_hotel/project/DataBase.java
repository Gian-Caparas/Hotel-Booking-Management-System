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

    // ================= DATABASE INFO =================

    private static final String DB_URL =
            "jdbc:mysql://localhost:3307/hoteldb"
                    + "?useSSL=false"
                    + "&serverTimezone=UTC"
                    + "&allowPublicKeyRetrieval=true";

    private static final String DB_USER =
            "root";

    private static final String DB_PASSWORD =
            "";

    // ================= HIBERNATE =================

    private static SessionFactory sessionFactory;

    private static SessionFactory getFactory() {

        if (sessionFactory == null ||
                sessionFactory.isClosed()) {

            sessionFactory =
                    new Configuration()
                            .configure("hibernate.cfg.xml")
                            .addAnnotatedClass(Room.class)
                            .addAnnotatedClass(Guest.class)
                            .addAnnotatedClass(User.class)
                            .buildSessionFactory();
        }

        return sessionFactory;
    }

    public static void closeFactory() {

        if (sessionFactory != null &&
                !sessionFactory.isClosed()) {

            sessionFactory.close();
        }
    }

    // ================= JDBC CONNECTION =================

    public static Connection getConnection()
            throws SQLException {

        return DriverManager.getConnection(
                DB_URL,
                DB_USER,
                DB_PASSWORD
        );
    }

    public static void checkConnection()
            throws SQLException {

        Connection con = getConnection();

        System.out.println(
                "Connection success!"
        );

        con.close();
    }

    // ===================== ROOMS =====================

    public static List<Room> getRooms() {

        try (Session session =
                     getFactory().openSession()) {

            session.beginTransaction();

            List<Room> rooms =
                    session.createQuery(
                                    "from Room",
                                    Room.class
                            )
                            .list();

            session.getTransaction().commit();

            return rooms;
        }
    }

    public static List<Room> getAvailableRooms() {

        try (Session session =
                     getFactory().openSession()) {

            session.beginTransaction();

            List<Room> rooms =
                    session.createQuery(
                                    "from Room r where r.status = 'AVAILABLE'",
                                    Room.class
                            )
                            .list();

            session.getTransaction().commit();

            return rooms;
        }
    }

    public static Room getRoomById(int roomID) {

        try (Session session =
                     getFactory().openSession()) {

            return session.get(
                    Room.class,
                    roomID
            );
        }
    }

    public static void saveRoom(Room room) {

        try (Session session =
                     getFactory().openSession()) {

            session.beginTransaction();

            session.persist(room);

            session.getTransaction().commit();
        }
    }

    public static void updateRoom(Room room) {

        try (Session session =
                     getFactory().openSession()) {

            session.beginTransaction();

            session.merge(room);

            session.getTransaction().commit();
        }
    }

    // ===================== GUESTS =====================

    public static List<Guest> getGuests() {

        try (Session session =
                     getFactory().openSession()) {

            session.beginTransaction();

            List<Guest> guests =
                    session.createQuery(
                                    "from Guest",
                                    Guest.class
                            )
                            .list();

            session.getTransaction().commit();

            return guests;
        }
    }

    public static void saveGuest(Guest guest) {

        try (Session session =
                     getFactory().openSession()) {

            session.beginTransaction();

            session.persist(guest);

            session.getTransaction().commit();
        }
    }

    // ===================== USERS =====================

    public static List<User> getUsers() {

        try (Session session =
                     getFactory().openSession()) {

            session.beginTransaction();

            List<User> users =
                    session.createQuery(
                                    "from User",
                                    User.class
                            )
                            .list();

            session.getTransaction().commit();

            return users;
        }
    }

    public static User validateLogin(
            String username,
            String password
    ) {

        try (Session session =
                     getFactory().openSession()) {

            return session.createQuery(
                            "from User " +
                                    "where username = :u " +
                                    "and password = :p",
                            User.class
                    )
                    .setParameter("u", username)
                    .setParameter("p", password)
                    .uniqueResult();

        } catch (Exception e) {

            e.printStackTrace();

            return null;
        }
    }

    public static boolean saveUser(User user) {

        List<User> users = getUsers();

        for (User u : users) {

            if (
                    u.getUsername()
                            .equalsIgnoreCase(
                                    user.getUsername()
                            )
            ) {

                return false;
            }
        }

        try (Session session =
                     getFactory().openSession()) {

            session.beginTransaction();

            session.persist(user);

            session.getTransaction().commit();
        }

        return true;
    }

    public static boolean deleteUser(
            String username
    ) {

        try (Session session =
                     getFactory().openSession()) {

            session.beginTransaction();

            int deleted =
                    session.createMutationQuery(
                                    "delete from User " +
                                            "where username = :u"
                            )
                            .setParameter("u", username)
                            .executeUpdate();

            session.getTransaction().commit();

            return deleted > 0;
        }
    }

    // ===================== MAIN TEST =====================

    public static void main(String[] args) {

        System.out.println(
                "╔══════════════════════════════════════╗"
        );

        System.out.println(
                "║   WildCat Hotel — DB Connection Test ║"
        );

        System.out.println(
                "╚══════════════════════════════════════╝"
        );

        // ================= JDBC TEST =================

        try {

            checkConnection();

            System.out.println(
                    "[PASS] JDBC connection SUCCESS"
            );

        } catch (Exception e) {

            System.out.println(
                    "[FAIL] JDBC connection FAILED"
            );

            System.out.println(
                    e.getMessage()
            );

            return;
        }

        // ================= ROOM TEST =================

        try {

            List<Room> rooms =
                    getRooms();

            System.out.println(
                    "[PASS] Rooms loaded: "
                            + rooms.size()
            );

            rooms.stream()
                    .limit(5)
                    .forEach(r ->

                            System.out.println(
                                    "Room #"
                                            + r.getRoomID()
                                            + " | "
                                            + r.getRoomType()
                                            + " | "
                                            + r.getRoomCapacity()
                                            + " | "
                                            + r.toString()
                            )
                    );

        } catch (Exception e) {

            System.out.println(
                    "[FAIL] Rooms query FAILED"
            );

            System.out.println(
                    e.getMessage()
            );
        }

        // ================= USER TEST =================

        try {

            List<User> users =
                    getUsers();

            System.out.println(
                    "[PASS] Users loaded: "
                            + users.size()
            );

            users.forEach(u ->

                    System.out.println(
                            "User="
                                    + u.getUsername()
                                    + " | Role="
                                    + u.getRole()
                                    + " | Admin="
                                    + u.isAdmin()
                    )
            );

        } catch (Exception e) {

            System.out.println(
                    "[FAIL] Users query FAILED"
            );

            System.out.println(
                    e.getMessage()
            );
        }

        // ================= LOGIN TEST =================

        try {

            User user =
                    validateLogin(
                            "admin",
                            "admin123"
                    );

            if (user != null) {

                System.out.println(
                        "[PASS] Login SUCCESS"
                );

                System.out.println(
                        "Welcome "
                                + user.getUsername()
                );

            } else {

                System.out.println(
                        "[FAIL] Invalid credentials"
                );
            }

        } catch (Exception e) {

            System.out.println(
                    "[FAIL] Login test FAILED"
            );

            System.out.println(
                    e.getMessage()
            );
        }

        System.out.println(
                "══════════════════════════════════════════"
        );

        System.out.println(
                "All tests complete!"
        );

        System.out.println(
                "══════════════════════════════════════════"
        );

        closeFactory();
    }
}