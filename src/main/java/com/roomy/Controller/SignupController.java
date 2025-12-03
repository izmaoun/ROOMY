package com.roomy.Controller;

import com.roomy.Dao.ClientDAO;
import com.roomy.entities.Client;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import org.mindrot.jbcrypt.BCrypt;

import static com.roomy.Controller.VerificationController.sendOtpEmail;

public class SignupController {
    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField emailField;
    @FXML private TextField telephoneField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;

    private ClientDAO clientDAO = new ClientDAO();

    @FXML
    private void goBack(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/welcome.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setMaximized(false);
            stage.show();
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors du retour: " + e.getMessage());
        }
    }

    @FXML
    private void handleInscription(ActionEvent event) {
        String nom = nomField.getText().trim();
        String prenom = prenomField.getText().trim();
        String email = emailField.getText().trim();
        String telephone = telephoneField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Validation
        if (!validateInputs(nom, prenom, email, telephone, password, confirmPassword)) {
            return;
        }

        // Vérifier si l'email existe déjà
        if (this.clientDAO.findByEmail(email) != null) {
            showAlert("Erreur", "Cet email est déjà utilisé.");
            return;
        }
        String otp = generateOTP();
        try{
            sendOtpEmail(email, otp);

        }catch(Exception e) {
            showAlert("Erreur", "Erreur lors de l'envvoie du code OTP: " + e.getMessage());
            return;
        }
        //entrer OTP par l'utilisateur
        TextInputDialog otpDialog = new TextInputDialog();
        otpDialog.setTitle("Vérification OTP");
        otpDialog.setHeaderText("Un code OTP a été envoyé à votre email.");
        otpDialog.setContentText("Veuillez entrer le code OTP :");

        String userInputOtp = otpDialog.showAndWait().orElse("");
        if (!userInputOtp.equals(otp)) {
            showAlert("Erreur", "Code OTP incorrect.");
            return;
        }

        try {
            // Hasher le mot de passe
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

            Client client = new Client(nom, prenom, email, telephone, hashedPassword);
            boolean res = this.clientDAO.signup(client);

            if (res) {
                showAlert("Succès", "Inscription réussie !");
                clearFields();
                goToLogin(event);
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

    private boolean validateInputs(String nom, String prenom, String email, String telephone,
                                   String password, String confirmPassword) {
        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() ||
                telephone.isEmpty() || password.isEmpty()) {
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

        return true;
    }

    private void clearFields() {
        nomField.clear();
        prenomField.clear();
        emailField.clear();
        telephoneField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private static String generateOTP(){
        int OTP = (int)(Math.random() * 900000) + 100000;
        return String.valueOf(OTP);
    }

}