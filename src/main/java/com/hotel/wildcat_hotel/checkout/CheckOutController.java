package com.hotel.wildcat_hotel.checkout;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;

public class CheckOutController {

    @FXML private TextField roomSearchField; 
    @FXML private TextField guestNameField;
    @FXML private TextField stayDurationField;
    @FXML private TextField roomChargesField;
    @FXML private TextField totalBalanceField;

    @FXML
    void handleSearch(ActionEvent event) {
        String roomNumber = roomSearchField.getText();
        
        if (roomNumber.isEmpty()) {
            System.out.println("Please enter a room number to search.");
            return;
        }

        System.out.println("Searching database for Room: " + roomNumber);
    }

    @FXML
    void handleCheckOut(ActionEvent event) {
        System.out.println("Finalizing check-out for Room: " + roomSearchField.getText());
    }
}