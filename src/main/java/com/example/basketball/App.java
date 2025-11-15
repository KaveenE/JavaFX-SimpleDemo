package com.example.basketball;

import com.example.basketball.util.DatabaseServer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // 1. Start H2 TCP + Web console
        DatabaseServer.start();
        DatabaseServer.initIfNeeded();

        // 2. Load login UI
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/fxml/login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 300);
        stage.setTitle("Basketball Management System");
        stage.setScene(scene);
        stage.show();

        // 3. Stop servers when window closes
        stage.setOnCloseRequest(e -> {
            DatabaseServer.stop();
            Platform.exit();
        });
    }

    public static void main(String[] args) {
        launch();
    }
}