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

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/fxml/welcome.fxml"));
        // Taille initiale optimale
        Scene scene = new Scene(fxmlLoader.load(), 1300, 750);

        stage.setTitle("Roomy - Dashboard Admin");
        stage.setResizable(true);

        // Limites minimales pour ne pas casser l'UI
        stage.setMinWidth(1100);
        stage.setMinHeight(650);

        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}