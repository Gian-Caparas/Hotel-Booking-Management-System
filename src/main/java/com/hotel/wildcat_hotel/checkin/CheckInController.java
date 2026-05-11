package com.hotel.wildcat_hotel.checkin;

import com.hotel.wildcat_hotel.project.DataBase;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class CheckInController {

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextField cityField;
    @FXML private TextField nationalityField;
    @FXML private ComboBox<String> roomTypeComboBox;
    @FXML private ComboBox<String> roomCapacityComboBox;
    @FXML private DatePicker checkInDatePicker;
    @FXML private DatePicker checkOutDatePicker;
    @FXML private Button checkInButton;

    @FXML
    private void handleCheckIn(ActionEvent event) {
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String city = cityField.getText().trim();
        String nationality = nationalityField.getText().trim();
        String roomType = roomTypeComboBox.getValue();
        String roomCapacity = roomCapacityComboBox.getValue();
        LocalDate checkInDate = checkInDatePicker.getValue();
        LocalDate checkOutDate = checkOutDatePicker.getValue();

        // ================= VALIDATION =================
        if (
                firstName.isEmpty() ||
                lastName.isEmpty() ||
                email.isEmpty() ||
                phone.isEmpty() ||
                city.isEmpty() ||
                nationality.isEmpty() ||
                roomType == null ||
                roomCapacity == null ||
                checkInDate == null ||
                checkOutDate == null
        ) {
            showNotification(
                    "Missing Information",
                    "Please fill out all fields."
            );
            return;
        }

        if (checkOutDate.isBefore(checkInDate)) {
            showNotification(
                    "Date Error",
                    "Check-out date cannot be before check-in date."
            );
            return;
        }
        try {
            Connection con = getDatabaseConnection();
            // ================= GET AVAILABLE ROOM =================
            String roomSQL =
                    "SELECT * FROM room " +
                    "WHERE status = 'AVAILABLE' " +
                    "AND room_type = ? " +
                    "AND room_capacity = ? " +
                    "LIMIT 1";
            PreparedStatement roomStmt = con.prepareStatement(roomSQL);
            roomStmt.setString(1, roomType);
            roomStmt.setString(2, roomCapacity);

            ResultSet rs = roomStmt.executeQuery();
            if (!rs.next()) {
                showNotification(
                        "No Rooms",
                        "No available rooms found."
                );
                return;
            }

            int roomID = rs.getInt("roomID");
            double ratePerNight = rs.getDouble("rate_per_night");
            long numberOfDays = ChronoUnit.DAYS.between( checkInDate, checkOutDate);
            if (numberOfDays <= 0) {
                numberOfDays = 1;
            }
            double totalFees =
                    ratePerNight * numberOfDays;
            // ================= INSERT GUEST =================

           // Add room_type and room_capacity to the list (14 columns total)
           String insertSQL =
                "INSERT INTO guest (" +
                "room_ID, first_name, last_name, email, phone_no, city, nationality, " +
                "room_type, room_capacity, check_in_date, check_out_date, " + 
                "number_of_days, rate_per_night, total_fees" +
                ") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)"; // Use 14 placeholders

            PreparedStatement insertStmt = con.prepareStatement(insertSQL);
            insertStmt.setInt(1, roomID);
            insertStmt.setString(2, firstName);
            insertStmt.setString(3, lastName);
            insertStmt.setString(4, email);
            insertStmt.setString(5, phone);
            insertStmt.setString(6, city);
            insertStmt.setString(7, nationality);
            insertStmt.setString(8, roomType);
            insertStmt.setString(9, roomCapacity);
            insertStmt.setDate(10, java.sql.Date.valueOf(checkInDate));
            insertStmt.setDate(11, java.sql.Date.valueOf(checkOutDate));
            insertStmt.setLong(12, numberOfDays);
            insertStmt.setDouble(13, ratePerNight);
            insertStmt.setDouble(14, totalFees);
            insertStmt.executeUpdate();

            // ================= UPDATE ROOM STATUS =================

            String updateRoomSQL =
                    "UPDATE room " +
                    "SET status = 'OCCUPIED', " +
                    "Check_In_Date = ?, " +
                    "Check_Out_Date = ? " +
                    "WHERE roomID = ?";

            PreparedStatement updateStmt = con.prepareStatement(updateRoomSQL);
            updateStmt.setDate(1, java.sql.Date.valueOf(checkInDate));
            updateStmt.setDate(2, java.sql.Date.valueOf(checkOutDate));
            updateStmt.setInt(3, roomID);
            updateStmt.executeUpdate();

            // ================= SUCCESS =================
            showNotification(
                    "Check-In Successful",
                    "Guest checked into Room #" +
                    roomID +
                    "\nTotal Fees: ₱" +
                    totalFees
            );

            clearFields();
            rs.close();
            roomStmt.close();
            insertStmt.close();
            updateStmt.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
            showNotification(
                    "Database Error",
                    e.getMessage()
            );
        }
    }

    // ================= CLEAR =================
    private void clearFields() {
        firstNameField.clear();
        lastNameField.clear();
        emailField.clear();
        phoneField.clear();
        cityField.clear();
        nationalityField.clear();
        roomTypeComboBox.setValue(null);
        roomCapacityComboBox.setValue(null);
        checkInDatePicker.setValue(null);
        checkOutDatePicker.setValue(null);
    }

    // ================= ALERT =================

    private void showNotification(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private Connection getDatabaseConnection() throws Exception {
        DataBase dataBase = new DataBase();
        String[] methodNames = {
                "getConnection",
                "getConnectionDB",
                "connect",
                "getConnect",
                "getConn"
        };
        for (String methodName : methodNames) {
                try {
                Object result = DataBase.class.getMethod(methodName).invoke(dataBase);
                        if (result instanceof Connection) {
                                return (Connection) result;
                        }
                } catch (NoSuchMethodException ignored) {
                        // Try the next possible method name.

                }
        }
        throw new IllegalStateException(
                "No compatible database connection method found in DataBase class."
                );
        }

        @FXML public void initialize() {
        // Populate Room Type ComboBox
        roomTypeComboBox.getItems().addAll("Economy", "Normal", "Vip");

        // Populate Room Capacity ComboBox
        roomCapacityComboBox.getItems().addAll("Single", "Double", "Triple");
        }
}