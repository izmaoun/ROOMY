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
                Label confirmLabel = new Label(getStatutDisplay(h.getStatutVerification().name()));
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