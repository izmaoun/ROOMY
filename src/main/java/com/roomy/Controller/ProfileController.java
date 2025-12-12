package com.roomy.Controller;

import com.roomy.Dao.ClientDAO;
import com.roomy.Dao.DBConnection;
import com.roomy.entities.Client;
import com.roomy.service.Session;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

public class ProfileController {

    @FXML private Label welcomeLabel;
    @FXML private Label emailHeaderLabel;
    @FXML private Label nomLabel;
    @FXML private Label prenomLabel;
    @FXML private Label emailLabel;
    @FXML private Label telephoneLabel;

    private ClientDAO clientDAO = new ClientDAO();

    @FXML
    public void initialize() {
        loadClientData();
    }

    private void loadClientData() {
        Client client = Session.getCurrentClient();
        if (client == null) return;

        // En-tête principal
        welcomeLabel.setText("Bonjour " + client.getPrenom());
        emailHeaderLabel.setText(client.getEmail());

        // Tableau profil
        nomLabel.setText(client.getNom());
        prenomLabel.setText(client.getPrenom());
        emailLabel.setText(client.getEmail());

        String tel = client.getTelephone();
        if (tel != null && tel.length() > 4) {
            telephoneLabel.setText(tel.substring(0, 2) + "*****" + tel.substring(tel.length() - 2));
        } else {
            telephoneLabel.setText(tel != null ? tel : "Non renseigné");
        }
    }


    @FXML
    private void goToAccueil() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/welcome.fxml"));
            Parent root = fxmlLoader.load();

