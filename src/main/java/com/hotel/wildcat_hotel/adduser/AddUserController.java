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

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private RadioButton adminRadio;

    @FXML
    private RadioButton staffRadio;

    @FXML
    private RadioButton customerRadio;

    @FXML
    private ToggleGroup roleGroup;

    @FXML
    private Label statusLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        statusLabel.setText("");

        // Default selected role
        staffRadio.setSelected(true);
    }

    @FXML
    private void handleAddUser(ActionEvent event) {

        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        // ================= VALIDATION =================

        if (username.isEmpty() || password.isEmpty()) {
            setStatus("⚠ Fields cannot be empty.", false);
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

        // ================= ROLE SELECTION =================

        String role;

        if (adminRadio.isSelected()) {
            role = "Admin";
        }
        else if (customerRadio.isSelected()) {
            role = "Customer";
        }
        else {
            role = "Staff";
        }

        // ================= CREATE USER =================

        User newUser = new User(username, password, role);

        // ================= SAVE USER =================

        boolean success = DataBase.saveUser(newUser);

        if (success) {

            setStatus("✓ User '" + username +
                    "' added as " + role + ".", true);

            clearForm();

        } else {

            setStatus("✗ Username already exists.", false);
        }
    }

    // ================= CLEAR FORM =================

    private void clearForm() {

        usernameField.clear();
        passwordField.clear();

        // Reset default role
        staffRadio.setSelected(true);
    }

    // ================= STATUS MESSAGE =================

    private void setStatus(String message, boolean success) {

        String color = success
                ? "#4CAF50"
                : "#FF6B6B";

        statusLabel.setStyle(
                "-fx-text-fill: " + color + ";" +
                "-fx-font-size: 13px;" +
                "-fx-font-weight: bold;"
        );

        statusLabel.setText(message);
    }
}