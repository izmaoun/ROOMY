package com.roomy.Controller;

import com.roomy.entities.Hotel;
import com.roomy.entities.Chambre;
import com.roomy.entities.Image_hotel;
import com.roomy.entities.Image_chambre;
import com.roomy.Dao.HotelDAO;
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
    @FXML private Label descriptionLabel;
    @FXML private FlowPane imagesContainer;
    @FXML private FlowPane servicesContainer;
    @FXML private VBox roomsContainer;
    @FXML private Button bookButton;
    @FXML private Button backButton;

    private Hotel hotel;
    private HotelDAO hotelDAO = new HotelDAO();

    public void setHotel(Hotel hotel) {
        System.out.println("Setting hotel: " + (hotel != null ? hotel.getNomHotel() : "null"));
        this.hotel = hotel;
        updateUI();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("HotelDetailsController initialized");
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

        if (descriptionLabel != null) {
            descriptionLabel.setWrapText(true);
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
                    320, 220
            );
            imagesContainer.getChildren().add(defaultImg);
            return;
        }

        // Afficher toutes les images de l'hôtel
        for (Image_hotel img : hotel.getImgs()) {
            String imageUrl = img.getUrl();
            if (imageUrl != null && !imageUrl.trim().isEmpty()) {
                ImageView imageView = createImageView(imageUrl, 320, 220);
                imagesContainer.getChildren().add(imageView);
            }
        }

        // Si aucune image valide
        if (imagesContainer.getChildren().isEmpty()) {
            ImageView defaultImg = createImageView(
                    "https://images.unsplash.com/photo-1564501049412-61c2a3083791?w=600",
                    320, 220
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
        imageView.setStyle("-fx-border-radius: 15px; " +
                "-fx-background-radius: 15px; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(56,15,23,0.2), 10, 0, 0, 2);");

        // Add clipping for rounded corners
        javafx.scene.shape.Rectangle clip = new javafx.scene.shape.Rectangle(width, height);
        clip.setArcWidth(30);
        clip.setArcHeight(30);
        imageView.setClip(clip);

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
                serviceLabel.setStyle("-fx-background-color: #8F0B13; " +
                        "-fx-text-fill: #EFDFC5; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 10px 18px; " +
                        "-fx-border-radius: 25px; " +
                        "-fx-background-radius: 25px; " +
                        "-fx-effect: dropshadow(three-pass-box, rgba(56,15,23,0.3), 5, 0, 0, 1); " +
                        "-fx-cursor: hand;");
                
                // Hover effects
                serviceLabel.setOnMouseEntered(e -> {
                    serviceLabel.setStyle("-fx-background-color: #A50C15; " +
                            "-fx-text-fill: #EFDFC5; " +
                            "-fx-font-weight: bold; " +
                            "-fx-padding: 10px 18px; " +
                            "-fx-border-radius: 25px; " +
                            "-fx-background-radius: 25px; " +
                            "-fx-effect: dropshadow(three-pass-box, rgba(143,11,19,0.5), 8, 0, 0, 2); " +
                            "-fx-cursor: hand; -fx-scale-x: 1.05; -fx-scale-y: 1.05;");
                });
                
                serviceLabel.setOnMouseExited(e -> {
                    serviceLabel.setStyle("-fx-background-color: #8F0B13; " +
                            "-fx-text-fill: #EFDFC5; " +
                            "-fx-font-weight: bold; " +
                            "-fx-padding: 10px 18px; " +
                            "-fx-border-radius: 25px; " +
                            "-fx-background-radius: 25px; " +
                            "-fx-effect: dropshadow(three-pass-box, rgba(56,15,23,0.3), 5, 0, 0, 1); " +
                            "-fx-cursor: hand;");
                });
                
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
        card.setStyle("-fx-background-color: #EFDFC5; " +
                "-fx-border-color: #4C4F54; " +
                "-fx-border-width: 1px; " +
                "-fx-border-radius: 15px; " +
                "-fx-background-radius: 15px; " +
                "-fx-padding: 20px; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(56,15,23,0.15), 8, 0, 0, 2); " +
                "-fx-cursor: hand;");
        card.setAlignment(Pos.CENTER_LEFT);
        
        // Hover effects for room cards
        card.setOnMouseEntered(e -> {
            card.setStyle("-fx-background-color: #EFDFC5; " +
                    "-fx-border-color: #8F0B13; " +
                    "-fx-border-width: 2px; " +
                    "-fx-border-radius: 15px; " +
                    "-fx-background-radius: 15px; " +
                    "-fx-padding: 19px; " +
                    "-fx-effect: dropshadow(three-pass-box, rgba(143,11,19,0.3), 12, 0, 0, 3); " +
                    "-fx-cursor: hand; -fx-scale-x: 1.02; -fx-scale-y: 1.02;");
        });
        
        card.setOnMouseExited(e -> {
            card.setStyle("-fx-background-color: #EFDFC5; " +
                    "-fx-border-color: #4C4F54; " +
                    "-fx-border-width: 1px; " +
                    "-fx-border-radius: 15px; " +
                    "-fx-background-radius: 15px; " +
                    "-fx-padding: 20px; " +
                    "-fx-effect: dropshadow(three-pass-box, rgba(56,15,23,0.15), 8, 0, 0, 2); " +
                    "-fx-cursor: hand;");
        });

        // Image de la chambre
        ImageView roomImage = createRoomImage(chambre);

        // Détails de la chambre
        VBox details = new VBox(12);
        details.setPrefWidth(350);

        // Type et numéro
        Label typeLabel = new Label(chambre.getType() + " - Room #" + chambre.getNumchambre());
        typeLabel.setStyle("-fx-font-size: 20px; " +
                "-fx-font-weight: bold; " +
                "-fx-text-fill: #380F17;");

        // Prix
        Label priceLabel = new Label("$" + String.format("%.2f", chambre.getPrix_nuit()) + " / night");
        priceLabel.setStyle("-fx-font-size: 18px; " +
                "-fx-font-weight: bold; " +
                "-fx-text-fill: #8F0B13;");

        // Détails
        HBox roomDetails = new HBox(20);
        roomDetails.setAlignment(Pos.CENTER_LEFT);

        Label capacityLabel = new Label("👥 " + chambre.getCapacity() + " people");
        capacityLabel.setStyle("-fx-text-fill: #4C4F54; -fx-font-size: 14px;");

        Label surfaceLabel = new Label("📏 " + chambre.getSurface() + " m²");
        surfaceLabel.setStyle("-fx-text-fill: #4C4F54; -fx-font-size: 14px;");

        roomDetails.getChildren().addAll(capacityLabel, surfaceLabel);

        // Description
        String description = chambre.getDescription();
        if (description == null || description.trim().isEmpty()) {
            description = "Comfortable room with all necessary amenities. " +
                    "Perfect for a relaxing stay.";
        }

        Label descLabel = new Label(description);
        descLabel.setWrapText(true);
        descLabel.setStyle("-fx-text-fill: #252B2B; -fx-font-size: 14px;");
        descLabel.setMaxWidth(350);
        descLabel.setPrefHeight(60);

        // Statut
        Label statusLabel = new Label("Status: " + chambre.getStatut().toString());
        statusLabel.setStyle("-fx-font-weight: bold; " +
                "-fx-text-fill: " + getStatusColor(chambre.getStatut()) + ";");

        // Bouton Réserver cette chambre
        Button bookRoomButton = new Button("Book This Room");
        bookRoomButton.setStyle("-fx-background-color: #8F0B13; " +
                "-fx-text-fill: #EFDFC5; " +
                "-fx-font-weight: bold; " +
                "-fx-pref-width: 150px; " +
                "-fx-pref-height: 40px; " +
                "-fx-background-radius: 15px; " +
                "-fx-cursor: hand;");
        bookRoomButton.setOnAction(e -> bookRoom(chambre));

        // Effet hover
        bookRoomButton.setOnMouseEntered(e -> {
            bookRoomButton.setStyle("-fx-background-color: #A50C15; " +
                    "-fx-text-fill: #EFDFC5; " +
                    "-fx-font-weight: bold; " +
                    "-fx-pref-width: 150px; " +
                    "-fx-pref-height: 40px; " +
                    "-fx-background-radius: 15px; " +
                    "-fx-cursor: hand; " +
                    "-fx-effect: dropshadow(three-pass-box, rgba(143,11,19,0.4), 8, 0, 0, 2);");
        });

        bookRoomButton.setOnMouseExited(e -> {
            bookRoomButton.setStyle("-fx-background-color: #8F0B13; " +
                    "-fx-text-fill: #EFDFC5; " +
                    "-fx-font-weight: bold; " +
                    "-fx-pref-width: 150px; " +
                    "-fx-pref-height: 40px; " +
                    "-fx-background-radius: 15px; " +
                    "-fx-cursor: hand;");
        });

        details.getChildren().addAll(typeLabel, priceLabel, roomDetails, descLabel, statusLabel, bookRoomButton);
        card.getChildren().addAll(roomImage, details);
        return card;
    }

    private String getStatusColor(com.roomy.ENUMS.Statut_technique_Chambre statut) {
        switch (statut) {
            case disponible: return "#8F0B13"; // Brand red
            case en_maintenance: return "#FF8C00"; // Orange
            case en_netoyage: return "#4C4F54"; // Dark gray
            case hors_service: return "#DC143C"; // Crimson
            default: return "#4C4F54"; // Default gray
        }
    }

    private ImageView createRoomImage(Chambre chambre) {
        ImageView imageView = new ImageView();
        imageView.setFitWidth(250);
        imageView.setFitHeight(180);
        imageView.setPreserveRatio(false);
        imageView.setStyle("-fx-border-radius: 10px; -fx-background-radius: 10px;");

        // Add clipping for rounded corners
        javafx.scene.shape.Rectangle clip = new javafx.scene.shape.Rectangle(250, 180);
        clip.setArcWidth(20);
        clip.setArcHeight(20);
        imageView.setClip(clip);

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
        redirectToSignup();
    }

    private void bookRoom(Chambre chambre) {
        // Vous pouvez ajouter de la logique spécifique à la chambre ici
        showAlert("Room Selected",
                "You have selected " + chambre.getType() + " - Room #" + chambre.getNumchambre() +
                        "\nPrice: $" + chambre.getPrix_nuit() + " per night");

        redirectToSignup();
    }

    private void redirectToSignup() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/signup.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) bookButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Sign Up - ROOMY");

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Cannot open sign up page: " + e.getMessage());
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