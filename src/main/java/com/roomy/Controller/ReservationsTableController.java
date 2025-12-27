package com.roomy.Controller;

import com.roomy.Dao.ReservationDAO;
import com.roomy.entities.Client;
import com.roomy.entities.Reservation;
import com.roomy.service.Session;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReservationsTableController {

    @FXML private TableView<Reservation> reservationsTable;
    @FXML private TableColumn<Reservation, Integer> colId;
    @FXML private TableColumn<Reservation, String> colHotel;
    @FXML private TableColumn<Reservation, String> colChambre;
    @FXML private TableColumn<Reservation, String> colDateDebut;
    @FXML private TableColumn<Reservation, String> colDateFin;
    @FXML private TableColumn<Reservation, Integer> colPersonnes;
    @FXML private TableColumn<Reservation, Double> colMontant;
    @FXML private TableColumn<Reservation, String> colStatut;
    @FXML private TableColumn<Reservation, Void> colActions;
    @FXML private Label noReservationsLabel;

    private ReservationDAO reservationDAO = new ReservationDAO();
    private ObservableList<Reservation> reservationsList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        System.out.println("=== Initialisation ReservationsTableController ===");
        setupTableColumns();
        loadReservations();
    }

    private void setupTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idReservation"));
        
        colHotel.setCellValueFactory(cellData -> {
            String hotelName = "Inconnu";
            if (cellData.getValue().getChambre() != null && 
                cellData.getValue().getChambre().getHotel() != null) {
                hotelName = cellData.getValue().getChambre().getHotel().getNomHotel();
            }
            return new javafx.beans.property.SimpleStringProperty(hotelName);
        });
        
        colChambre.setCellValueFactory(cellData -> {
            String chambreInfo = "Inconnue";
            if (cellData.getValue().getChambre() != null) {
                chambreInfo = "Ch. " + cellData.getValue().getChambre().getNumchambre();
            }
            return new javafx.beans.property.SimpleStringProperty(chambreInfo);
        });
        
        colDateDebut.setCellValueFactory(cellData -> {
            String date = cellData.getValue().getDateDebutSejour()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            return new javafx.beans.property.SimpleStringProperty(date);
        });
        
        colDateFin.setCellValueFactory(cellData -> {
            String date = cellData.getValue().getDateFinSejour()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            return new javafx.beans.property.SimpleStringProperty(date);
        });
        
        colPersonnes.setCellValueFactory(new PropertyValueFactory<>("nombrePersonnes"));
        colMontant.setCellValueFactory(new PropertyValueFactory<>("montantTotal"));
        colStatut.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getStatut().toString()));
        
        // Colonne Actions avec boutons
        colActions.setCellFactory(param -> new TableCell<Reservation, Void>() {
            private final Button btnDetails = new Button("Détails");
            private final Button btnAnnuler = new Button("Annuler");
            
            {
                btnDetails.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 10px;");
                btnAnnuler.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 10px;");
                
                btnDetails.setOnAction(e -> {
                    Reservation reservation = getTableView().getItems().get(getIndex());
                    showReservationDetails(reservation);
                });
                
                btnAnnuler.setOnAction(e -> {
                    Reservation reservation = getTableView().getItems().get(getIndex());
                    cancelReservation(reservation);
                });
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    javafx.scene.layout.HBox buttons = new javafx.scene.layout.HBox(5);
                    buttons.getChildren().addAll(btnDetails, btnAnnuler);
                    setGraphic(buttons);
                }
            }
        });
    }

    private void loadReservations() {
        Client client = Session.getCurrentClient();
        
        if (client == null) {
            System.err.println("❌ Aucun client connecté!");
            noReservationsLabel.setVisible(true);
            return;
        }

        System.out.println("🔍 Chargement réservations pour client: " + client.getEmail());
        
        try {
            List<Reservation> reservations = reservationDAO.getReservationsByClientId(client.getIdClient());
            System.out.println("📊 Réservations trouvées: " + reservations.size());
            
            reservationsList.clear();
            reservationsList.addAll(reservations);
            reservationsTable.setItems(reservationsList);
            
            noReservationsLabel.setVisible(reservations.isEmpty());
            
        } catch (Exception e) {
            System.err.println("❌ Erreur chargement réservations: " + e.getMessage());
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger les réservations: " + e.getMessage());
        }
    }

    @FXML
    private void handleNouvelleReservation() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/landing-page.fxml"));
            Parent root = loader.load();
            
            LandingPageController controller = loader.getController();
            controller.hideAuthButtons();
            
            Stage stage = (Stage) reservationsTable.getScene().getWindow();
            stage.setScene(new Scene(root, 1200, 800));
            stage.setTitle("ROOMY - Nouvelle Réservation");
            stage.setMaximized(true);
        } catch (Exception e) {
            showAlert("Erreur", "Impossible d'ouvrir la page de réservation");
            e.printStackTrace();
        }
    }

    private void showReservationDetails(Reservation reservation) {
        String details = String.format(
            "Réservation #%d\n\n" +
            "Hôtel: %s\n" +
            "Chambre: %d\n" +
            "Du %s au %s\n" +
            "Personnes: %d\n" +
            "Montant: %.2f DH\n" +
            "Statut: %s",
            reservation.getIdReservation(),
            reservation.getChambre().getHotel().getNomHotel(),
            reservation.getChambre().getNumchambre(),
            reservation.getDateDebutSejour().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
            reservation.getDateFinSejour().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
            reservation.getNombrePersonnes(),
            reservation.getMontantTotal(),
            reservation.getStatut()
        );
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Détails de la réservation");
        alert.setHeaderText(null);
        alert.setContentText(details);
        alert.showAndWait();
    }

    private void cancelReservation(Reservation reservation) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Annuler la réservation");
        confirm.setHeaderText("Êtes-vous sûr ?");
        confirm.setContentText("Voulez-vous vraiment annuler cette réservation ?");
        
        if (confirm.showAndWait().get() == ButtonType.OK) {
            boolean success = reservationDAO.annulerReservation(reservation.getIdReservation());
            if (success) {
                showAlert("Succès", "Réservation annulée avec succès");
                loadReservations(); // Recharger
            } else {
                showAlert("Erreur", "Impossible d'annuler la réservation");
            }
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}