package com.hotel.wildcat_hotel.checkin;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;

public class ConfirmnDataController {

    @FXML
    private Label lblGuestName;

    @FXML
    private Label lblRoomDetails;

    @FXML
    private Label lblTotalAmount;

    @FXML
    private Button btnFinalize;

    
    public void setConfirmationDetails(String name, String room, String total) {
        lblGuestName.setText(name);
        lblRoomDetails.setText("Room: " + room);
        lblTotalAmount.setText("PHP " + total);
    }

    @FXML
    private void handleFinalize(ActionEvent event) {
        
        System.out.println("Finalized! Saving to MySQL...");
        
        
    }
}