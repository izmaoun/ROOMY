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

    @FXML
<<<<<<< HEAD
    private void goToLandingPage(ActionEvent event) {
        switchScene(event, "landingPage.fxml");
    }


=======
    private void goBack(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/landing-page.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1200, 800);
            stage.setScene(scene);
            stage.setTitle("ROOMY - Accueil");
            stage.setMaximized(true);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

>>>>>>> 146ddc43664c4b11e5d3f96cac87047998ebacd1
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