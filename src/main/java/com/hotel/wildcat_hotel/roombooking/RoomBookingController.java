package com.hotel.wildcat_hotel.roombooking;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.hotel.wildcat_hotel.project.DataBase; 
import com.hotel.wildcat_hotel.hotel.Room;
import java.util.List;

public class RoomBookingController {

    @FXML
    private TextField guestNameField;

    @FXML
    private ComboBox<Room> roomComboBox;

    @FXML
    private TextField stayDurationField;

    @FXML
    public void initialize() {
        try {
            // Use the method from the DataBase class Gian updated
            List<Room> availableRooms = DataBase.getAvailableRooms();
            
            ObservableList<Room> roomList = FXCollections.observableArrayList(availableRooms);
            roomComboBox.setItems(roomList);
            
            System.out.println("Loaded " + availableRooms.size() + " available rooms.");
        } catch (Exception e) {
            System.out.println("Error loading rooms: " + e.getMessage());
        }
    }

    @FXML
    private void handleNext() {
        String guestName = guestNameField.getText();
        Room selectedRoom = roomComboBox.getValue();
        
        if (guestName.isEmpty() || selectedRoom == null) {
            System.out.println("Please fill in all fields.");
            return;
        }

        // Logic to transition to ConfirmDataController
        System.out.println("Proceeding to confirmation for: " + guestName);
        
        
    }
}