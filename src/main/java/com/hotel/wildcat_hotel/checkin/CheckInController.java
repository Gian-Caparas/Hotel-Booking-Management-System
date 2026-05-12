package com.hotel.wildcat_hotel.checkin;

import com.hotel.wildcat_hotel.project.DataBase;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.util.Callback;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class CheckInController {

    @FXML private TextField     firstNameField;
    @FXML private TextField     lastNameField;
    @FXML private TextField     emailField;
    @FXML private TextField     phoneField;
    @FXML private TextField     cityField;
    @FXML private TextField     nationalityField;
    @FXML private ComboBox<String> roomTypeComboBox;
    @FXML private ComboBox<String> roomCapacityComboBox;
    @FXML private DatePicker    checkInDatePicker;
    @FXML private DatePicker    checkOutDatePicker;
    @FXML private Button        checkInButton;

    // ── Initialize ComboBoxes ─────────────────────────────────────────────────

    @FXML
    public void initialize() {
        roomTypeComboBox.getItems().addAll("Economy", "Normal", "Vip");
        roomCapacityComboBox.getItems().addAll("Single", "Double", "Triple");

        // Block past dates on the Check-In DatePicker
        checkInDatePicker.setDayCellFactory(buildPastDateBlocker());

        // Whenever the check-in date changes, also update the
        // check-out picker so it can never be before or equal to check-in.
        checkInDatePicker.valueProperty().addListener((obs, oldDate, newCheckIn) -> {
            if (newCheckIn != null) {
                // Reset check-out if it is no longer valid
                LocalDate currentCheckOut = checkOutDatePicker.getValue();
                if (currentCheckOut != null && !currentCheckOut.isAfter(newCheckIn)) {
                    checkOutDatePicker.setValue(null);
                }
                // Block check-out dates that are <= check-in date
                checkOutDatePicker.setDayCellFactory(picker -> new DateCell() {
                    @Override
                    public void updateItem(LocalDate date, boolean empty) {
                        super.updateItem(date, empty);
                        if (date != null && !date.isAfter(newCheckIn)) {
                            setDisable(true);
                            setStyle("-fx-background-color: #d3d3d3; -fx-text-fill: #a0a0a0;");
                        }
                    }
                });
            }
        });

        // Block past dates on the Check-Out DatePicker as a baseline
        checkOutDatePicker.setDayCellFactory(buildPastDateBlocker());
    }

    /**
     * Returns a DayCellFactory that disables and grays out every date
     * strictly before today (LocalDate.now()).
     */
    private Callback<DatePicker, DateCell> buildPastDateBlocker() {
        return picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (date != null && date.isBefore(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #d3d3d3; -fx-text-fill: #a0a0a0;");
                }
            }
        };
    }

    // ── Handle Check-In ───────────────────────────────────────────────────────

    @FXML
    private void handleCheckIn(ActionEvent event) {

        // --- Collect field values ---
        String firstName   = firstNameField.getText().trim();
        String lastName    = lastNameField.getText().trim();
        String email       = emailField.getText().trim();
        String phone       = phoneField.getText().trim();
        String city        = cityField.getText().trim();
        String nationality = nationalityField.getText().trim();
        String roomType    = roomTypeComboBox.getValue();
        String roomCapacity= roomCapacityComboBox.getValue();
        LocalDate checkIn  = checkInDatePicker.getValue();
        LocalDate checkOut = checkOutDatePicker.getValue();

        // --- Validation ---
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()
                || phone.isEmpty() || city.isEmpty() || nationality.isEmpty()
                || roomType == null || roomCapacity == null
                || checkIn == null || checkOut == null) {
            showAlert("Missing Information", "Please fill out all fields.");
            return;
        }

        if (!checkOut.isAfter(checkIn)) {
            showAlert("Date Error", "Check-out date must be after check-in date.");
            return;
        }

        // Guard: reject a check-in date that is in the past
        if (checkIn.isBefore(LocalDate.now())) {
            showAlert("Invalid Check-In Date",
                    "Check-in date cannot be in the past.\nPlease select today or a future date.");
            return;
        }

        try (Connection con = DataBase.getConnection()) {

            // ── 1. Find an available room ─────────────────────────────────────
            String roomSQL =
                    "SELECT roomID, room_rate FROM room " +
                    "WHERE status = 'AVAILABLE' " +
                    "  AND room_type = ? " +
                    "  AND room_capacity = ? " +
                    "LIMIT 1";

            int    roomID   = -1;
            double roomRate = 0;

            try (PreparedStatement roomStmt = con.prepareStatement(roomSQL)) {
                roomStmt.setString(1, roomType);
                roomStmt.setString(2, roomCapacity);

                try (ResultSet rs = roomStmt.executeQuery()) {
                    if (!rs.next()) {
                        showAlert("No Rooms Available",
                                "No available " + roomType + " " + roomCapacity + " rooms.");
                        return;
                    }
                    roomID   = rs.getInt("roomID");
                    roomRate = rs.getDouble("room_rate");
                }
            }

            // ── 2. Compute stay length and cost ───────────────────────────────
            long numberOfDays = ChronoUnit.DAYS.between(checkIn, checkOut);
            if (numberOfDays <= 0) numberOfDays = 1;
            double totalCost = roomRate * numberOfDays;

            Timestamp tsCheckIn  = Timestamp.valueOf(checkIn.atStartOfDay());
            Timestamp tsCheckOut = Timestamp.valueOf(checkOut.atStartOfDay());

            // ── 3. Insert into guest (personal info only) ─────────────────────
            String insertGuestSQL =
                    "INSERT INTO guest " +
                    "(roomID, first_name, last_name, email, phone_no, city, nationality) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

            int newGuestID;
            try (PreparedStatement guestStmt = con.prepareStatement(
                    insertGuestSQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
                guestStmt.setInt(1, roomID);
                guestStmt.setString(2, firstName);
                guestStmt.setString(3, lastName);
                guestStmt.setString(4, email);
                guestStmt.setString(5, phone);
                guestStmt.setString(6, city);
                guestStmt.setString(7, nationality);
                guestStmt.executeUpdate();

                try (ResultSet keys = guestStmt.getGeneratedKeys()) {
                    if (!keys.next()) {
                        showAlert("Database Error", "Failed to retrieve new guest ID.");
                        return;
                    }
                    newGuestID = keys.getInt(1);
                }
            }

            // ── 4. Insert into reservation (booking info) ─────────────────────
            String insertReservationSQL =
                    "INSERT INTO reservation " +
                    "(guestID, roomID, check_in_date, check_out_date, number_of_days, total_cost) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

            try (PreparedStatement resStmt = con.prepareStatement(insertReservationSQL)) {
                resStmt.setInt(1, newGuestID);
                resStmt.setInt(2, roomID);
                resStmt.setTimestamp(3, tsCheckIn);
                resStmt.setTimestamp(4, tsCheckOut);
                resStmt.setLong(5, numberOfDays);
                resStmt.setDouble(6, totalCost);
                resStmt.executeUpdate();
            }

            // ── 5. Mark room as OCCUPIED ──────────────────────────────────────
            String updateRoomSQL =
                    "UPDATE room SET status = 'OCCUPIED' WHERE roomID = ?";

            try (PreparedStatement updateStmt = con.prepareStatement(updateRoomSQL)) {
                updateStmt.setInt(1, roomID);
                updateStmt.executeUpdate();
            }

            // ── 6. Success ────────────────────────────────────────────────────
            showAlert("Check-In Successful",
                    "Guest checked into Room #" + roomID +
                    "\nDuration : " + numberOfDays + " night(s)" +
                    "\nTotal Cost: ₱" + String.format("%.2f", totalCost));

            clearFields();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Database Error", e.getMessage());
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void clearFields() {
        firstNameField.clear();
        lastNameField.clear();
        emailField.clear();
        phoneField.clear();
        cityField.clear();
        nationalityField.clear();
        roomTypeComboBox.setValue(null);
        roomCapacityComboBox.setValue(null);
        checkInDatePicker.setValue(null);
        checkOutDatePicker.setValue(null);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}