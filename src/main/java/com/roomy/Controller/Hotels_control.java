package com.roomy.Controller;
import com.roomy.Dao.HotelDAO;
import com.roomy.Dao.HotelierDAO;
import com.roomy.entities.Hotel;  // À créer ou adapter selon ton modèle
import com.roomy.entities.Hotelier;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;

public class Hotels_control {

    @FXML private FlowPane flowHotels;
    @FXML private Label lblNoHotels;
    @FXML private VBox leftMenu;
    private HotelDAO hotelDAO = new HotelDAO();

    private int currentHotelierId = Dash_hotelier_Control.getCurrentHotelierId(); // Récupère l'ID connecté

    @FXML
    private void initialize() {
        loadHotels();
    }

    private void loadHotels() {
        ObservableList<Hotel> hotels = FXCollections.observableArrayList(
                hotelDAO.getHotelsByHotelier(currentHotelierId)
        );

        if (hotels.isEmpty()) {
            lblNoHotels.setVisible(true);
            flowHotels.setVisible(false);
        } else {
            lblNoHotels.setVisible(false);
            flowHotels.setVisible(true);
            flowHotels.getChildren().clear();
            for (Hotel hotel : hotels) {
                VBox hotelCard = createHotelCard(hotel);
                flowHotels.getChildren().add(hotelCard);
            }
        }
    }

    private VBox createHotelCard(Hotel hotel) {
        VBox card = new VBox(10);
        card.setAlignment(javafx.geometry.Pos.CENTER);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-padding: 15; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 10, 0, 0, 5);");
        card.setPrefWidth(200);
        card.setPrefHeight(250);

        ImageView imageView = new ImageView();
        imageView.setFitWidth(180);
        imageView.setFitHeight(180);
        imageView.setPreserveRatio(true);

        // Charger l'image depuis la base de données
        if (!hotel.getImgs().isEmpty()) {
            String imagePath = hotel.getImgs().get(0).getUrl();
            try {
                File imageFile = new File(imagePath);
                if (imageFile.exists()) {
                    imageView.setImage(new Image(imageFile.toURI().toString()));
                } else {
                    loadDefaultImage(imageView);
                }
            } catch (Exception e) {
                System.err.println("Erreur chargement image : " + e.getMessage());
                loadDefaultImage(imageView);
            }
        } else {
            // Image par défaut si aucune image en base
            loadDefaultImage(imageView);
        }

        Label nameLabel = new Label(hotel.getNomHotel());
        nameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #380F17;");

        card.getChildren().addAll(imageView, nameLabel);
        card.setOnMouseClicked(event -> showHotelDetails(hotel));

        return card;
    }

    private void loadDefaultImage(ImageView imageView) {
        try {
            Image defaultImage = new Image(getClass().getResourceAsStream("/images/img.jpg"));
            imageView.setImage(defaultImage);
        } catch (Exception e) {
            System.err.println("Erreur chargement image par défaut : " + e.getMessage());
        }
    }

    private void showHotelDetails(Hotel hotel) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/hotel_form.fxml"));
            Parent root = loader.load();

            HotelFormController controller = loader.getController();
            controller.setModeModification(hotel, hotel.getHotelier());

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Modifier : " + hotel.getNomHotel());
            stage.setScene(new Scene(root, 600, 700));
            stage.showAndWait();

            loadHotels();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openAddHotelForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/hotel_form.fxml"));
            Parent root = loader.load();

            HotelFormController controller = loader.getController();
            Hotelier hotelier = new HotelierDAO().findById(currentHotelierId);
            controller.setModeAjout(hotelier);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Ajouter un nouvel hôtel");
            stage.setScene(new Scene(root, 600, 700));
            stage.showAndWait();

            loadHotels();
        } catch (Exception e) {
            e.printStackTrace();
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

    // Ajoute ces méthodes pour coordination avec Dash_hotelier_Control
    @FXML private void goToDashboard()     { /* Appel à loadCenter depuis parent si nécessaire */ }

    @FXML private void goToMesHotels()     { /* Déjà sur cette page */ }

    @FXML private void goToChambres()      { /* Implémente si nécessaire */ }

    @FXML private void goToStatistiques()  { /* Implémente si nécessaire */ }

    @FXML private void goToProfil()        { /* Implémente si nécessaire */ }

    @FXML
    private void handleDeconnexion() {
        // Similaire à Dash_hotelier_Control
        try {
            Parent login = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
            flowHotels.getScene().setRoot(login);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
