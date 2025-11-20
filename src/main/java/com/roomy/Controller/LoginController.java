package com.roomy.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import com.roomy.service.AuthService;

public class LoginController {

    @FXML
    private TextField userField;
    @FXML
    private PasswordField passField;
    @FXML
    private Label msg;

    private final AuthService auth = new AuthService();

    @FXML
    public void handleLogin() {
        String u = userField.getText();
        String p = passField.getText();

        String role = auth.authenticate(u, p);

        switch (role) {
            case "CLIENT" -> load("/fxml/dash_client.fxml");
            case "HOTELIER" -> load("/fxml/dash_hotelier.fxml");
            case "ADMIN" -> load("/fxml/dash_admin.fxml");
            default -> msg.setText("Invalid credentials!");
        }
    }

    @FXML
    public void handleSignupNav() {
        try {
            Stage stage = (Stage) userField.getScene().getWindow();
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/fxml/signup.fxml"))));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleBack() {
        Stage stage = (Stage) userField.getScene().getWindow();
        stage.close();
    }

    private void load(String fxml) {
        try {
            Stage stage = (Stage) userField.getScene().getWindow();
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource(fxml))));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
