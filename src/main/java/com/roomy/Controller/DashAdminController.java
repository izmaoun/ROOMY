package com.roomy.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.control.Separator;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;

import com.roomy.Dao.AdminDAO;
import com.roomy.Dao.HotelierDAO;
import com.roomy.Dao.ClientDAO;
import com.roomy.entities.Admin;
import com.roomy.entities.Hotelier;
import com.roomy.entities.Client;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority;
import javafx.scene.control.Separator;
import javafx.geometry.Pos;
import javafx.scene.text.Text;

public class DashAdminController {

    @FXML private Label lblAdminName;
    @FXML private ImageView logo;
    @FXML private Button btnLogout;
    @FXML private Button btnMenu;

    @FXML private Button btnValidation;
    @FXML private Button btnUsers;
    @FXML private Button btnStats;
    @FXML private VBox leftMenu;

    @FXML private StackPane centerStack;
    @FXML private AnchorPane validationPane;
    @FXML private AnchorPane usersPane;
    @FXML private AnchorPane statsPane;

    @FXML private FlowPane activeUsersFlow;
    @FXML private FlowPane blockedUsersFlow;

    @FXML private VBox validationList;
    @FXML private VBox activeUsersContainer;
    @FXML private VBox blockedUsersContainer;
    @FXML private VBox statsList;

    private AdminDAO adminDAO = new AdminDAO();
    private HotelierDAO hotelierDAO = new HotelierDAO();
    private ClientDAO clientDAO = new ClientDAO();
    private Admin currentAdmin;

