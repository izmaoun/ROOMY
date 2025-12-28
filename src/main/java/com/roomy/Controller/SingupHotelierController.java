package com.roomy.Controller;

import com.roomy.Dao.HotelierDAO;
import com.roomy.entities.Hotelier;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import org.mindrot.jbcrypt.BCrypt;
import javafx.scene.control.Alert;



public class SingupHotelierController {
      private HotelierDAO hotelierDAO = new HotelierDAO();

        @FXML private TextField nomEtablissementField;
        @FXML private TextField nomGerantField;
        @FXML private TextField prenomGerantField;
        @FXML private TextField villeField;
        @FXML private TextField emailField;
        @FXML private TextField telephoneField;
        @FXML private PasswordField passwordField;
        @FXML private PasswordField confirmPasswordField;
        @FXML private TextField iceField;

        @FXML
        private void goBack(ActionEvent event) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/fxml/welcome.fxml"));
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("ROOMY - Choix du type de compte");
                stage.setMaximized(false);
                stage.show();
            } catch (Exception e) {
                showAlert("Erreur", "Erreur lors du retour: " + e.getMessage());
            }
        }

        @FXML
        private void handleInscription(ActionEvent event) {
            String nomEtablissement = nomEtablissementField.getText().trim();
            String nomGerant = nomGerantField.getText().trim();
            String prenomGerant = prenomGerantField.getText().trim();
            String ville = villeField.getText().trim();
            String email = emailField.getText().trim();
            String telephone = telephoneField.getText().trim();
            String password = passwordField.getText();
            String confirmPassword = confirmPasswordField.getText();
            String ice = iceField.getText().trim();

            // Validation
            if (!validateInputs(nomEtablissement, nomGerant, prenomGerant, ville,
                    email, telephone, password, confirmPassword, ice)) {
                return;
            }

            // Vérifications
            if (this.hotelierDAO.findByEmail(email) != null) {
                showAlert("Erreur", "Cet email est déjà utilisé.");
                return;
            }

            if (this.hotelierDAO.findByIce(ice) != null) {
                showAlert("Erreur", "Cet ICE est déjà utilisé.");
                return;
            }

            try {
                // Hasher le mot de passe
                String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

                Hotelier hotelier = new Hotelier(nomEtablissement, nomGerant, prenomGerant,
                        ville, email, telephone, hashedPassword, ice);
                boolean res = this.hotelierDAO.signup(hotelier);

                if (res) {
                    showAlert("Succès", "Inscription réussie ! Votre compte est en attente de vérification.");
                    clearFields();
                    Parent root = FXMLLoader.load(getClass().getResource("/fxml/en_attente_verification.fxml"));
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.show();
                } else {
                    showAlert("Erreur", "Échec de l'inscription.");
                }
            } catch (Exception e) {
                showAlert("Erreur", "Erreur lors de l'inscription: " + e.getMessage());
            }
        }

        @FXML
        private void goToLogin(ActionEvent event) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root, 800, 600));
                stage.show();
            } catch (Exception e) {
                showAlert("Erreur", "Erreur lors de la navigation: " + e.getMessage());
            }
        }

        private boolean validateInputs(String nomEtablissement, String nomGerant, String prenomGerant,
                                       String ville, String email, String telephone,
                                       String password, String confirmPassword, String ice) {
            // Vérification des champs vides
            if (nomEtablissement.isEmpty() || nomGerant.isEmpty() || prenomGerant.isEmpty() ||
                    ville.isEmpty() || email.isEmpty() || telephone.isEmpty() ||
                    password.isEmpty() || ice.isEmpty()) {
                showAlert("Erreur", "Veuillez remplir tous les champs.");
                return false;
            }

            if (!password.equals(confirmPassword)) {
                showAlert("Erreur", "Les mots de passe ne correspondent pas.");
                return false;
            }

            if (password.length() < 6) {
                showAlert("Erreur", "Le mot de passe doit contenir au moins 6 caractères.");
                return false;
            }

            if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                showAlert("Erreur", "Format d'email invalide.");
                return false;
            }

            if (!ice.matches("^[0-9]{15}$")) {
                showAlert("Erreur", "L'ICE doit contenir 15 chiffres.");
                return false;
            }

            return true;
        }

        private void clearFields() {
            nomEtablissementField.clear();
            nomGerantField.clear();
            prenomGerantField.clear();
            villeField.clear();
            emailField.clear();
            telephoneField.clear();
            passwordField.clear();
            confirmPasswordField.clear();
            iceField.clear();
        }

        private void showAlert(String title, String message) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        }
    }