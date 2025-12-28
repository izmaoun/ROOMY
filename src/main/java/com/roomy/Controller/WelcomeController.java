package com.roomy.Controller;

import com.roomy.util.WindowUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.event.ActionEvent;
import javafx.scene.Node;

public class WelcomeController {
    @FXML
    private void goClient(ActionEvent event) {
        switchScene(event, "client_signup.fxml", "Roomy: Client Sign Up");
    }

    @FXML
    private void goCollaborateur(ActionEvent event) {
        switchScene(event, "collab_signup.fxml", "Roomy: Hotel Sign Up");
    }

    private void switchScene(ActionEvent event, String fxmlName, String title) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/" + fxmlName));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Capture current window size
            double currentWidth = stage.getWidth();
            double currentHeight = stage.getHeight();

            Scene scene = new Scene(root, currentWidth, currentHeight);
            stage.setScene(scene);
            stage.setResizable(true);
            stage.setTitle(title);
            
            // Set favicon
            try {
                javafx.scene.image.Image icon = new javafx.scene.image.Image(getClass().getResourceAsStream("/images/Logo_favicon.png"));
                stage.getIcons().add(icon);
            } catch (Exception e) {
                System.err.println("Could not load favicon: " + e.getMessage());
            }
            
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}