package com.hotel.wildcat_hotel.viewusers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class ViewUsersController {

    @FXML private TextField usernamefield;
    @FXML private Button searchButton;
    
    
    @FXML private void handleSearchUser() {

        String username = usernamefield.getText().trim();

        System.out.println("Searching for: " + username);

    }
}