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

    @FXML private TextField     usernameField;
    @FXML private PasswordField passwordField;
    @FXML private TextField     emailField;
    @FXML private TextField     phoneField;
    @FXML private RadioButton   adminRadio;
    @FXML private RadioButton   staffRadio;
    @FXML private RadioButton   customerRadio;
    @FXML private ToggleGroup   roleGroup;
    @FXML private Label         statusLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        statusLabel.setText("");
        staffRadio.setSelected(true); // default role
    }

    @FXML
    private void handleAddUser(ActionEvent event) {

        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String email    = emailField.getText().trim();
        String phone    = phoneField.getText().trim();

        // ── Validation ────────────────────────────────────────────────────────

        if (username.isEmpty() || password.isEmpty()
                || email.isEmpty() || phone.isEmpty()) {
            setStatus("⚠ All fields cannot be empty.", false);
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
        if (!email.matches("^[\\w.+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$")) {
            setStatus("⚠ Please enter a valid email address.", false);
            return;
        }
        if (!phone.matches("^[0-9]{7,11}$")) {
            setStatus("⚠ Phone number must be 7–11 digits.", false);
            return;
        }

        // ── Role selection ────────────────────────────────────────────────────

        String role;
        if (adminRadio.isSelected()) {
            role = "Admin";
        } else if (customerRadio.isSelected()) {
            role = "Customer";
        } else {
            role = "Staff"; // default
        }

        // ── Create & save user ────────────────────────────────────────────────
        // Use the full constructor: (username, password, role, email, phoneNo)
        User newUser = new User(username, password, role, email, phone);

        boolean saved = DataBase.saveUser(newUser);
        if (saved) {
            setStatus("✓ User '" + username + "' added as " + role + ".", true);
            clearForm();
        } else {
            setStatus("✗ Username '" + username + "' already exists.", false);
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void clearForm() {
        usernameField.clear();
        passwordField.clear();
        emailField.clear();
        phoneField.clear();
        staffRadio.setSelected(true);
        statusLabel.setText("");
    }

    private void setStatus(String message, boolean success) {
        statusLabel.setStyle(
                "-fx-text-fill: " + (success ? "#4CAF50" : "#FF6B6B") + ";" +
                "-fx-font-size: 13px;" +
                "-fx-font-weight: bold;"
        );
        statusLabel.setText(message);
    }
}