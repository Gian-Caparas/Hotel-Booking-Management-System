package com.hotel.wildcat_hotel.checkin;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.event.ActionEvent;
import java.time.LocalDate;

public class CheckInController {

    @FXML
    private TextField txtGuestName;

    @FXML
    private TextField txtRoomNumber;

    @FXML
    private DatePicker datePickerCheckIn;

    @FXML
    private Button btnConfirmCheckIn;

    @FXML
    private void handleCheckIn(ActionEvent event) {
        String name = txtGuestName.getText();
        String room = txtRoomNumber.getText();
        LocalDate checkInDate = datePickerCheckIn.getValue();

        if (name.isEmpty() || room.isEmpty() || checkInDate == null) {
            showNotification("Error", "All fields are required!");
        } else {
           
            System.out.println("Processing Check-in for: " + name + " in Room " + room);
            showNotification("Success", "Guest " + name + " checked into room " + room);
            clearFields();
        }
    }

    private void clearFields() {
        txtGuestName.clear();
        txtRoomNumber.clear();
        datePickerCheckIn.setValue(null);
    }

    private void showNotification(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}