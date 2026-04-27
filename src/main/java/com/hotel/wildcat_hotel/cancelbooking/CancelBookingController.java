package com.hotel.wildcat_hotel.cancelbooking;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.event.ActionEvent;

public class CancelBookingController {

    @FXML
    private TextField txtBookingId; 

    @FXML
    private Button btnCancel;

    @FXML
    private void handleCancelBooking(ActionEvent event) {
        String bookingId = txtBookingId.getText();

        if (bookingId.isEmpty()) {
            showAlert("Error", "Please enter a Booking ID to cancel.");
            return;
        }

        // Logic to delete from MySQL via Hibernate will go here
        System.out.println("Canceling booking ID: " + bookingId);
        
        // After deleting, show success
        showAlert("Success", "Booking #" + bookingId + " has been canceled.");
        txtBookingId.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}