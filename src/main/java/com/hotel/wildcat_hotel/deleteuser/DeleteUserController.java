package com.hotel.wildcat_hotel.deleteuser;

import com.hotel.wildcat_hotel.project.DataBase;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class DeleteUserController {

    @FXML private Button deleteuser;
    @FXML private TextField usernamefield;
    
    
    @FXML private void handleSearchUser() {

        String username = usernamefield.getText().trim();

        System.out.println("Searching for: " + username);

    }

    @FXML
    private void handleDeleteUser() {

        String username = usernamefield.getText().trim();

        boolean deleted = DataBase.deleteUser(username);

        if(deleted){
            System.out.println("User deleted!");
        } else {
            System.out.println("User not found!");
        }
    }
}