package com.hotel.wildcat_hotel.checkout;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;

public class CheckOutController {

    @FXML private TextField txtBookingId;
    @FXML private Label lblGuestName;
    @FXML private Label lblTotalAmount;
    @FXML private Button btnProcessCheckOut;

    @FXML
    void handleCheckOut(ActionEvent event) {
        //It will get the Booking ID from the text field
        //It will search your database for that guest
        //It will update the room status in MySQL to 'Available'
        System.out.println("Processing check-out for ID: " + txtBookingId.getText());
    }
}