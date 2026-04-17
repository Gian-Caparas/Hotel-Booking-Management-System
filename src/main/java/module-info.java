module com.hotel.wildcat_hotel {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.hotel.wildcat_hotel to javafx.fxml;
    exports com.hotel.wildcat_hotel;
    exports com.hotel.wildcat_hotel.controller;
    opens com.hotel.wildcat_hotel.controller to javafx.fxml;
}