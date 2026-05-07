package com.hotel.wildcat_hotel.checkin;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;

public class ConfirmnDataController {

    @FXML private Label lblGuestName;
    @FXML private Label lblRoomDetails;
    @FXML private Label lblTotalAmount;
    @FXML private Button btnFinalize;

    public void setConfirmationDetails(String name, String room, String total) {
        lblGuestName.setText(name);
        lblRoomDetails.setText("Room Selection: " + room);
        lblTotalAmount.setText("Total Due: PHP " + total);
    }

    @FXML
    private void handleFinalize(ActionEvent event) {
        // This is where you would call your Database Manager class
        System.out.println("Inserting data into MySQL database...");
        
        // Example: DBConnection.saveBooking(guestObj);
        
        btnFinalize.setDisable(true);
        btnFinalize.setText("Checked In");
    }
}