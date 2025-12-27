package com.roomy.Controller;

import com.roomy.service.StatistiquesService;
import com.roomy.Dao.HotelierDAO;
import com.roomy.dto.HotelStats; // Assure-toi d'importer le DTO
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

import java.io.IOException;

public class Dash_hotelier_Control {

    // ID de l'hôtelier connecté (Partagé avec les autres contrôleurs)
    private static int currentHotelierId = 0;

    public static int getCurrentHotelierId() { return currentHotelierId; }
    public static void setCurrentHotelierId(int id) { currentHotelierId = id; }

    @FXML private BorderPane mainBorderPane;
    @FXML private Label lblwelcome;

    @FXML private Text txtNbHotels;
    @FXML private Text txtNbChambresTotal;
    @FXML private Text txtNbChambresDispo;
    @FXML private Text txtNbChambresOccupees;

    private final StatistiquesService statistiquesService = new StatistiquesService();

    @FXML
    private void initialize() {
        if (currentHotelierId > 0) {
            HotelierDAO hotelierDAO = new HotelierDAO();
            String nomComplet = hotelierDAO.getNomComplet(currentHotelierId);
            if (lblwelcome != null) lblwelcome.setText("Bienvenue, " + nomComplet);

            // On lance le dashboard par défaut au centre
            loadDashboardContent();
        }
    }

    // Affiche les stats globales dans la barre du haut ou le dashboard
    public void refreshStats() {
        try {
            // Utilisation du DTO global pour éviter 4 appels BDD
            HotelStats stats = statistiquesService.getGlobalStats(currentHotelierId);

            if (txtNbHotels != null) txtNbHotels.setText(String.valueOf(stats.getNbHotels()));
            if (txtNbChambresTotal != null) txtNbChambresTotal.setText(String.valueOf(stats.getNbChambresTotal()));
            if (txtNbChambresDispo != null) txtNbChambresDispo.setText(String.valueOf(stats.getNbChambresDispo()));
            if (txtNbChambresOccupees != null) txtNbChambresOccupees.setText(String.valueOf(stats.getNbChambresOccupees()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Méthode pour charger les fichiers FXML dans le centre
    private void loadCenter(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent page = loader.load();
            mainBorderPane.setCenter(page);
        } catch (IOException e) {
            System.err.println("Erreur chargement FXML: " + fxmlFile);
            e.printStackTrace();
        }
    }

    // --- NAVIGATION ---

    @FXML
    private void goToDashboard() {
        // Pour le dashboard, soit on recharge les stats, soit on a un FXML "Accueil"
        // Ici, je suppose que tu veux voir les cartes :
        refreshStats();
        // Si tu as un contenu spécifique pour l'accueil (ex: derniers clients), charge-le ici :
        // loadCenter("/fxml/accueil_dashboard.fxml");
    }

    private void loadDashboardContent() {
        refreshStats();
        // Optionnel : charger un contenu par défaut au centre
    }

    @FXML private void goToMesHotels() { loadCenter("/fxml/mes_hotels.fxml"); }
    @FXML private void goToChambres() { loadCenter("/fxml/chambres.fxml"); }
    @FXML private void goToStatistiques() { loadCenter("/fxml/statistiques.fxml"); } // Charge le fichier ci-dessous
    @FXML private void goToProfil() { loadCenter("/fxml/profile.fxml"); }

    @FXML
    private void handleDeconnexion() {
        try {
            Parent login = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
            mainBorderPane.getScene().setRoot(login);
        } catch (Exception e) { e.printStackTrace(); }
    }
}