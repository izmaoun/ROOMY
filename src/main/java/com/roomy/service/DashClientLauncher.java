package com.roomy.service;

import com.roomy.entities.Client;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DashClientLauncher extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        System.out.println("=== Démarrage DashClientLauncher ===");

        // Client de test pour la session
        Client testClient = new Client("Imane", "Imanita", "imane@example.com", "0600000000", "hashedpass");
        testClient.setIdClient(1);
        Session.setCurrentClient(testClient);
        System.out.println("✅ Client de test créé: " + testClient.getPrenom());

        try {
            // Charger le FXML
            System.out.println("Chargement du FXML: /fxml/dash_client.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/dash_client.fxml"));

            if (fxmlLoader.getLocation() == null) {
                System.err.println("❌ ERREUR: Fichier FXML introuvable!");
                showErrorStage("Fichier dash_client.fxml introuvable!");
                return;
            }

            System.out.println("FXML trouvé, création de la scène...");
            Scene scene = new Scene(fxmlLoader.load(), 1200, 700);

            // Configuration de la fenêtre
            stage.setTitle("Roomy - Dashboard Client");
            stage.setResizable(true);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();

            System.out.println("✅ Dashboard Client lancé avec succès !");
            System.out.println("📋 Client connecté: " + testClient.getPrenom() + " " + testClient.getNom());

        } catch (Exception e) {
            System.err.println("❌ ERREUR lors du démarrage: " + e.getMessage());
            e.printStackTrace();
            showErrorStage("Erreur: " + e.getMessage());
        }
    }

    private void showErrorStage(String message) {
        try {
            Stage errorStage = new Stage();
            errorStage.setTitle("Erreur - Roomy");
            javafx.scene.control.Label label = new javafx.scene.control.Label(message);
            javafx.scene.layout.VBox vbox = new javafx.scene.layout.VBox(label);
            vbox.setPadding(new javafx.geometry.Insets(20));
            Scene errorScene = new Scene(vbox, 400, 200);
            errorStage.setScene(errorScene);
            errorStage.show();
        } catch (Exception e) {
            System.err.println("Impossible d'afficher l'erreur: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        System.out.println("Lancement de l'application DashClientLauncher...");
        launch(args);
    }
}