package com.hotel.wildcat_hotel.checkout;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;

import com.hotel.wildcat_hotel.guests.GuestsController;

public class CheckOutController {

    @FXML private TextField roomSearchField;
    @FXML private TextField guestNameField;
    @FXML private TextField checkInField;
    @FXML private TextField checkOutField;
    @FXML private TextField roomTypeField;
    @FXML private TextField roomCapacityField;
    @FXML private TextField numberOfDaysField;
    @FXML private TextField totalCostField;
    @FXML private Button    checkOutButton;

    private static final String DB_URL  = "jdbc:mysql://localhost:3306/hoteldb";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";

    // Holds loaded IDs for the checkout action
    private int loadedReservationId = -1;
    private int loadedRoomId        = -1;

    @FXML
    void handleSearch(ActionEvent event) {
        String roomIdText = roomSearchField.getText().trim();

        if (roomIdText.isEmpty()) {
            showAlert(AlertType.WARNING, "Missing Input", "Please enter a Room ID to search.");
            return;
        }

        int roomId;
        try {
            roomId = Integer.parseInt(roomIdText);
        } catch (NumberFormatException e) {
            showAlert(AlertType.WARNING, "Invalid Input", "Room ID must be a number.");
            return;
        }

        String sql = """
                SELECT
                    res.reservationID,
                    res.roomID,
                    CONCAT(g.first_name, ' ', g.last_name) AS guest_full_name,
                    DATE_FORMAT(res.check_in_date,  '%Y-%m-%d') AS checkin,
                    DATE_FORMAT(res.check_out_date, '%Y-%m-%d') AS checkout,
                    r.room_type AS room_type,
                    r.room_capacity AS room_capacity,
                    res.number_of_days AS number_of_days,
                    res.total_cost AS total_cost
                FROM reservation res
                JOIN guest g ON g.guestID = res.guestID
                JOIN room  r ON r.roomID  = res.roomID
                WHERE res.roomID = ?
                ORDER BY res.reservationID DESC
                LIMIT 1
                """;

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, roomId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                loadedReservationId = rs.getInt("reservationID");
                loadedRoomId        = rs.getInt("roomID");

                guestNameField.setText(rs.getString("guest_full_name"));
                checkInField  .setText(rs.getString("checkin"));
                checkOutField .setText(rs.getString("checkout"));
                roomTypeField.setText(rs.getString("room_type"));
                roomCapacityField.setText(rs.getString("room_capacity"));
                numberOfDaysField.setText(rs.getString("number_of_days"));
                totalCostField.setText(String.format("₱%.2f", rs.getDouble("total_cost")));

                checkOutButton.setDisable(false);
            } else {
                clearFields();
                loadedReservationId = -1;
                loadedRoomId        = -1;
                checkOutButton.setDisable(true);
                showAlert(AlertType.INFORMATION, "Not Found",
                        "No active reservation found for Room ID: " + roomId);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "DB Error", "Could not search reservation: " + e.getMessage());
        }
    }

    @FXML
    void handleCheckOut(ActionEvent event) {
        if (loadedReservationId == -1) {
            showAlert(AlertType.WARNING, "No Booking", "No reservation loaded. Please search first.");
            return;
        }

        String deleteResSql  = "DELETE FROM reservation WHERE reservationID = ?";
        String updateRoomSql = "UPDATE room SET status = 'AVAILABLE' WHERE roomID = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {

            try (PreparedStatement ps = conn.prepareStatement(deleteResSql)) {
                ps.setInt(1, loadedReservationId);
                ps.executeUpdate();
            }

            if (loadedRoomId != -1) {
                try (PreparedStatement ps = conn.prepareStatement(updateRoomSql)) {
                    ps.setInt(1, loadedRoomId);
                    ps.executeUpdate();
                }
            }

            showAlert(AlertType.INFORMATION, "Check-Out Complete",
                    "Guest has been successfully checked out from Room " + roomSearchField.getText().trim() + ".");

            clearFields();
            loadedReservationId = -1;
            loadedRoomId        = -1;
            checkOutButton.setDisable(true);
            GuestsController.refreshOpenView();

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "DB Error", "Could not complete check-out: " + e.getMessage());
        }
    }

    private void clearFields() {
        guestNameField.clear();
        checkInField  .clear();
        checkOutField .clear();
        roomTypeField.clear();
        roomCapacityField.clear();
        numberOfDaysField.clear();
        totalCostField.clear();
    }

    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}