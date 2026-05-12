package com.hotel.wildcat_hotel.homepage;

import java.net.URL;
import java.util.ResourceBundle;

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

public class HomePageController implements Initializable {

    @FXML private StackPane contentPane;
    @FXML private Label     currentUserLabel;

    // Staff-only controls
    @FXML private Label  operationsLabel;
    @FXML private Button checkInButton;
    @FXML private Button checkOutButton;
    @FXML private Label  recordsLabel;
    @FXML private Button viewGuestsButton;
    @FXML private Button viewRoomsButton;

    // Customer-only controls
    @FXML private Label  customerLabel;
    @FXML private Button bookRoomButton;
    @FXML private Button cancelBookingButton;
    @FXML private Button availableRoomButton;

    // Admin-only controls
    @FXML private Label  adminLabel;
    @FXML private Button addUserButton;
    @FXML private Button deleteUserButton;
    @FXML private Button viewUsersButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // ── Step 1: Hide every role-specific control by default ───────────────
        hideAll();

        // ── Step 2: Show only what the current user's role allows ─────────────
        if (LoginController.currentUser == null) return;

        String role = LoginController.currentUser.getRole(); // "Admin" | "Staff" | "Customer"

        currentUserLabel.setText(
                "Logged in as: " + LoginController.currentUser.getUsername()
                + " (" + role + ")"
        );

        switch (role) {

            case "Admin":
                // Admins see everything
                showAdminControls();
                showStaffControls();
                showCustomerControls();
                break;

            case "Staff":
                showStaffControls();
                break;

            case "Customer":
                showCustomerControls();
                break;

            default:
                // Unknown role — nothing extra shown
                break;
        }
    }

    // ── Role-visibility helpers ───────────────────────────────────────────────

    private void hideAll() {
        // Admin
        adminLabel.setVisible(false);
        addUserButton.setVisible(false);
        deleteUserButton.setVisible(false);
        viewUsersButton.setVisible(false);
        // Staff
        operationsLabel.setVisible(false);
        checkInButton.setVisible(false);
        checkOutButton.setVisible(false);
        recordsLabel.setVisible(false);
        viewGuestsButton.setVisible(false);
        viewRoomsButton.setVisible(false);
        // Customer
        customerLabel.setVisible(false);
        bookRoomButton.setVisible(false);
        cancelBookingButton.setVisible(false);
        availableRoomButton.setVisible(false);
    }

    private void showAdminControls() {
        adminLabel.setVisible(true);
        addUserButton.setVisible(true);
        deleteUserButton.setVisible(true);
        viewUsersButton.setVisible(true);
    }

    private void showStaffControls() {
        operationsLabel.setVisible(true);
        checkInButton.setVisible(true);
        checkOutButton.setVisible(true);
        recordsLabel.setVisible(true);
        viewGuestsButton.setVisible(true);
        viewRoomsButton.setVisible(true);
    }

    private void showCustomerControls() {
        customerLabel.setVisible(true);
        bookRoomButton.setVisible(true);
        cancelBookingButton.setVisible(true);
        availableRoomButton.setVisible(true);
    }

    // ── Navigation handlers ───────────────────────────────────────────────────

    @FXML private void openCheckIn(ActionEvent e)       { loadView(Paths.CHECKINVIEW); }
    @FXML private void openCheckOut(ActionEvent e)      { loadView(Paths.CHECKOUTVIEW); }
    @FXML private void openRoomBooking(ActionEvent e)   { loadView(Paths.ROOMBOOKINGVIEW); }
    @FXML private void openCancelBooking(ActionEvent e) { loadView(Paths.CANCELBOOKINGVIEW); }
    @FXML private void openGuests(ActionEvent e)        { loadView(Paths.GUESTSVIEW); }
    @FXML private void openRooms(ActionEvent e)         { loadView(Paths.ROOMSVIEW); }
    @FXML private void openAvailableRooms(ActionEvent e) { loadView(Paths.AVAILABLEROOMVIEW); }
    @FXML private void openAddUser(ActionEvent e)       { loadView(Paths.ADDUSERVIEW); }
    @FXML private void openDeleteUser(ActionEvent e)    { loadView(Paths.DELETEUSERVIEW); }
    @FXML private void openViewUsers(ActionEvent e)     { loadView(Paths.VIEWUSERSVIEW); }

    private void loadView(String fxmlPath) {
        try {
            Parent view = FXMLLoader.load(getClass().getResource(fxmlPath));
            contentPane.getChildren().setAll(view);
        } catch (Exception e) {
            e.printStackTrace();
            Label err = new Label("⚠ Could not load: " + fxmlPath + "\n" + e.getMessage());
            err.setStyle("-fx-text-fill: #ff6b6b; -fx-font-size: 13;");
            contentPane.getChildren().setAll(err);
        }
    }

    // ── Logout ────────────────────────────────────────────────────────────────

    @FXML
    private void logoutAction(ActionEvent event) {
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