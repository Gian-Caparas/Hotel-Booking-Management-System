package com.hotel.wildcat_hotel.project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // Ensure Paths.LOGINVIEW points to the correct string path
        Parent root = FXMLLoader.load(getClass().getResource(Paths.LOGINVIEW));
        
        // Explicitly set the width and height from your FXML (480x420)
        Scene scene = new Scene(root, 480, 420); 
        
        stage.setScene(scene);
        stage.setTitle("WildCat Hotel Reservation System");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