            Stage currentStage = (Stage) welcomeLabel.getScene().getWindow();
            currentStage.setScene(new Scene(root, 960, 600));
            currentStage.setTitle("Roomy - Accueil");
            currentStage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir l'accueil", Alert.AlertType.ERROR);
        }
    }


    @FXML
    private void goToReservations() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/client_reservations.fxml"));
            Parent root = fxmlLoader.load();

            Stage currentStage = (Stage) welcomeLabel.getScene().getWindow();
            currentStage.setScene(new Scene(root, 960, 600));
            currentStage.setTitle("Roomy - Mes réservations");
            currentStage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir les réservations", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void logout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Déconnexion");
        alert.setHeaderText(null);
        alert.setContentText("Voulez-vous vraiment vous déconnecter ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.out.println("Déconnexion...");
            Session.logout();

            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.close();

            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
                Parent root = fxmlLoader.load();
                Stage loginStage = new Stage();
                loginStage.setTitle("Roomy - Connexion");
                loginStage.setScene(new Scene(root));
                loginStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleModifierNom() {
        Client client = Session.getCurrentClient();
        if (client == null) return;

        showStyledTextInputDialog("Modifier le nom", "Nouveau nom:", client.getNom(), "Nom");
    }

    @FXML
    private void handleModifierPrenom() {
        Client client = Session.getCurrentClient();
        if (client == null) return;

        showStyledTextInputDialog("Modifier le prénom", "Nouveau prénom:", client.getPrenom(), "Prénom");
    }

    @FXML
    private void handleModifierEmail() {
        Client client = Session.getCurrentClient();
        if (client == null) return;

        showStyledTextInputDialog("Modifier l'email", "Nouvel email:", client.getEmail(), "Email");
    }

    @FXML
    private void handleModifierTelephone() {
        Client client = Session.getCurrentClient();
        if (client == null) return;

        showStyledTextInputDialog("Modifier le téléphone", "Nouveau numéro:", client.getTelephone(), "Téléphone");
    }

    // Méthode pour afficher un dialogue de saisie de texte stylisé
    private void showStyledTextInputDialog(String title, String contentText, String defaultValue, String fieldType) {
        // Créer un dialogue personnalisé
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle(title);

        // Créer le contenu personnalisé
        VBox content = new VBox(15);
        content.setPadding(new Insets(20, 25, 20, 25));
        content.setStyle("-fx-background-color: white;");

        // En-tête du dialogue
        Label headerLabel = new Label(title);
        headerLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #380F17;");

        // Instruction
        Label instructionLabel = new Label(contentText);
        instructionLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #555555;");

        // Champ de saisie
        TextField valueField = new TextField(defaultValue);
        valueField.setStyle("-fx-background-color: white; " +
                "-fx-border-color: #D4C8B0; " +
                "-fx-border-width: 2px; " +
                "-fx-border-radius: 6px; " +
                "-fx-background-radius: 6px; " +
                "-fx-padding: 10px 12px; " +
                "-fx-font-size: 14px; " +
                "-fx-text-fill: #333333; " +
                "-fx-pref-width: 300px;");

        valueField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                valueField.setStyle("-fx-background-color: #FFFEFC; " +
                        "-fx-border-color: #8B4C4C; " +
                        "-fx-border-width: 2px; " +
                        "-fx-border-radius: 6px; " +
                        "-fx-background-radius: 6px; " +
                        "-fx-padding: 10px 12px; " +
                        "-fx-font-size: 14px; " +
                        "-fx-text-fill: #333333; " +
                        "-fx-pref-width: 300px;");
            } else {
                valueField.setStyle("-fx-background-color: white; " +
                        "-fx-border-color: #D4C8B0; " +
                        "-fx-border-width: 2px; " +
                        "-fx-border-radius: 6px; " +
                        "-fx-background-radius: 6px; " +
                        "-fx-padding: 10px 12px; " +
                        "-fx-font-size: 14px; " +
                        "-fx-text-fill: #333333; " +
                        "-fx-pref-width: 300px;");
            }
        });

        content.getChildren().addAll(headerLabel, instructionLabel, valueField);
        dialog.getDialogPane().setContent(content);

        // Ajouter les boutons
        ButtonType saveButtonType = new ButtonType("Enregistrer", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(cancelButtonType, saveButtonType);

        // Styliser le DialogPane
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.setStyle("-fx-background-color: white; " +
                "-fx-background-radius: 12px; " +
                "-fx-border-radius: 12px; " +
                "-fx-border-color: #E0D6C2; " +
                "-fx-border-width: 2px; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(56, 15, 23, 0.2), 20, 0, 0, 10);");

        // Styliser les boutons
        Button saveButton = (Button) dialogPane.lookupButton(saveButtonType);
        Button cancelButton = (Button) dialogPane.lookupButton(cancelButtonType);

        // Style pour le bouton Enregistrer
        styleDialogButton(saveButton, "#380F17", "#5A1A29", "#2A0A11");

        // Style pour le bouton Annuler
        styleDialogButton(cancelButton, "#95A5A6", "#7F8C8D", "#6A7B8C");

        // Définir le résultat du dialogue
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                return valueField.getText();
            }
            return null;
        });

        // Afficher le dialogue et traiter le résultat
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(newValue -> {
            if (!newValue.trim().isEmpty()) {
                processFieldUpdate(fieldType, newValue.trim());
            }
        });
    }

    // Méthode pour styliser les boutons du dialogue
    private void styleDialogButton(Button button, String normalColor, String hoverColor, String pressedColor) {
        button.setStyle("-fx-background-color: " + normalColor + "; " +
                "-fx-text-fill: white; " +
                "-fx-font-weight: 600; " +
                "-fx-font-size: 14px; " +
                "-fx-padding: 10px 25px; " +
                "-fx-background-radius: 6px; " +
                "-fx-border-radius: 6px; " +
                "-fx-cursor: hand;");

        button.setOnMouseEntered(e -> {
            button.setStyle("-fx-background-color: " + hoverColor + "; " +
                    "-fx-text-fill: white; " +
                    "-fx-font-weight: 600; " +
                    "-fx-font-size: 14px; " +
                    "-fx-padding: 10px 25px; " +
                    "-fx-background-radius: 6px; " +
                    "-fx-border-radius: 6px; " +
                    "-fx-cursor: hand;");
        });

        button.setOnMouseExited(e -> {
            button.setStyle("-fx-background-color: " + normalColor + "; " +
                    "-fx-text-fill: white; " +
                    "-fx-font-weight: 600; " +
                    "-fx-font-size: 14px; " +
                    "-fx-padding: 10px 25px; " +
                    "-fx-background-radius: 6px; " +
                    "-fx-border-radius: 6px; " +
                    "-fx-cursor: hand;");
        });

        button.setOnMousePressed(e -> {
            button.setStyle("-fx-background-color: " + pressedColor + "; " +
                    "-fx-text-fill: white; " +
                    "-fx-font-weight: 600; " +
                    "-fx-font-size: 14px; " +
                    "-fx-padding: 10px 25px; " +
                    "-fx-background-radius: 6px; " +
                    "-fx-border-radius: 6px; " +
                    "-fx-cursor: hand;");
        });
    }

    // Méthode pour traiter la mise à jour d'un champ
    private void processFieldUpdate(String fieldType, String newValue) {
        Client client = Session.getCurrentClient();
        if (client == null) return;

        switch (fieldType) {
            case "Nom":
                client.setNom(newValue);
                updateClient(client);
                break;

            case "Prénom":
                client.setPrenom(newValue);
                updateClient(client);
                break;

            case "Email":
                if (!newValue.equals(client.getEmail())) {
                    // Vérifier si l'email est déjà utilisé
                    Client existing = clientDAO.findByEmail(newValue);

                    if (existing != null && existing.getIdClient() != client.getIdClient()) {
                        showAlert("Erreur", "Cet email est déjà utilisé par un autre compte.", Alert.AlertType.ERROR);
                    } else {
                        // Demander confirmation pour l'email
                        showConfirmationDialog("Confirmer la modification",
                                "Êtes-vous sûr de vouloir changer votre email de " + client.getEmail() + " à " + newValue + " ?",
                                () -> {
                                    String oldEmail = client.getEmail();
                                    client.setEmail(newValue);
                                    boolean success = updateEmailInDatabase(client.getIdClient(), newValue);

                                    if (success) {
                                        Session.setCurrentClient(client);
                                        loadClientData();
                                        showAlert("Succès",
                                                "Email modifié avec succès!\nAncien: " + oldEmail + "\nNouveau: " + newValue,
                                                Alert.AlertType.INFORMATION);
                                    } else {
                                        client.setEmail(oldEmail); // Revert
                                        showAlert("Erreur", "Impossible de modifier l'email.", Alert.AlertType.ERROR);
                                    }
                                });
                    }
                }
                break;

            case "Téléphone":
                client.setTelephone(newValue);
                updateClient(client);
                break;
        }
    }

    @FXML
    private void handleModifierPassword() {
        Client client = Session.getCurrentClient();
        if (client == null) return;

        // Créer une boîte de dialogue personnalisée pour le mot de passe
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Modifier le mot de passe");

        // Créer le contenu personnalisé
        VBox content = new VBox(15);
        content.setPadding(new Insets(20, 25, 20, 25));
        content.setStyle("-fx-background-color: white;");

        // En-tête du dialogue
        Label headerLabel = new Label("Modifier le mot de passe");
        headerLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #380F17;");

        // Instruction
        Label instructionLabel = new Label("Remplissez les champs ci-dessous pour changer votre mot de passe:");
        instructionLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #555555;");

        // Champs de saisie
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 0, 0, 0));

        PasswordField oldPassword = new PasswordField();
        oldPassword.setPromptText("Ancien mot de passe");
        styleTextField(oldPassword);

        PasswordField newPassword = new PasswordField();
        newPassword.setPromptText("Nouveau mot de passe");
        styleTextField(newPassword);

        PasswordField confirmPassword = new PasswordField();
        confirmPassword.setPromptText("Confirmer le nouveau mot de passe");
        styleTextField(confirmPassword);

        grid.add(new Label("Ancien mot de passe:"), 0, 0);
        grid.add(oldPassword, 1, 0);
        grid.add(new Label("Nouveau mot de passe:"), 0, 1);
        grid.add(newPassword, 1, 1);
        grid.add(new Label("Confirmation:"), 0, 2);
        grid.add(confirmPassword, 1, 2);

        content.getChildren().addAll(headerLabel, instructionLabel, grid);
        dialog.getDialogPane().setContent(content);

        // Ajouter les boutons
        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(cancelButtonType, okButtonType);

        // Styliser le DialogPane
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.setStyle("-fx-background-color: white; " +
                "-fx-background-radius: 12px; " +
                "-fx-border-radius: 12px; " +
                "-fx-border-color: #E0D6C2; " +
                "-fx-border-width: 2px; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(56, 15, 23, 0.2), 20, 0, 0, 10);");

        // Styliser les boutons
        Button okButton = (Button) dialogPane.lookupButton(okButtonType);
        Button cancelButton = (Button) dialogPane.lookupButton(cancelButtonType);

        styleDialogButton(okButton, "#380F17", "#5A1A29", "#2A0A11");
        styleDialogButton(cancelButton, "#95A5A6", "#7F8C8D", "#6A7B8C");

        // Convertir le résultat
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                return new Pair<>(newPassword.getText(), confirmPassword.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(passwords -> {
            String newPass = passwords.getKey();
            String confirmPass = passwords.getValue();

            // Vérifications
            if (newPass.isEmpty() || confirmPass.isEmpty()) {
                showAlert("Erreur", "Tous les champs sont requis.", Alert.AlertType.ERROR);
            } else if (!newPass.equals(confirmPass)) {
                showAlert("Erreur", "Les mots de passe ne correspondent pas.", Alert.AlertType.ERROR);
            } else if (newPass.length() < 6) {
                showAlert("Erreur", "Le mot de passe doit contenir au moins 6 caractères.", Alert.AlertType.ERROR);
            } else {
                // Confirmation
                showConfirmationDialog("Confirmer",
                        "Êtes-vous sûr de vouloir changer votre mot de passe ?",
                        () -> {
                            // Hasher le nouveau mot de passe (avec BCrypt normalement)
                            String hashedPassword = newPass; // À remplacer par BCrypt.hashpw(newPass, BCrypt.gensalt())

                            // Mettre à jour en base
                            boolean success = updatePasswordInDatabase(client.getIdClient(), hashedPassword);

                            if (success) {
                                showAlert("Succès", "Mot de passe modifié avec succès !", Alert.AlertType.INFORMATION);
                            } else {
                                showAlert("Erreur", "Impossible de modifier le mot de passe.", Alert.AlertType.ERROR);
                            }
                        });
            }
        });
    }

    // Méthode pour styliser les champs de texte
    private void styleTextField(TextField textField) {
        textField.setStyle("-fx-background-color: white; " +
                "-fx-border-color: #D4C8B0; " +
                "-fx-border-width: 1px; " +
                "-fx-border-radius: 5px; " +
                "-fx-background-radius: 5px; " +
                "-fx-padding: 8px 10px; " +
                "-fx-font-size: 14px; " +
                "-fx-pref-width: 250px;");

        textField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                textField.setStyle("-fx-background-color: #FFFEFC; " +
                        "-fx-border-color: #8B4C4C; " +
                        "-fx-border-width: 2px; " +
                        "-fx-border-radius: 5px; " +
                        "-fx-background-radius: 5px; " +
                        "-fx-padding: 8px 10px; " +
                        "-fx-font-size: 14px; " +
                        "-fx-pref-width: 250px;");
            } else {
                textField.setStyle("-fx-background-color: white; " +
                        "-fx-border-color: #D4C8B0; " +
                        "-fx-border-width: 1px; " +
                        "-fx-border-radius: 5px; " +
                        "-fx-background-radius: 5px; " +
                        "-fx-padding: 8px 10px; " +
                        "-fx-font-size: 14px; " +
                        "-fx-pref-width: 250px;");
            }
        });
    }

    // Méthode pour afficher un dialogue de confirmation stylisé
    private void showConfirmationDialog(String title, String message, Runnable onConfirm) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        // Appliquer le style au dialogue
        DialogPane alertPane = alert.getDialogPane();
        alertPane.setStyle("-fx-background-color: white; " +
                "-fx-background-radius: 12px; " +
                "-fx-border-radius: 12px; " +
                "-fx-border-color: #E0D6C2; " +
                "-fx-border-width: 2px; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(56, 15, 23, 0.2), 20, 0, 0, 10);");

        // Styliser les boutons
        Button okButton = (Button) alertPane.lookupButton(ButtonType.OK);
        Button cancelButton = (Button) alertPane.lookupButton(ButtonType.CANCEL);

        styleDialogButton(okButton, "#380F17", "#5A1A29", "#2A0A11");
        styleDialogButton(cancelButton, "#95A5A6", "#7F8C8D", "#6A7B8C");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            onConfirm.run();
        }
    }

    private boolean updateEmailInDatabase(int clientId, String newEmail) {
        String sql = "UPDATE clients SET email = ? WHERE id_client = ?";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, newEmail);
            ps.setInt(2, clientId);

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Erreur modification email: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private boolean updatePasswordInDatabase(int clientId, String hashedPassword) {
        String sql = "UPDATE clients SET password = ? WHERE id_client = ?";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, hashedPassword);
            ps.setInt(2, clientId);

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Erreur modification mot de passe: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private void updateClient(Client client) {
        boolean success = clientDAO.update(client);
        if (success) {
            Session.setCurrentClient(client);
            loadClientData();
            showAlert("Succès", "Informations mises à jour avec succès !", Alert.AlertType.INFORMATION);
        } else {
            showAlert("Erreur", "Impossible de mettre à jour les informations.", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        // Appliquer le style au dialogue
        DialogPane alertPane = alert.getDialogPane();
        alertPane.setStyle("-fx-background-color: white; " +
                "-fx-background-radius: 12px; " +
                "-fx-border-radius: 12px; " +
                "-fx-border-color: #E0D6C2; " +
                "-fx-border-width: 2px; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(56, 15, 23, 0.2), 20, 0, 0, 10);");

        // Styliser les boutons
        Button okButton = (Button) alertPane.lookupButton(ButtonType.OK);
        styleDialogButton(okButton, "#8B4C4C", "#9A5A5A", "#7A3C3C");

        alert.showAndWait();
    }
}