package com.roomy.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
//import javafx.scene.Scene;
//import javafx.stage.Stage;
//import javafx.fxml.FXMLLoader;
//import com.roomy.service.AuthService;

import com.roomy.Dao.ClientDAO;
import com.roomy.Dao.HotelierDAO;
import com.roomy.entities.Client;
import com.roomy.entities.Hotelier;
import org.mindrot.jbcrypt.BCrypt;

    public class LoginController {

        @FXML private TextField txtEmail;
        @FXML private PasswordField txtPassword;
        @FXML private Label lblError;

        private ClientDAO clientDAO = new ClientDAO();
        private HotelierDAO hotelierDAO = new HotelierDAO();

        @FXML
        private void handleLogin() {
            String email = txtEmail.getText().trim();
            String password = txtPassword.getText();

            if (email.isEmpty() || password.isEmpty()) {
                lblError.setText("Veuillez remplir tous les champs");
                return;
            }

            // Essai comme Client
            Client client = clientDAO.findByEmail(email);
            if (client != null && BCrypt.checkpw(password, client.getPassword())) {
                lblError.setText("Connexion Client réussie !");
                // → ouvrir dash_client.fxml
                return;
            }

            // Essai comme Hôtelier
            Hotelier hotelier = hotelierDAO.findByEmail(email);
            if (hotelier != null && BCrypt.checkpw(password, hotelier.getPassword())) {
                lblError.setText("Connexion Hôtelier réussie !");
                // → ouvrir dash_hotelier.fxml
                return;
            }

            lblError.setText("Email ou mot de passe incorrect");
        }

        @FXML
        private void handleBack() {
            // Logique pour revenir à la page précédente
            System.out.println("Retour à la page précédente...");
        }
        @FXML
        private void handleForgotPassword() {
            // Logique pour gérer le mot de passe oublié
            System.out.println("Redirection vers la récupération de mot de passe...");
        }
        @FXML
        private void handleSignupNav() {
            // Logique pour gérer l'inscription
            System.out.println("Redirection vers la page d'inscription...");
        }
    }