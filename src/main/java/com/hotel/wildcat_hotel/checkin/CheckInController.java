package com.hotel.wildcat_hotel.checkin;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.event.ActionEvent;
import java.time.LocalDate;

public class CheckInController {

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextField cityField;
    @FXML private TextField nationalityField;
    @FXML private DatePicker checkInDatePicker;
    @FXML private DatePicker checkOutDatePicker;
    @FXML private Button checkInButton;

    @FXML
    private void handleCheckIn(ActionEvent event) {
        // Collect data
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        LocalDate checkInDate = checkInDatePicker.getValue();
        LocalDate checkOutDate = checkOutDatePicker.getValue();

        // Validation
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || 
            checkInDate == null || checkOutDate == null) {
            
            showNotification("Missing Information", "Please fill out all required fields.");
        } else {
            // Logic for moving to the next screen or saving
            System.out.println("Registering: " + firstName + " " + lastName);
            showNotification("Registration Successful", "Guest details have been recorded.");
            // clearFields(); // Optional: clear if staying on page
        }
    }

    private void clearFields() {
        firstNameField.clear();
        lastNameField.clear();
        emailField.clear();
        phoneField.clear();
        cityField.clear();
        nationalityField.clear();
        checkInDatePicker.setValue(null);
        checkOutDatePicker.setValue(null);
    }

    private void showNotification(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.getDialogPane().setStyle("-fx-background-color: #1e1e38;");
        alert.showAndWait();
    }
}