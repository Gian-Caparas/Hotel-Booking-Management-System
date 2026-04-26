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
    // Singleton SessionFactory (create once, reuse)
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
        String url = "jdbc:mysql://localhost:3306/hotel?useSSL=false&serverTimezone=UTC";
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
        // Check for duplicate username
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
}
