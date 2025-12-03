package com.roomy.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;

import com.roomy.Dao.ClientDAO;
import com.roomy.Dao.HotelierDAO;
import com.roomy.Dao.AdminDAO;
import com.roomy.entities.Client;
import com.roomy.entities.Hotelier;
import com.roomy.entities.Admin;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;

public class LoginController {
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtPassword;
    @FXML private Label lblError;

    private final ClientDAO clientDAO = new ClientDAO();
    private final HotelierDAO hotelierDAO = new HotelierDAO();
    private final AdminDAO adminDAO = new AdminDAO();

    @FXML
    private void handleLogin(ActionEvent event) {
        String email = txtEmail.getText().trim();
        String password = txtPassword.getText();

        // Réinitialiser le message d'erreur
        lblError.setText("");

        if (email.isEmpty() || password.isEmpty()) {
            lblError.setText("Veuillez remplir tous les champs");
            return;
        }

        try {
            // Essai comme Client
            Client client = clientDAO.findByEmail(email);
            if (client != null && BCrypt.checkpw(password, client.getPassword())) {
                if (client.isEstBloque()) {
                    lblError.setText("Votre compte est bloqué. Contactez l'administration.");
                    return;
                }
                showAlert("Succès", "Connexion Client réussie !");
                openDashboard(event, "dash_client.fxml", "Client");
                return;
            }

            // Essai comme Hôtelier
            Hotelier hotelier = hotelierDAO.findByEmail(email);
            if (hotelier != null && BCrypt.checkpw(password, hotelier.getPassword())) {
                if ("rejete".equals(hotelier.getStatutVerification())) {
                    lblError.setText("Votre compte a été rejeté. Contactez l'administration.");
                    return;
                }
                if ("en_attente".equals(hotelier.getStatutVerification())) {
                    openDashboard(event, "en_attente_verification.fxml", "En attente");
                    return;
                }
                showAlert("Succès", "Connexion Hôtelier réussie !");
                openDashboard(event, "dash_hotelier.fxml", "Hotelier");
                return;
            }

            // Essai comme Administrateur (par username puis email)
            Admin admin = adminDAO.findByUsername(email); // l'utilisateur peut entrer son username
            if (admin == null) {
                admin = adminDAO.findByEmail(email); // ou son email
            }
            if (admin != null && BCrypt.checkpw(password, admin.getPassword())) {
                showAlert("Succès", "Connexion Admin réussie !");

                // Charger le dashboard admin et passer l'objet admin au contrôleur
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dash_admin.fxml"));
                    Parent root = loader.load();

                    // Récupérer le contrôleur et lui passer l'objet Admin
                    DashAdminController controller = loader.getController();
                    controller.setAdmin(admin);

                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.setMaximized(true);
                    stage.setTitle("Dashboard Admin");
                    stage.show();
                } catch (IOException e) {
                    logError(e, "Erreur lors du chargement du fichier FXML dash_admin.fxml");
                    lblError.setText("Erreur lors du chargement du fichier FXML dash_admin.fxml");
                } catch (Exception e) {
                    logError(e, "Erreur inattendue lors de l'ouverture du tableau de bord admin");
                    lblError.setText("Erreur inattendue lors de l'ouverture du tableau de bord admin");
                }

                return;
            }

            lblError.setText("Email ou mot de passe incorrect");

        } catch (Exception e) {
            lblError.setText("Erreur lors de la connexion");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/welcome.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setMaximized(false);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleForgotPassword(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/forgot_password.fxml"));
            // Récupérer la fenêtre actuelle
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            // Remplacer la scène
            stage.setTitle("mot de passe oublié");
            stage.setScene(new Scene(root));

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }    }

    @FXML
    private void handleSignupNav(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/welcome.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setMaximized(false);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openDashboard(ActionEvent event, String fxmlFile, String userType) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.setTitle("Dashboard " + userType);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void logError(Exception e, String message) {
        System.err.println(message);
        e.printStackTrace();
    }
}