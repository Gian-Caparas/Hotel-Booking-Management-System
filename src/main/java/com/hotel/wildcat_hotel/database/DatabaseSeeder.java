package com.hotel.wildcat_hotel.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseSeeder {
    public static void main(String[] args) {
        try (Connection conn = DBConnection.getConnection()) {
            System.out.println("Initializing Database Tables...");
            Statement st = conn.createStatement();

            // 1. Create Tables (Based on your SQL Schema)
            st.executeUpdate("CREATE TABLE users (id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY, username VARCHAR(50) NOT NULL UNIQUE, password VARCHAR(255) NOT NULL, role VARCHAR(10) NOT NULL)");
            st.executeUpdate("CREATE TABLE rooms (room_number VARCHAR(10) PRIMARY KEY, room_type VARCHAR(10) NOT NULL, capacity INTEGER NOT NULL)");
            st.executeUpdate("CREATE TABLE bookings (id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY, user_id INTEGER REFERENCES users(id), name VARCHAR(100) NOT NULL, room_number VARCHAR(10) REFERENCES rooms(room_number), room_type VARCHAR(10) NOT NULL, persons INTEGER NOT NULL, check_in DATE NOT NULL, check_out DATE NOT NULL, status VARCHAR(20) DEFAULT 'CONFIRMED')");

            System.out.println("Tables created. Seeding room data...");

            // 2. Insert Single Rooms (101-120)
            for (int i = 101; i <= 120; i++) {
                st.executeUpdate("INSERT INTO rooms VALUES ('" + i + "', 'SINGLE', 1)");
            }
            // 3. Insert Double Rooms (201-210)
            for (int i = 201; i <= 210; i++) {
                st.executeUpdate("INSERT INTO rooms VALUES ('" + i + "', 'DOUBLE', 2)");
            }
            // 4. Insert Deluxe Rooms (301-305)
            for (int i = 301; i <= 305; i++) {
                st.executeUpdate("INSERT INTO rooms VALUES ('" + i + "', 'DELUXE', 4)");
            }

            System.out.println("Seeding successful! Tables created and 35 rooms added.");
        } catch (SQLException e) {
            System.err.println("Database Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}