package com.hotel.wildcat_hotel.viewusers;

import com.hotel.wildcat_hotel.project.User;
import com.hotel.wildcat_hotel.project.DataBase; 
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class ViewUsersController {

    @FXML private TextField usernamefield;
    @FXML private TableView<User> usersTable;
    
    // Only keeping the three columns you need
    @FXML private TableColumn<User, Integer> userIdColumn;
    @FXML private TableColumn<User, String> usernameColumn;
    @FXML private TableColumn<User, String> roleColumn;

    private ObservableList<User> masterData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Link columns to User entity getters: getUserId(), getUsername(), getRole()
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        loadInitialData();
    }

    private void loadInitialData() {
        // Fetch users from your Database helper
        masterData.setAll(DataBase.getUsers());
        usersTable.setItems(masterData);
    }

    @FXML 
    private void handleSearchUser() {
        String query = usernamefield.getText().trim().toLowerCase();
        
        if (query.isEmpty()) {
            usersTable.setItems(masterData);
        } else {
            ObservableList<User> filteredData = FXCollections.observableArrayList();
            for (User user : masterData) {
                if (user.getUsername().toLowerCase().contains(query)) {
                    filteredData.add(user);
                }
            }
            usersTable.setItems(filteredData);
        }
    }
}