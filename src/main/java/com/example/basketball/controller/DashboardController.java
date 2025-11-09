// src/main/java/com/example/basketball/controller/DashboardController.java
package com.example.basketball.controller;

import com.example.basketball.model.Player;
import com.example.basketball.service.PlayerService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardController {

    @FXML private Label userNameLabel;
    @FXML private TextField nameField, ageField, ppgField;
    @FXML private ComboBox<String> positionBox;
    @FXML private ListView<Player> playerListView;
    @FXML private Label statusLabel;

    private final PlayerService playerService = new PlayerService();
    private String currentUserName;

    public void setUserName(String name) {
        this.currentUserName = name;
        userNameLabel.setText(name);
        refreshPlayerList();
    }

    @FXML
    private void initialize() {
        positionBox.setItems(FXCollections.observableArrayList("Guard", "Forward", "Center"));
        refreshPlayerList();
    }

    @FXML
    private void onAddPlayerClick() {
        try {
            String name = nameField.getText().trim();
            String ageStr = ageField.getText().trim();
            String ppgStr = ppgField.getText().trim();
            String position = positionBox.getValue();

            if (name.isEmpty() || ageStr.isEmpty() || ppgStr.isEmpty() || position == null) {
                showStatus("All fields are required.", true);
                return;
            }

            int age = Integer.parseInt(ageStr);
            double ppg = Double.parseDouble(ppgStr);

            Player player = new Player(name, age, position, ppg);
            playerService.addPlayer(player);

            clearForm();
            refreshPlayerList();
            showStatus("Player added: " + name, false);

        } catch (NumberFormatException e) {
            showStatus("Age and PPG must be numbers.", true);
        }
    }

    @FXML
    private void onLogoutClick() {
        switchToLoginScene();
    }

    private void refreshPlayerList() {
        playerListView.setItems(FXCollections.observableArrayList(playerService.getAllPlayers()));
    }

    private void clearForm() {
        nameField.clear();
        ageField.clear();
        ppgField.clear();
        positionBox.setValue(null);
    }

    private void showStatus(String message, boolean isError) {
        statusLabel.setStyle(isError ? "-fx-text-fill: red;" : "-fx-text-fill: green;");
        statusLabel.setText(message);
    }

    private void switchToLoginScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login-view.fxml"));
            Scene loginScene = new Scene(loader.load(), 400, 300);

            Stage stage = (Stage) userNameLabel.getScene().getWindow();
            stage.setScene(loginScene);
            stage.setTitle("Basketball Management System - Login");

            // Reset login message
            LoginController loginCtrl = loader.getController();
            // Optional: pass message if needed

        } catch (IOException e) {
            e.printStackTrace();
            showStatus("Failed to load login screen.", true);
        }
    }
}