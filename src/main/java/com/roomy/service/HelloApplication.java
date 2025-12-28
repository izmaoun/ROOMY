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
        DataInit.init();

        stage.setWidth(900);
        stage.setHeight(600);

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/fxml/landing-page.fxml"));
        // Taille initiale optimale
        Scene scene = new Scene(fxmlLoader.load());

        stage.setTitle("ROOMY - Accueil");
        stage.setResizable(true);

        // Limites minimales pour ne pas casser l'UI
        stage.setMinWidth(900);
        stage.setMinHeight(650);

        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}