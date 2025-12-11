package com.roomy.Controller;

import com.roomy.Dao.HotelDAO;
import com.roomy.entities.Hotel;
import com.roomy.entities.Adresse;
import com.roomy.entities.Image_hotel;
import com.roomy.entities.Chambre;
import com.roomy.ENUMS.TypeChambre;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class LandingPageController {

    @FXML private FlowPane hotelsContainer;
    @FXML private TextField searchField;    // Recherche header
    @FXML private TextField searchField2;   // Recherche filtres
    @FXML private TextField minPriceField;
    @FXML private TextField maxPriceField;
    @FXML private Slider priceSlider;
    @FXML private Label priceRangeLabel;
    @FXML private ComboBox<String> cityComboBox;
    @FXML private ComboBox<String> roomTypeComboBox;
    @FXML private CheckBox star5, star4, star3;
    @FXML private CheckBox wifiCheck, poolCheck, spaCheck, restaurantCheck, breakfastCheck, parkingCheck;
    @FXML private Button loginButton, registerButton;
    @FXML private Button testButton; // Pour tester les filtres
    @FXML private Button test2000_3000Button; // Pour tester spécifiquement 2000-3000

    private HotelDAO hotelDAO = new HotelDAO();
    private List<Hotel> allHotels = new ArrayList<>();

    @FXML
    public void initialize() {
        // D'abord déboguer les prix
        hotelDAO.debugHotelPrices();

        loadCities();
        loadRoomTypes();
        setupFilters();
        setupSearchListeners();
        loadHotels();

        // Configurer les boutons de test
        if (test2000_3000Button != null) {
            test2000_3000Button.setOnAction(e -> testSpecificInterval2000_3000());
        }
        if (testButton != null) {
            testButton.setOnAction(e -> testPriceFilters());
        }
    }

    // CONFIGURATION AMÉLIORÉE DES FILTRES
    private void setupFilters() {
        // Configurer le slider
        priceSlider.setMin(0);
        priceSlider.setMax(10000);
        priceSlider.setValue(3000);
        priceSlider.setBlockIncrement(100);
        priceSlider.setMajorTickUnit(1000);
        priceSlider.setMinorTickCount(5);
        priceSlider.setShowTickLabels(true);
        priceSlider.setShowTickMarks(true);

        // Listener pour le slider
        priceSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            int value = newVal.intValue();
            priceRangeLabel.setText("Up to $\" + value + \" / night");
            maxPriceField.setText(String.valueOf(value));
        });

        // Valeurs par défaut
        minPriceField.setText("0");
        maxPriceField.setText("3000");
        priceRangeLabel.setText("Up to $3000 / night");

        // Listener pour les champs de prix
        minPriceField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*(\\.\\d*)?")) {
                minPriceField.setText(oldVal);
            }
        });

        maxPriceField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*(\\.\\d*)?")) {
                maxPriceField.setText(oldVal);
            } else if (!newVal.isEmpty()) {
                try {
                    double value = Double.parseDouble(newVal);
                    priceSlider.setValue(value);
                } catch (NumberFormatException e) {
                    // Ignorer
                }
            }
        });
    }

    // CONFIGURER LES LISTENERS DE RECHERCHE
    private void setupSearchListeners() {
        // Recherche avec Entrée dans le header
        searchField.setOnAction(e -> handleSearch());

        // Recherche avec Entrée dans les filtres
        searchField2.setOnAction(e -> handleSearch());

        // Recherche en temps réel (optionnel)
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty() && searchField2.getText().isEmpty()) {
                displayHotels(allHotels);
            }
        });

        searchField2.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty() && searchField.getText().isEmpty()) {
                displayHotels(allHotels);
            }
        });
    }

    // GÉRER LA RECHERCHE
    @FXML
    private void handleSearch() {
        String searchTerm = searchField.getText().trim();
        if (searchTerm.isEmpty()) {
            searchTerm = searchField2.getText().trim();
        }

        if (!searchTerm.isEmpty()) {
            performSearch(searchTerm);
        } else {
            displayHotels(allHotels);
        }
    }

    // EFFECTUER LA RECHERCHE
    private void performSearch(String searchTerm) {
        try {
            List<Hotel> searchResults = hotelDAO.searchHotels(searchTerm);
            if (searchResults.isEmpty()) {
                showAlert("Recherche", "Aucun hôtel trouvé pour: " + searchTerm);
                displayHotels(allHotels);
            } else {
                displayHotels(searchResults);
            }
        } catch (Exception e) {
            System.err.println("Erreur recherche: " + e.getMessage());
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors de la recherche: " + e.getMessage());
            displayHotels(allHotels);
        }
    }

    private void loadHotels() {
        try {
            allHotels = hotelDAO.findAll();
            System.out.println("✅ " + allHotels.size() + " hotels loaded");
            displayHotels(allHotels);
        } catch (Exception e) {
            System.err.println("Erreur chargement hôtels: " + e.getMessage());
            e.printStackTrace();
            // Données d'exemple en cas d'erreur
            displayHotels(getSampleHotels());
        }
    }

    private void displayHotels(List<Hotel> hotels) {
        hotelsContainer.getChildren().clear();

        if (hotels.isEmpty()) {
            // Afficher un message "Aucun résultat"
            Label noResults = new Label("No hotel matches your search criteria.");
            noResults.setStyle("-fx-font-size: 16px; -fx-text-fill: #7f8c8d; -fx-padding: 20px;");
            hotelsContainer.getChildren().add(noResults);
            return;
        }

        for (Hotel hotel : hotels) {
            VBox hotelCard = createHotelCard(hotel);
            hotelsContainer.getChildren().add(hotelCard);
        }
    }

    private VBox createHotelCard(Hotel hotel) {
        VBox card = new VBox();
        card.setStyle("-fx-background-color: white; " +
                "-fx-border-color: #e0e0e0; " +
                "-fx-border-width: 1px; " +
                "-fx-border-radius: 10px; " +
                "-fx-background-radius: 10px; " +
                "-fx-padding: 0px; " +
                "-fx-spacing: 0px; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);" +
                "-fx-cursor: hand;");

        // Taille fixe pour que FlowPane les aligne bien
        card.setPrefSize(380, 400);
        card.setMaxSize(380, 400);

        // Image
        ImageView imageView = new ImageView();
        String imageUrl = getHotelImageUrl(hotel);

        try {
            Image image = new Image(imageUrl, 380, 220, false, true);
            imageView.setImage(image);
        } catch (Exception e) {
            imageView.setImage(new Image("https://images.unsplash.com/photo-1564501049412-61c2a3083791?w=600",
                    380, 220, false, true));
        }

        imageView.setFitWidth(380);
        imageView.setFitHeight(220);
        imageView.setStyle("-fx-border-radius: 10px 10px 0 0;");

        // Détails
        VBox content = new VBox(10);
        content.setPadding(new Insets(15));
        content.setAlignment(Pos.TOP_LEFT);
        content.setPrefHeight(180); // Hauteur fixe pour uniformité

        // Nom et étoiles
        HBox titleBox = new HBox(10);
        titleBox.setAlignment(Pos.CENTER_LEFT);

        Label nameLabel = new Label(hotel.getNomHotel());
        nameLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        nameLabel.setWrapText(true);
        nameLabel.setMaxWidth(250);

        String stars = "★".repeat(hotel.getEtoiles()) + "☆".repeat(5 - hotel.getEtoiles());
        Label starsLabel = new Label(stars);
        starsLabel.setStyle("-fx-text-fill: #FFD700; -fx-font-size: 16px;");

        titleBox.getChildren().addAll(nameLabel, starsLabel);

        // Ville
        Adresse adresse = hotel.getAdresse();
        String ville = (adresse != null && adresse.getVille() != null) ? adresse.getVille() : "Unknown city";
        Label cityLabel = new Label("📍 " + ville);
        cityLabel.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 14px;");

        // Prix (calculé localement)
        double minPrice = calculateHotelMinPrice(hotel);
        Label priceLabel = new Label("Up to $" + String.format("%.2f", minPrice) + " / night");
        priceLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #f1c40f;");

        // Boutons
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button detailsBtn = new Button("See details");
        detailsBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; " +
                "-fx-font-weight: bold; -fx-pref-width: 120px; -fx-pref-height: 35px;");
        detailsBtn.setOnAction(e -> showHotelDetails(hotel));

        Button bookBtn = new Button("Book now");
        bookBtn.setStyle("-fx-background-color: #f1c40f; -fx-text-fill: #2c3e50; " +
                "-fx-font-weight: bold; -fx-pref-width: 120px; -fx-pref-height: 35px;");
        bookBtn.setOnAction(e -> handleReservation(hotel));

        buttonBox.getChildren().addAll(detailsBtn, bookBtn);

        content.getChildren().addAll(titleBox, cityLabel, priceLabel, buttonBox);
        card.getChildren().addAll(imageView, content);

        // Effet hover sur la carte
        card.setOnMouseEntered(e -> {
            card.setStyle("-fx-background-color: #f8f9fa; " +
                    "-fx-border-color: #3498db; " +
                    "-fx-border-width: 2px; " +
                    "-fx-border-radius: 10px; " +
                    "-fx-background-radius: 10px; " +
                    "-fx-padding: 0px; " +
                    "-fx-spacing: 0px; " +
                    "-fx-effect: dropshadow(three-pass-box, rgba(52,152,219,0.2), 15, 0, 0, 0);" +
                    "-fx-cursor: hand;");
        });

        card.setOnMouseExited(e -> {
            card.setStyle("-fx-background-color: white; " +
                    "-fx-border-color: #e0e0e0; " +
                    "-fx-border-width: 1px; " +
                    "-fx-border-radius: 10px; " +
                    "-fx-background-radius: 10px; " +
                    "-fx-padding: 0px; " +
                    "-fx-spacing: 0px; " +
                    "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);" +
                    "-fx-cursor: hand;");
        });

        // Clic sur la carte
        card.setOnMouseClicked(e -> showHotelDetails(hotel));

        return card;
    }

    // CALCULER LE PRIX MINIMUM D'UN HÔTEL (méthode locale)
    private double calculateHotelMinPrice(Hotel hotel) {
        if (hotel.getChambres() == null || hotel.getChambres().isEmpty()) {
            return 0.0;
        }

        double minPrice = Double.MAX_VALUE;
        for (Chambre chambre : hotel.getChambres()) {
            if (chambre.getPrix_nuit() < minPrice) {
                minPrice = chambre.getPrix_nuit();
            }
        }
        return minPrice == Double.MAX_VALUE ? 0.0 : minPrice;
    }

    private String getHotelImageUrl(Hotel hotel) {
        if (hotel.getImgs() != null && !hotel.getImgs().isEmpty()) {
            for (Image_hotel img : hotel.getImgs()) {
                if (img.getUrl() != null && !img.getUrl().trim().isEmpty()) {
                    return img.getUrl();
                }
            }
        }
        return "https://images.unsplash.com/photo-1564501049412-61c2a3083791?w=600";
    }

    private void loadCities() {
        try {
            List<String> cities = hotelDAO.getDistinctCities();
            cityComboBox.getItems().clear();
            cityComboBox.getItems().addAll(cities);
        } catch (Exception e) {
            System.err.println("Error loading cities: " + e.getMessage());
            cityComboBox.getItems().addAll("Paris", "Lyon", "Marseille", "Nice", "Bordeaux", "Toulouse", "Strasbourg");
        }
    }

    private void loadRoomTypes() {
        try {
            List<String> roomTypes = hotelDAO.getDistinctRoomTypes();
            roomTypeComboBox.getItems().clear();
            roomTypeComboBox.getItems().add("All");
            roomTypeComboBox.getItems().addAll(roomTypes);
            roomTypeComboBox.setValue("All");
        } catch (Exception e) {
            System.err.println("Error loading types: " + e.getMessage());
            roomTypeComboBox.getItems().addAll("All", "Simple", "Double", "Suite", "Familiale");
            roomTypeComboBox.setValue("All");
        }
    }

    // MÉTHODE APPLYFILTERS CORRIGÉE - VÉRIFICATION DOUBLE
    @FXML
    private void applyFilters() {
        System.out.println("\n═══════════════════════════════════════════════════════");
        System.out.println("          APPLICATION DES FILTRES (VÉRIFICATION STRICTE)");
        System.out.println("═══════════════════════════════════════════════════════");

        String search = searchField2.getText().trim();
        String city = cityComboBox.getValue();
        String roomType = roomTypeComboBox.getValue();

        // Gestion des prix
        Double minPrice = null;
        Double maxPrice = null;

        try {
            // Prix minimum
            String minText = minPriceField.getText().trim();
            if (!minText.isEmpty()) {
                minPrice = Double.parseDouble(minText);
            }

            // Prix maximum
            String maxText = maxPriceField.getText().trim();
            if (!maxText.isEmpty()) {
                maxPrice = Double.parseDouble(maxText);
            } else {
                maxPrice = priceSlider.getValue();
            }

            // Validation
            if (minPrice != null && minPrice < 0) {
                showAlert("Error", "Minimum price must be lower than maximum price.");
                return;
            }

            if (maxPrice != null && maxPrice < 0) {
                showAlert("Error", "Maximum price cannot be negative.");
                return;
            }

            if (minPrice != null && maxPrice != null && minPrice > maxPrice) {
                showAlert("Error", "Minimum price must be lower than maximum price.");
                return;
            }

            System.out.println("🎯 Requested interval: " +
                    (minPrice != null ? "$" + minPrice : "no min") + " - " +
                    (maxPrice != null ? "$" + maxPrice : "no max"));

        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid price format. Use numbers (ex: 100.50).");
            return;
        }

        // Récupérer les étoiles sélectionnées
        List<Integer> stars = new ArrayList<>();
        if (star5.isSelected()) stars.add(5);
        if (star4.isSelected()) stars.add(4);
        if (star3.isSelected()) stars.add(3);

        // Récupérer les services sélectionnés
        List<String> services = new ArrayList<>();
        if (wifiCheck.isSelected()) services.add("Wi-Fi");
        if (poolCheck.isSelected()) services.add("Swimming Pool");
        if (spaCheck.isSelected()) services.add("Spa");
        if (restaurantCheck.isSelected()) services.add("Restaurant");
        if (breakfastCheck.isSelected()) services.add("Breakfast");
        if (parkingCheck.isSelected()) services.add("Parking");

        // Appliquer les filtres avec vérification STRICTE
        try {
            // 1. Obtenir les hôtels avec filtres SQL
            List<Hotel> filteredHotels = hotelDAO.findWithFiltersStrict(
                    search, city, minPrice, maxPrice, stars, services, roomType
            );

            // 2. VÉRIFICATION SUPPLEMENTAIRE côté Java (pour être absolument sûr)
            List<Hotel> strictlyFilteredHotels = new ArrayList<>();

            for (Hotel hotel : filteredHotels) {
                double hotelMinPrice = calculateHotelMinPrice(hotel);
                boolean isValid = true;

                // Vérifier le prix minimum
                if (minPrice != null && hotelMinPrice < minPrice) {
                    isValid = false;
                    System.out.println("❌ " + hotel.getNomHotel() + " - Min price: $" + hotelMinPrice +
                            " ✗ (less than min $" + minPrice + ")");
                }

                // Vérifier le prix maximum
                if (maxPrice != null && hotelMinPrice > maxPrice) {
                    isValid = false;
                    System.out.println("❌ " + hotel.getNomHotel() + " - Max price: $" + hotelMinPrice +
                            " ✗ (greater than max $" + maxPrice + ")");
                }

                if (isValid) {
                    strictlyFilteredHotels.add(hotel);
                    System.out.println("✅ " + hotel.getNomHotel() + " - Prix min: $" + hotelMinPrice + " ✓");
                }
            }

            // 3. Afficher les résultats
            displayFilteredResults(strictlyFilteredHotels, minPrice, maxPrice);

        } catch (Exception e) {
            System.err.println("❌ Error while filtering: " + e.getMessage());
            e.printStackTrace();

            // Fallback: filtrer manuellement de façon stricte
            filterHotelsStrictlyManually(minPrice, maxPrice, search, city, roomType, stars, services);
        }
    }

    // FILTRAGE MANUEL STRICT (FALLBACK)
    private void filterHotelsStrictlyManually(Double minPrice, Double maxPrice,
                                              String search, String city, String roomType,
                                              List<Integer> stars, List<String> services) {
        System.out.println("⚠️ Utilisation du filtrage manuel strict...");

        List<Hotel> filtered = new ArrayList<>();

        for (Hotel hotel : allHotels) {
            boolean isValid = true;

            // 1. Vérifier le prix minimum de l'hôtel
            double hotelMinPrice = calculateHotelMinPrice(hotel);

            if (minPrice != null && hotelMinPrice < minPrice) {
                isValid = false;
            }

            if (maxPrice != null && hotelMinPrice > maxPrice) {
                isValid = false;
            }

            // 2. Vérifier la recherche
            if (isValid && search != null && !search.isEmpty()) {
                String searchLower = search.toLowerCase();
                String hotelName = hotel.getNomHotel() != null ? hotel.getNomHotel().toLowerCase() : "";
                String hotelCity = hotel.getAdresse() != null && hotel.getAdresse().getVille() != null ?
                        hotel.getAdresse().getVille().toLowerCase() : "";

                if (!hotelName.contains(searchLower) && !hotelCity.contains(searchLower)) {
                    isValid = false;
                }
            }

            // 3. Vérifier la ville
            if (isValid && city != null && !city.isEmpty() && !"All".equals(city)) {
                String hotelCity = hotel.getAdresse() != null ? hotel.getAdresse().getVille() : "";
                if (!city.equalsIgnoreCase(hotelCity)) {
                    isValid = false;
                }
            }

            // 4. Vérifier les étoiles
            if (isValid && stars != null && !stars.isEmpty()) {
                if (!stars.contains(hotel.getEtoiles())) {
                    isValid = false;
                }
            }

            // 5. Vérifier le type de chambre
            if (isValid && roomType != null && !roomType.isEmpty() && !"All".equals(roomType)) {
                boolean hasRoomType = false;
                for (Chambre chambre : hotel.getChambres()) {
                    if (chambre.getType() != null &&
                            chambre.getType().name().equalsIgnoreCase(roomType)) {
                        hasRoomType = true;
                        break;
                    }
                }
                if (!hasRoomType) {
                    isValid = false;
                }
            }

            // 6. Vérifier les services
            if (isValid && services != null && !services.isEmpty()) {
                List<String> hotelServices = hotel.getServices();
                for (String service : services) {
                    if (!hotelServices.contains(service)) {
                        isValid = false;
                        break;
                    }
                }
            }

            if (isValid) {
                filtered.add(hotel);
                System.out.println("✅ " + hotel.getNomHotel() + " - Prix min: $" + hotelMinPrice + " ✓");
            } else {
                System.out.println("❌ " + hotel.getNomHotel() + " - Prix min: $" + hotelMinPrice + " ✗");
            }
        }

        displayFilteredResults(filtered, minPrice, maxPrice);
    }

    // MÉTHODE POUR AFFICHER LES RÉSULTATS FILTRÉS
    private void displayFilteredResults(List<Hotel> hotels, Double minPrice, Double maxPrice) {
        // Construire le message de l'intervalle
        String intervalMessage;
        if (minPrice != null && maxPrice != null) {
            intervalMessage = "Intervalle de prix: $" + String.format("%.2f", minPrice) +
                    " - $" + String.format("%.2f", maxPrice);
        } else if (minPrice != null) {
            intervalMessage = "Prix minimum: $" + String.format("%.2f", minPrice);
        } else if (maxPrice != null) {
            intervalMessage = "Prix maximum: $" + String.format("%.2f", maxPrice);
        } else {
            intervalMessage = "Pas de filtre de prix";
        }

        if (hotels.isEmpty()) {
            showAlert("Aucun résultat",
                    "Aucun hôtel ne correspond à vos critères.\n" +
                            intervalMessage +
                            "\n\nEssayez d'élargir votre recherche.");
        } else {
            // Afficher un résumé dans la console
            System.out.println("\n═══════════════════════════════════════════════════════");
            System.out.println("           RÉSULTATS FINAUX DU FILTRAGE");
            System.out.println("═══════════════════════════════════════════════════════");
            System.out.println(intervalMessage);
            System.out.println("Hôtels correspondants: " + hotels.size());
            System.out.println("───────────────────────────────────────────────────────");

            for (Hotel hotel : hotels) {
                double hotelMinPrice = calculateHotelMinPrice(hotel);
                System.out.printf("✅ %-30s - Prix min: $%.2f%n",
                        hotel.getNomHotel(), hotelMinPrice);
            }
            System.out.println("═══════════════════════════════════════════════════════\n");

            // Afficher une alerte avec les résultats
            StringBuilder alertMessage = new StringBuilder();
            alertMessage.append(intervalMessage).append("\n\n");

            if (hotels.size() == 1) {
                alertMessage.append("1 hôtel trouvé:\n\n");
            } else {
                alertMessage.append(hotels.size()).append(" hôtels trouvés:\n\n");
            }

            for (int i = 0; i < Math.min(hotels.size(), 5); i++) {
                Hotel hotel = hotels.get(i);
                double price = calculateHotelMinPrice(hotel);
                alertMessage.append("• ").append(hotel.getNomHotel())
                        .append(" - À partir de $").append(String.format("%.2f", price))
                        .append("\n");
            }

            if (hotels.size() > 5) {
                alertMessage.append("\n... et ").append(hotels.size() - 5).append(" autres hôtels");
            }

            // Créer une alerte personnalisée
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Résultats de la recherche");
            alert.setHeaderText("Filtrage appliqué");
            alert.setContentText(alertMessage.toString());

            // Ajouter un bouton OK personnalisé
            ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            alert.getButtonTypes().setAll(okButton);

            alert.showAndWait();
        }

        displayHotels(hotels);
    }

    // MÉTHODE POUR TESTER SPÉCIFIQUEMENT L'INTERVALLE 2000-3000
    @FXML
    private void testSpecificInterval2000_3000() {
        System.out.println("\n═══════════════════════════════════════════════════════");
        System.out.println("          TEST SPÉCIFIQUE: INTERVALLE 2000-3000");
        System.out.println("═══════════════════════════════════════════════════════");

        // Forcer l'intervalle 2000-3000
        minPriceField.setText("2000");
        maxPriceField.setText("3000");
        priceSlider.setValue(3000);
        priceRangeLabel.setText("Jusqu'à $3000 / nuit");

        // Nettoyer les autres filtres pour mieux voir
        searchField2.clear();
        if (cityComboBox.getSelectionModel() != null) {
            cityComboBox.getSelectionModel().clearSelection();
        }
        roomTypeComboBox.setValue("All");
        star5.setSelected(false);
        star4.setSelected(false);
        star3.setSelected(false);
        wifiCheck.setSelected(false);
        poolCheck.setSelected(false);
        spaCheck.setSelected(false);
        restaurantCheck.setSelected(false);
        breakfastCheck.setSelected(false);
        parkingCheck.setSelected(false);

        // Vérifier d'abord manuellement tous les hôtels
        System.out.println("\n=== VÉRIFICATION MANUELLE DE TOUS LES HÔTELS ===");
        System.out.println("Intervalle: $2000 - $3000");
        System.out.println("─────────────────────────────────────────────");

        List<Hotel> hotelsInRange = new ArrayList<>();
        for (Hotel hotel : allHotels) {
            double price = calculateHotelMinPrice(hotel);
            boolean inRange = (price >= 2000 && price <= 3000);

            if (inRange) {
                System.out.println("✅ " + hotel.getNomHotel() + " - Prix: $" + price + " ✓ DANS L'INTERVALLE");
                hotelsInRange.add(hotel);
            } else {
                System.out.println("❌ " + hotel.getNomHotel() + " - Prix: $" + price + " ✗ HORS INTERVALLE");
            }
        }

        System.out.println("\n=== RÉSULTAT ATTENDU ===");
        System.out.println("Hôtels attendus dans l'intervalle 2000-3000: " + hotelsInRange.size());
        if (!hotelsInRange.isEmpty()) {
            for (Hotel hotel : hotelsInRange) {
                System.out.println("  • " + hotel.getNomHotel() + " - $" + calculateHotelMinPrice(hotel));
            }
        } else {
            System.out.println("  Aucun hôtel dans cet intervalle (c'est normal si vos prix sont plus bas)");
        }

        // Maintenant appliquer les filtres
        System.out.println("\n=== APPLICATION DES FILTRES ===");
        applyFilters();
    }

    // MÉTHODE POUR TESTER LES FILTRES GÉNÉRAUX
    @FXML
    private void testPriceFilters() {
        System.out.println("\n═══════════════════════════════════════════════════════");
        System.out.println("              TESTS D'INTERVALLES DE PRIX");
        System.out.println("═══════════════════════════════════════════════════════");

        // Test 1: Large intervalle
        testInterval("0", "10000", "Large (0-10000)");

        // Test 2: Intervalle moyen
        testInterval("100", "500", "Moyen (100-500)");

        // Test 3: Petit intervalle
        testInterval("200", "300", "Petit (200-300)");

        // Test 4: Intervalle spécifique
        testInterval("300", "450", "Spécifique (300-450)");

        // Test 5: Intervalle bas
        testInterval("0", "200", "Bas (0-200)");

        // Test 6: Intervalle haut
        testInterval("500", "1000", "Haut (500-1000)");
    }

    private void testInterval(String min, String max, String label) {
        System.out.println("\nTest: " + label + " ($" + min + " - $" + max + ")");
        minPriceField.setText(min);
        maxPriceField.setText(max);
        applyFilters();

        // Attendre un peu entre les tests
        try { Thread.sleep(500); } catch (InterruptedException e) {}
    }

    // MÉTHODE RESETFILTERS AMÉLIORÉE
    @FXML
    private void resetFilters() {
        System.out.println("\n═══════════════════════════════════════════════════════");
        System.out.println("          RÉINITIALISATION DES FILTRES");
        System.out.println("═══════════════════════════════════════════════════════");

        searchField.clear();
        searchField2.clear();
        minPriceField.setText("0");
        maxPriceField.setText("3000");
        priceSlider.setValue(3000);

        if (cityComboBox.getSelectionModel() != null) {
            cityComboBox.getSelectionModel().clearSelection();
        }

        roomTypeComboBox.setValue("All");
        star5.setSelected(false);
        star4.setSelected(false);
        star3.setSelected(false);
        wifiCheck.setSelected(false);
        poolCheck.setSelected(false);
        spaCheck.setSelected(false);
        restaurantCheck.setSelected(false);
        breakfastCheck.setSelected(false);
        parkingCheck.setSelected(false);

        displayHotels(allHotels);

        showAlert("Filtres réinitialisés",
                "Tous les filtres ont été réinitialisés.\n" +
                        "Affichage de tous les hôtels (" + allHotels.size() + ").");
    }

    private void showHotelDetails(Hotel hotel) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/hotel-details.fxml"));
            Parent root = loader.load();

            HotelDetailsController controller = loader.getController();
            controller.setHotel(hotel);

            Stage stage = new Stage();
            stage.setScene(new Scene(root, 1000, 700));
            stage.setTitle(hotel.getNomHotel() + " - Détails");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'afficher les détails: " + e.getMessage());
        }
    }

    @FXML
    private void handleReservation(Hotel hotel) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/signup.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) hotelsContainer.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Inscription - ROOMY");

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir la page d'inscription: " + e.getMessage());
        }
    }

    @FXML
    private void openLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) hotelsContainer.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Connexion - ROOMY");

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir la connexion: " + e.getMessage());
        }
    }

    @FXML
    private void openRegister() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/signup.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) hotelsContainer.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Inscription - ROOMY");

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir l'inscription: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Méthode pour données d'exemple (en cas d'erreur BD)
    private List<Hotel> getSampleHotels() {
        List<Hotel> sampleHotels = new ArrayList<>();

        // Hôtel 1 - Prix BAS (1200)
        Hotel hotel1 = new Hotel();
        hotel1.setIdhotel(1);
        hotel1.setNomHotel("Dubai Palace Hotel");
        hotel1.setEtoiles(4);
        hotel1.setDescription("Luxury hotel in Dubai with amazing views.");

        Adresse adresse1 = new Adresse();
        adresse1.setVille("Dubai");
        adresse1.setPays("UAE");
        hotel1.setAdresse(adresse1);

        Image_hotel img1 = new Image_hotel();
        img1.setUrl("https://images.unsplash.com/photo-1566073771259-6a8506099945?w=600");
        hotel1.getImgs().add(img1);

        hotel1.getServices().addAll(List.of("Wi-Fi", "Pool", "Spa", "Restaurant", "Parking"));

        // Ajouter plusieurs chambres avec différents prix
        Chambre chambre1_1 = new Chambre();
        chambre1_1.setPrix_nuit(1200.00);  // Prix bas
        chambre1_1.setType(TypeChambre.Double);
        hotel1.getChambres().add(chambre1_1);

        Chambre chambre1_2 = new Chambre();
        chambre1_2.setPrix_nuit(2500.00);  // Prix dans l'intervalle 2000-3000
        chambre1_2.setType(TypeChambre.Suite);
        hotel1.getChambres().add(chambre1_2);

        Chambre chambre1_3 = new Chambre();
        chambre1_3.setPrix_nuit(3500.00);  // Prix haut
        chambre1_3.setType(TypeChambre.Suite);
        hotel1.getChambres().add(chambre1_3);

        // Hôtel 2 - Prix MOYEN (800)
        Hotel hotel2 = new Hotel();
        hotel2.setIdhotel(2);
        hotel2.setNomHotel("Paris Luxury Hotel");
        hotel2.setEtoiles(5);
        hotel2.setDescription("5-star hotel in Paris.");

        Adresse adresse2 = new Adresse();
        adresse2.setVille("Paris");
        adresse2.setPays("France");
        hotel2.setAdresse(adresse2);

        Image_hotel img2 = new Image_hotel();
        img2.setUrl("https://images.unsplash.com/photo-1542314831-068cd1dbfeeb?w=600");
        hotel2.getImgs().add(img2);

        hotel2.getServices().addAll(List.of("Wi-Fi", "Pool", "Breakfast", "Parking"));

        Chambre chambre2_1 = new Chambre();
        chambre2_1.setPrix_nuit(800.00);
        chambre2_1.setType(TypeChambre.Simple);
        hotel2.getChambres().add(chambre2_1);

        Chambre chambre2_2 = new Chambre();
        chambre2_2.setPrix_nuit(1200.00);
        chambre2_2.setType(TypeChambre.Double);
        hotel2.getChambres().add(chambre2_2);

        // Hôtel 3 - Prix DANS INTERVALLE (2200)
        Hotel hotel3 = new Hotel();
        hotel3.setIdhotel(3);
        hotel3.setNomHotel("New York Premium Suites");
        hotel3.setEtoiles(5);
        hotel3.setDescription("Premium suites in New York.");

        Adresse adresse3 = new Adresse();
        adresse3.setVille("New York");
        adresse3.setPays("USA");
        hotel3.setAdresse(adresse3);

        Image_hotel img3 = new Image_hotel();
        img3.setUrl("https://images.unsplash.com/photo-1564501049412-61c2a3083791?w=600");
        hotel3.getImgs().add(img3);

        hotel3.getServices().addAll(List.of("Wi-Fi", "Spa", "Restaurant", "Concierge", "Valet Parking"));

        Chambre chambre3_1 = new Chambre();
        chambre3_1.setPrix_nuit(2200.00);  // Prix DANS intervalle 2000-3000
        chambre3_1.setType(TypeChambre.Suite);
        hotel3.getChambres().add(chambre3_1);

        Chambre chambre3_2 = new Chambre();
        chambre3_2.setPrix_nuit(2800.00);  // Prix DANS intervalle 2000-3000
        chambre3_2.setType(TypeChambre.Suite);
        hotel3.getChambres().add(chambre3_2);

        // Hôtel 4 - Prix HAUT (3500)
        Hotel hotel4 = new Hotel();
        hotel4.setIdhotel(4);
        hotel4.setNomHotel("Tokyo Sky Resort");
        hotel4.setEtoiles(5);
        hotel4.setDescription("High-end resort in Tokyo.");

        Adresse adresse4 = new Adresse();
        adresse4.setVille("Tokyo");
        adresse4.setPays("Japan");
        hotel4.setAdresse(adresse4);

        Image_hotel img4 = new Image_hotel();
        img4.setUrl("https://images.unsplash.com/photo-1520250497591-112f2f40a3f4?w=600");
        hotel4.getImgs().add(img4);

        hotel4.getServices().addAll(List.of("Wi-Fi", "Spa", "Restaurant", "Gym", "Pool"));

        Chambre chambre4_1 = new Chambre();
        chambre4_1.setPrix_nuit(3500.00);  // Prix HAUT (hors intervalle)
        chambre4_1.setType(TypeChambre.Familiale);
        hotel4.getChambres().add(chambre4_1);

        Chambre chambre4_2 = new Chambre();
        chambre4_2.setPrix_nuit(4200.00);  // Prix TRÈS HAUT
        chambre4_2.setType(TypeChambre.Suite);
        hotel4.getChambres().add(chambre4_2);

        sampleHotels.add(hotel1);
        sampleHotels.add(hotel2);
        sampleHotels.add(hotel3);
        sampleHotels.add(hotel4);

        return sampleHotels;
    }
}
