package com.roomy.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import com.roomy.Dao.ClientDAO;
import com.roomy.entities.Client;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;

public class ForgotPasswordController {

    @FXML private TextField emailField;
    @FXML private TextField otpField;
    @FXML private PasswordField newPassField;
    @FXML private PasswordField confirmPassField;

    private ClientDAO clientDAO = new ClientDAO();
    private String generatedOtp;
    private Client foundClient;

    @FXML
    private void goBack(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setMaximized(false);
            stage.show();
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors du retour: " + e.getMessage());
        }
    }
    // -------------------------------------------------------
    // 1) ENVOYER OTP
    // -------------------------------------------------------
    @FXML
    public void handleOTPmdp() {
        String email = emailField.getText().trim();

        if (email.isEmpty()) {
            showAlert("Erreur", "Veuillez entrer votre email.");
            return;
        }

        // Vérifier si un client existe avec cet email
        foundClient = clientDAO.findByEmail(email);
        if (foundClient == null) {
            showAlert("Erreur", "Aucun compte trouvé avec cet email.");
            return;
        }

        try {
            generatedOtp = generateOTP();
            VerificationController.sendOTPmdp(email, generatedOtp);
            showAlert("Information", "Un code OTP a été envoyé à votre email.");
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de l'envoi du mail : " + e.getMessage());
        }
    }

    // -------------------------------------------------------
    // 2) RÉINITIALISATION DU MOT DE PASSE
    // -------------------------------------------------------
    @FXML
    public void handleResetPassword(ActionEvent event) throws IOException {

        // 2.1 Vérifier OTP
        String userOtp = otpField.getText().trim();
        if (userOtp.isEmpty()) {
            showAlert("Erreur", "Veuillez entrer le code OTP.");
            return;
        }

        if (generatedOtp == null || !userOtp.equals(generatedOtp)) {
            showAlert("Erreur", "Code OTP incorrect.");
            return;
        }

        // 2.2 Vérifier mots de passe
        String newPass = newPassField.getText().trim();
        String confirmPass = confirmPassField.getText().trim();

        if (newPass.isEmpty() || confirmPass.isEmpty()) {
            showAlert("Erreur", "Veuillez remplir les champs du mot de passe.");
            return;
        }

        if (!newPass.equals(confirmPass)) {
            showAlert("Erreur", "Les mots de passe ne correspondent pas.");
            return;
        }

        if (newPass.length() < 6) {
            showAlert("Erreur", "Le mot de passe doit comporter au moins 6 caractères.");
            return;
        }

        try {
            // 2.3 Hachage du nouveau mdp
            String email = emailField.getText().trim();
            String hashed = BCrypt.hashpw(newPass, BCrypt.gensalt());

            // 2.4 Mise à jour dans la base
            boolean updated = clientDAO.updatePassword(email, hashed);


            if (updated) {
                showAlert("Succès", "Mot de passe réinitialisé avec succès.");
                clearFields();
            } else {
                showAlert("Erreur", "Impossible de mettre à jour le mot de passe.");
            }

        } catch (Exception e) {
            showAlert("Erreur", "Erreur : " + e.getMessage());
        }
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        return;
    }
    @FXML
    private void handleBackToLogin(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // -------------------------------------------------------
    // MÉTHODES UTILITAIRES
    // -------------------------------------------------------
    private String generateOTP() {
        int otp = (int) (Math.random() * 900000) + 100000;
        return String.valueOf(otp);
    }

    private void clearFields() {
        emailField.clear();
        otpField.clear();
        newPassField.clear();
        confirmPassField.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
