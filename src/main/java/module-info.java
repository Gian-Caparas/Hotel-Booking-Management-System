module com.hotel.wildcat_hotel {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.hotel.wildcat_hotel to javafx.fxml;
    opens com.hotel.wildcat_hotel.controller to javafx.fxml;

    exports com.hotel.wildcat_hotel;
    exports com.hotel.wildcat_hotel.controller;
}