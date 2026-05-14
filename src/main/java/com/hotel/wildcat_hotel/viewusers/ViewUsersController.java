package com.hotel.wildcat_hotel.viewusers;

import java.util.List;

import com.hotel.wildcat_hotel.core.HotelApplicationContext;
import com.hotel.wildcat_hotel.core.Service;
import com.hotel.wildcat_hotel.project.User;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class ViewUsersController {

    @FXML private TextField usernamefield;
    @FXML private TableView<User> usersTable;
    @FXML private TableColumn<User, String> usernameColumn;
    @FXML private TableColumn<User, String> roleColumn;
    @FXML private TableColumn<User, String> usernameColumn1; // Phone Number
    @FXML private TableColumn<User, String> usernameColumn2; // Email

    private ObservableList<User> masterData = FXCollections.observableArrayList();

    // ✓ NEW: Polymorphic service reference
    private Service<User> userService;

    @FXML public void initialize() {
        // ✓ NEW: Inject service
        userService = HotelApplicationContext.getDefault().getUserService();

        usernameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getUsername()));
        roleColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getRole()));
        usernameColumn1.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getPhoneNo()));
        usernameColumn2.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEmail()));

        loadInitialData();
    }

    private void loadInitialData() {
        try {
            List<User> users = userService.getAll();
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

    @FXML private void handleSearchUser() {
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