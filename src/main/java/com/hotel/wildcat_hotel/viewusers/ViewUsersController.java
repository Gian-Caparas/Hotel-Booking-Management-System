package com.hotel.wildcat_hotel.viewusers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class ViewUsersController {

    @FXML
    private TextField txtSearch;

    @FXML
    private Button btnSearch;

    @FXML
    private TableView<?> usersTable;

    // This is the method the error is complaining about!
    @FXML
    private void handleSearch(ActionEvent event) {
        String searchText = txtSearch.getText();
        System.out.println("Searching for: " + searchText);
        // Add your search logic here
    }
}