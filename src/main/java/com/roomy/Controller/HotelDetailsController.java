package com.roomy.Controller;

import com.roomy.entities.Hotel;
import com.roomy.entities.Chambre;
import com.roomy.entities.Image_hotel;
import com.roomy.entities.Image_chambre;
import com.roomy.Dao.HotelDAO;
import com.roomy.service.Session;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.List;

public class HotelDetailsController implements Initializable {

    @FXML private Label hotelNameLabel;
    @FXML private Label starsLabel;
    @FXML private Label locationLabel;
    @FXML private Label priceRangeLabel;
    @FXML private TextArea descriptionLabel;
    @FXML private FlowPane imagesContainer;
    @FXML private FlowPane servicesContainer;
    @FXML private VBox roomsContainer;
    @FXML private Button bookButton;
    @FXML private Button backButton;

    private Hotel hotel;
    private HotelDAO hotelDAO = new HotelDAO();
    private Chambre selectedChambre; // Pour stocker la chambre sélectionnée

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
        updateUI();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Configuration initiale
        setupStyle();
    }

    private void setupStyle() {
        if (imagesContainer != null) {
            imagesContainer.setHgap(15);
            imagesContainer.setVgap(15);
            imagesContainer.setPadding(new Insets(10));
        }

        if (servicesContainer != null) {
            servicesContainer.setHgap(10);
            servicesContainer.setVgap(10);
            servicesContainer.setPadding(new Insets(10));
        }

        if (roomsContainer != null) {
            roomsContainer.setSpacing(20);
            roomsContainer.setPadding(new Insets(10));
        }

        // Style du TextArea
        if (descriptionLabel != null) {
            descriptionLabel.setWrapText(true);
            descriptionLabel.setEditable(false);
            descriptionLabel.setStyle("-fx-control-inner-background: #f8f9fa; " +
                    "-fx-border-color: #e0e0e0; " +
                    "-fx-border-radius: 5px; " +
                    "-fx-padding: 10px;");
        }
    }

    private void updateUI() {
        if (hotel == null) return;

        // Nom et étoiles
        hotelNameLabel.setText(hotel.getNomHotel());
        starsLabel.setText("★".repeat(hotel.getEtoiles()));

        // Localisation
        if (hotel.getAdresse() != null) {
            String ville = hotel.getAdresse().getVille() != null ? hotel.getAdresse().getVille() : "Unknown";
            String pays = hotel.getAdresse().getPays() != null ? hotel.getAdresse().getPays() : "Unknown";
            locationLabel.setText("📍 " + ville + ", " + pays);
        } else {
            locationLabel.setText("📍 Location not specified");
        }

        // Description
        String description = hotel.getDescription();
        if (description == null || description.trim().isEmpty()) {
            description = "This luxury hotel offers premium amenities and exceptional service. " +
                    "Located in a prime area, it provides comfortable accommodations " +
                    "for both business and leisure travelers.";
        }
        descriptionLabel.setText(description);

        // Prix
        updatePriceRange();

        // Images
        displayImages();

        // Services
        displayServices();

        // Chambres
        displayRooms();
    }

    private void updatePriceRange() {
        if (hotel.getChambres() == null || hotel.getChambres().isEmpty()) {
            priceRangeLabel.setText("No rooms available");
            return;
        }

        double minPrice = hotel.getMinPrice();
        double maxPrice = hotel.getChambres().stream()
                .mapToDouble(Chambre::getPrix_nuit)
                .max()
                .orElse(minPrice);

        if (minPrice == maxPrice) {
            priceRangeLabel.setText("$" + String.format("%.2f", minPrice) + " / night");
        } else {
            priceRangeLabel.setText("$" + String.format("%.2f", minPrice) +
                    " - $" + String.format("%.2f", maxPrice) + " / night");
        }
    }

    private void displayImages() {
        imagesContainer.getChildren().clear();

        if (hotel.getImgs() == null || hotel.getImgs().isEmpty()) {
            // Image par défaut
            ImageView defaultImg = createImageView(
                    "https://images.unsplash.com/photo-1564501049412-61c2a3083791?w=600",
                    300, 200
            );
            imagesContainer.getChildren().add(defaultImg);
            return;
        }

        // Afficher toutes les images de l'hôtel
        for (Image_hotel img : hotel.getImgs()) {
            String imageUrl = img.getUrl();
            if (imageUrl != null && !imageUrl.trim().isEmpty()) {
                ImageView imageView = createImageView(imageUrl, 300, 200);
                imagesContainer.getChildren().add(imageView);
            }
        }

        // Si aucune image valide
        if (imagesContainer.getChildren().isEmpty()) {
            ImageView defaultImg = createImageView(
                    "https://images.unsplash.com/photo-1564501049412-61c2a3083791?w=600",
                    300, 200
            );
            imagesContainer.getChildren().add(defaultImg);
        }
    }

    private ImageView createImageView(String url, double width, double height) {
        ImageView imageView = new ImageView();
        try {
            Image image = new Image(url, width, height, true, true);
            imageView.setImage(image);
        } catch (Exception e) {
            // Image par défaut en cas d'erreur
            Image defaultImage = new Image(
                    "https://images.unsplash.com/photo-1564501049412-61c2a3083791?w=600",
                    width, height, true, true
            );
            imageView.setImage(defaultImage);
        }

        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        imageView.setPreserveRatio(false);
        imageView.setStyle("-fx-border-radius: 10px; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");

        return imageView;
    }

    private void displayServices() {
        servicesContainer.getChildren().clear();

        if (hotel.getServices() == null || hotel.getServices().isEmpty()) {
            Label noServices = new Label("No services information available");
            noServices.setStyle("-fx-text-fill: #7f8c8d; -fx-font-style: italic;");
            servicesContainer.getChildren().add(noServices);
            return;
        }

        for (String service : hotel.getServices()) {
            if (service != null && !service.trim().isEmpty()) {
                Label serviceLabel = new Label(service);
                serviceLabel.setStyle("-fx-background-color: #3498db; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 8px 15px; " +
                        "-fx-border-radius: 20px; " +
                        "-fx-background-radius: 20px;");
                servicesContainer.getChildren().add(serviceLabel);
            }
        }
    }

    private void displayRooms() {
        roomsContainer.getChildren().clear();

        if (hotel.getChambres() == null || hotel.getChambres().isEmpty()) {
            Label noRoomsLabel = new Label("No rooms available at the moment");
            noRoomsLabel.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 16px; -fx-font-style: italic;");
            roomsContainer.getChildren().add(noRoomsLabel);
            return;
        }

        // Afficher chaque chambre
        for (Chambre chambre : hotel.getChambres()) {
            HBox roomCard = createRoomCard(chambre);
            roomsContainer.getChildren().add(roomCard);
        }
    }

    private HBox createRoomCard(Chambre chambre) {
        HBox card = new HBox(20);
        card.setStyle("-fx-background-color: white; " +
                "-fx-border-color: #e0e0e0; " +
                "-fx-border-width: 1px; " +
                "-fx-border-radius: 10px; " +
                "-fx-padding: 20px; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 10, 0, 0, 0);");
        card.setAlignment(Pos.CENTER_LEFT);

        // Image de la chambre
        ImageView roomImage = createRoomImage(chambre);

        // Détails de la chambre
        VBox details = new VBox(12);
        details.setPrefWidth(350);

        // Type et numéro
        Label typeLabel = new Label(chambre.getType() + " - Room #" + chambre.getNumchambre());
        typeLabel.setStyle("-fx-font-size: 20px; " +
                "-fx-font-weight: bold; " +
                "-fx-text-fill: #2c3e50;");

        // Prix
        Label priceLabel = new Label("$" + String.format("%.2f", chambre.getPrix_nuit()) + " / night");
        priceLabel.setStyle("-fx-font-size: 18px; " +
                "-fx-font-weight: bold; " +
                "-fx-text-fill: #f1c40f;");

        // Détails
        HBox roomDetails = new HBox(20);
        roomDetails.setAlignment(Pos.CENTER_LEFT);

        Label capacityLabel = new Label("👥 " + chambre.getCapacity() + " people");
        capacityLabel.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 14px;");

        Label surfaceLabel = new Label("📏 " + chambre.getSurface() + " m²");
        surfaceLabel.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 14px;");

        roomDetails.getChildren().addAll(capacityLabel, surfaceLabel);

        // Description
        String description = chambre.getDescription();
        if (description == null || description.trim().isEmpty()) {
            description = "Comfortable room with all necessary amenities. " +
                    "Perfect for a relaxing stay.";
        }

        Label descLabel = new Label(description);
        descLabel.setWrapText(true);
        descLabel.setStyle("-fx-text-fill: #34495e; -fx-font-size: 14px;");
        descLabel.setMaxWidth(350);
        descLabel.setPrefHeight(60);

        // Statut
        Label statusLabel = new Label("Status: " + chambre.getStatut().toString());
        statusLabel.setStyle("-fx-font-weight: bold; " +
                "-fx-text-fill: " + getStatusColor(chambre.getStatut()) + ";");

        // Bouton Réserver cette chambre
        Button bookRoomButton = new Button("Réserver");
        bookRoomButton.setStyle("-fx-background-color: #27ae60; " +
                "-fx-text-fill: white; " +
                "-fx-font-weight: bold; " +
                "-fx-pref-width: 150px; " +
                "-fx-pref-height: 40px; " +
                "-fx-background-radius: 5px; " +
                "-fx-cursor: hand;");
        bookRoomButton.setOnAction(e -> handleBookingFlow(chambre));

        // Effet hover
        bookRoomButton.setOnMouseEntered(e -> {
            bookRoomButton.setStyle("-fx-background-color: #219653; " +
                    "-fx-text-fill: white; " +
                    "-fx-font-weight: bold; " +
                    "-fx-pref-width: 150px; " +
                    "-fx-pref-height: 40px; " +
                    "-fx-background-radius: 5px; " +
                    "-fx-cursor: hand;");
        });

        bookRoomButton.setOnMouseExited(e -> {
            bookRoomButton.setStyle("-fx-background-color: #27ae60; " +
                    "-fx-text-fill: white; " +
                    "-fx-font-weight: bold; " +
                    "-fx-pref-width: 150px; " +
                    "-fx-pref-height: 40px; " +
                    "-fx-background-radius: 5px; " +
                    "-fx-cursor: hand;");
        });

        details.getChildren().addAll(typeLabel, priceLabel, roomDetails, descLabel, statusLabel, bookRoomButton);
        card.getChildren().addAll(roomImage, details);
        return card;
    }

    private String getStatusColor(com.roomy.ENUMS.Statut_technique_Chambre statut) {
        switch (statut) {
            case disponible: return "#27ae60"; // Vert
            case en_maintenance: return "#f39c12"; // Orange
            case en_netoyage: return "#3498db"; // Bleu
            case hors_service: return "#e74c3c"; // Rouge
            default: return "#7f8c8d"; // Gris
        }
    }

    private ImageView createRoomImage(Chambre chambre) {
        ImageView imageView = new ImageView();
        imageView.setFitWidth(250);
        imageView.setFitHeight(180);
        imageView.setPreserveRatio(false);
        imageView.setStyle("-fx-border-radius: 5px;");

        // Vérifier si la chambre a des images
        if (chambre.getImgs() != null && !chambre.getImgs().isEmpty()) {
            for (Image_chambre img : chambre.getImgs()) {
                if (img.getUrl() != null && !img.getUrl().trim().isEmpty()) {
                    try {
                        Image image = new Image(img.getUrl(), 250, 180, true, true);
                        imageView.setImage(image);
                        break;
                    } catch (Exception e) {
                        continue;
                    }
                }
            }
        }

        // Image par défaut si aucune image valide
        if (imageView.getImage() == null) {
            Image defaultImage = new Image(
                    "https://images.unsplash.com/photo-1611892440504-42a792e24d32?w=400",
                    250, 180, true, true
            );
            imageView.setImage(defaultImage);
        }

        return imageView;
    }

    @FXML
    private void handleBooking() {
        // Flux général de réservation pour l'hôtel
        handleBookingFlow(null);
    }

    /**
     * FLUX 1 : Utilisateur NON connecté → Redirige vers inscription → Auto-redirection vers réservation
     * FLUX 2 : Utilisateur DÉJÀ connecté → Formulaire réservation DIRECT
     */
    private void handleBookingFlow(Chambre chambre) {
        this.selectedChambre = chambre;
        
        // Vérifier si l'utilisateur est connecté
        if (Session.getCurrentClient() != null) {
            // FLUX 2 : Utilisateur connecté → Réservation directe
            redirectToBookingForm();
        } else {
            // FLUX 1 : Utilisateur non connecté → Inscription avec redirection auto
            storeBookingContext();
            redirectToSignup();
        }
    }
    
    private void storeBookingContext() {
        // Stocker le contexte de réservation dans la session pour redirection automatique
        BookingContext.setHotel(this.hotel);
        BookingContext.setChambre(this.selectedChambre);
    }
    
    private void redirectToBookingForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/booking_form.fxml"));
            Parent root = loader.load();
            
            BookingController controller = loader.getController();
            controller.setHotelAndChambre(this.hotel, this.selectedChambre);
            
            Stage stage = (Stage) bookButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Réservation - ROOMY");
            
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir le formulaire de réservation: " + e.getMessage());
        }
    }

    private void redirectToSignup() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/client_signup.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) bookButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Inscription - ROOMY");

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir la page d'inscription: " + e.getMessage());
        }
    }

    @FXML
    private void handleBack() {
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}