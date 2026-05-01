package com.hotel.wildcat_hotel.adduser;

import java.net.URL;
import java.util.ResourceBundle;

import com.hotel.wildcat_hotel.project.DataBase;
import com.hotel.wildcat_hotel.project.User;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

public class AddUserController implements Initializable {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private RadioButton adminRadio;
    @FXML private RadioButton staffRadio;
    @FXML private ToggleGroup roleGroup;
    @FXML private Label statusLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        statusLabel.setText("");
    }

    @FXML
    private void handleAddUser(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        // Validation
        if (username.isEmpty()) {
            setStatus("⚠ Username cannot be empty.", false);
            return;
        }
        if (password.isEmpty()) {
            setStatus("⚠ Password cannot be empty.", false);
            return;
        }
        if (username.length() < 3) {
            setStatus("⚠ Username must be at least 3 characters.", false);
            return;
        }
        if (password.length() < 4) {
            setStatus("⚠ Password must be at least 4 characters.", false);
            return;
        }

        boolean isAdmin = adminRadio.isSelected();
        User newUser = new User(username, password, isAdmin);
        boolean success = DataBase.saveUser(newUser);

        if (success) {
            setStatus("✓ User '" + username + "' added as "
                + (isAdmin ? "Admin" : "Staff") + ".", true);
            clearForm();
        } else {
            setStatus("✗ Username '" + username + "' already exists.", false);
        }
    }

    private void clearForm() {
        usernameField.clear();
        passwordField.clear();
        staffRadio.setSelected(true);
        statusLabel.setText("");
    }

    private void setStatus(String message, boolean success) {
        String color = success ? "#4caf50" : "#ff6b6b";
        statusLabel.setStyle("-fx-text-fill: " + color + "; -fx-font-size: 13;");
        statusLabel.setText(message);
    }
}