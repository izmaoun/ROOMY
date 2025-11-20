package com.roomy.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import com.roomy.Dao.ClientDAO;
import com.roomy.entities.Client;
import org.mindrot.jbcrypt.BCrypt;

public class SignupController {
    @FXML
    private TextField userField;
    @FXML
    private PasswordField passField;
    @FXML
    private ComboBox<String> typeBox;
    @FXML
    public void initialize() {
        typeBox.getItems().addAll("Client", "Hotelier");
    }

    @FXML
    private Label msg;

    @FXML
    public void handleSignup() {
        String username = userField.getText();
        String password = passField.getText();
        String type = typeBox.getValue();

        if (username.isEmpty() || password.isEmpty() || type == null) {
            msg.setText("Tous les champs sont obligatoires.");
            return;
        }

        String hash = BCrypt.hashpw(password, BCrypt.gensalt());
        boolean success = false;
        if (type.equals("Client")) {
            Client client = new Client();
            client.setUsername(username);
            client.setPassword(hash);
            success = new ClientDAO().signup(client);
        } else if (type.equals("Hotelier")) {
            com.roomy.entities.Hotelier hotelier = new com.roomy.entities.Hotelier();
            hotelier.setUsername(username);
            hotelier.setPassword(hash);
            success = new com.roomy.Dao.HotelierDAO().signup(hotelier);
        }
        if (success) {
            msg.setText("Inscription réussie ! Vous pouvez vous connecter.");
        } else {
            msg.setText("Nom d'utilisateur déjà utilisé ou erreur.");
        }
    }

    @FXML
    public void handleBack() {
        try {
            Stage stage = (Stage) userField.getScene().getWindow();
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/fxml/login.fxml"))));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

