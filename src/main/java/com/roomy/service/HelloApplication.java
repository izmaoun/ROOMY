package com.roomy.service;

import com.roomy.database.DataInit;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        DataInit.init(); // ← Ajoute cette ligne !

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/fxml/welcome.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 650);
        stage.setTitle("Roomy - Réservation Hôtels");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}