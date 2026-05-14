package com.hotel.wildcat_hotel.availableroom;

import java.util.List;
import java.util.stream.Collectors;

import com.hotel.wildcat_hotel.core.HotelApplicationContext;
import com.hotel.wildcat_hotel.hotel.Room;
import com.hotel.wildcat_hotel.service.RoomService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class AvailableRoomController {

    @FXML private TextField usernamefield;

    @FXML private TableView<RoomModel>            usersTable;
    @FXML private TableColumn<RoomModel, Integer> usernameColumn;   // Room ID
    @FXML private TableColumn<RoomModel, String>  roleColumn;       // Room Type
    @FXML private TableColumn<RoomModel, String>  usernameColumn1;  // Room Capacity
    @FXML private TableColumn<RoomModel, Double>  usernameColumn2;  // Room Rate

    private RoomService roomService;

    // ── Auto-called when FXML loads ──────────────────────────────────
    @FXML
    public void initialize() {
        roomService = HotelApplicationContext.getDefault().getRoomService();

        usernameColumn .setCellValueFactory(new PropertyValueFactory<>("roomId"));
        roleColumn     .setCellValueFactory(new PropertyValueFactory<>("roomType"));
        usernameColumn1.setCellValueFactory(new PropertyValueFactory<>("roomCapacity"));
        usernameColumn2.setCellValueFactory(new PropertyValueFactory<>("roomRate"));

        loadAvailableRooms(null); // show all available rooms on startup
    }

    // ── Search button handler ────────────────────────────────────────
    @FXML
    private void handleSearchUser() {
        String keyword = usernamefield.getText().trim();
        loadAvailableRooms(keyword.isEmpty() ? null : keyword);
    }

    // ── Query using repository/service abstraction ───────────────────
    private void loadAvailableRooms(String keyword) {
        List<Room> rooms = roomService.getAvailableRooms();

        if (keyword != null) {
            String lower = keyword.toLowerCase();
            rooms = rooms.stream()
                    .filter(room -> String.valueOf(room.getRoomID()).contains(lower)
                                 || room.getRoomType().toLowerCase().contains(lower))
                    .collect(Collectors.toList());
        }

        ObservableList<RoomModel> data = FXCollections.observableArrayList();
        for (Room room : rooms) {
            data.add(new RoomModel(
                    room.getRoomID(),
                    room.getRoomType(),
                    room.getRoomCapacity(),
                    room.getRoomRate()
            ));
        }

        usersTable.setItems(data);
    }

    // ── Row model ────────────────────────────────────────────────────
    public static class RoomModel {
        private final int    roomId;
        private final String roomType;
        private final String roomCapacity;
        private final double roomRate;

        public RoomModel(int roomId, String roomType, String roomCapacity, double roomRate) {
            this.roomId       = roomId;
            this.roomType     = roomType;
            this.roomCapacity = roomCapacity;
            this.roomRate     = roomRate;
        }

        public int    getRoomId()       { return roomId; }
        public String getRoomType()     { return roomType; }
        public String getRoomCapacity() { return roomCapacity; }
        public double getRoomRate()     { return roomRate; }
    }
}