package com.roomy.Controller;

import com.roomy.Dao.ChambreDAO;
import com.roomy.Dao.HotelDAO;
import com.roomy.Dao.HotelierDAO;
import com.roomy.entities.Hotelier;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.event.ActionEvent;


public class Dash_hotelier_Control {
    private static int currentHotelierId = 0;
    private static Dash_hotelier_Control instance;
    @FXML private BorderPane mainBorderPane;
    @FXML private VBox leftMenu;
    @FXML private Label lblwelcome;
    @FXML private Text txtNbHotels;
    @FXML private Text txtNbChambresTotal;
    @FXML private Text txtNbChambresDispo;
    @FXML private Text txtNbChambresOccupees;
    @FXML private Button btnToggleMenu;
    @FXML private Button btnDashboard;
    @FXML private Button btnHotels;
    @FXML private Button btnChambres;
    @FXML private Button btnStats;
    @FXML private Button btnProfile;

    private final HotelDAO hotelDAO = new HotelDAO();
    private final ChambreDAO chambreDAO = new ChambreDAO();
    private final HotelierDAO hotelierDAO = new HotelierDAO();
    public static void setCurrentHotelierId(int id) {
        currentHotelierId = id;
    }

    public static int getCurrentHotelierId() {
        return currentHotelierId;
    }

    @FXML
    private void initialize() {
        instance = this;
        refreshDashboard();
        setActiveButton(btnDashboard); //dashboard katban lwla duh
    }
    public static Dash_hotelier_Control getInstance() {
        return instance;
    }

    public BorderPane getMainBorderPane() {
        return mainBorderPane;
    }

    @FXML
    private void toggleMenu(ActionEvent e) {
        boolean hidden = leftMenu.getTranslateX() < 0;

        if (hidden) {
            leftMenu.setTranslateX(0);        // montrer
        } else {
            leftMenu.setTranslateX(-220);     // cacher
        }
    }
    private void updateButtonStyles() {
        // Style par défaut (non actif)
        String inactiveStyle = "-fx-background-color: transparent; -fx-text-fill: #EFDFC5; -fx-font-size: 13px; -fx-background-radius: 12; -fx-cursor: hand;";

        // Réinitialiser tous les boutons
        btnDashboard.setStyle(inactiveStyle);
        btnHotels.setStyle(inactiveStyle);
        btnChambres.setStyle(inactiveStyle);
        btnStats.setStyle(inactiveStyle);
        btnProfile.setStyle(inactiveStyle);
    }
    private void setActiveButton(Button activeButton) {
        updateButtonStyles();
        activeButton.setStyle("-fx-background-color: #A59090; -fx-text-fill: #380F17; -fx-font-size: 13px; -fx-font-weight: bold; -fx-background-radius: 12; -fx-cursor: hand;");
    }

    public void refreshDashboard() {
        if (currentHotelierId <= 0) return;
        // Récupérer le nom du hôtelier
        Hotelier hotelier = hotelierDAO.findById(currentHotelierId);
        String nomHotelier = (hotelier != null) ? hotelier.getNomGerant() : "Utilisateur";
        lblwelcome.setText("Bienvenue, " + nomHotelier);
        // Charger les stats du dashboard
        int nbHotels = hotelDAO.getNombreHotels(currentHotelierId);
        int nbChambresTotal = chambreDAO.getNombreChambresTotal(currentHotelierId);
        int nbChambresDisponibles = chambreDAO.getNombreChambresDisponibles(currentHotelierId);
        int nbChambresOccupees = nbChambresTotal - nbChambresDisponibles;

        txtNbHotels.setText(String.valueOf(nbHotels));
        txtNbChambresTotal.setText(String.valueOf(nbChambresTotal));
        txtNbChambresDispo.setText(String.valueOf(nbChambresDisponibles));
        txtNbChambresOccupees.setText(String.valueOf(nbChambresOccupees));

        // Charger le centre avec le dashboard initial
        loadCenter("dash_hotelier_dashboard.fxml");
    }

    private void loadCenter(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + fxmlFile));
            Node center = loader.load();
            mainBorderPane.setCenter(center);
        } catch (Exception e) {
            System.err.println("Erreur chargement " + fxmlFile + " : " + e.getMessage());
            e.printStackTrace();
        }
    }


    public void goToDashboard() {

        refreshDashboard();
    }

    @FXML
    public void goToMesHotels() {
        setActiveButton(btnHotels);
        loadCenter("mes_hotels.fxml");
    }

    @FXML
    public void goToChambres() {
        setActiveButton(btnChambres);
        loadCenter("chambres.fxml");
    }

    @FXML
    public void goToStatistiques() {
        setActiveButton(btnStats);
        loadCenter("statistiques.fxml");
    }

    @FXML
    public void goToProfil() {
        setActiveButton(btnProfile);
        loadCenter("profile.fxml");
    }


    @FXML
    private void handleDeconnexion() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) mainBorderPane.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setMaximized(false);
            stage.setTitle("Connexion");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}