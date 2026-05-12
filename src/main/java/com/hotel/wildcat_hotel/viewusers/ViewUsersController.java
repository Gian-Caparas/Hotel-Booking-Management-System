package com.hotel.wildcat_hotel.viewusers;

import com.hotel.wildcat_hotel.project.User;
import com.hotel.wildcat_hotel.project.DataBase; 
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.*;
import java.util.List;

public class ViewUsersController {

    @FXML private TextField usernamefield;
    @FXML private TableView<User> usersTable;
    
    // Keeping columns mapped to existing User properties
    @FXML private TableColumn<User, String> usernameColumn;
    @FXML private TableColumn<User, String> roleColumn;

    private ObservableList<User> masterData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Use explicit property extraction so table rendering does not depend on reflection.
        usernameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getUsername()));
        roleColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getRole()));

        loadInitialData();
    }

    private void loadInitialData() {
        // Keep the page visible even if the database is unreachable.
        try {
            List<User> users = DataBase.getUsers();
            if (users != null) {
                masterData.setAll(users);
            } else {
                masterData.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
            masterData.clear();
        }
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