package com.hotel.wildcat_hotel.cancelbooking;

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

public class CancelBookingController {

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField roomTypeField;
    @FXML private TextField roomCapacityField;
    @FXML private TextField checkInField;
    @FXML private TextField checkOutField;
    @FXML private TextField totalCostField;
    @FXML private TextField numDaysField;
    @FXML private Button    confirmCancelButton;

    // ── DB credentials ───────────────────────────────────────────────
    private static final String DB_URL  = "jdbc:mysql://localhost:3306/hoteldb";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";

    // Holds the loaded reservationID so we can delete it on confirm
    private int loadedReservationId = -1;

    // ── Auto-called when FXML loads ──────────────────────────────────
    @FXML
    public void initialize() {
        // Load the latest/active reservation automatically on open
        loadLatestReservation();
    }

    // ── Load the most recent reservation into the form fields ────────
    private void loadLatestReservation() {
        String sql = """
                SELECT
                    res.reservationID,
                    g.first_name,
                    g.last_name,
                    r.room_type,
                    r.room_capacity,
                    DATE_FORMAT(res.check_in_date,  '%Y-%m-%d') AS checkin,
                    DATE_FORMAT(res.check_out_date, '%Y-%m-%d') AS checkout,
                    res.total_cost,
                    res.number_of_days
                FROM reservation res
                JOIN guest g ON g.guestID = res.guestID
                JOIN room  r ON r.roomID  = res.roomID
                ORDER BY res.reservationID DESC
                LIMIT 1
                """;

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                loadedReservationId = rs.getInt("reservationID");
                firstNameField   .setText(rs.getString("first_name"));
                lastNameField    .setText(rs.getString("last_name"));
                roomTypeField    .setText(rs.getString("room_type"));
                roomCapacityField.setText(rs.getString("room_capacity"));
                checkInField     .setText(rs.getString("checkin"));
                checkOutField    .setText(rs.getString("checkout"));
                totalCostField   .setText(String.valueOf(rs.getDouble("total_cost")));
                numDaysField     .setText(String.valueOf(rs.getInt("number_of_days")));
            } else {
                showAlert(AlertType.INFORMATION, "No Booking", "No active reservation found.");
                confirmCancelButton.setDisable(true);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "DB Error", "Could not load reservation: " + e.getMessage());
        }
    }

    // ── Confirm Cancel Booking button ────────────────────────────────
    @FXML
    private void handleCancelBooking() {
        if (loadedReservationId == -1) {
            showAlert(AlertType.WARNING, "No Booking", "No reservation is loaded to cancel.");
            return;
        }

        // Get the roomID so we can set it back to AVAILABLE
        String getRoomSql = "SELECT roomID FROM reservation WHERE reservationID = ?";
        String deleteResSql = "DELETE FROM reservation WHERE reservationID = ?";
        String updateRoomSql = "UPDATE room SET status = 'AVAILABLE' WHERE roomID = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {

            // Step 1: Get the roomID
            int roomId = -1;
            try (PreparedStatement ps = conn.prepareStatement(getRoomSql)) {
                ps.setInt(1, loadedReservationId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) roomId = rs.getInt("roomID");
            }

            // Step 2: Delete the reservation
            try (PreparedStatement ps = conn.prepareStatement(deleteResSql)) {
                ps.setInt(1, loadedReservationId);
                ps.executeUpdate();
            }

            // Step 3: Set room back to AVAILABLE
            if (roomId != -1) {
                try (PreparedStatement ps = conn.prepareStatement(updateRoomSql)) {
                    ps.setInt(1, roomId);
                    ps.executeUpdate();
                }
            }

            showAlert(AlertType.INFORMATION, "Success",
                    "Booking #" + loadedReservationId + " has been successfully cancelled.");

            // Clear all fields after cancellation
            clearFields();
            loadedReservationId = -1;
            confirmCancelButton.setDisable(true);

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "DB Error", "Could not cancel booking: " + e.getMessage());
        }
    }

    // ── Helper: clear all fields ─────────────────────────────────────
    private void clearFields() {
        firstNameField   .clear();
        lastNameField    .clear();
        roomTypeField    .clear();
        roomCapacityField.clear();
        checkInField     .clear();
        checkOutField    .clear();
        totalCostField   .clear();
        numDaysField     .clear();
    }

    // ── Helper: show alert ───────────────────────────────────────────
    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}