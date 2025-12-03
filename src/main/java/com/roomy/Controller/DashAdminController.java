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
        centerStack.setStyle("-fx-background-color: #181818; -fx-padding: 24;");

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

        // par défaut montrer validation
        showValidation(null);

        // charger les éléments
        loadValidationList();
    }

    private void loadValidationList() {
        validationList.getChildren().clear();
        try {
            java.util.List<Hotelier> list = hotelierDAO.findAllEnAttente();
            if (list.isEmpty()) {
                Label none = new Label("Aucune demande en attente");
                none.setStyle("-fx-text-fill: white;");
                validationList.getChildren().add(none);
                return;
            }

            for (Hotelier h : list) {
                HBox row = new HBox(12);
                row.setStyle("-fx-background-color: #232323; -fx-padding: 12; -fx-border-radius: 8; -fx-background-radius: 8;");
                row.setAlignment(Pos.CENTER_LEFT);

                VBox info = new VBox(4);
                Label name = new Label(h.getNomEtablissement());
                name.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
                Label email = new Label(h.getEmailGerant());
                email.setStyle("-fx-text-fill: #cccccc;");
                Label ville = new Label(h.getVille());
                ville.setStyle("-fx-text-fill: #cccccc;");
                info.getChildren().addAll(name, email, ville);

                VBox actions = new VBox(6);
                Button accept = new Button("Accepter");
                accept.setStyle("-fx-background-color: #3bbf66; -fx-text-fill: white;");
                Button reject = new Button("Refuser");
                reject.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white;");

                accept.setOnAction(ev -> {
                    boolean ok = hotelierDAO.updateStatutVerification(h.getIdHotelier(), "verifie");
                    if (ok) {
                        loadValidationList();
                    }
                });

                reject.setOnAction(ev -> {
                    boolean ok = hotelierDAO.updateStatutVerification(h.getIdHotelier(), "rejete");
                    if (ok) {
                        loadValidationList();
                    }
                });

                actions.getChildren().addAll(accept, reject);

                row.getChildren().addAll(info, new Separator(), actions);
                validationList.getChildren().add(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Label err = new Label("Erreur lors du chargement");
            err.setStyle("-fx-text-fill: white;");
            validationList.getChildren().add(err);
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

            // Avatar utilisateur : si l'image n'existe pas, utiliser une icône par défaut
            Image avatarImg;
            try {
                avatarImg = new Image(getClass().getResourceAsStream("/com/roomy/images/avatar.png"));
                if (avatarImg.isError() || avatarImg.getPixelReader() == null) {
                    throw new Exception();
                }
            } catch (Exception ex) {
                // Utiliser une image JavaFX par défaut si le fichier est absent
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
     * Retourne le nombre de réservations d'un client (à implémenter)
     */
    private int getReservationCount(Client c) {
        // TODO : requête SQL réelle
        return 3;
    }

    public void setAdmin(Admin admin) {
        this.currentAdmin = admin;
        if (admin != null) {
            lblAdminName.setText("Admin\n" + admin.getUsername());
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
