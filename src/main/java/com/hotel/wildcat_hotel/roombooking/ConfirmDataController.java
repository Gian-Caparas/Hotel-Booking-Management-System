package com.hotel.wildcat_hotel.roombooking;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import com.hotel.wildcat_hotel.project.DataBase; // FIX 1: Updated to the correct sub-folder
import com.hotel.wildcat_hotel.hotel.Guest;      // Needed for saving guest data
import com.hotel.wildcat_hotel.hotel.Room;       // Needed for room updates

public class ConfirmDataController {

    @FXML
    private Label roomIDLabel;
    @FXML
    private Label guestNameLabel;
    @FXML
    private Label totalBillLabel;

    public void displayInformation(String name, String roomID, String price) {
        guestNameLabel.setText(name);
        roomIDLabel.setText(roomID);
        totalBillLabel.setText(price);
    }

    @FXML
    private void handleConfirmBooking() {
       
        try {
           
            
            System.out.println("Booking confirmed for: " + guestNameLabel.getText());
            System.out.println("Room ID: " + roomIDLabel.getText());
            
           
            
        } catch (Exception e) {
            System.out.println("Error saving booking: " + e.getMessage());
        }
    }
}