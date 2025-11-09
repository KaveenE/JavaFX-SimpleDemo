// src/main/java/com/example/basketball/controller/LoginController.java
package com.example.basketball.controller;

import com.example.basketball.util.PasswordManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML private TextField nameField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;

    private static final String CORRECT_PASSWORD = PasswordManager.getPassword();

    @FXML
    private void onLoginClick() {
        String name = nameField.getText().trim();
        String password = passwordField.getText();

        if (name.isEmpty()) {
            showError("Please enter your name.");
            return;
        }

        if (!CORRECT_PASSWORD.equals(password)) {
            showError("Incorrect password.");
            return;
        }

        // Success: Load dashboard
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main-dashboard.fxml"));
            Scene dashboardScene = new Scene(loader.load(), 700, 600);

            DashboardController dashboardCtrl = loader.getController();
            dashboardCtrl.setUserName(name);

            Stage stage = (Stage) nameField.getScene().getWindow();
            stage.setScene(dashboardScene);
            stage.setTitle("Dashboard - " + name);

            showSuccess("Login successful!");
        } catch (IOException e) {
            showError("Failed to load dashboard.");
            e.printStackTrace();
        }
    }

    private void showError(String message) {
        messageLabel.setStyle("-fx-text-fill: red;");
        messageLabel.setText(message);
    }

    private void showSuccess(String message) {
        messageLabel.setStyle("-fx-text-fill: green;");
        messageLabel.setText(message);
    }
}