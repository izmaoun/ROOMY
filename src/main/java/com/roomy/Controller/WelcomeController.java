package com.roomy.Controller;

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
        switchScene(event, "client_signup.fxml");
    }

    @FXML
    private void goCollaborateur(ActionEvent event) {
        switchScene(event, "collab_signup.fxml");
    }

    private void switchScene(ActionEvent event, String fxmlName) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/" + fxmlName));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            stage.setMaximized(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}