package com.roomy.Controller;

import com.roomy.Dao.ClientDAO;
import com.roomy.Dao.HotelierDAO;
import com.roomy.entities.Client;
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
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;


public class SingupHotelier {
    private HotelierDAO hotelierDAO=new HotelierDAO();
    @FXML private TextField nomHotelierId;
    @FXML private TextField villeId;
    @FXML private TextField emailId;
    @FXML private PasswordField passwordId;
    @FXML private PasswordField confirmPasswordId;
    @FXML private TextField iceId;


    @FXML
    private void goBack(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/welcome.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setMaximized(false);
            stage.show();
            System.out.println("Retour à la page d'accueil...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleInscription(ActionEvent event){
        System.out.println("Nom de l'hôtel : " + nomHotelierId.getText());
        System.out.println("Ville : " + villeId.getText());
        System.out.println("Email : " + emailId.getText());
        System.out.println("Mot de passe : " + passwordId.getText());
        System.out.println("Confirmation : " + confirmPasswordId.getText());
        System.out.println("ICE : " + iceId.getText());
        if(this.hotelierDAO.findByEmail(emailId.getText())!=null){
            System.out.println("Cet email existe déjà !");
        }else {
            Hotelier hotelier=new Hotelier();
            hotelier.setNom(nomHotelierId.getText());
            hotelier.setVille(villeId.getText());
            hotelier.setEmail(emailId.getText());
            hotelier.setPassword(passwordId.getText());
            hotelier.setIce(iceId.getText());
            this.hotelierDAO.signup(hotelier);
            System.out.println("Inscription réussie !");
        }
    }

    @FXML
    private void goToLogin(ActionEvent event) {
//        try {
//            Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml")); // ← Créez ce fichier
//            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//            Scene scene = new Scene(root, 800, 600);
//            stage.setScene(scene);
//
//            stage.show();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}