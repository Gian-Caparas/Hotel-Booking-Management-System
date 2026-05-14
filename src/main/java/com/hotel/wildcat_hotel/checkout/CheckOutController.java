package com.hotel.wildcat_hotel.checkout;

import java.util.Optional;

import com.hotel.wildcat_hotel.core.HotelApplicationContext;
import com.hotel.wildcat_hotel.guests.GuestsController;
import com.hotel.wildcat_hotel.hotel.Reservation;
import com.hotel.wildcat_hotel.hotel.Room;
import com.hotel.wildcat_hotel.service.GuestService;
import com.hotel.wildcat_hotel.service.ReservationService;
import com.hotel.wildcat_hotel.service.RoomService;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

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

    private RoomService roomService;
    private ReservationService reservationService;
    private GuestService guestService;

    // Holds loaded IDs for the checkout action
    private int loadedReservationId = -1;
    private int loadedRoomId        = -1;

    @FXML
    public void initialize() {
        roomService = HotelApplicationContext.getDefault().getRoomService();
        reservationService = HotelApplicationContext.getDefault().getReservationService();
        guestService = HotelApplicationContext.getDefault().getGuestService();
        checkOutButton.setDisable(true);
    }

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

        Optional<Reservation> maybeReservation = reservationService.findLatestByRoomId(roomId);
        if (maybeReservation.isEmpty()) {
            clearFields();
            loadedReservationId = -1;
            loadedRoomId = -1;
            checkOutButton.setDisable(true);
            showAlert(AlertType.INFORMATION, "Not Found",
                    "No active reservation found for Room ID: " + roomId);
            return;
        }

        Reservation reservation = maybeReservation.get();
        loadedReservationId = reservation.getReservationID();
        loadedRoomId = reservation.getRoomID();

        String guestName = guestService.getById(reservation.getGuestID())
                .map(guest -> guest.getFirstName() + " " + guest.getLastName())
                .orElse("");

        Optional<Room> maybeRoom = roomService.getById(reservation.getRoomID());

        guestNameField.setText(guestName);
        checkInField.setText(reservation.getCheckInDate().toLocalDateTime().toLocalDate().toString());
        checkOutField.setText(reservation.getCheckOutDate().toLocalDateTime().toLocalDate().toString());
        roomTypeField.setText(maybeRoom.map(Room::getRoomType).orElse(""));
        roomCapacityField.setText(maybeRoom.map(Room::getRoomCapacity).orElse(""));
        numberOfDaysField.setText(String.valueOf(reservation.getNumberOfDays()));
        totalCostField.setText(String.format("₱%.2f", reservation.getTotalCost()));

        checkOutButton.setDisable(false);
    }

    @FXML
    void handleCheckOut(ActionEvent event) {
        if (loadedReservationId == -1) {
            showAlert(AlertType.WARNING, "No Booking", "No reservation loaded. Please search first.");
            return;
        }

        boolean deleted = reservationService.delete(loadedReservationId);
        if (!deleted) {
            showAlert(AlertType.ERROR, "Check-Out Failed",
                    "Unable to delete the reservation record for Room ID " + loadedRoomId + ".");
            return;
        }

        roomService.getById(loadedRoomId).ifPresent(room -> {
            room.setStatus("AVAILABLE");
            roomService.update(room);
        });

        showAlert(AlertType.INFORMATION, "Check-Out Complete",
                "Guest has been successfully checked out from Room " + roomSearchField.getText().trim() + ".");

        clearFields();
        loadedReservationId = -1;
        loadedRoomId = -1;
        checkOutButton.setDisable(true);
        GuestsController.refreshOpenView();
    }

    private void clearFields() {
        guestNameField.clear();
        checkInField.clear();
        checkOutField.clear();
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