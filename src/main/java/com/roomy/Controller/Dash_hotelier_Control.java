package com.roomy.Controller;

import com.roomy.Dao.HotelDAO;
import com.roomy.Dao.HotelierDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class Dash_hotelier_Control {

    @FXML private BorderPane mainBorderPane;
    @FXML private Label lblwelcome;
    @FXML private VBox leftMenu;
    @FXML private Text txtNbHotels;
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
        // Tenter d'afficher le nom réel de l'hôtelier si l'id est renseigné
        updateWelcomeLabel();
        loadHotelCount();
    }

    private void loadHotelCount() {
        try {
            if (currentHotelierId > 0) {
                HotelDAO hotelDAO = new HotelDAO();
                int count = hotelDAO.getNombreHotels(currentHotelierId);
                System.out.println("Nombre d'hôtels : " + count);

                // Si tu as un Label pour afficher le nombre, mets à jour ici
                txtNbHotels.setText(String.valueOf(count));
            } else {
                System.out.println("ID hôtelier non défini : " + currentHotelierId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void refreshDashboard() {
        updateWelcomeLabel();
        loadHotelCount();
    }

    // Méthode publique pour rafraîchir l'affichage du nom (à appeler si on définit l'ID après le load)
    public void refreshWelcome() {
        updateWelcomeLabel();
    }

    private void updateWelcomeLabel() {
        try {
            if (currentHotelierId > 0) {
                HotelierDAO hotelierDAO = new HotelierDAO();
                String nomComplet = hotelierDAO.getNomComplet(currentHotelierId);
                lblwelcome.setText("Bonjour, " + (nomComplet == null ? "Utilisateur" : nomComplet));
            } else {
                lblwelcome.setText("Bonjour, Utilisateur");
            }
        } catch (Exception e) {
            e.printStackTrace();
            lblwelcome.setText("Bonjour, Utilisateur");
        }
    }

    @FXML
    private void toggleMenu() {
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

    // Méthode générique pour charger une page FXML dans le center (ressource chargée depuis classpath:/fxml/)
    private void loadCenter(String fxmlFileName) {
        try {
            // fxmlFileName doit être par exemple "mes_hotels.fxml"
            Parent page = FXMLLoader.load(getClass().getResource("/fxml/" + fxmlFileName));
            mainBorderPane.setCenter(page);
        } catch (Exception e) {
            e.printStackTrace();
            lblwelcome.setText("Erreur : page non trouvée (" + fxmlFileName + ")");
        }
    }

    // ==================== ACTIONS DES BOUTONS DU MENU ====================
    @FXML private void goToDashboard() {
        // Recharge tout le dashboard (acceptable au début)
        try {
            Parent dash = FXMLLoader.load(getClass().getResource("/fxml/dash_hotelier.fxml"));
            mainBorderPane.getScene().setRoot(dash);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML private void goToMesHotels()     { loadCenter("mes_hotels.fxml"); }

    @FXML private void goToChambres()      { loadCenter("chambres.fxml"); }

    @FXML private void goToStatistiques()  { loadCenter("statistiques.fxml"); }

    @FXML private void goToProfil()        { loadCenter("profile.fxml"); }

    @FXML
    private void handleDeconnexion() {
        // Retour à l'écran de login (adapte le chemin selon ton fichier login)
        try {
            Parent login = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
            mainBorderPane.getScene().setRoot(login);
        } catch (Exception e) {
            e.printStackTrace();
            lblwelcome.setText("Erreur lors de la déconnexion");
        }
    }
}