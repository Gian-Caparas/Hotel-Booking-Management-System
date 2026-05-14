package com.hotel.wildcat_hotel.checkin;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import com.hotel.wildcat_hotel.core.HotelApplicationContext;
import com.hotel.wildcat_hotel.hotel.Guest;
import com.hotel.wildcat_hotel.hotel.Reservation;
import com.hotel.wildcat_hotel.hotel.Room;
import com.hotel.wildcat_hotel.service.GuestService;
import com.hotel.wildcat_hotel.service.ReservationService;
import com.hotel.wildcat_hotel.service.RoomService;
import com.hotel.wildcat_hotel.login.LoginController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.util.Callback;

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

    private RoomService roomService;
    private GuestService guestService;
    private ReservationService reservationService;

    // ── Initialize ComboBoxes ─────────────────────────────────────────────────

    @FXML
    public void initialize() {
        roomService = HotelApplicationContext.getDefault().getRoomService();
        guestService = HotelApplicationContext.getDefault().getGuestService();
        reservationService = HotelApplicationContext.getDefault().getReservationService();

        roomTypeComboBox.getItems().addAll("Economy", "Normal", "Vip");
        roomCapacityComboBox.getItems().addAll("Single", "Double", "Triple");

        // Block past dates on the Check-In DatePicker
        checkInDatePicker.setDayCellFactory(buildPastDateBlocker());

        // Whenever the check-in date changes, also update the
        // check-out picker so it can never be before or equal to check-in.
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

        // Must be logged in to create a booking tied to a user
        if (LoginController.currentUser == null) {
            showAlert("Authentication Required", "Please login before checking in a guest.");
            return;
        }

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

        Room selectedRoom = roomService.getAvailableRooms().stream()
                .filter(room -> roomType.equals(room.getRoomType())
                             && roomCapacity.equals(room.getRoomCapacity()))
                .findFirst()
                .orElse(null);

        if (selectedRoom == null) {
            showAlert("No Rooms Available",
                    "No available " + roomType + " " + roomCapacity + " rooms.");
            return;
        }

        long numberOfDays = ChronoUnit.DAYS.between(checkIn, checkOut);
        if (numberOfDays <= 0) numberOfDays = 1;
        double totalCost = selectedRoom.getRoomRate() * numberOfDays;

        Timestamp tsCheckIn  = Timestamp.valueOf(checkIn.atStartOfDay());
        Timestamp tsCheckOut = Timestamp.valueOf(checkOut.atStartOfDay());

        Guest guest = new Guest(
                selectedRoom.getRoomID(),
                firstName,
                lastName,
                email,
                phone,
                city,
                nationality);

        try {
            Guest createdGuest = guestService.create(guest);
            if (createdGuest == null) {
                showAlert("Database Error", "Unable to save guest information.");
                return;
            }

                int userId = 0;
                if (LoginController.currentUser != null) {
                    userId = HotelApplicationContext.getDefault()
                            .getUserService()
                            .findByUsername(LoginController.currentUser.getUsername())
                            .map(u -> u.getUserID())
                            .orElse(LoginController.currentUser.getUserID());
                }

                Reservation reservation = new Reservation(
                    createdGuest.getGuestID(),
                    selectedRoom.getRoomID(), userId,
                    tsCheckIn,
                    tsCheckOut,
                    (int) numberOfDays,
                    totalCost);

            Reservation createdReservation = reservationService.create(reservation);
            if (createdReservation == null) {
                showAlert("Database Error", "Unable to save reservation.");
                return;
            }

            selectedRoom.setStatus("OCCUPIED");
            roomService.update(selectedRoom);

            showAlert("Check-In Successful",
                    "Guest checked into Room #" + selectedRoom.getRoomID() +
                    "\nDuration : " + numberOfDays + " night(s)" +
                    "\nTotal Cost: ₱" + String.format("%.2f", totalCost));

            clearFields();
        } catch (IllegalArgumentException ex) {
            showAlert("Validation Error", ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            showAlert("Database Error", ex.getMessage());
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