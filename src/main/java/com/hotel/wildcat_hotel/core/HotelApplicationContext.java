package com.hotel.wildcat_hotel.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.hotel.wildcat_hotel.hotel.Guest;
import com.hotel.wildcat_hotel.hotel.Reservation;
import com.hotel.wildcat_hotel.hotel.Room;
import com.hotel.wildcat_hotel.project.User;
import com.hotel.wildcat_hotel.repository.GuestRepository;
import com.hotel.wildcat_hotel.repository.ReservationRepository;
import com.hotel.wildcat_hotel.repository.RoomRepository;
import com.hotel.wildcat_hotel.repository.UserRepository;
import com.hotel.wildcat_hotel.service.GuestService;
import com.hotel.wildcat_hotel.service.ReservationService;
import com.hotel.wildcat_hotel.service.RoomService;
import com.hotel.wildcat_hotel.service.UserService;
import com.hotel.wildcat_hotel.validation.GuestValidator;
import com.hotel.wildcat_hotel.validation.ReservationValidator;
import com.hotel.wildcat_hotel.validation.RoomValidator;
import com.hotel.wildcat_hotel.validation.UserValidator;

public final class HotelApplicationContext {

    // Use the same database name as in hibernate.cfg.xml (lowercase 'hoteldb')
    private static final String DB_URL = "jdbc:mysql://localhost:3306/hoteldb"
            + "?useSSL=false"
            + "&serverTimezone=UTC"
            + "&allowPublicKeyRetrieval=true";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    private static final HotelApplicationContext DEFAULT = new HotelApplicationContext();

    private final SessionFactory sessionFactory;
    private final RoomRepository roomRepository;
    private final GuestRepository guestRepository;
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;

    private final RoomService roomService;
    private final GuestService guestService;
    private final ReservationService reservationService;
    private final UserService userService;

    private HotelApplicationContext() {
        sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Room.class)
                .addAnnotatedClass(Guest.class)
                .addAnnotatedClass(Reservation.class)
                .addAnnotatedClass(User.class)
                .buildSessionFactory();

        roomRepository = new RoomRepository(sessionFactory);
        guestRepository = new GuestRepository(sessionFactory);
        reservationRepository = new ReservationRepository(sessionFactory);
        userRepository = new UserRepository(sessionFactory);

        roomService = new RoomService(roomRepository, new RoomValidator());
        guestService = new GuestService(guestRepository, new GuestValidator());
        reservationService = new ReservationService(reservationRepository, new ReservationValidator());
        userService = new UserService(userRepository, new UserValidator());
    }

    public static HotelApplicationContext getDefault() {
        return DEFAULT;
    }

    public RoomService getRoomService() {
        return roomService;
    }

    public GuestService getGuestService() {
        return guestService;
    }

    public ReservationService getReservationService() {
        return reservationService;
    }

    public UserService getUserService() {
        return userService;
    }

    public void checkConnection() throws SQLException {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            // connection opens and closes immediately
        }
    }

    public void close() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
    }
}