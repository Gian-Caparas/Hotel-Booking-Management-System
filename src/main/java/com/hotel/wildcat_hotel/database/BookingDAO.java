package com.hotel.wildcat_hotel.database;

import com.hotel.wildcat_hotel.model.Booking;
import com.hotel.wildcat_hotel.model.Room;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {
    // Create: Adds a new booking to the database [cite: 145]
    public static boolean createBooking(Booking b) {
        String sql = "INSERT INTO bookings (name, room_number, room_type, persons, check_in, check_out) VALUES (?,?,?,?,?,?)";
        try (Connection c = DBConnection.getConnection(); 
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, b.getName());
            ps.setString(2, b.getRoomNumber());
            ps.setString(3, b.getRoomType());
            ps.setInt(4, b.getPersons());
            ps.setString(5, b.getCheckIn());
            ps.setString(6, b.getCheckOut());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Read: Gets all bookings for the Admin Dashboard [cite: 145]
    public static List<Booking> getAllBookings() {
        List<Booking> list = new ArrayList<>();
        try (Connection c = DBConnection.getConnection(); 
             Statement st = c.createStatement(); 
             ResultSet rs = st.executeQuery("SELECT * FROM bookings")) {
            while (rs.next()) {
                Booking b = new Booking();
                b.setId(rs.getInt("id"));
                b.setName(rs.getString("name"));
                b.setRoomNumber(rs.getString("room_number"));
                b.setRoomType(rs.getString("room_type"));
                b.setPersons(rs.getInt("persons"));
                b.setCheckIn(rs.getString("check_in"));
                b.setCheckOut(rs.getString("check_out"));
                list.add(b);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}