// src/main/java/com/example/basketball/controller/PlayersTabController.java
package com.example.basketball.controller;

import com.example.basketball.model.enums.Handedness;
import com.example.basketball.model.Player;
import com.example.basketball.service.PlayerService;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.net.URL;
import java.util.Arrays;

public class PlayersTabController {

    @FXML private TextField nameField, ageField, heightField, weightField, wingspanField, verticalField;
    @FXML private ComboBox<Handedness> handednessBox;
    @FXML private ComboBox<String> photoComboBox;
    @FXML private Slider staminaSlider, agilitySlider, speedSlider;
    @FXML private Label staminaLabel, agilityLabel, speedLabel;
    @FXML private ImageView photoPreview;
    @FXML private ListView<Player> playerListView;
    @FXML private Label statusLabel;

    private final PlayerService playerService = new PlayerService();

    @FXML
    private void initialize() {
        // Handedness
        handednessBox.setItems(FXCollections.observableArrayList(Handedness.values()));

        // Photos
        loadPhotoOptions();

        // Sliders
        bindSlider(staminaSlider, staminaLabel);
        bindSlider(agilitySlider, agilityLabel);
        bindSlider(speedSlider, speedLabel);

        // Photo preview
        photoComboBox.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) ->  updatePhotoPreview(newVal)
        );

        refreshPlayerList();
    }

    private void loadPhotoOptions() {
        // Scan /photos folder
        URL photosDir = getClass().getResource("/photos");
        if (photosDir != null) {
            try {
                File dir = new File(photosDir.toURI());
                String[] files = dir.list((d, name) -> name.matches(".*\\.(jpg|png|jpeg)"));
                if (files != null) {
                    Arrays.stream(files).sorted().forEach(f -> photoComboBox.getItems().add("/photos/" + f));
                }
            } catch (Exception e) {
                statusLabel.setText("Warning: Could not load photos.");
            }
        }
        if (photoComboBox.getItems().isEmpty()) {
            photoComboBox.getItems().add("/photos/default.png");
        }
    }

    private void bindSlider(Slider slider, Label label) {
        label.textProperty().bind(slider.valueProperty().asString("%.0f"));
    }

    private void updatePhotoPreview(String path) {
        if (path != null && !path.isEmpty()) {
            Image img = new Image(getClass().getResourceAsStream(path), 80, 80, true, true);
            photoPreview.setImage(img);
        }
    }

    @FXML
    private void onAddPlayerClick() {
        String name = nameField.getText().trim();

        // --- Only name is mandatory ---
        if (name.isEmpty()) {
            showStatus("Name is required.", true);
            return;
        }

        try {
            // Parse optional numeric fields safely
            int age = parseIntOrZero(ageField.getText());
            double height = parseDoubleOrZero(heightField.getText());
            double weight = parseDoubleOrZero(weightField.getText());
            double wingspan = parseDoubleOrZero(wingspanField.getText());
            double vertical = parseDoubleOrZero(verticalField.getText());

            Handedness handedness = handednessBox.getValue(); // can be null
            String photoPath = photoComboBox.getValue();     // can be null

            int stamina = (int) staminaSlider.getValue();
            int agility = (int) agilitySlider.getValue();
            int speed = (int) speedSlider.getValue();

            Player player = new Player(
                    name, age, height, weight, wingspan,
                    handedness, vertical, stamina, agility, speed, photoPath
            );

            playerService.addPlayer(player);
            clearForm();
            refreshPlayerList();
            showStatus("Player added: " + name, false);

        } catch (Exception e) {
            showStatus("Invalid input: " + e.getMessage(), true);
        }
    }

    private void refreshPlayerList() {
        playerListView.setItems(FXCollections.observableArrayList(playerService.getAllPlayers()));
    }

    private void clearForm() {
        nameField.clear(); ageField.clear(); heightField.clear(); weightField.clear();
        wingspanField.clear(); verticalField.clear();
        handednessBox.setValue(null); photoComboBox.setValue(null);
        staminaSlider.setValue(50); agilitySlider.setValue(50); speedSlider.setValue(50);
    }

    private void showStatus(String msg, boolean error) {
        statusLabel.setStyle(error ? "-fx-text-fill: red;" : "-fx-text-fill: green;");
        statusLabel.setText(msg);
    }

    private int parseIntOrZero(String text) {
        try {
            return text == null || text.trim().isEmpty() ? 0 : Integer.parseInt(text.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private double parseDoubleOrZero(String text) {
        try {
            return text == null || text.trim().isEmpty() ? 0.0 : Double.parseDouble(text.trim());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}