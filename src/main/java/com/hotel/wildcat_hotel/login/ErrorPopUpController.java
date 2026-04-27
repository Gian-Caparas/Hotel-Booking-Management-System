package com.hotel.wildcat_hotel.login;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;;
public class ErrorPopUpController implements Initializable {
    
    @FXML
    private Label messageLabel;

    @FXML
    private Button okBtn;

    @FXML
    private Label messageLabel1;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    @FXML
    private void closePopUpAction(ActionEvent event) {
        // Close the pop-up window
        okBtn.getScene().getWindow().hide();
    }
}
