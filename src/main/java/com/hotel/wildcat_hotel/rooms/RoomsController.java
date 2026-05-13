package com.hotel.wildcat_hotel.rooms;

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

public class RoomsController {

    @FXML private TextField usernamefield;

    @FXML private TableView<RoomModel>             usersTable;
    @FXML private TableColumn<RoomModel, Integer>  usernameColumn;   // Room ID
    @FXML private TableColumn<RoomModel, String>   roleColumn;       // Room Type
    @FXML private TableColumn<RoomModel, String>   usernameColumn1;  // Room Capacity
    @FXML private TableColumn<RoomModel, String>   usernameColumn2;  // Status
    @FXML private TableColumn<RoomModel, String>   usernameColumn3;  // Occupied By
    @FXML private TableColumn<RoomModel, String>   usernameColumn4;  // Check-in
    @FXML private TableColumn<RoomModel, String>   usernameColumn5;  // Check-out

    // ── DB credentials ──────────────────────────────────────────────
    private static final String DB_URL  = "jdbc:mysql://localhost:3306/hoteldb";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";          // your phpMyAdmin password (usually blank for XAMPP)

    // ── Auto-called when the FXML loads ─────────────────────────────
    @FXML
    public void initialize() {
        usernameColumn .setCellValueFactory(new PropertyValueFactory<>("roomId"));
        roleColumn     .setCellValueFactory(new PropertyValueFactory<>("roomType"));
        usernameColumn1.setCellValueFactory(new PropertyValueFactory<>("roomCapacity"));
        usernameColumn2.setCellValueFactory(new PropertyValueFactory<>("status"));
        usernameColumn3.setCellValueFactory(new PropertyValueFactory<>("occupiedBy"));
        usernameColumn4.setCellValueFactory(new PropertyValueFactory<>("checkin"));
        usernameColumn5.setCellValueFactory(new PropertyValueFactory<>("checkout"));

        loadRooms(null);   // show all rooms on startup
    }

    // ── Search button ────────────────────────────────────────────────
    @FXML
    private void handleSearchUser() {
        String keyword = usernamefield.getText().trim();
        loadRooms(keyword.isEmpty() ? null : keyword);
    }

    // ── Main query ───────────────────────────────────────────────────
    private void loadRooms(String keyword) {
        ObservableList<RoomModel> data = FXCollections.observableArrayList();

        // LEFT JOIN so rooms with NO reservation still appear
        String sql = """
                SELECT
                    r.roomID,
                    r.room_type,
                    r.room_capacity,
                    r.status,
                    CASE
                        WHEN res.guestID IS NOT NULL
                        THEN CONCAT(g.first_name, ' ', g.last_name)
                        ELSE 'NULL'
                    END AS occupied_by,
                    COALESCE(DATE_FORMAT(res.check_in_date,  '%Y-%m-%d'), '') AS checkin,
                    COALESCE(DATE_FORMAT(res.check_out_date, '%Y-%m-%d'), '') AS checkout
                FROM room r
                LEFT JOIN reservation res ON res.roomID = r.roomID
                LEFT JOIN guest g         ON g.guestID  = res.guestID
                """;

        // Optional search filter
        if (keyword != null) {
            sql += """
                    WHERE r.roomID   LIKE ?
                       OR r.room_type LIKE ?
                       OR CONCAT(g.first_name, ' ', g.last_name) LIKE ?
                    """;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (keyword != null) {
                String like = "%" + keyword + "%";
                ps.setString(1, like);
                ps.setString(2, like);
                ps.setString(3, like);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                data.add(new RoomModel(
                        rs.getInt("roomID"),
                        rs.getString("room_type"),
                        rs.getString("room_capacity"),
                        rs.getString("status"),
                        rs.getString("occupied_by"),
                        rs.getString("checkin"),
                        rs.getString("checkout")
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
        private final String status;
        private final String occupiedBy;
        private final String checkin;
        private final String checkout;

        public RoomModel(int roomId, String roomType, String roomCapacity,
                         String status, String occupiedBy,
                         String checkin, String checkout) {
            this.roomId       = roomId;
            this.roomType     = roomType;
            this.roomCapacity = roomCapacity;
            this.status       = status;
            this.occupiedBy   = occupiedBy;
            this.checkin      = checkin;
            this.checkout     = checkout;
        }

        public int    getRoomId()       { return roomId; }
        public String getRoomType()     { return roomType; }
        public String getRoomCapacity() { return roomCapacity; }
        public String getStatus()       { return status; }
        public String getOccupiedBy()   { return occupiedBy; }
        public String getCheckin()      { return checkin; }
        public String getCheckout()     { return checkout; }
    }
}