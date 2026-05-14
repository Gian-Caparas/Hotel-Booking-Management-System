package com.hotel.wildcat_hotel.rooms;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import com.hotel.wildcat_hotel.core.HotelApplicationContext;
import com.hotel.wildcat_hotel.hotel.Reservation;
import com.hotel.wildcat_hotel.hotel.Room;
import com.hotel.wildcat_hotel.service.GuestService;
import com.hotel.wildcat_hotel.service.ReservationService;
import com.hotel.wildcat_hotel.service.RoomService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class RoomsController {

    @FXML private TextField usernamefield;

    @FXML private TableView<RoomModel>             usersTable;
    @FXML private TableColumn<RoomModel, Integer>  usernameColumn;   // Room ID
    @FXML private TableColumn<RoomModel, String>   roleColumn;       // Room Type
    @FXML private TableColumn<RoomModel, String>   usernameColumn1;  // Room Capacity
    @FXML private TableColumn<RoomModel, String>   usernameColumn2;  // Status
    @FXML private TableColumn<RoomModel, String>   usernameColumn3;  // Occupied By
    @FXML private TableColumn<RoomModel, String>   usernameColumn4;  // Check-in
    @FXML private TableColumn<RoomModel, String>   usernameColumn5;  // Check-out

    private RoomService roomService;
    private ReservationService reservationService;
    private GuestService guestService;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // ── Auto-called when the FXML loads ─────────────────────────────
    @FXML
    public void initialize() {
        roomService = HotelApplicationContext.getDefault().getRoomService();
        reservationService = HotelApplicationContext.getDefault().getReservationService();
        guestService = HotelApplicationContext.getDefault().getGuestService();

        usernameColumn .setCellValueFactory(new PropertyValueFactory<>("roomId"));
        roleColumn     .setCellValueFactory(new PropertyValueFactory<>("roomType"));
        usernameColumn1.setCellValueFactory(new PropertyValueFactory<>("roomCapacity"));
        usernameColumn2.setCellValueFactory(new PropertyValueFactory<>("status"));
        usernameColumn3.setCellValueFactory(new PropertyValueFactory<>("occupiedBy"));
        usernameColumn4.setCellValueFactory(new PropertyValueFactory<>("checkin"));
        usernameColumn5.setCellValueFactory(new PropertyValueFactory<>("checkout"));

        loadRooms(null);   // show all rooms on startup
    }

    // ── Search button ────────────────────────────────────────────────
    @FXML
    private void handleSearchUser() {
        String keyword = usernamefield.getText().trim();
        loadRooms(keyword.isEmpty() ? null : keyword);
    }

    // ── Main query using service/repository abstraction ──────────────
    private void loadRooms(String keyword) {
        List<Room> rooms = roomService.getAll();
        String lowerKeyword = keyword == null ? null : keyword.toLowerCase(Locale.ROOT);

        ObservableList<RoomModel> data = FXCollections.observableArrayList();

        for (Room room : rooms) {
            Optional<Reservation> maybeReservation = reservationService.findLatestByRoomId(room.getRoomID());
            String occupiedBy = "NULL";
            String checkin   = "";
            String checkout  = "";

            if (maybeReservation.isPresent()) {
                Reservation reservation = maybeReservation.get();
                occupiedBy = guestService.getById(reservation.getGuestID())
                        .map(guest -> guest.getFirstName() + " " + guest.getLastName())
                        .orElse("NULL");
                checkin = reservation.getCheckInDate().toLocalDateTime().toLocalDate().format(DATE_FORMATTER);
                checkout = reservation.getCheckOutDate().toLocalDateTime().toLocalDate().format(DATE_FORMATTER);
            }

            if (lowerKeyword == null
                    || String.valueOf(room.getRoomID()).contains(lowerKeyword)
                    || room.getRoomType().toLowerCase(Locale.ROOT).contains(lowerKeyword)
                    || occupiedBy.toLowerCase(Locale.ROOT).contains(lowerKeyword)) {
                data.add(new RoomModel(
                        room.getRoomID(),
                        room.getRoomType(),
                        room.getRoomCapacity(),
                        room.getStatus(),
                        occupiedBy,
                        checkin,
                        checkout
                ));
            }
        }

        usersTable.setItems(data);
    }

    // ── Row model ────────────────────────────────────────────────────
    public static class RoomModel {
        private final int    roomId;
        private final String roomType;
        private final String roomCapacity;
        private final String status;
        private final String occupiedBy;
        private final String checkin;
        private final String checkout;

        public RoomModel(int roomId, String roomType, String roomCapacity,
                         String status, String occupiedBy,
                         String checkin, String checkout) {
            this.roomId       = roomId;
            this.roomType     = roomType;
            this.roomCapacity = roomCapacity;
            this.status       = status;
            this.occupiedBy   = occupiedBy;
            this.checkin      = checkin;
            this.checkout     = checkout;
        }

        public int    getRoomId()       { return roomId; }
        public String getRoomType()     { return roomType; }
        public String getRoomCapacity() { return roomCapacity; }
        public String getStatus()       { return status; }
        public String getOccupiedBy()   { return occupiedBy; }
        public String getCheckin()      { return checkin; }
        public String getCheckout()     { return checkout; }
    }
}