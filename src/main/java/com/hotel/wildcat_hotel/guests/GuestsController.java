package com.hotel.wildcat_hotel.guests;

import java.util.List;

import com.hotel.wildcat_hotel.hotel.Guest;
import com.hotel.wildcat_hotel.project.DataBase;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class GuestsController {

	@FXML private TextField usernamefield;
	@FXML private TableView<Guest> usersTable;

	@FXML private TableColumn<Guest, Number> usernameColumn;
	@FXML private TableColumn<Guest, Number> roleColumn;
	@FXML private TableColumn<Guest, String> usernameColumn1;
	@FXML private TableColumn<Guest, String> usernameColumn2;
	@FXML private TableColumn<Guest, String> usernameColumn3;
	@FXML private TableColumn<Guest, String> usernameColumn4;
	@FXML private TableColumn<Guest, String> usernameColumn5;

	private final ObservableList<Guest> masterData = FXCollections.observableArrayList();

	@FXML
	public void initialize() {
		usernameColumn.setCellValueFactory(cellData ->
				new SimpleIntegerProperty(cellData.getValue().getGuestID()));
		roleColumn.setCellValueFactory(cellData ->
				new SimpleIntegerProperty(cellData.getValue().getRoomID()));
		usernameColumn1.setCellValueFactory(cellData ->
				new SimpleStringProperty(cellData.getValue().getFirstName()));
		usernameColumn2.setCellValueFactory(cellData ->
				new SimpleStringProperty(cellData.getValue().getLastName()));
		usernameColumn3.setCellValueFactory(cellData ->
				new SimpleStringProperty(cellData.getValue().getEmail()));
		usernameColumn4.setCellValueFactory(cellData ->
				new SimpleStringProperty(cellData.getValue().getPhoneNo()));
		usernameColumn5.setCellValueFactory(cellData ->
				new SimpleStringProperty(cellData.getValue().getCity()));

		loadInitialData();
	}

	private void loadInitialData() {
		try {
			List<Guest> guests = DataBase.getGuests();
			if (guests != null) {
				masterData.setAll(guests);
			} else {
				masterData.clear();
			}
		} catch (Exception e) {
			e.printStackTrace();
			masterData.clear();
		}
		usersTable.setItems(masterData);
	}

	@FXML
	private void handleSearchUser() {
		String query = usernamefield.getText() == null
				? ""
				: usernamefield.getText().trim().toLowerCase();

		if (query.isEmpty()) {
			usersTable.setItems(masterData);
			return;
		}

		ObservableList<Guest> filteredData = FXCollections.observableArrayList();
		for (Guest guest : masterData) {
			String first = guest.getFirstName() == null ? "" : guest.getFirstName().toLowerCase();
			String last = guest.getLastName() == null ? "" : guest.getLastName().toLowerCase();
			String email = guest.getEmail() == null ? "" : guest.getEmail().toLowerCase();
			if (first.contains(query) || last.contains(query) || email.contains(query)) {
				filteredData.add(guest);
			}
		}

		usersTable.setItems(filteredData);
	}
}
