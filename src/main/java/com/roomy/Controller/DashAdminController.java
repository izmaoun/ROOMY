package com.roomy.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
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

    @FXML private VBox validationList;
    @FXML private VBox usersList;
    @FXML private VBox statsList;

    private AdminDAO adminDAO = new AdminDAO();
    private HotelierDAO hotelierDAO = new HotelierDAO();
    private ClientDAO clientDAO = new ClientDAO();
    private Admin currentAdmin;

    @FXML
    public void initialize() {
        // Appliquer le fond noir sur le centre
        centerStack.setStyle("-fx-background-color: #000000; -fx-padding: 40 60;");

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

    private void loadValidationList() {
        validationList.getChildren().clear();
        try {
            // Charger TOUS les hôteliers, pas seulement ceux en attente
            java.util.List<Hotelier> list = hotelierDAO.findAll();

            if (list.isEmpty()) {
                Label none = new Label("Aucune demande");
                none.setStyle("-fx-text-fill: white; -fx-padding: 20;");
                validationList.getChildren().add(none);
                return;
            }

            // Index pour alterner les couleurs de fond
            int index = 0;

            for (Hotelier h : list) {
                // Alterner entre deux couleurs de fond
                String bgColor = (index % 2 == 0) ? "#252525" : "#2a2a2a";

                HBox row = new HBox(10);
                row.setStyle("-fx-background-color: " + bgColor + "; " +
                        "-fx-padding: 15 20; " +
                        "-fx-border-color: #333333; " +
                        "-fx-border-width: 0 0 1 0;");
                row.setAlignment(Pos.CENTER_LEFT);
                row.setFillHeight(true);

                // Colonne Nom de l'Hôtel - Responsive
                Label nameLabel = new Label(h.getNomEtablissement());
                nameLabel.setStyle("-fx-text-fill: white; -fx-min-width: 120; -fx-pref-width: 140; -fx-font-size: 11px;");
                nameLabel.setWrapText(true);
                HBox.setHgrow(nameLabel, Priority.SOMETIMES);

                // Colonne Email du Gerant - Responsive
                Label emailLabel = new Label(h.getEmailGerant());
                emailLabel.setStyle("-fx-text-fill: white; -fx-min-width: 120; -fx-pref-width: 140; -fx-font-size: 11px;");
                emailLabel.setWrapText(true);
                HBox.setHgrow(emailLabel, Priority.SOMETIMES);

                // Colonne ICE - Responsive
                Label iceLabel = new Label(h.getIce() != null ? h.getIce() : "N/A");
                iceLabel.setStyle("-fx-text-fill: white; -fx-min-width: 90; -fx-pref-width: 110; -fx-font-size: 11px;");
                HBox.setHgrow(iceLabel, Priority.SOMETIMES);

                // Colonne Ville - Responsive
                Label villeLabel = new Label(h.getVille());
                villeLabel.setStyle("-fx-text-fill: white; -fx-min-width: 70; -fx-pref-width: 90; -fx-font-size: 11px;");
                HBox.setHgrow(villeLabel, Priority.SOMETIMES);

                // Colonne Confirmation - Responsive
                Label confirmLabel = new Label(getStatutDisplay(h.getStatutVerification() != null ? h.getStatutVerification().name() : null));
                confirmLabel.setStyle("-fx-text-fill: white; -fx-min-width: 80; -fx-pref-width: 100; -fx-font-size: 11px;");
                HBox.setHgrow(confirmLabel, Priority.SOMETIMES);

                // Colonne Status (Boutons) - Responsive
                HBox actionBox = new HBox(8);
                actionBox.setStyle("-fx-min-width: 150; -fx-pref-width: 180;");
                actionBox.setAlignment(Pos.CENTER_LEFT);
                HBox.setHgrow(actionBox, Priority.SOMETIMES);

                String statut = h.getStatutVerification() != null ? h.getStatutVerification().name().toLowerCase() : "en_attente";

                if (statut.equals("en_attente") || statut.equals("en attente")) {
                    // Afficher les deux boutons
                    Button acceptBtn = new Button("✓ Accepté");
                    acceptBtn.setStyle("-fx-background-color: #4a9d5f; " +
                            "-fx-text-fill: white; " +
                            "-fx-background-radius: 15; " +
                            "-fx-padding: 5 12; " +
                            "-fx-font-size: 10px; " +
                            "-fx-cursor: hand;");
                    acceptBtn.setOnAction(ev -> {
                        boolean ok = hotelierDAO.updateStatutVerification(h.getIdHotelier(), "verifie");
                        if (ok) {
                            loadValidationList();
                        }
                    });

                    Button rejectBtn = new Button("✗ Refuser");
                    rejectBtn.setStyle("-fx-background-color: #c74444; " +
                            "-fx-text-fill: white; " +
                            "-fx-background-radius: 15; " +
                            "-fx-padding: 5 12; " +
                            "-fx-font-size: 10px; " +
                            "-fx-cursor: hand;");
                    rejectBtn.setOnAction(ev -> {
                        boolean ok = hotelierDAO.updateStatutVerification(h.getIdHotelier(), "rejete");
                        if (ok) {
                            loadValidationList();
                        }
                    });

                    actionBox.getChildren().addAll(acceptBtn, rejectBtn);

                } else if (statut.equals("verifie") || statut.equals("accepte") || statut.equals("accepté")) {
                    // Afficher seulement le bouton Accepté
                    Button acceptedBtn = new Button("✓ Accepté");
                    acceptedBtn.setStyle("-fx-background-color: #4a9d5f; " +
                            "-fx-text-fill: white; " +
                            "-fx-background-radius: 15; " +
                            "-fx-padding: 5 12; " +
                            "-fx-font-size: 10px;");
                    acceptedBtn.setDisable(true);
                    actionBox.getChildren().add(acceptedBtn);

                } else if (statut.equals("rejete") || statut.equals("refusé") || statut.equals("refuse")) {
                    // Afficher seulement le bouton Refusé
                    Button rejectedBtn = new Button("✗ Refuser");
                    rejectedBtn.setStyle("-fx-background-color: #c74444; " +
                            "-fx-text-fill: white; " +
                            "-fx-background-radius: 15; " +
                            "-fx-padding: 5 12; " +
                            "-fx-font-size: 10px;");
                    rejectedBtn.setDisable(true);
                    actionBox.getChildren().add(rejectedBtn);
                }

                row.getChildren().addAll(nameLabel, emailLabel, iceLabel, villeLabel, confirmLabel, actionBox);
                validationList.getChildren().add(row);

                index++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Label err = new Label("Erreur lors du chargement");
            err.setStyle("-fx-text-fill: white; -fx-padding: 20;");
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
        usersList.getChildren().clear();
        java.util.List<Client> allClients = clientDAO.findAll();
        VBox activeUsers = new VBox(12);
        VBox blockedUsers = new VBox(12);

        Label activeTitle = new Label("liste des utilisateurs");
        activeTitle.setStyle("-fx-font-size: 18; -fx-text-fill: #bba89b; -fx-font-family: serif; -fx-padding: 8 0 8 0;");
        activeUsers.getChildren().add(activeTitle);

        Label blockedTitle = new Label("liste des utilisateurs bloqués");
        blockedTitle.setStyle("-fx-font-size: 18; -fx-text-fill: #bba89b; -fx-font-family: serif; -fx-padding: 8 0 8 0;");
        blockedUsers.getChildren().add(blockedTitle);

        for (Client c : allClients) {
            VBox card = new VBox(4);
            card.setStyle("-fx-background-color: #3a3a3a; -fx-padding: 12; -fx-border-radius: 10; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 8,0,0,2);");
            HBox header = new HBox(8);

            // Avatar utilisateur
            Image avatarImg;
            try {
                avatarImg = new Image(getClass().getResourceAsStream("/com/roomy/images/avatar.png"));
                if (avatarImg.isError() || avatarImg.getPixelReader() == null) {
                    throw new Exception();
                }
            } catch (Exception ex) {
                avatarImg = new Image("https://ui-avatars.com/api/?name=" + c.getNom() + "+" + c.getPrenom() + "&background=3a3a3a&color=fff&size=64");
            }
            ImageView avatar = new ImageView(avatarImg);
            avatar.setFitHeight(40); avatar.setFitWidth(40);
            VBox info = new VBox(2);
            Label name = new Label(c.getNom() + " " + c.getPrenom());
            name.setStyle(c.isEstBloque() ? "-fx-text-fill: #a44; -fx-font-weight: bold;" : "-fx-text-fill: #fff; -fx-font-weight: bold;");
            info.getChildren().add(name);
            header.getChildren().addAll(avatar, info);
            card.getChildren().add(header);
            Label nbRes = new Label("Nombre de réservation : " + getReservationCount(c));
            nbRes.setStyle("-fx-text-fill: #ccc;");
            card.getChildren().add(nbRes);
            Label statut = new Label(c.isEstBloque() ? "Suspendu" : "Active");
            statut.setStyle(c.isEstBloque() ? "-fx-text-fill: #a44;" : "-fx-text-fill: #3fa44a;");
            card.getChildren().add(statut);
            HBox actions = new HBox(8);
            Button actionBtn = new Button(c.isEstBloque() ? "Activer" : "Bloquer");
            actionBtn.setStyle(c.isEstBloque() ? "-fx-background-color: #e6f2d6; -fx-text-fill: #3fa44a;" : "-fx-background-color: #f2d6d6; -fx-text-fill: #a44;");
            actionBtn.setOnAction(e -> handleUserAction(c));
            actions.getChildren().add(actionBtn);
            card.getChildren().add(actions);
            if (c.isEstBloque()) blockedUsers.getChildren().add(card);
            else activeUsers.getChildren().add(card);
        }
        usersList.getChildren().addAll(activeUsers, blockedUsers);
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

    private void showValidation(ActionEvent e) {
        validationPane.setVisible(true);
        usersPane.setVisible(false);
        statsPane.setVisible(false);
    }

    private void showUsers(ActionEvent e) {
        validationPane.setVisible(false);
        usersPane.setVisible(true);
        statsPane.setVisible(false);
        loadUsersList();
    }

    private void showStats(ActionEvent e) {
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
    private void toggleMenu(ActionEvent e) {
        boolean isVisible = leftMenu.isVisible();

        if (!isVisible) {
            // Ouvrir le menu
            leftMenu.setVisible(true);
            leftMenu.setManaged(true);
            leftMenu.setPrefWidth(220);
        } else {
            // Fermer le menu
            leftMenu.setVisible(false);
            leftMenu.setManaged(false);
            leftMenu.setPrefWidth(0);
        }
    }

    private void adjustLayoutForScreenSize() {
        Stage stage = (Stage) centerStack.getScene().getWindow();

        // Mettre à jour la taille minimale pour éviter la disparition des boutons
        double minWidth = 1000;
        double minHeight = 600;

        stage.setMinWidth(minWidth);
        stage.setMinHeight(minHeight);

        // Forcer le redimensionnement du scroll pane si nécessaire
        centerStack.getScene().getRoot().layout();
    }

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
