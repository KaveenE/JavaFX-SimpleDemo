package com.example.basketball.controller;

import com.example.basketball.model.Player;
import com.example.basketball.model.enums.Handedness;
import com.example.basketball.service.PlayerService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.Arrays;

public class PlayerEditDialogController {

    @FXML private TextField nameField, ageField, heightField, weightField, wingspanField, verticalField;
    @FXML private ComboBox<Handedness> handednessBox;
    @FXML private ComboBox<String> photoComboBox;
    @FXML private Slider staminaSlider, agilitySlider, speedSlider;
    @FXML private Label staminaLabel, agilityLabel, speedLabel;
    @FXML private ImageView photoPreview;
    @FXML private Label statusLabel;

    private final PlayerService playerService = new PlayerService();
    private Player player;               // the player we are editing
    private Stage dialogStage;
    private boolean saved = false;      // tells caller if Save was pressed

    /* --------------------------------------------------------------------- */
    @FXML
    private void initialize() {
        // Handedness
        handednessBox.setItems(FXCollections.observableArrayList(Handedness.values()));
    }
    public void setDialogStage(Stage stage) { this.dialogStage = stage; }
    public boolean isSaved() { return saved; }

    public void setPlayer(Player p) {
        this.player = p;
        populateFields();
        loadPhotoOptions();
        bindSliders();
        setupPhotoListener();
    }

    private void populateFields() {
        nameField.setText(player.getName());
        ageField.setText(player.getAge() > 0 ? String.valueOf(player.getAge()) : "");
        heightField.setText(player.getHeight() > 0 ? String.format("%.1f", player.getHeight()) : "");
        weightField.setText(player.getWeight() > 0 ? String.format("%.1f", player.getWeight()) : "");
        wingspanField.setText(player.getWingspan() > 0 ? String.format("%.1f", player.getWingspan()) : "");
        verticalField.setText(player.getMaxVerticalLeap() > 0 ? String.format("%.1f", player.getMaxVerticalLeap()) : "");

        handednessBox.setValue(player.getHandedness());

        staminaSlider.setValue(player.getStamina());
        agilitySlider.setValue(player.getAgility());
        speedSlider.setValue(player.getSpeed());

        photoComboBox.setValue(player.getPhotoPath());
        updatePhotoPreview(player.getPhotoPath());
    }

    private void loadPhotoOptions() {
        URL photosDir = getClass().getResource("/photos");
        if (photosDir != null) {
            try {
                File dir = new File(photosDir.toURI());
                String[] files = dir.list((d, n) -> n.matches(".*\\.(jpg|png|jpeg)"));
                if (files != null) {
                    Arrays.stream(files).sorted()
                            .forEach(f -> photoComboBox.getItems().add("/photos/" + f));
                }
            } catch (Exception ignored) {}
        }
        if (photoComboBox.getItems().isEmpty()) {
            photoComboBox.getItems().add("/photos/default.png");
        }
    }

    private void bindSliders() {
        staminaLabel.textProperty().bind(staminaSlider.valueProperty().asString("%.0f"));
        agilityLabel.textProperty().bind(agilitySlider.valueProperty().asString("%.0f"));
        speedLabel.textProperty().bind(speedSlider.valueProperty().asString("%.0f"));
    }

    private void setupPhotoListener() {
        photoComboBox.getSelectionModel().selectedItemProperty()
                .addListener((obs, old, newer) -> updatePhotoPreview(newer));
    }

    private void updatePhotoPreview(String path) {
        if (path != null && !path.isEmpty()) {
            Image img = new Image(getClass().getResourceAsStream(path), 80, 80, true, true);
            photoPreview.setImage(img);
        } else {
            photoPreview.setImage(null);
        }
    }

    /* --------------------------- BUTTON ACTIONS -------------------------- */
    @FXML private void onSaveClick() {
        if (nameField.getText().trim().isEmpty()) {
            statusLabel.setText("Name is required.");
            return;
        }

        player.setName(nameField.getText().trim());
        player.setAge(parseIntOrZero(ageField.getText()));
        player.setHeight(parseDoubleOrZero(heightField.getText()));
        player.setWeight(parseDoubleOrZero(weightField.getText()));
        player.setWingspan(parseDoubleOrZero(wingspanField.getText()));
        player.setHandedness(handednessBox.getValue());
        player.setMaxVerticalLeap(parseDoubleOrZero(verticalField.getText()));
        player.setStamina((int) staminaSlider.getValue());
        player.setAgility((int) agilitySlider.getValue());
        player.setSpeed((int) speedSlider.getValue());
        player.setPhotoPath(photoComboBox.getValue());

        playerService.updatePlayer(player);
        saved = true;
        dialogStage.close();
    }

    @FXML private void onDeleteClick() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete " + player.getName() + " ?", ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.YES) {
                playerService.deletePlayer(player.getId());
                saved = true;               // tell caller to refresh
                dialogStage.close();
            }
        });
    }

    @FXML private void onCancelClick() {
        dialogStage.close();
    }

    /* --------------------------- HELPERS ------------------------------- */
    private int parseIntOrZero(String txt) {
        try { return txt == null || txt.trim().isEmpty() ? 0 : Integer.parseInt(txt.trim()); }
        catch (NumberFormatException e) { return 0; }
    }

    private double parseDoubleOrZero(String txt) {
        try { return txt == null || txt.trim().isEmpty() ? 0.0 : Double.parseDouble(txt.trim()); }
        catch (NumberFormatException e) { return 0.0; }
    }
}