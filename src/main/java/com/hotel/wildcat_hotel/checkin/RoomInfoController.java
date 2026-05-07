package com.hotel.wildcat_hotel.checkin;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;

public class RoomInfoController {

    @FXML private ComboBox<String> roomTypeComboBox;
    @FXML private ComboBox<String> availableRoomsComboBox;
    @FXML private Label pricePerNightLabel;

    @FXML
    public void initialize() {
        // Initialize room types (Suite, Deluxe, Standard)
        roomTypeComboBox.getItems().addAll("Standard", "Deluxe", "Wildcat Suite");
    }

    @FXML
    private void onRoomTypeSelected(ActionEvent event) {
        String selectedType = roomTypeComboBox.getValue();
        // Here you would query your DB for rooms where status = 'Available'
        System.out.println("Filtering available rooms for: " + selectedType);
        
        // Mock logic
        if(selectedType.equals("Standard")) pricePerNightLabel.setText("₱ 2,500");
    }

    @FXML
    private void handleProceedToConfirmation(ActionEvent event) {
        // Logic to pass selected room data to ConfirmnDataController
    }
}