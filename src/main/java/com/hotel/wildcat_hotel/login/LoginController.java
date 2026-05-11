package com.hotel.wildcat_hotel.login;

import com.hotel.wildcat_hotel.project.DataBase;
import com.hotel.wildcat_hotel.project.Paths;
import com.hotel.wildcat_hotel.project.User;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    public static User currentUser;

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @Override public void initialize(URL url, ResourceBundle rb) {}
    @FXML private void loginAction(Event event) {

        // ================= DATABASE CONNECTION TEST =================
        try {
            DataBase.checkConnection();
        } catch (Exception e) {
            showAlert(
                    "Connection Error",
                    "Cannot connect to the database.\n" +
                    "Make sure XAMPP MySQL is running."
            );
            return;
        }

        // ================= INPUT VALIDATION =================

        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        if (username.isEmpty() || password.isEmpty()) {
            showAlert(
                    "Input Error",
                    "Please enter both username and password."
            );
            return;
        }

        // ================= LOGIN VALIDATION =================

        User loggedInUser = authenticateUser(username, password);
        if (loggedInUser != null) {
            currentUser = loggedInUser;
            goToHomePage();
        } else {
            showAlert(
                    "Login Failed",
                    "Invalid username or password."
            );
        }
    }

    // ================= AUTHENTICATION =================
    private User authenticateUser(String username, String password) {
        List<User> users = DataBase.getUsers();
        for (User user : users) {
            if (user.getUsername().equals(username)
                    && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    // ================= NAVIGATION =================
    private void goToHomePage() {
        try {
            Parent root =
                    FXMLLoader.load(
                            getClass().getResource(Paths.HOMEPAGEVIEW)
                    );

            Stage stage = new Stage();
            stage.setScene(new Scene(root, 1200, 750));
            stage.setTitle("WildCat Hotel - Home");
            stage.show();
            // Close login window
            ((Stage) usernameField
                    .getScene()
                    .getWindow())
                    .close();
        } catch (Exception ex) {
            ex.printStackTrace();
            showAlert(
                    "Navigation Error",
                    "Could not load home page.\n"
                            + ex.getMessage()
            );
        }
    }

    // ================= ALERT =================
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);      
        alert.showAndWait();
    }
}