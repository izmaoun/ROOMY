package com.roomy.Controller;

import com.roomy.service.Session;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class DashClientController {

    @FXML private Label lblClientName;
    @FXML private Button btnLogout;
    @FXML private Button btnReservations;
    @FXML private Button btnProfile;
    @FXML private Button btnHotels;
    @FXML private StackPane centerStack;

    @FXML
    public void initialize() {
        // Afficher le nom du client connecté
        if (Session.getCurrentClient() != null) {
            lblClientName.setText(Session.getCurrentClient().getPrenom() + " " +
                    Session.getCurrentClient().getNom());
        }

        // Configuration des boutons du menu
        btnReservations.setOnAction(e -> loadReservationsPage());
        btnProfile.setOnAction(e -> loadProfilePage());
        btnHotels.setOnAction(e -> loadHotelsPage());

        // Bouton déconnexion
        btnLogout.setOnAction(e -> handleLogout());
        
        // Charger automatiquement la page des réservations au démarrage
        loadReservations();
    }

    private void loadReservations() {
        loadReservationsPage();
    }


    @FXML
    private void loadReservationsPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/client_reservations.fxml"));
            Parent reservationsContent = loader.load();
            
            centerStack.getChildren().clear();
            centerStack.getChildren().add(reservationsContent);

            System.out.println("Page réservations (tableau) chargée");

        } catch (Exception e) {
            System.err.println("Erreur chargement réservations: " + e.getMessage());
            e.printStackTrace();
        }
    }


    @FXML
    private void loadProfilePage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/client_profile.fxml"));
            Parent profileContent = loader.load();

            centerStack.getChildren().clear();
            centerStack.getChildren().add(profileContent);

            System.out.println("Page profil chargée");

        } catch (IOException e) {
            System.err.println("Erreur chargement profil: " + e.getMessage());
            e.printStackTrace();
        }
    }


    @FXML
    private void loadHotelsPage() {
        // Afficher un indicateur de chargement
        System.out.println("Chargement de la page hôtels...");
        
        // Utiliser Platform.runLater pour éviter le blocage
        javafx.application.Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/landing-page.fxml"));
                Parent root = loader.load();
                
                LandingPageController controller = loader.getController();
                controller.hideAuthButtons();
                controller.showBackToAccountButton();
                
                Stage stage = (Stage) btnHotels.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("ROOMY - Hôtels");
                stage.setMaximized(true);
                
                System.out.println("Page hôtels chargée avec succès");
            } catch (Exception e) {
                System.err.println("Erreur chargement hôtels: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }


    @FXML
    private void handleLogout() {
        Session.logout();
        System.out.println("Déconnexion effectuée");

        // Rediriger vers la page de login
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Parent loginRoot = loader.load();

            Stage stage = (Stage) btnLogout.getScene().getWindow();
            Scene scene = new Scene(loginRoot);
            stage.setScene(scene);
            stage.setTitle("Roomy - Connexion");
            stage.setMaximized(true);

        } catch (IOException e) {
            System.err.println("Erreur lors de la redirection vers login: " + e.getMessage());
            e.printStackTrace();
        }
    }
}