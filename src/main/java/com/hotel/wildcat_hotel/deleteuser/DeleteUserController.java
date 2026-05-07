package com.hotel.wildcat_hotel.deleteuser;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.hotel.wildcat_hotel.project.DataBase;
import com.hotel.wildcat_hotel.project.User;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class DeleteUserController implements Initializable {

    @FXML private Button deleteuser;
    @FXML private Button searchButton;
    @FXML private TextField usernamefield;
    @FXML private TableView<User> usersTable;
    @FXML private TableColumn<User, String> usernameColumn;
    @FXML private TableColumn<User, String> roleColumn;

    private ObservableList<User> tableData = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Encapsulation - accessing User data only through getters
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        // This now works because we added getRole() to User.java
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        usersTable.setItems(tableData);

        // Remove button disabled until user selects a row
        deleteuser.setDisable(true);
        usersTable.getSelectionModel().selectedItemProperty()
            .addListener(new javafx.beans.value.ChangeListener<User>() {
                @Override
                public void changed(
                    javafx.beans.value.ObservableValue<? extends User> obs,
                    User oldVal,
                    User newVal) {
                    // Encapsulation - enable only when a row is selected
                    deleteuser.setDisable(newVal == null);
                }
            });

        loadAllUsers();
    }

    @FXML
    private void handleSearchUser() {
        String input = usernamefield.getText().trim();
        tableData.clear();

        if (input.isEmpty()) {
            loadAllUsers();
            return;
        }

        List<User> allUsers = DataBase.getUsers();
        if (allUsers == null) return;

        for (int i = 0; i < allUsers.size(); i++) {
            User user = allUsers.get(i);
            // Encapsulation - using getter to access username
            String username = user.getUsername().toLowerCase();
            if (username.contains(input.toLowerCase())) {
                tableData.add(user);
            }
        }

        if (tableData.isEmpty()) {
            setStatus("No users found matching \"" + input + "\".");
        }
    }

    @FXML
    private void handleDeleteUser() {
        // Encapsulation - getting selected user through TableView's method
        User selectedUser = usersTable.getSelectionModel().getSelectedItem();

        if (selectedUser == null) {
            setStatus("Please select a user to delete.");
            return;
        }

        // Encapsulation - accessing username only through getter
        boolean deleted = DataBase.deleteUser(selectedUser.getUsername());

        if (deleted) {
            tableData.remove(selectedUser);
            usernamefield.clear();
            deleteuser.setDisable(true);
            setStatus("User deleted successfully.");
        } else {
            setStatus("Failed to delete \"" + selectedUser.getUsername() + "\".");
        }
    }

    // Abstraction - hiding the placeholder update logic behind a method
    private void setStatus(String message) {
        usersTable.setPlaceholder(new Label(message));
    }

    // Abstraction - hiding the data loading logic behind a method
    private void loadAllUsers() {
        tableData.clear();
        List<User> allUsers = DataBase.getUsers();

        if (allUsers != null && !allUsers.isEmpty()) {
            tableData.addAll(allUsers);
        } else {
            setStatus("No users in the system.");
        }
    }
}