package com.hotel.wildcat_hotel.rooms;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.event.ActionEvent;

public class RoomsController {

    @FXML private TableView<?> tblRooms;
    @FXML private TableColumn<?, ?> colRoomNumber;
    @FXML private TableColumn<?, ?> colType;
    @FXML private TableColumn<?, ?> colStatus;
    @FXML private Button btnRefresh;

    @FXML
    void handleRefresh(ActionEvent event) {
       //triggers the reload data of MySQL hotel_db
        System.out.println("Refreshing room list....");
    }
}