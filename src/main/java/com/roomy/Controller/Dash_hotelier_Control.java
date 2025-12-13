package com.roomy.Controller;

import com.roomy.Dao.ChambreDAO;
import com.roomy.Dao.HotelDAO;
import com.roomy.Dao.HotelierDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class Dash_hotelier_Control {

    @FXML private BorderPane mainBorderPane;
    @FXML private Label lblwelcome;

    // ID de l'hôtelier connecté
    private static int currentHotelierId = 0;

    public static void setCurrentHotelierId(int id) {
        currentHotelierId = id;
    }

    public static int getCurrentHotelierId() {
        return currentHotelierId;
    }

    @FXML
    private void initialize() {
        // Affichage du nom réel de l'hôtelier
        if (currentHotelierId > 0) {
            HotelierDAO hotelierDAO = new HotelierDAO();
            String nomComplet = hotelierDAO.getNomComplet(currentHotelierId);
            lblwelcome.setText("Bienvenue, " + nomComplet);
        }
    }

    // Méthode générique pour charger une page dans le center
    private void loadCenter(String fxmlFile) {
        try {
            Parent page = FXMLLoader.load(getClass().getResource("/src/main/resources/fxml" + fxmlFile));
            mainBorderPane.setCenter(page);
        } catch (Exception e) {
            e.printStackTrace();
            lblwelcome.setText("Erreur : page non trouvée");
        }
    }

    // ==================== ACTIONS DES BOUTONS DU MENU ====================
    @FXML private void goToDashboard()     { loadCenter("dash_hotelier.fxml"); }

    @FXML private void goToMesHotels()     { loadCenter("mes_hotels.fxml"); }

    @FXML private void goToChambres()      { loadCenter("chambres.fxml"); }

    @FXML private void goToStatistiques()  { loadCenter("statistiques.fxml"); }

    @FXML private void goToProfil()        { loadCenter("profil.fxml"); }

    @FXML
    private void handleDeconnexion() {
        // Retour à l'écran de login (adapte le chemin selon ton fichier login)
        try {
            Parent login = FXMLLoader.load(getClass().getResource("src/main/resources/fxml/login.fxml"));
            mainBorderPane.getScene().setRoot(login);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}