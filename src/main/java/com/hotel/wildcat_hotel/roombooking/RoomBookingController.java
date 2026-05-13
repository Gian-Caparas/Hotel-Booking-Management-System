package com.hotel.wildcat_hotel.roombooking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import com.hotel.wildcat_hotel.project.DataBase;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.util.Callback;

public class RoomBookingController {

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextField cityField;
    @FXML private TextField nationalityField;
    @FXML private ComboBox<String> roomTypeComboBox;
    @FXML private ComboBox<String> roomCapacityComboBox;
    @FXML private DatePicker checkInDatePicker;
    @FXML private DatePicker checkOutDatePicker;
    @FXML private Button bookRoomButton; // Gi-match sa fx:id="bookRoomButton"

    // ── Initialize ────────────────────────────────────────────────────────────

    @FXML
    public void initialize() {
        // Gi-match sa imong FXML categories
        roomTypeComboBox.getItems().addAll("Economy", "Normal", "Vip");
        roomCapacityComboBox.getItems().addAll("Single", "Double", "Triple");

        // Block past dates
        checkInDatePicker.setDayCellFactory(buildPastDateBlocker());

        // Update checkout constraints based on check-in selection
        checkInDatePicker.valueProperty().addListener((obs, oldDate, newCheckIn) -> {
            if (newCheckIn != null) {
                LocalDate currentCheckOut = checkOutDatePicker.getValue();
                if (currentCheckOut != null && !currentCheckOut.isAfter(newCheckIn)) {
                    checkOutDatePicker.setValue(null);
                }
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

        checkOutDatePicker.setDayCellFactory(buildPastDateBlocker());
    }

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

    // ── Handle Room Booking ───────────────────────────────────────────────────

    @FXML
    private void handleBookRoom(ActionEvent event) { // Gi-match sa onAction="#handleBookRoom"

        // --- Collect field values ---
        String firstName    = firstNameField.getText().trim();
        String lastName     = lastNameField.getText().trim();
        String email        = emailField.getText().trim();
        String phone        = phoneField.getText().trim();
        String city         = cityField.getText().trim();
        String nationality  = nationalityField.getText().trim();
        String roomType     = roomTypeComboBox.getValue();
        String roomCapacity = roomCapacityComboBox.getValue();
        LocalDate checkIn   = checkInDatePicker.getValue();
        LocalDate checkOut  = checkOutDatePicker.getValue();

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

        try (Connection con = DataBase.getConnection()) {

            // 1. Find an available room
            String roomSQL = "SELECT roomID, room_rate FROM room WHERE status = 'AVAILABLE' " +
                             "AND room_type = ? AND room_capacity = ? LIMIT 1";

            int roomID = -1;
            double roomRate = 0;

            try (PreparedStatement roomStmt = con.prepareStatement(roomSQL)) {
                roomStmt.setString(1, roomType);
                roomStmt.setString(2, roomCapacity);
                try (ResultSet rs = roomStmt.executeQuery()) {
                    if (!rs.next()) {
                        showAlert("No Rooms Available", "No available " + roomType + " " + roomCapacity + " rooms.");
                        return;
                    }
                    roomID = rs.getInt("roomID");
                    roomRate = rs.getDouble("room_rate");
                }
            }

            // 2. Compute costs
            long numberOfDays = ChronoUnit.DAYS.between(checkIn, checkOut);
            if (numberOfDays <= 0) numberOfDays = 1;
            double totalCost = roomRate * numberOfDays;

            Timestamp tsCheckIn  = Timestamp.valueOf(checkIn.atStartOfDay());
            Timestamp tsCheckOut = Timestamp.valueOf(checkOut.atStartOfDay());

            // 3. Insert Guest
            String insertGuestSQL = "INSERT INTO guest (roomID, first_name, last_name, email, phone_no, city, nationality) " +
                                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
            int newGuestID;
            try (PreparedStatement guestStmt = con.prepareStatement(insertGuestSQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
                guestStmt.setInt(1, roomID);
                guestStmt.setString(2, firstName);
                guestStmt.setString(3, lastName);
                guestStmt.setString(4, email);
                guestStmt.setString(5, phone);
                guestStmt.setString(6, city);
                guestStmt.setString(7, nationality);
                guestStmt.executeUpdate();
                try (ResultSet keys = guestStmt.getGeneratedKeys()) {
                    if (!keys.next()) return;
                    newGuestID = keys.getInt(1);
                }
            }

            // 4. Insert Reservation
            String insertResSQL = "INSERT INTO reservation (guestID, roomID, check_in_date, check_out_date, number_of_days, total_cost) " +
                                  "VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement resStmt = con.prepareStatement(insertResSQL)) {
                resStmt.setInt(1, newGuestID);
                resStmt.setInt(2, roomID);
                resStmt.setTimestamp(3, tsCheckIn);
                resStmt.setTimestamp(4, tsCheckOut);
                resStmt.setLong(5, numberOfDays);
                resStmt.setDouble(6, totalCost);
                resStmt.executeUpdate();
            }

            // 5. Update Room Status
            String updateRoomSQL = "UPDATE room SET status = 'OCCUPIED' WHERE roomID = ?";
            try (PreparedStatement updateStmt = con.prepareStatement(updateRoomSQL)) {
                updateStmt.setInt(1, roomID);
                updateStmt.executeUpdate();
            }

            showAlert("Booking Successful", "Room #" + roomID + " is now booked for " + firstName + "!");
            clearFields();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Database Error", e.getMessage());
        }
    }

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