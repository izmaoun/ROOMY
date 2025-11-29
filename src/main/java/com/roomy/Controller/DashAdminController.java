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
import com.roomy.entities.Admin;
import com.roomy.entities.Hotelier;

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
    private Admin currentAdmin;

    @FXML
    public void initialize() {
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
