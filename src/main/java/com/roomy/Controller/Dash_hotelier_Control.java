package com.roomy. Controller;

import com.roomy.service.StatistiquesService;
import com.roomy.Dao.HotelDAO;
import com.roomy.Dao.HotelierDAO;
import com.roomy.dto.HotelStats;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control. Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout. VBox;
import javafx.scene.text.Text;

import java.io.IOException;

public class Dash_hotelier_Control {

    // ID de l'hôtelier connecté (Partagé avec les autres contrôleurs)
    private static int currentHotelierId = 0;

    public static int getCurrentHotelierId() {
        return currentHotelierId;
    }

    public static void setCurrentHotelierId(int id) {
        currentHotelierId = id;
    }

    // Éléments FXML
    @FXML private BorderPane mainBorderPane;
    @FXML private Label lblwelcome;
    @FXML private VBox leftMenu;

    // Statistiques
    @FXML private Text txtNbHotels;
    @FXML private Text txtNbChambresTotal;
    @FXML private Text txtNbChambresDispo;
    @FXML private Text txtNbChambresOccupees;

    // Services
    private final StatistiquesService statistiquesService = new StatistiquesService();

    @FXML
    private void initialize() {
        if (currentHotelierId > 0) {
            // Récupère et affiche le nom de l'hôtelier
            updateWelcomeLabel();
            // Charge les statistiques
            refreshStats();
            // Charge le contenu du dashboard par défaut
            loadDashboardContent();
        }
    }

    /**
     * Met à jour le label de bienvenue avec le nom réel de l'hôtelier
     */
    private void updateWelcomeLabel() {
        try {
            if (currentHotelierId > 0) {
                HotelierDAO hotelierDAO = new HotelierDAO();
                String nomComplet = hotelierDAO.getNomComplet(currentHotelierId);
                if (lblwelcome != null) {
                    lblwelcome.setText("Bienvenue, " + (nomComplet != null ? nomComplet : "Utilisateur"));
                }
            } else {
                if (lblwelcome != null) {
                    lblwelcome.setText("Bienvenue, Utilisateur");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (lblwelcome != null) {
                lblwelcome.setText("Bienvenue, Utilisateur");
            }
        }
    }

    /**
     * Rafraîchit les statistiques globales dans les cartes du dashboard
     * Utilise un DTO pour minimiser les appels BD
     */
    public void refreshStats() {
        try {
            HotelStats stats = statistiquesService.getGlobalStats(currentHotelierId);

            if (txtNbHotels != null) txtNbHotels.setText(String.valueOf(stats.getNbHotels()));
            if (txtNbChambresTotal != null) txtNbChambresTotal.setText(String.valueOf(stats.getNbChambresTotal()));
            if (txtNbChambresDispo != null) txtNbChambresDispo. setText(String.valueOf(stats. getNbChambresDispo()));
            if (txtNbChambresOccupees != null) txtNbChambresOccupees.setText(String. valueOf(stats.getNbChambresOccupees()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Charge le contenu du dashboard (cartes de stats)
     */
    private void loadDashboardContent() {
        refreshStats();
        // Optionnel : charger un FXML spécifique si nécessaire
        // loadCenter("accueil_dashboard.fxml");
    }

    /**
     * Charge un fichier FXML dans la zone centrale du BorderPane
     */
    private void loadCenter(String fxmlFileName) {
        try {
            Parent page = FXMLLoader. load(getClass().getResource("/fxml/" + fxmlFileName));
            mainBorderPane.setCenter(page);
        } catch (IOException e) {
            System.err.println("Erreur chargement FXML: " + fxmlFileName);
            e.printStackTrace();
            if (lblwelcome != null) {
                lblwelcome.setText("Erreur : page non trouvée (" + fxmlFileName + ")");
            }
        }
    }

    /**
     * Bascule la visibilité du menu latéral
     */
    @FXML
    private void toggleMenu() {
        if (leftMenu != null) {
            if (leftMenu.isVisible()) {
                leftMenu.setVisible(false);
                leftMenu.setManaged(false);
                leftMenu.setPrefWidth(0);
            } else {
                leftMenu.setVisible(true);
                leftMenu.setManaged(true);
                leftMenu.setPrefWidth(240);
            }
        }
    }

    // ==================== NAVIGATION ====================

    @FXML
    private void goToDashboard() {
        refreshStats();
        // Recharge le dashboard si nécessaire
    }

    @FXML
    private void goToMesHotels() {
        loadCenter("mes_hotels.fxml");
    }

    @FXML
    private void goToChambres() {
        loadCenter("chambres.fxml");
    }

    @FXML
    private void goToStatistiques() {
        loadCenter("statistiques.fxml");
    }

    @FXML
    private void goToProfil() {
        loadCenter("profile.fxml");
    }

    @FXML
    private void handleDeconnexion() {
        try {
            Parent login = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
            mainBorderPane.getScene().setRoot(login);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}