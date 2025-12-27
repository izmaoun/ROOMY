package com.roomy.Controller;

import com.roomy.Dao.*;
import com.roomy.entities.*;
import com.roomy.service.Session;
import com.roomy.ENUMS.StatutReservation;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class BookingController {
    
    @FXML private DatePicker dateArrivee;
    @FXML private DatePicker dateDepart;
    @FXML private Spinner<Integer> nombrePersonnes;
    @FXML private Label hotelNameLabel;
    @FXML private Label chambreInfoLabel;
    @FXML private Label prixLabel;
    @FXML private Label totalLabel;
    @FXML private Button confirmerButton;
    
    private Hotel selectedHotel;
    private Chambre selectedChambre;
    private ReservationDAO reservationDAO = new ReservationDAO();
    
    public void initialize() {
        // Configuration du spinner
        if (nombrePersonnes != null) {
            nombrePersonnes.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1));
            nombrePersonnes.valueProperty().addListener((obs, oldVal, newVal) -> updateTotal());
        }
        
        // Listeners pour les dates
        if (dateArrivee != null) {
            dateArrivee.valueProperty().addListener((obs, oldVal, newVal) -> updateTotal());
        }
        if (dateDepart != null) {
            dateDepart.valueProperty().addListener((obs, oldVal, newVal) -> updateTotal());
        }
    }
    
    public void setHotelAndChambre(Hotel hotel, Chambre chambre) {
        this.selectedHotel = hotel;
        this.selectedChambre = chambre;
        updateUI();
    }
    
    // Méthode pour réserver depuis l'hôtel (sans chambre spécifique)
    public void setHotelOnly(Hotel hotel) {
        this.selectedHotel = hotel;
        this.selectedChambre = null; // Pas de chambre spécifique
        updateUI();
    }
    
    private void updateUI() {
        if (selectedHotel != null) {
            hotelNameLabel.setText(selectedHotel.getNomHotel());
            
            if (selectedChambre != null) {
                // Cas : chambre spécifique choisie
                chambreInfoLabel.setText("Chambre " + selectedChambre.getNumchambre() + " - " + selectedChambre.getType());
                prixLabel.setText(String.format("%.2f DH/nuit", selectedChambre.getPrix_nuit()));
            } else {
                // Cas : réservation depuis l'hôtel (choix de chambre à faire)
                chambreInfoLabel.setText("Veuillez choisir une chambre ci-dessous");
                double minPrice = selectedHotel.getMinPrice();
                prixLabel.setText(String.format("À partir de %.2f DH/nuit", minPrice));
            }
            
            updateTotal();
        }
    }
    
    private void updateTotal() {
        if (selectedChambre != null && dateArrivee != null && dateDepart != null) {
            LocalDate debut = dateArrivee.getValue();
            LocalDate fin = dateDepart.getValue();
            
            if (debut != null && fin != null && fin.isAfter(debut)) {
                long nuits = java.time.temporal.ChronoUnit.DAYS.between(debut, fin);
                double total = nuits * selectedChambre.getPrix_nuit();
                totalLabel.setText(String.format("Total: %.2f DH", total));
            } else {
                totalLabel.setText("Total: -- DH");
            }
        } else {
            totalLabel.setText("Total: -- DH (sélectionnez une chambre)");
        }
    }
    
    @FXML
    private void handleConfirmerReservation(ActionEvent event) {
        // Vérifier si l'utilisateur est connecté
        Client client = Session.getCurrentClient();
        if (client == null) {
            showAlert("Erreur", "Vous devez être connecté pour réserver.");
            return;
        }
        
        // Validation des données
        if (!validateReservationData()) {
            return;
        }
        
        // Vérifier qu'une chambre est sélectionnée
        if (selectedChambre == null) {
            showAlert("Erreur", "Veuillez sélectionner une chambre avant de confirmer.");
            return;
        }
        
        try {
            // Créer la réservation
            Reservation reservation = new Reservation();
            reservation.setClient(client);
            reservation.setChambre(selectedChambre);
            reservation.setDateDebutSejour(dateArrivee.getValue().atTime(14, 0)); // Check-in 14h
            reservation.setDateFinSejour(dateDepart.getValue().atTime(11, 0)); // Check-out 11h
            reservation.setNombrePersonnes(nombrePersonnes.getValue());
            
            // Calculer le montant total
            long nuits = java.time.temporal.ChronoUnit.DAYS.between(dateArrivee.getValue(), dateDepart.getValue());
            double total = nuits * selectedChambre.getPrix_nuit();
            reservation.setMontantTotal(total);
            reservation.setStatut(StatutReservation.EN_ATTENTE);
            
            // Sauvegarder en base
            boolean success = reservationDAO.createReservation(reservation);
            
            if (success) {
                showSuccessAlert("Réservation confirmée !", 
                    "Votre réservation a été créée avec succès.\n" +
                    "Vous pouvez la consulter dans vos réservations.");
                goToReservations(event);
            } else {
                showAlert("Échec", "Échec de la création de la réservation.");
            }
            
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de la réservation: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private boolean validateReservationData() {
        if (dateArrivee.getValue() == null) {
            showAlert("Erreur", "Veuillez sélectionner une date d'arrivée.");
            return false;
        }
        
        if (dateDepart.getValue() == null) {
            showAlert("Erreur", "Veuillez sélectionner une date de départ.");
            return false;
        }
        
        if (dateArrivee.getValue().isBefore(LocalDate.now())) {
            showAlert("Erreur", "La date d'arrivée ne peut pas être dans le passé.");
            return false;
        }
        
        if (dateDepart.getValue().isBefore(dateArrivee.getValue()) || 
            dateDepart.getValue().isEqual(dateArrivee.getValue())) {
            showAlert("Erreur", "La date de départ doit être après la date d'arrivée.");
            return false;
        }
        
        if (selectedChambre != null && nombrePersonnes.getValue() > selectedChambre.getCapacity()) {
            showAlert("Erreur", "Le nombre de personnes dépasse la capacité de la chambre (" + 
                     selectedChambre.getCapacity() + " personnes max).");
            return false;
        }
        
        return true;
    }
    
    @FXML
    private void goBack(ActionEvent event) {
        try {
            // Retourner vers la landing page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/landing-page.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 1200, 800));
            stage.setTitle("ROOMY - Accueil");
        } catch (Exception e) {
            showAlert("Erreur", "Impossible de retourner à l'accueil.");
            e.printStackTrace();
        }
    }
    
    private void goToReservations(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dash_client.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Dashboard Client - ROOMY");
            stage.setMaximized(true);
        } catch (Exception e) {
            showAlert("Erreur", "Impossible d'ouvrir le dashboard client.");
            e.printStackTrace();
        }
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void showSuccessAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        
        // Style personnalisé pour le succès
        alert.getDialogPane().setStyle("-fx-background-color: #d4edda; -fx-border-color: #c3e6cb;");
        alert.showAndWait();
    }
}