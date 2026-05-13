package com.hotel.wildcat_hotel.availableroom;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class AvailableRoomController {

    @FXML private TextField usernamefield;

    @FXML private TableView<RoomModel>            usersTable;
    @FXML private TableColumn<RoomModel, Integer> usernameColumn;   // Room ID
    @FXML private TableColumn<RoomModel, String>  roleColumn;       // Room Type
    @FXML private TableColumn<RoomModel, String>  usernameColumn1;  // Room Capacity
    @FXML private TableColumn<RoomModel, Double>  usernameColumn2;  // Room Rate

    // ── DB credentials ───────────────────────────────────────────────
    private static final String DB_URL  = "jdbc:mysql://localhost:3306/hoteldb";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";

    // ── Auto-called when FXML loads ──────────────────────────────────
    @FXML
    public void initialize() {
        usernameColumn .setCellValueFactory(new PropertyValueFactory<>("roomId"));
        roleColumn     .setCellValueFactory(new PropertyValueFactory<>("roomType"));
        usernameColumn1.setCellValueFactory(new PropertyValueFactory<>("roomCapacity"));
        usernameColumn2.setCellValueFactory(new PropertyValueFactory<>("roomRate"));

        loadAvailableRooms(null); // show all available rooms on startup
    }

    // ── Search button handler ────────────────────────────────────────
    @FXML
    private void handleSearchUser() {
        String keyword = usernamefield.getText().trim();
        loadAvailableRooms(keyword.isEmpty() ? null : keyword);
    }

    // ── Query: only rooms with status = 'AVAILABLE' ──────────────────
    private void loadAvailableRooms(String keyword) {
        ObservableList<RoomModel> data = FXCollections.observableArrayList();

        String sql = """
                SELECT roomID, room_type, room_capacity, room_rate
                FROM room
                WHERE status = 'AVAILABLE'
                """;

        if (keyword != null) {
            sql += """
                    AND (roomID    LIKE ?
                      OR room_type LIKE ?)
                    """;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (keyword != null) {
                String like = "%" + keyword + "%";
                ps.setString(1, like);
                ps.setString(2, like);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                data.add(new RoomModel(
                        rs.getInt("roomID"),
                        rs.getString("room_type"),
                        rs.getString("room_capacity"),
                        rs.getDouble("room_rate")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        usersTable.setItems(data);
    }

    // ── Row model ────────────────────────────────────────────────────
    public static class RoomModel {
        private final int    roomId;
        private final String roomType;
        private final String roomCapacity;
        private final double roomRate;

        public RoomModel(int roomId, String roomType, String roomCapacity, double roomRate) {
            this.roomId       = roomId;
            this.roomType     = roomType;
            this.roomCapacity = roomCapacity;
            this.roomRate     = roomRate;
        }

        public int    getRoomId()       { return roomId; }
        public String getRoomType()     { return roomType; }
        public String getRoomCapacity() { return roomCapacity; }
        public double getRoomRate()     { return roomRate; }
    }
}