package com.hotel.wildcat_hotel.project;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SwitchScreen {
    // Switch to a new full screen
    public void switchTo(Event event, String fxmlPath, int width, int height, String title, String iconPath) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
        Stage stage = new Stage();
        stage.setScene(new Scene(root, width, height));
        stage.setTitle(title);
        if (iconPath != null && !iconPath.isEmpty()) {
            stage.getIcons().add(new Image(getClass().getResourceAsStream(iconPath)));
        }
        stage.show();
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    }

    // Open a popup (modal) window
    public void popUp(Event event, String fxmlPath, int width, int height, String title, String iconPath) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root, width, height));
        stage.setTitle(title);
        if (iconPath != null && !iconPath.isEmpty()) {
            stage.getIcons().add(new Image(getClass().getResourceAsStream(iconPath)));
        }
        stage.showAndWait();
    }

    // Load FXML content into a container pane (for tab/sidebar navigation)
    public Parent loadFXML(String fxmlPath) throws Exception {
        return FXMLLoader.load(getClass().getResource(fxmlPath));
    }
}
