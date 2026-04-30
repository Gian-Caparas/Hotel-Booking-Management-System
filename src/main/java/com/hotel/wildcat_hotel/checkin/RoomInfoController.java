package com.hotel.wildcat_hotel.checkin;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;

public class RoomInfoController {

    @FXML private Label lblRoomNumber;
    @FXML private Label lblRoomType;
    @FXML private Label lblPrice;
    @FXML private Label lblStatus;
    @FXML private Button btnConfirmSelection;

    @FXML
    void handleConfirmSelection(ActionEvent event) {
        // This is where we will pass the room data to the next screen
        System.out.println("Room selected!");
    }
}