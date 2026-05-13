package com.hotel.wildcat_hotel.cancelbooking;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.hotel.wildcat_hotel.guests.GuestsController;
import com.hotel.wildcat_hotel.login.LoginController;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class CancelBookingController {

    // ── FXML fields ──────────────────────────────────────────────────
    @FXML private HBox      searchRow;          // visible for ADMIN / STAFF only
    @FXML private TextField roomIdSearchField;  // search input (admin/staff)

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

    // Holds IDs for the cancel action
    private int loadedReservationId = -1;
    private int loadedRoomId        = -1;

    /**
     * Inject this reference from your main controller / navigation code
     * so we can call refresh() after a cancellation:
     *   cancelBookingController.setGuestsController(guestsController);
     */
    private GuestsController guestsController;

    public void setGuestsController(GuestsController gc) {
        this.guestsController = gc;
    }

    // ── Auto-called when FXML loads ──────────────────────────────────
    @FXML
    public void initialize() {
        confirmCancelButton.setDisable(true);

        // ── Role check ───────────────────────────────────────────────
        String role = getCurrentUserRole();

        if ("CUSTOMER".equalsIgnoreCase(role)) {
            // Hide the search row entirely so it takes no space
            searchRow.setVisible(false);
            searchRow.setManaged(false);
            // Auto-load the customer's own reservation
            loadReservationForCurrentCustomer();
        }
        // Admin / Staff leave the view open — they must search manually
    }

    // ── ADMIN / STAFF: search by Room ID ────────────────────────────
    @FXML
    private void handleSearch() {
        String roomIdText = roomIdSearchField.getText().trim();

        if (roomIdText.isEmpty()) {
            showAlert(AlertType.WARNING, "Missing Input", "Please enter a Room ID.");
            return;
        }

        int roomId;
        try {
            roomId = Integer.parseInt(roomIdText);
        } catch (NumberFormatException e) {
            showAlert(AlertType.WARNING, "Invalid Input", "Room ID must be a number.");
            return;
        }

        loadReservationByRoomId(roomId);
    }

    // ── Shared: load reservation by roomID ──────────────────────────
    private void loadReservationByRoomId(int roomId) {
        String sql = """
                SELECT
                    res.reservationID,
                    res.roomID,
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
                WHERE res.roomID = ?
                ORDER BY res.reservationID DESC
                LIMIT 1
                """;

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, roomId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                populateFields(rs);
                confirmCancelButton.setDisable(false);
            } else {
                clearFields();
                loadedReservationId = -1;
                loadedRoomId        = -1;
                confirmCancelButton.setDisable(true);
                showAlert(AlertType.INFORMATION, "Not Found",
                        "No active reservation found for Room ID: " + roomId);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "DB Error", "Could not load reservation: " + e.getMessage());
        }
    }

    // ── CUSTOMER: auto-load their own latest reservation ────────────
    private void loadReservationForCurrentCustomer() {
        int guestId = getCurrentGuestId();

        if (guestId == -1) {
            showAlert(AlertType.WARNING, "Session Error", "Could not determine logged-in guest.");
            confirmCancelButton.setDisable(true);
            return;
        }

        String sql = """
                SELECT
                    res.reservationID,
                    res.roomID,
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
                WHERE res.guestID = ?
                ORDER BY res.reservationID DESC
                LIMIT 1
                """;

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, guestId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                populateFields(rs);
                confirmCancelButton.setDisable(false);
            } else {
                showAlert(AlertType.INFORMATION, "No Booking", "You have no active reservation.");
                confirmCancelButton.setDisable(true);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "DB Error", "Could not load your reservation: " + e.getMessage());
        }
    }

    // ── Shared: fill all form fields from a ResultSet row ───────────
    private void populateFields(ResultSet rs) throws SQLException {
        loadedReservationId = rs.getInt("reservationID");
        loadedRoomId        = rs.getInt("roomID");

        firstNameField   .setText(rs.getString("first_name"));
        lastNameField    .setText(rs.getString("last_name"));
        roomTypeField    .setText(rs.getString("room_type"));
        roomCapacityField.setText(rs.getString("room_capacity"));
        checkInField     .setText(rs.getString("checkin"));
        checkOutField    .setText(rs.getString("checkout"));
        totalCostField   .setText(String.valueOf(rs.getDouble("total_cost")));
        numDaysField     .setText(String.valueOf(rs.getInt("number_of_days")));
    }

    // ── Confirm Cancel ───────────────────────────────────────────────
    @FXML
    private void handleCancelBooking() {
        if (loadedReservationId == -1) {
            showAlert(AlertType.WARNING, "No Booking", "No reservation loaded to cancel.");
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

            showAlert(AlertType.INFORMATION, "Cancelled",
                    "Booking #" + loadedReservationId + " has been successfully cancelled.");

            clearFields();
            loadedReservationId = -1;
            loadedRoomId        = -1;
            confirmCancelButton.setDisable(true);

            // ── Refresh the Guests view so the cancelled guest disappears ──
            if (guestsController != null) {
                guestsController.refresh();
            }
            GuestsController.refreshOpenView();

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "DB Error", "Could not cancel booking: " + e.getMessage());
        }
    }

    // ── Helpers: replace with your real Session class calls ─────────

    /**
     * Returns the current user's role: "ADMIN", "STAFF", or "CUSTOMER".
     */
    private String getCurrentUserRole() {
        if (LoginController.currentUser == null) {
            return "";
        }
        return LoginController.currentUser.getRole();
    }

    /**
     * Returns the guestID linked to the currently logged-in customer.
     * The project does not store a direct user-to-guest relation, so the
     * latest reservation matching the logged-in user's email is used.
     */
    private int getCurrentGuestId() {
        if (LoginController.currentUser == null) {
            return -1;
        }

        String sql = """
                SELECT g.guestID
                FROM reservation res
                JOIN guest g ON g.guestID = res.guestID
                WHERE LOWER(g.email) = LOWER(?)
                ORDER BY res.reservationID DESC
                LIMIT 1
                """;

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, LoginController.currentUser.getEmail());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("guestID");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

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

    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}