    @FXML
    public void initialize() {
        // Appliquer le fond noir sur le centre avec padding réduit
        centerStack.setStyle("-fx-background-color: #000000; -fx-padding: 20;");
        setActiveButton(btnValidation);



        // charger logo si disponible
        try {
            Image img = new Image(getClass().getResourceAsStream("/com/roomy/images/logo.png"));
            logo.setImage(img);
        } catch (Exception e) {
            // ignore
        }

        // attaches des actions
        btnValidation.setOnAction(this::showValidation);
        btnUsers.setOnAction(this::showUsers);
        btnStats.setOnAction(this::showStats);
        btnLogout.setOnAction(this::handleLogout);
        btnMenu.setOnAction(this::toggleMenu);

        // par défaut montrer validation
        showValidation(null);

        // charger les éléments
        loadValidationList();

        centerStack.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null && newScene.getWindow() != null) {
                newScene.getWindow().widthProperty().addListener((o, oldVal, newVal) -> {
                    adjustLayoutForScreenSize();
                });
                newScene.getWindow().heightProperty().addListener((o, oldVal, newVal) -> {
                    adjustLayoutForScreenSize();
                });
            }
        });
    }

    private void adjustLayoutForScreenSize() {
        Stage stage = (Stage) centerStack.getScene().getWindow();

        // Taille minimale pour éviter la disparition des éléments
        stage.setMinWidth(1100);
        stage.setMinHeight(650);

        // Forcer le redimensionnement
        centerStack.getScene().getRoot().layout();
    }

    private void updateButtonStyles() {
        // Style par défaut (non actif)
        String inactiveStyle = "-fx-background-color: transparent; -fx-text-fill: #EFDFC5; -fx-font-size: 13px; -fx-background-radius: 12; -fx-cursor: hand;";

        // Style actif
        String activeStyle = "-fx-background-color: #A59090; -fx-text-fill: #380F17; -fx-font-size: 13px; -fx-font-weight: bold; -fx-background-radius: 12; -fx-cursor: hand;";

        // Réinitialiser tous les boutons
        btnValidation.setStyle(inactiveStyle);
        btnUsers.setStyle(inactiveStyle);
        btnStats.setStyle(inactiveStyle);
    }

    private void setActiveButton(Button activeButton) {
        updateButtonStyles();
        activeButton.setStyle("-fx-background-color: #A59090; -fx-text-fill: #380F17; -fx-font-size: 13px; -fx-font-weight: bold; -fx-background-radius: 12; -fx-cursor: hand;");
    }

    private void loadValidationList() {
        validationList.getChildren().clear();

        // D'abord ajouter le header
        HBox header = new HBox(10);
        header.setStyle("-fx-background-color: #2a2a2a; -fx-padding: 10 15; -fx-spacing: 10;");
        header.setAlignment(Pos.CENTER_LEFT);

        Label hHotel = new Label("Hôtel");
        hHotel.setStyle("-fx-text-fill: #d4d4d4; -fx-font-weight: bold; -fx-pref-width: 130; -fx-font-size: 12px;");

        Label hGerant = new Label("Gérant");
        hGerant.setStyle("-fx-text-fill: #d4d4d4; -fx-font-weight: bold; -fx-pref-width: 130; -fx-font-size: 12px;");

        Label hICE = new Label("ICE");
        hICE.setStyle("-fx-text-fill: #d4d4d4; -fx-font-weight: bold; -fx-pref-width: 100; -fx-font-size: 12px;");

        Label hVille = new Label("Ville");
        hVille.setStyle("-fx-text-fill: #d4d4d4; -fx-font-weight: bold; -fx-pref-width: 85; -fx-font-size: 12px;");

        Label hConfirm = new Label("Confirmation");
        hConfirm.setStyle("-fx-text-fill: #d4d4d4; -fx-font-weight: bold; -fx-pref-width: 95; -fx-font-size: 12px;");

        Label hActions = new Label("Actions");
        hActions.setStyle("-fx-text-fill: #d4d4d4; -fx-font-weight: bold; -fx-pref-width: 170; -fx-font-size: 12px;");

        header.getChildren().addAll(hHotel, hGerant, hICE, hVille, hConfirm, hActions);
        validationList.getChildren().add(header);

        try {
            java.util.List<Hotelier> list = hotelierDAO.findAll();

            if (list.isEmpty()) {
                Label none = new Label("Aucune demande");
                none.setStyle("-fx-text-fill: white; -fx-padding: 20; -fx-font-size: 13px;");
                validationList.getChildren().add(none);
                return;
            }

            int index = 0;
            for (Hotelier h : list) {
                String bgColor = (index % 2 == 0) ? "#1f1f1f" : "#252525";

                HBox row = new HBox(10);
                row.setStyle("-fx-background-color: " + bgColor + "; " +
                        "-fx-padding: 12 15; " +
                        "-fx-border-color: #333333; " +
                        "-fx-border-width: 0 0 0.5 0;");
                row.setAlignment(Pos.CENTER_LEFT);

                // Colonne Nom de l'Hôtel
                Label nameLabel = new Label(h.getNomEtablissement());
                nameLabel.setStyle("-fx-text-fill: white; -fx-pref-width: 130; -fx-font-size: 11px;");
                nameLabel.setWrapText(true);
                nameLabel.setMaxWidth(130);

                // Colonne Email du Gérant
                Label emailLabel = new Label(h.getEmailGerant());
                emailLabel.setStyle("-fx-text-fill: white; -fx-pref-width: 130; -fx-font-size: 11px;");
                emailLabel.setWrapText(true);
                emailLabel.setMaxWidth(130);

                // Colonne ICE
                Label iceLabel = new Label(h.getIce() != null ? h.getIce() : "N/A");
                iceLabel.setStyle("-fx-text-fill: white; -fx-pref-width: 100; -fx-font-size: 11px;");
                iceLabel.setMaxWidth(100);

                // Colonne Ville
                Label villeLabel = new Label(h.getVille());
                villeLabel.setStyle("-fx-text-fill: white; -fx-pref-width: 85; -fx-font-size: 11px;");
                villeLabel.setMaxWidth(85);

                // Colonne Confirmation
                Label confirmLabel = new Label(getStatutDisplay(h.getStatutVerification() != null ? h.getStatutVerification().name() : null));
                confirmLabel.setStyle("-fx-text-fill: white; -fx-pref-width: 95; -fx-font-size: 11px;");
                confirmLabel.setMaxWidth(95);

                // Colonne Actions (Boutons)
                HBox actionBox = new HBox(6);
                actionBox.setStyle("-fx-pref-width: 170;");
                actionBox.setAlignment(Pos.CENTER_LEFT);

                String statut = h.getStatutVerification() != null ? h.getStatutVerification().name().toLowerCase() : "en_attente";

                if (statut.equals("en_attente") || statut.equals("en attente")) {
                    Button acceptBtn = new Button("✓");
                    acceptBtn.setStyle("-fx-background-color: #4a9d5f; " +
                            "-fx-text-fill: white; " +
                            "-fx-background-radius: 10; " +
                            "-fx-padding: 4 10; " +
                            "-fx-font-size: 11px; " +
                            "-fx-cursor: hand;");
                    acceptBtn.setOnAction(ev -> {
                        boolean ok = hotelierDAO.updateStatutVerification(h.getIdHotelier(), "verifie");
                        if (ok) loadValidationList();
                    });

                    Button rejectBtn = new Button("✗");
                    rejectBtn.setStyle("-fx-background-color: #c74444; " +
                            "-fx-text-fill: white; " +
                            "-fx-background-radius: 10; " +
                            "-fx-padding: 4 10; " +
                            "-fx-font-size: 11px; " +
                            "-fx-cursor: hand;");
                    rejectBtn.setOnAction(ev -> {
                        boolean ok = hotelierDAO.updateStatutVerification(h.getIdHotelier(), "rejete");
                        if (ok) loadValidationList();
                    });

                    actionBox.getChildren().addAll(acceptBtn, rejectBtn);

                } else if (statut.equals("verifie") || statut.equals("accepte") || statut.equals("accepté")) {
                    Label acceptedLabel = new Label("✓ Accepté");
                    acceptedLabel.setStyle("-fx-background-color: #4a9d5f; " +
                            "-fx-text-fill: white; " +
                            "-fx-background-radius: 10; " +
                            "-fx-padding: 4 10; " +
                            "-fx-font-size: 10px;");
                    actionBox.getChildren().add(acceptedLabel);

                } else if (statut.equals("rejete") || statut.equals("refusé") || statut.equals("refuse")) {
                    Label rejectedLabel = new Label("✗ Refusé");
                    rejectedLabel.setStyle("-fx-background-color: #c74444; " +
                            "-fx-text-fill: white; " +
                            "-fx-background-radius: 10; " +
                            "-fx-padding: 4 10; " +
                            "-fx-font-size: 10px;");
                    actionBox.getChildren().add(rejectedLabel);
                }

                row.getChildren().addAll(nameLabel, emailLabel, iceLabel, villeLabel, confirmLabel, actionBox);
                validationList.getChildren().add(row);

                index++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Label err = new Label("Erreur lors du chargement");
            err.setStyle("-fx-text-fill: white; -fx-padding: 20; -fx-font-size: 13px;");
            validationList.getChildren().add(err);
        }
    }

    /**
     * Retourne l'affichage du statut pour la colonne Confirmation
     */
    private String getStatutDisplay(String statut) {
        if (statut == null) return "En attente";

        switch (statut.toLowerCase()) {
            case "verifie":
            case "accepte":
            case "accepté":
                return "Accepté";
            case "rejete":
            case "refuse":
            case "refusé":
                return "Refusé";
            case "en_attente":
            case "en attente":
            default:
                return "En attente";
        }
    }

    /**
     * Charge la liste des utilisateurs actifs et bloqués dans le dashboard admin
     */
    private void loadUsersList() {
        activeUsersFlow.getChildren().clear();
        blockedUsersFlow.getChildren().clear();

        java.util.List<Client> allClients = clientDAO.findAll();

        for (Client c : allClients) {
            // Design du Block (Carte)
            VBox card = new VBox(10);
            card.setAlignment(Pos.CENTER_LEFT);
            card.setPadding(new javafx.geometry.Insets(15));

            // Taille dynamique : prend la largeur de la colonne moins les marges
            card.prefWidthProperty().bind(activeUsersFlow.widthProperty().subtract(40));
            card.setMaxWidth(450); // Pour éviter que ce soit trop large sur grand écran

            card.setStyle("-fx-background-color: #1a1a1a; -fx-background-radius: 10; " +
                    "-fx-border-color: " + (c.isEstBloque() ? "#c74444" : "#4a9d5f") + "; -fx-border-width: 0 0 0 4;");

            // Infos
            Label name = new Label(c.getNom() + " " + c.getPrenom());
            name.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");

            Label email = new Label(c.getEmail());
            email.setStyle("-fx-text-fill: #777; -fx-font-size: 11px;");

            // Bouton stylisé
            Button actionBtn = new Button(c.isEstBloque() ? "Débloquer" : "Bloquer l'accès");
            actionBtn.setMaxWidth(Double.MAX_VALUE); // Prend toute la largeur du block
            actionBtn.setCursor(javafx.scene.Cursor.HAND);
            actionBtn.setStyle(c.isEstBloque() ?
                    "-fx-background-color: #4a9d5f; -fx-text-fill: white; -fx-background-radius: 5;" :
                    "-fx-background-color: #c74444; -fx-text-fill: white; -fx-background-radius: 5;");

            actionBtn.setOnAction(e -> handleUserAction(c));

            card.getChildren().addAll(name, email, actionBtn);

            // Envoi dans la bonne colonne (Gauche ou Droite)
            if (c.isEstBloque()) {
                blockedUsersFlow.getChildren().add(card);
            } else {
                activeUsersFlow.getChildren().add(card);
            }
        }
    }
    /**
     * Action sur utilisateur (bloquer/activer)
     */
    private void handleUserAction(Client c) {
        if (c.isEstBloque()) {
            c.setEstBloque(false);
        } else {
            c.setEstBloque(true);
        }
        clientDAO.updateBlockStatus(c.getIdClient(), c.isEstBloque());
        loadUsersList();
    }

    /**
     * Retourne le nombre de réservations d'un client
     */
    private int getReservationCount(Client c) {
        // TODO : requête SQL réelle
        return 3;
    }

    public void setAdmin(Admin admin) {
        this.currentAdmin = admin;
        if (admin != null) {
            lblAdminName.setText(admin.getUsername());
        }
    }

    @FXML
    private void showValidation(ActionEvent e) {
        setActiveButton(btnValidation);
        validationPane.setVisible(true);
        usersPane.setVisible(false);
        statsPane.setVisible(false);
    }

    @FXML
    private void showUsers(ActionEvent e) {
        setActiveButton(btnUsers);
        validationPane.setVisible(false);
        usersPane.setVisible(true);
        statsPane.setVisible(false);
        loadUsersList();
    }

    @FXML
    private void showStats(ActionEvent e) {
        setActiveButton(btnStats);
        validationPane.setVisible(false);
        usersPane.setVisible(false);
        statsPane.setVisible(true);
        loadStatistics();
    }

    /**
     * Charge et affiche les statistiques avec des graphiques
     */
    private void loadStatistics() {
        statsList.getChildren().clear();

        int currentYear = java.time.Year.now().getValue();

        // 1. Graphique circulaire : Nombre total d'utilisateurs vs hôteliers
        VBox totalCountBox = createTotalCountChart();
        statsList.getChildren().add(totalCountBox);

        // 2. Graphique en barres : Inscriptions par mois
        VBox monthlyChart = createMonthlyRegistrationsChart(currentYear);
        statsList.getChildren().add(monthlyChart);

        // 3. Répartition géographique des établissements
        VBox geographicChart = createGeographicDistributionChart();
        statsList.getChildren().add(geographicChart);
    }

    /**
     * Crée un graphique circulaire simple montrant le nombre total d'utilisateurs et d'hôteliers vérifiés
     */
    private VBox createTotalCountChart() {
        VBox container = new VBox(15);
        container.setStyle("-fx-background-color: #2a2a2a; -fx-padding: 20; -fx-background-radius: 10;");

        Label title = new Label("Nombre total d'utilisateurs et d'hôteliers vérifiés");
        title.setStyle("-fx-text-fill: #f2d6c6; -fx-font-size: 18px; -fx-font-weight: bold;");

        int nbClients = clientDAO.countAll();
        int nbHoteliers = hotelierDAO.countVerified(); // Utiliser countVerified() au lieu de countAll()
        int total = nbClients + nbHoteliers;

        // Créer une représentation visuelle simple avec des barres
        HBox statsBox = new HBox(30);
        statsBox.setStyle("-fx-alignment: center; -fx-padding: 20;");

        // Colonne Clients
        VBox clientBox = new VBox(10);
        clientBox.setStyle("-fx-alignment: center;");
        Label clientLabel = new Label("Utilisateurs");
        clientLabel.setStyle("-fx-text-fill: #4a9d5f; -fx-font-size: 16px; -fx-font-weight: bold;");
        Label clientCount = new Label(String.valueOf(nbClients));
        clientCount.setStyle("-fx-text-fill: white; -fx-font-size: 36px; -fx-font-weight: bold;");

        // Barre visuelle pour les clients
        HBox clientBar = new HBox();
        clientBar.setStyle("-fx-background-color: #4a9d5f; -fx-pref-height: 30;");
        double clientPercentage = total > 0 ? (nbClients * 300.0 / total) : 0;
        clientBar.setPrefWidth(clientPercentage);

        clientBox.getChildren().addAll(clientLabel, clientCount, clientBar);

        // Colonne Hôteliers
        VBox hotelierBox = new VBox(10);
        hotelierBox.setStyle("-fx-alignment: center;");
        Label hotelierLabel = new Label("Hôteliers vérifiés");
        hotelierLabel.setStyle("-fx-text-fill: #c74444; -fx-font-size: 16px; -fx-font-weight: bold;");
        Label hotelierCount = new Label(String.valueOf(nbHoteliers));
        hotelierCount.setStyle("-fx-text-fill: white; -fx-font-size: 36px; -fx-font-weight: bold;");

        // Barre visuelle pour les hôteliers
        HBox hotelierBar = new HBox();
        hotelierBar.setStyle("-fx-background-color: #c74444; -fx-pref-height: 30;");
        double hotelierPercentage = total > 0 ? (nbHoteliers * 300.0 / total) : 0;
        hotelierBar.setPrefWidth(hotelierPercentage);

        hotelierBox.getChildren().addAll(hotelierLabel, hotelierCount, hotelierBar);

        statsBox.getChildren().addAll(clientBox, hotelierBox);
        container.getChildren().addAll(title, statsBox);

        return container;
    }

    /**
     * Crée un graphique en barres montrant les inscriptions vérifiées par mois
     */
    private VBox createMonthlyRegistrationsChart(int year) {
        VBox container = new VBox(15);
        container.setStyle("-fx-background-color: #2a2a2a; -fx-padding: 20; -fx-background-radius: 10;");

        Label title = new Label("Inscriptions vérifiées par mois en " + year);
        title.setStyle("-fx-text-fill: #f2d6c6; -fx-font-size: 18px; -fx-font-weight: bold;");

        // Récupérer les données
        java.util.Map<Integer, Integer> clientsByMonth = clientDAO.countByMonthForYear(year);
        java.util.Map<Integer, Integer> hoteliersByMonth = hotelierDAO.countByMonthForYear(year);

        // Noms des mois
        String[] monthNames = {"Jan", "Fév", "Mar", "Avr", "Mai", "Jun",
                               "Jul", "Aoû", "Sep", "Oct", "Nov", "Déc"};

        // Trouver la valeur maximale pour la mise à l'échelle
        int maxValue = 1;
        for (int i = 1; i <= 12; i++) {
            int monthTotal = clientsByMonth.get(i) + hoteliersByMonth.get(i);
            if (monthTotal > maxValue) maxValue = monthTotal;
        }

        // Conteneur pour le graphique
        VBox chartBox = new VBox(5);
        chartBox.setStyle("-fx-padding: 20; -fx-alignment: bottom-left;");

        // Créer les barres pour chaque mois
        HBox barsContainer = new HBox(15);
        barsContainer.setStyle("-fx-alignment: bottom-left; -fx-pref-height: 300;");

        for (int month = 1; month <= 12; month++) {
            int clientsCount = clientsByMonth.get(month);
            int hoteliersCount = hoteliersByMonth.get(month);

            VBox monthColumn = new VBox(5);
            monthColumn.setStyle("-fx-alignment: bottom-center;");
            monthColumn.setMinWidth(60);

            // Stack de barres (clients + hôteliers)
            VBox barsStack = new VBox(0);
            barsStack.setStyle("-fx-alignment: bottom-center;");

            // Barre des hôteliers (en haut)
            if (hoteliersCount > 0) {
                VBox hotelierBar = new VBox();
                hotelierBar.setStyle("-fx-background-color: #c74444; -fx-background-radius: 5 5 0 0;");
                double hotelierHeight = (hoteliersCount * 200.0) / maxValue;
                hotelierBar.setPrefHeight(hotelierHeight);
                hotelierBar.setPrefWidth(50);

                Label hotelierLabel = new Label(String.valueOf(hoteliersCount));
                hotelierLabel.setStyle("-fx-text-fill: white; -fx-font-size: 10px; -fx-padding: 2;");
                hotelierBar.getChildren().add(hotelierLabel);
                hotelierBar.setStyle(hotelierBar.getStyle() + "-fx-alignment: center;");

                barsStack.getChildren().add(hotelierBar);
            }

            // Barre des clients (en bas)
            if (clientsCount > 0) {
                VBox clientBar = new VBox();
                String radius = hoteliersCount > 0 ? "0 0 5 5" : "5 5 5 5";
                clientBar.setStyle("-fx-background-color: #4a9d5f; -fx-background-radius: " + radius + ";");
                double clientHeight = (clientsCount * 200.0) / maxValue;
                clientBar.setPrefHeight(clientHeight);
                clientBar.setPrefWidth(50);

                Label clientLabel = new Label(String.valueOf(clientsCount));
                clientLabel.setStyle("-fx-text-fill: white; -fx-font-size: 10px; -fx-padding: 2;");
                clientBar.getChildren().add(clientLabel);
                clientBar.setStyle(clientBar.getStyle() + "-fx-alignment: center;");

                barsStack.getChildren().add(clientBar);
            }

            // Si aucune inscription ce mois
            if (clientsCount == 0 && hoteliersCount == 0) {
                VBox emptyBar = new VBox();
                emptyBar.setStyle("-fx-background-color: #444; -fx-background-radius: 5;");
                emptyBar.setPrefHeight(10);
                emptyBar.setPrefWidth(50);
                barsStack.getChildren().add(emptyBar);
            }

            // Nom du mois
            Label monthLabel = new Label(monthNames[month - 1]);
            monthLabel.setStyle("-fx-text-fill: #ccc; -fx-font-size: 12px; -fx-padding: 5 0 0 0;");

            monthColumn.getChildren().addAll(barsStack, monthLabel);
            barsContainer.getChildren().add(monthColumn);
        }

        // Légende
        HBox legend = new HBox(20);
        legend.setStyle("-fx-alignment: center; -fx-padding: 20 0 0 0;");

        HBox clientLegend = new HBox(8);
        clientLegend.setStyle("-fx-alignment: center-left;");
        HBox clientColor = new HBox();
        clientColor.setStyle("-fx-background-color: #4a9d5f; -fx-pref-width: 20; -fx-pref-height: 15; -fx-background-radius: 3;");
        Label clientLegendLabel = new Label("Utilisateurs");
        clientLegendLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        clientLegend.getChildren().addAll(clientColor, clientLegendLabel);

        HBox hotelierLegend = new HBox(8);
        hotelierLegend.setStyle("-fx-alignment: center-left;");
        HBox hotelierColor = new HBox();
        hotelierColor.setStyle("-fx-background-color: #c74444; -fx-pref-width: 20; -fx-pref-height: 15; -fx-background-radius: 3;");
        Label hotelierLegendLabel = new Label("Hôteliers vérifiés");
        hotelierLegendLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        hotelierLegend.getChildren().addAll(hotelierColor, hotelierLegendLabel);

        legend.getChildren().addAll(clientLegend, hotelierLegend);

        container.getChildren().addAll(title, barsContainer, legend);

        return container;
    }

    /**
     * Crée un graphique montrant la répartition géographique des établissements vérifiés
     */
    private VBox createGeographicDistributionChart() {
        VBox container = new VBox(15);
        container.setStyle("-fx-background-color: #2a2a2a; -fx-padding: 20; -fx-background-radius: 10;");

        Label title = new Label("Répartition géographique des établissements vérifiés");
        title.setStyle("-fx-text-fill: #f2d6c6; -fx-font-size: 18px; -fx-font-weight: bold;");

        // Récupérer les données
        java.util.Map<String, Integer> hoteliersByCity = hotelierDAO.countByCity();

        if (hoteliersByCity.isEmpty()) {
            Label noData = new Label("Aucune donnée disponible");
            noData.setStyle("-fx-text-fill: #ccc; -fx-font-size: 14px; -fx-padding: 20;");
            container.getChildren().addAll(title, noData);
            return container;
        }

        // Trouver le maximum pour la mise à l'échelle
        int maxCount = hoteliersByCity.values().stream().max(Integer::compare).orElse(1);

        // Conteneur pour les barres
        VBox barsBox = new VBox(12);
        barsBox.setStyle("-fx-padding: 20 0;");

        // Créer une barre pour chaque ville (limité aux 10 premières)
        int count = 0;
        for (java.util.Map.Entry<String, Integer> entry : hoteliersByCity.entrySet()) {
            if (count >= 10) break; // Limiter à 10 villes

            String city = entry.getKey();
            int total = entry.getValue();

            HBox cityRow = new HBox(15);
            cityRow.setStyle("-fx-alignment: center-left;");

            // Nom de la ville
            Label cityLabel = new Label(city);
            cityLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px; -fx-min-width: 120; -fx-font-weight: bold;");

            // Barre de progression
            HBox barContainer = new HBox();
            barContainer.setStyle("-fx-background-color: #444; -fx-pref-height: 25; -fx-background-radius: 5;");
            barContainer.setPrefWidth(400);
            HBox.setHgrow(barContainer, Priority.ALWAYS);

            HBox progressBar = new HBox();
            progressBar.setStyle("-fx-background-color: #c74444; -fx-pref-height: 25; -fx-background-radius: 5; -fx-alignment: center-left; -fx-padding: 0 8;");
            double percentage = (total * 100.0) / maxCount;
            double barWidth = (percentage * 400.0) / 100.0;
            progressBar.setPrefWidth(barWidth);
            progressBar.setMinWidth(barWidth);
            progressBar.setMaxWidth(barWidth);

            // Label du nombre
            Label countLabel = new Label(total + " établissement" + (total > 1 ? "s" : ""));
            countLabel.setStyle("-fx-text-fill: white; -fx-font-size: 11px; -fx-font-weight: bold;");

            if (barWidth > 100) {
                progressBar.getChildren().add(countLabel);
            } else {
                // Si la barre est trop petite, afficher le label à côté
                Label externalCount = new Label(String.valueOf(total));
                externalCount.setStyle("-fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 0 0 0 10;");
                cityRow.getChildren().addAll(cityLabel, barContainer, externalCount);
                barContainer.getChildren().add(progressBar);
                barsBox.getChildren().add(cityRow);
                count++;
                continue;
            }

            barContainer.getChildren().add(progressBar);
            cityRow.getChildren().addAll(cityLabel, barContainer);
            barsBox.getChildren().add(cityRow);
            count++;
        }

        container.getChildren().addAll(title, barsBox);

        return container;
    }

    /**
     * Toggle le menu de navigation latéral
     */
    @FXML
    private void toggleMenu(ActionEvent e) {
        boolean hidden = leftMenu.getTranslateX() < 0;

        if (hidden) {
            leftMenu.setTranslateX(0);        // montrer
        } else {
            leftMenu.setTranslateX(-260);     // cacher
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/welcome.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setMaximized(false);
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
