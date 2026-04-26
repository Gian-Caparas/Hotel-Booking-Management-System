package com.hotel.wildcat_hotel.project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource(Paths.LOGINVIEW));
        Scene scene = new Scene(root);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/img/launcher_icon.png")));
        stage.setScene(scene);
        stage.setTitle("Hotel Reservation System");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
