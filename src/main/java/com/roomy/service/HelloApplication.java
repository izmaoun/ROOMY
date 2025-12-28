package com.roomy.service;

import com.roomy.database.DataInit;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        DataInit.init(); // ← Ajoute cette ligne !

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/fxml/welcome.fxml"));
        
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Roomy: Welcome");
        
        // Set favicon
        try {
            javafx.scene.image.Image icon = new javafx.scene.image.Image(getClass().getResourceAsStream("/images/Logo_favicon.png"));
            stage.getIcons().add(icon);
        } catch (Exception e) {
            System.err.println("Could not load favicon: " + e.getMessage());
        }
        
        stage.setScene(scene);
        stage.setResizable(true);
        stage.setMaximized(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}