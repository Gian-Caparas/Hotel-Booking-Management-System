package com.hotel.wildcat_hotel;

import com.hotel.wildcat_hotel.database.DBConnection; // Import your connection utility
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("MainView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("WildCat Hotel - Booking Management System");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        // --- FINAL FOUNDATION TEST ---
        System.out.println("Checking database connection...");
        try (Connection conn = DBConnection.getConnection()) {
            if (conn != null) {
                System.out.println("DATABASE STATUS: ONLINE");
            }
        } catch (Exception e) {
            System.err.println("DATABASE STATUS: OFFLINE - Check HotelDB folder.");
        }

        launch();
    }
}