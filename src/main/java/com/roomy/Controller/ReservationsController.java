package com.roomy.Controller;

import com.roomy.Dao.ClientDAO;
import com.roomy.Dao.ReservationDAO;
import com.roomy.entities.Client;
import com.roomy.entities.Reservation;
import com.roomy.service.Session;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class ReservationsController {

    @FXML private VBox upcomingReservationsContainer;
    @FXML private VBox pastReservationsContainer;
    @FXML private Label noUpcomingLabel;
    @FXML private Label noPastLabel;

    private ReservationDAO reservationDAO = new ReservationDAO();
    private ClientDAO clientDAO = new ClientDAO();
    private DateTimeFormatter yearFormatter = DateTimeFormatter.ofPattern("yyyy");
    private DateTimeFormatter monthDayFormatter = DateTimeFormatter.ofPattern("MMM dd");

    @FXML
    public void initialize() {
        System.out.println("Initialisation de ReservationsController...");
        loadReservations();
    }


    private void loadReservations() {
        Client client = Session.getCurrentClient();

        if (client == null) {
            System.err.println("❌ Aucun client connecté dans la session!");
            showAlert("Erreur", "Aucun client connecté", Alert.AlertType.ERROR);
            return;
        }

        System.out.println("Chargement des réservations pour client ID: " + client.getIdClient());

        try {
            List<Reservation> reservations = reservationDAO.getReservationsByClientId(client.getIdClient());
            System.out.println("Nombre de réservations récupérées: " + reservations.size());

            // Vider les containers
            upcomingReservationsContainer.getChildren().clear();
            pastReservationsContainer.getChildren().clear();

            LocalDate today = LocalDate.now();
            System.out.println("Date d'aujourd'hui: " + today);

            boolean hasUpcoming = false;
            boolean hasPast = false;

            for (Reservation reservation : reservations) {
                System.out.println("Traitement réservation ID: " + reservation.getIdReservation() +
                        ", Statut: " + reservation.getStatut());

                // Sauter les réservations annulées
                if (reservation.getStatut() != null &&
                        reservation.getStatut().toString().equalsIgnoreCase("ANNULEE")) {
                    System.out.println("Sautée (annulée)");
                    continue;
                }

                HBox card = createReservationCard(reservation);

                // Convertir LocalDateTime en LocalDate pour comparaison
                LocalDate dateDebut = reservation.getDateDebutSejour().toLocalDate();
                System.out.println("Date début: " + dateDebut + ", Aujourd'hui: " + today);

                // Séparer les réservations futures et passées
                if (dateDebut.isAfter(today) || dateDebut.isEqual(today)) {
                    System.out.println("→ Ajoutée aux réservations à venir");
                    upcomingReservationsContainer.getChildren().add(card);
                    hasUpcoming = true;
                } else {
                    System.out.println("→ Ajoutée aux réservations passées");
                    pastReservationsContainer.getChildren().add(card);
                    hasPast = true;
                }
            }

            // Afficher les messages si vide
            noUpcomingLabel.setVisible(!hasUpcoming);
            noPastLabel.setVisible(!hasPast);

            System.out.println("Réservations à venir: " + (hasUpcoming ? "OUI" : "NON"));
            System.out.println("Réservations passées: " + (hasPast ? "OUI" : "NON"));

        } catch (Exception e) {
            System.err.println("❌ Erreur lors du chargement des réservations: " + e.getMessage());
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger les réservations: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }


    private HBox createReservationCard(Reservation reservation) {
        System.out.println("Création carte réservation ID: " + reservation.getIdReservation());

        HBox card = new HBox(20);
        card.getStyleClass().add("reservation-card");
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; -fx-border-width: 1px; -fx-border-radius: 8px;");

        // Convertir LocalDateTime en LocalDate
        LocalDate dateDebut = reservation.getDateDebutSejour().toLocalDate();
        LocalDate dateFin = reservation.getDateFinSejour().toLocalDate();

        // Date début
        VBox dateDebutBox = createDateBox(dateDebut);

        // Flèche
        Label arrow = new Label("→");
        arrow.setStyle("-fx-font-size: 20px; -fx-text-fill: #1a1a1a;");

        // Date fin
        VBox dateFinBox = createDateBox(dateFin);

        // Informations de la chambre/hôtel
        VBox infoBox = new VBox(5);
        infoBox.setAlignment(Pos.CENTER_LEFT);

        String hotelName = "Hôtel non spécifié";
        String chambreInfo = "Chambre non spécifiée";

        if (reservation.getChambre() != null) {
            chambreInfo = "Chambre " + reservation.getChambre().getNumchambre();

            if (reservation.getChambre().getType() != null) {
                chambreInfo += " (" + reservation.getChambre().getType() + ")";
            }

            if (reservation.getChambre().getHotel() != null &&
                    reservation.getChambre().getHotel().getNomHotel() != null) {
                hotelName = reservation.getChambre().getHotel().getNomHotel();
            }
        }

        Label hotelLabel = new Label(hotelName);
        hotelLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #1a1a1a;");

        Label chambreLabel = new Label(chambreInfo);
        chambreLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #666666;");

        infoBox.getChildren().addAll(hotelLabel, chambreLabel);

        // Espace flexible
        Region spacer = new Region();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        // Prix
        Label priceLabel = new Label(String.format("%.0f DH", reservation.getMontantTotal()));
        priceLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #380F17;");

        // Bouton avec options
        MenuButton optionsBtn = new MenuButton("Options");
        optionsBtn.setStyle("-fx-background-color: #380F17; -fx-text-fill: white; -fx-font-weight: bold;");

        MenuItem detailsItem = new MenuItem("Voir détails");
        detailsItem.setOnAction(e -> showReservationDetails(reservation));

        MenuItem cancelItem = new MenuItem("Annuler");
        cancelItem.setOnAction(e -> cancelReservation(reservation));

        // Désactiver si la date de début est passée
        boolean isPast = reservation.getDateDebutSejour().isBefore(LocalDateTime.now());
        cancelItem.setDisable(isPast);

        if (isPast) {
            cancelItem.setText("Annuler (indisponible)");
        }

        optionsBtn.getItems().addAll(detailsItem, cancelItem);

        card.getChildren().addAll(dateDebutBox, arrow, dateFinBox, infoBox, spacer, priceLabel, optionsBtn);

        return card;
    }


    private VBox createDateBox(LocalDate date) {
        VBox box = new VBox(5);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(5, 10, 5, 10));
        box.setStyle("-fx-background-color: #380F17; -fx-background-radius: 5;");

        // Mois et jour (ex: "Jan 08")
        String monthDay = date.format(monthDayFormatter);
        Label monthLabel = new Label(monthDay);
        monthLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: white;");

        // Année
        String year = date.format(yearFormatter);
        Label yearLabel = new Label(year);
        yearLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: white;");

        box.getChildren().addAll(monthLabel, yearLabel);

        return box;
    }


    private void showReservationDetails(Reservation reservation) {
        String hotelName = "Non spécifié";
        String chambreInfo = "Non spécifiée";
        double prixNuit = 0.0;
        int capacity = 0;

        if (reservation.getChambre() != null) {
            chambreInfo = "Chambre n°" + reservation.getChambre().getNumchambre();

            if (reservation.getChambre().getType() != null) {
                chambreInfo += " (" + reservation.getChambre().getType() + ")";
            }

            prixNuit = reservation.getChambre().getPrix_nuit();
            capacity = reservation.getChambre().getCapacity();

            if (reservation.getChambre().getHotel() != null) {
                hotelName = reservation.getChambre().getHotel().getNomHotel();
            }
        }

        long nights = java.time.temporal.ChronoUnit.DAYS.between(
                reservation.getDateDebutSejour().toLocalDate(),
                reservation.getDateFinSejour().toLocalDate()
        );

        String details = String.format(
                "DÉTAILS DE LA RÉSERVATION\n\n" +
                        "Hôtel: %s\n" +
                        "Chambre: %s\n" +
                        "Prix/nuit: %.2f DH\n" +
                        "Capacité: %d personnes\n\n" +
                        "Date d'arrivée: %s\n" +
                        "Date de départ: %s\n" +
                        "Nombre de nuits: %d\n" +
                        "Nombre de personnes: %d\n\n" +
                        "Montant total: %.2f DH\n" +
                        "Statut: %s\n" +
                        "Date de réservation: %s",
                hotelName,
                chambreInfo,
                prixNuit,
                capacity,
                reservation.getDateDebutSejour().toLocalDate(),
                reservation.getDateFinSejour().toLocalDate(),
                nights,
                reservation.getNombrePersonnes(),
                reservation.getMontantTotal(),
                reservation.getStatut(),
                reservation.getDateReservation() != null ?
                        reservation.getDateReservation().toLocalDate() : "Non spécifiée"
        );

        // Créer un dialogue personnalisé
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Détails de la réservation");

        // Créer le contenu personnalisé
        VBox content = new VBox(15);
        content.setPadding(new Insets(20, 25, 20, 25));
        content.setStyle("-fx-background-color: white;");

        // En-tête du dialogue
        Label headerLabel = new Label("Réservation #" + reservation.getIdReservation());
        headerLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #380F17;");

        // Contenu des détails
        TextArea detailsArea = new TextArea(details);
        detailsArea.setEditable(false);
        detailsArea.setWrapText(true);
        detailsArea.setStyle("-fx-background-color: #F9F7F2; " +
                "-fx-border-color: #E0D6C2; " +
                "-fx-border-width: 1px; " +
                "-fx-border-radius: 5px; " +
                "-fx-background-radius: 5px; " +
                "-fx-padding: 10px; " +
                "-fx-font-size: 13px; " +
                "-fx-text-fill: #333333; " +
                "-fx-pref-height: 300px;");

        content.getChildren().addAll(headerLabel, detailsArea);
        dialog.getDialogPane().setContent(content);

        // Ajouter le bouton OK
        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(okButtonType);

        // Styliser le DialogPane
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.setStyle("-fx-background-color: white; " +
                "-fx-background-radius: 12px; " +
                "-fx-border-radius: 12px; " +
                "-fx-border-color: #E0D6C2; " +
                "-fx-border-width: 2px; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(56, 15, 23, 0.2), 20, 0, 0, 10);");

        // Styliser le bouton
        Button okButton = (Button) dialogPane.lookupButton(okButtonType);
        styleDialogButton(okButton, "#8B4C4C", "#9A5A5A", "#7A3C3C");

        dialog.showAndWait();
    }


    private void cancelReservation(Reservation reservation) {
        // Vérifier si la réservation est déjà passée
        if (reservation.getDateDebutSejour().isBefore(LocalDateTime.now())) {
            showAlert("Impossible", "Vous ne pouvez pas annuler une réservation déjà commencée.",
                    Alert.AlertType.WARNING);
            return;
        }

        final int reservationId = reservation.getIdReservation();

        String hotelName = (reservation.getChambre() != null &&
                reservation.getChambre().getHotel() != null) ?
                reservation.getChambre().getHotel().getNomHotel() : "Inconnu";

        String dates = reservation.getDateDebutSejour().toLocalDate() + " au " +
                reservation.getDateFinSejour().toLocalDate();

        // Créer un dialogue de confirmation personnalisé
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Annuler la réservation");

        // Créer le contenu personnalisé
        VBox content = new VBox(15);
        content.setPadding(new Insets(20, 25, 20, 25));
        content.setStyle("-fx-background-color: white;");

        // En-tête du dialogue
        Label headerLabel = new Label("Confirmation d'annulation");
        headerLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #380F17;");

        // Icone d'avertissement
        Label warningIcon = new Label("⚠️");
        warningIcon.setStyle("-fx-font-size: 30px; -fx-padding: 0 0 10px 0;");

        // Message de confirmation
        Label messageLabel = new Label("Êtes-vous sûr de vouloir annuler cette réservation ?");
        messageLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #555555; -fx-font-weight: bold;");

        // Détails de la réservation
        VBox detailsBox = new VBox(5);
        detailsBox.setStyle("-fx-background-color: #F9F7F2; " +
                "-fx-border-color: #E0D6C2; " +
                "-fx-border-width: 1px; " +
                "-fx-border-radius: 5px; " +
                "-fx-background-radius: 5px; " +
                "-fx-padding: 10px;");

        Label hotelLabel = new Label("Hôtel: " + hotelName);
        hotelLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #333333;");

        Label datesLabel = new Label("Dates: " + dates);
        datesLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #333333;");

        Label amountLabel = new Label("Montant: " + String.format("%.2f DH", reservation.getMontantTotal()));
        amountLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #333333;");

        detailsBox.getChildren().addAll(hotelLabel, datesLabel, amountLabel);

        content.getChildren().addAll(headerLabel, warningIcon, messageLabel, detailsBox);
        dialog.getDialogPane().setContent(content);

        // Ajouter les boutons
        ButtonType cancelButtonType = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType confirmButtonType = new ButtonType("Confirmer l'annulation", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(cancelButtonType, confirmButtonType);

        // Styliser le DialogPane
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.setStyle("-fx-background-color: white; " +
                "-fx-background-radius: 12px; " +
                "-fx-border-radius: 12px; " +
                "-fx-border-color: #E0D6C2; " +
                "-fx-border-width: 2px; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(56, 15, 23, 0.2), 20, 0, 0, 10);");

        // Styliser les boutons
        Button cancelButton = (Button) dialogPane.lookupButton(cancelButtonType);
        Button confirmButton = (Button) dialogPane.lookupButton(confirmButtonType);

        styleDialogButton(cancelButton, "#95A5A6", "#7F8C8D", "#6A7B8C");
        styleDialogButton(confirmButton, "#e74c3c", "#c0392b", "#a93226"); // Rouge pour annulation

        // Afficher le dialogue et attendre la réponse
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == confirmButtonType) {
            boolean success = reservationDAO.annulerReservation(reservationId);
            if (success) {
                showAlert("Succès", "Réservation annulée avec succès.", Alert.AlertType.INFORMATION);
                loadReservations(); // Recharger la liste
            } else {
                showAlert("Erreur", "Impossible d'annuler la réservation.", Alert.AlertType.ERROR);
            }
        }
    }


    @FXML
    private void handleNouvelleReservation() {
        showAlert("Information", "La création de nouvelle réservation n'est pas encore disponible.",
                Alert.AlertType.INFORMATION);
    }


    @FXML
    private void goToAccueil() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/welcome.fxml"));
            Parent root = fxmlLoader.load();

            Stage currentStage = (Stage) upcomingReservationsContainer.getScene().getWindow();
            currentStage.setScene(new Scene(root, 960, 600));
            currentStage.setTitle("Roomy - Accueil");
            currentStage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir l'accueil", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void goToProfile() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/client_profile.fxml"));
            Parent root = fxmlLoader.load();

            Stage currentStage = (Stage) upcomingReservationsContainer.getScene().getWindow();
            currentStage.setScene(new Scene(root, 960, 600));
            currentStage.setTitle("Roomy - Profil");
            currentStage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir le profil", Alert.AlertType.ERROR);
        }
    }


    @FXML
    private void logout() {
        // Créer un dialogue de confirmation personnalisé
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Déconnexion");

        // Créer le contenu personnalisé
        VBox content = new VBox(15);
        content.setPadding(new Insets(20, 25, 20, 25));
        content.setStyle("-fx-background-color: white;");

        // En-tête du dialogue
        Label headerLabel = new Label("Déconnexion");
        headerLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #380F17;");

        // Message de confirmation
        Label messageLabel = new Label("Voulez-vous vraiment vous déconnecter ?");
        messageLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #555555;");

        content.getChildren().addAll(headerLabel, messageLabel);
        dialog.getDialogPane().setContent(content);

        // Ajouter les boutons
        ButtonType cancelButtonType = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType confirmButtonType = new ButtonType("Se déconnecter", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(cancelButtonType, confirmButtonType);

        // Styliser le DialogPane
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.setStyle("-fx-background-color: white; " +
                "-fx-background-radius: 12px; " +
                "-fx-border-radius: 12px; " +
                "-fx-border-color: #E0D6C2; " +
                "-fx-border-width: 2px; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(56, 15, 23, 0.2), 20, 0, 0, 10);");

        // Styliser les boutons
        Button cancelButton = (Button) dialogPane.lookupButton(cancelButtonType);
        Button confirmButton = (Button) dialogPane.lookupButton(confirmButtonType);

        styleDialogButton(cancelButton, "#95A5A6", "#7F8C8D", "#6A7B8C");
        styleDialogButton(confirmButton, "#380F17", "#5A1A29", "#2A0A11");

        // Afficher le dialogue et attendre la réponse
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == confirmButtonType) {
            System.out.println("Déconnexion...");
            Session.logout();

            Stage stage = (Stage) upcomingReservationsContainer.getScene().getWindow();
            stage.close();

            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
                Parent root = fxmlLoader.load();
                Stage loginStage = new Stage();
                loginStage.setTitle("Roomy - Connexion");
                loginStage.setScene(new Scene(root));
                loginStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        // Appliquer le style au dialogue
        DialogPane alertPane = alert.getDialogPane();
        alertPane.setStyle("-fx-background-color: white; " +
                "-fx-background-radius: 12px; " +
                "-fx-border-radius: 12px; " +
                "-fx-border-color: #E0D6C2; " +
                "-fx-border-width: 2px; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(56, 15, 23, 0.2), 20, 0, 0, 10);");

        // Styliser les boutons selon le type d'alerte
        Button okButton = (Button) alertPane.lookupButton(ButtonType.OK);

        switch (type) {
            case ERROR:
                styleDialogButton(okButton, "#e74c3c", "#c0392b", "#a93226"); // Rouge
                break;
            case WARNING:
                styleDialogButton(okButton, "#f39c12", "#d68910", "#b9770e"); // Orange
                break;
            case INFORMATION:
                styleDialogButton(okButton, "#3498db", "#2980b9", "#1f639e"); // Bleu
                break;
            default:
                styleDialogButton(okButton, "#8B4C4C", "#9A5A5A", "#7A3C3C"); // Marron par défaut
        }

        alert.showAndWait();
    }

    private void styleDialogButton(Button button, String normalColor, String hoverColor, String pressedColor) {
        button.setStyle("-fx-background-color: " + normalColor + "; " +
                "-fx-text-fill: white; " +
                "-fx-font-weight: 600; " +
                "-fx-font-size: 14px; " +
                "-fx-padding: 10px 25px; " +
                "-fx-background-radius: 6px; " +
                "-fx-border-radius: 6px; " +
                "-fx-cursor: hand;");

        button.setOnMouseEntered(e -> {
            button.setStyle("-fx-background-color: " + hoverColor + "; " +
                    "-fx-text-fill: white; " +
                    "-fx-font-weight: 600; " +
                    "-fx-font-size: 14px; " +
                    "-fx-padding: 10px 25px; " +
                    "-fx-background-radius: 6px; " +
                    "-fx-border-radius: 6px; " +
                    "-fx-cursor: hand;");
        });

        button.setOnMouseExited(e -> {
            button.setStyle("-fx-background-color: " + normalColor + "; " +
                    "-fx-text-fill: white; " +
                    "-fx-font-weight: 600; " +
                    "-fx-font-size: 14px; " +
                    "-fx-padding: 10px 25px; " +
                    "-fx-background-radius: 6px; " +
                    "-fx-border-radius: 6px; " +
                    "-fx-cursor: hand;");
        });

        button.setOnMousePressed(e -> {
            button.setStyle("-fx-background-color: " + pressedColor + "; " +
                    "-fx-text-fill: white; " +
                    "-fx-font-weight: 600; " +
                    "-fx-font-size: 14px; " +
                    "-fx-padding: 10px 25px; " +
                    "-fx-background-radius: 6px; " +
                    "-fx-border-radius: 6px; " +
                    "-fx-cursor: hand;");
        });
    }
}