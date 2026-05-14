package com.hotel.wildcat_hotel.cancelbooking;

import java.util.Optional;

import com.hotel.wildcat_hotel.core.HotelApplicationContext;
import com.hotel.wildcat_hotel.guests.GuestsController;
import com.hotel.wildcat_hotel.hotel.Guest;
import com.hotel.wildcat_hotel.hotel.Reservation;
import com.hotel.wildcat_hotel.hotel.Room;
import com.hotel.wildcat_hotel.login.LoginController;
import com.hotel.wildcat_hotel.service.GuestService;
import com.hotel.wildcat_hotel.service.ReservationService;
import com.hotel.wildcat_hotel.service.RoomService;

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

    private RoomService roomService;
    private GuestService guestService;
    private ReservationService reservationService;

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
        roomService = HotelApplicationContext.getDefault().getRoomService();
        guestService = HotelApplicationContext.getDefault().getGuestService();
        reservationService = HotelApplicationContext.getDefault().getReservationService();

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
        Optional<Reservation> maybeReservation = reservationService.findLatestByRoomId(roomId);

        if (maybeReservation.isEmpty()) {
            clearFields();
            loadedReservationId = -1;
            loadedRoomId = -1;
            confirmCancelButton.setDisable(true);
            showAlert(AlertType.INFORMATION, "Not Found",
                    "No active reservation found for Room ID: " + roomId);
            return;
        }

        populateFields(maybeReservation.get());
        confirmCancelButton.setDisable(false);
    }

    // ── CUSTOMER: auto-load their own latest reservation ────────────
    private void loadReservationForCurrentCustomer() {
        if (LoginController.currentUser == null) {
            showAlert(AlertType.WARNING, "Session Error", "No logged-in user.");
            confirmCancelButton.setDisable(true);
            return;
        }

        String email = LoginController.currentUser.getEmail();
        Optional<Guest> maybeGuest = guestService.findByEmail(email);

        if (maybeGuest.isEmpty()) {
            showAlert(AlertType.INFORMATION, "No Booking", "You have no recorded guest profile.");
            confirmCancelButton.setDisable(true);
            return;
        }

        Optional<Reservation> maybeReservation = reservationService.findLatestByGuestId(maybeGuest.get().getGuestID());
        if (maybeReservation.isEmpty()) {
            showAlert(AlertType.INFORMATION, "No Booking", "You have no active reservation.");
            confirmCancelButton.setDisable(true);
            return;
        }

        populateFields(maybeReservation.get());
        confirmCancelButton.setDisable(false);
    }

    private void populateFields(Reservation reservation) {
        loadedReservationId = reservation.getReservationID();
        loadedRoomId = reservation.getRoomID();

        Optional<Guest> maybeGuest = guestService.getById(reservation.getGuestID());
        Optional<Room> maybeRoom = roomService.getById(reservation.getRoomID());

        firstNameField.setText(maybeGuest.map(Guest::getFirstName).orElse(""));
        lastNameField.setText(maybeGuest.map(Guest::getLastName).orElse(""));
        roomTypeField.setText(maybeRoom.map(Room::getRoomType).orElse(""));
        roomCapacityField.setText(maybeRoom.map(Room::getRoomCapacity).orElse(""));
        checkInField.setText(reservation.getCheckInDate().toLocalDateTime().toLocalDate().toString());
        checkOutField.setText(reservation.getCheckOutDate().toLocalDateTime().toLocalDate().toString());
        totalCostField.setText(String.valueOf(reservation.getTotalCost()));
        numDaysField.setText(String.valueOf(reservation.getNumberOfDays()));
    }

    // ── Confirm Cancel ───────────────────────────────────────────────
    @FXML
    private void handleCancelBooking() {
        if (loadedReservationId == -1) {
            showAlert(AlertType.WARNING, "No Booking", "No reservation loaded to cancel.");
            return;
        }

        boolean deleted = reservationService.delete(loadedReservationId);
        if (!deleted) {
            showAlert(AlertType.ERROR, "Cancellation Failed",
                    "Unable to cancel reservation #" + loadedReservationId + ".");
            return;
        }

        roomService.getById(loadedRoomId).ifPresent(room -> {
            room.setStatus("AVAILABLE");
            roomService.update(room);
        });

        showAlert(AlertType.INFORMATION, "Cancelled",
                "Booking #" + loadedReservationId + " has been successfully cancelled.");

        clearFields();
        loadedReservationId = -1;
        loadedRoomId = -1;
        confirmCancelButton.setDisable(true);

        if (guestsController != null) {
            guestsController.refresh();
        }
        GuestsController.refreshOpenView();
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