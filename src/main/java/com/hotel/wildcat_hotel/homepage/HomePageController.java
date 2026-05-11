package com.hotel.wildcat_hotel.homepage;

import com.hotel.wildcat_hotel.login.LoginController;
import com.hotel.wildcat_hotel.project.Paths;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

public class HomePageController implements Initializable {

    @FXML private StackPane contentPane;
    @FXML private Label currentUserLabel;

    //Admin buttons
    @FXML private Button addUserButton;
    @FXML private Button deleteUserButton;
    @FXML private Button viewUsersButton;
    @FXML private Label adminLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (LoginController.currentUser != null) {
            String role = LoginController.currentUser.isAdmin() ? "Admin" : "Staff";
            currentUserLabel.setText("Logged in as: "
                + LoginController.currentUser.getUsername()
                + " (" + role + ")");

                addUserButton.setVisible(LoginController.currentUser.isAdmin());
                deleteUserButton.setVisible(LoginController.currentUser.isAdmin());
                viewUsersButton.setVisible(LoginController.currentUser.isAdmin());
                adminLabel.setVisible(LoginController.currentUser.isAdmin());
        }       
    }

    @FXML private void openCheckIn(ActionEvent e)       { loadView(Paths.CHECKINVIEW); }
    @FXML private void openCheckOut(ActionEvent e)      { loadView(Paths.CHECKOUTVIEW); }
    @FXML private void openRoomBooking(ActionEvent e)   { loadView(Paths.ROOMBOOKINGVIEW); }
    @FXML private void openCancelBooking(ActionEvent e) { loadView(Paths.CANCELBOOKINGVIEW); }
    @FXML private void openGuests(ActionEvent e)        { loadView(Paths.GUESTSVIEW); }
    @FXML private void openRooms(ActionEvent e)         { loadView(Paths.ROOMSVIEW); }
    @FXML private void openAddUser(ActionEvent e)       { loadView(Paths.ADDUSERVIEW); }
    @FXML private void openDeleteUser(ActionEvent e)    { loadView(Paths.DELETEUSERVIEW); }
    @FXML private void openViewUsers(ActionEvent e)     { loadView(Paths.VIEWUSERSVIEW); }

    private void loadView(String fxmlPath) {
        try {
            Parent view = FXMLLoader.load(getClass().getResource(fxmlPath));
            contentPane.getChildren().setAll(view);
        } catch (Exception e) {
            e.printStackTrace();
            // Show error label in content area
            Label err = new Label("⚠ Could not load: " + fxmlPath
                + "\n" + e.getMessage());
            err.setStyle("-fx-text-fill: #ff6b6b; -fx-font-size: 13;");
            contentPane.getChildren().setAll(err);
        }
    }

    @FXML private void logoutAction(ActionEvent event) {
        // Clear current user and return to login screen
        LoginController.currentUser = null;
        try {
            Parent loginView = FXMLLoader.load(getClass().getResource(Paths.LOGINVIEW));
            Stage stage = (Stage) contentPane.getScene().getWindow();
            stage.setScene(new Scene(loginView, 480, 420));
            stage.setTitle("WildCat Hotel Reservation System");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
