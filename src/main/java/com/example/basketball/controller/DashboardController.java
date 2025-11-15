package com.example.basketball.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardController {

    @FXML private Label userNameLabel;
    @FXML private TabPane mainTabPane;

    private String currentUserName;

    /**
     * Called automatically after FXML is loaded.
     */
    @FXML
    private void initialize() {
        // Select Players tab by default
        if (mainTabPane != null) {
            mainTabPane.getSelectionModel().select(0); // Index 0 = Players
        }

        // Display username if already set
        if (currentUserName != null) {
            userNameLabel.setText(currentUserName);
        }
    }

    /**
     * Public method called from LoginController to pass the logged-in user's name.
     */
    public void setUserName(String name) {
        this.currentUserName = name;
        if (userNameLabel != null) {
            userNameLabel.setText(name);
        }
    }

    /**
     * Logout button handler – switches back to login screen
     */
    @FXML
    private void onLogoutClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login-view.fxml"));
            Scene loginScene = new Scene(loader.load(), 400, 300);

            Stage stage = (Stage) userNameLabel.getScene().getWindow();
            stage.setScene(loginScene);
            stage.setTitle("Basketball Management System - Login");
            stage.centerOnScreen();

        } catch (IOException e) {
            e.printStackTrace();
            showErrorInUI("Failed to return to login screen.");
        }
    }

    /**
     * Optional: utility to show errors (can be expanded later)
     */
    private void showErrorInUI(String message) {
        // You can add a status label in main-dashboard.fxml later if needed
        System.err.println(message);
    }
}