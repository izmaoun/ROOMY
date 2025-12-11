package com.roomy.service;

import com.roomy.database.DataInit;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TestLandingPage extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("🚀 Starting Landing Page Test...");

        // Initialise DB
        DataInit.init();
        System.out.println("✅ Database initialized!");

        // Test connection
        try {
            com.roomy.Dao.DBConnection.getConnection();
            System.out.println("✅ Database connection OK!");
        } catch (Exception e) {
            System.err.println("❌ Database connection failed: " + e.getMessage());
            return;
        }

        // Load landing page
        System.out.println("Loading FXML from: /com/roomy/fxml/landing-page.fxml");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/landing-page.fxml"));

        Scene scene = new Scene(loader.load(), 1200, 800);

        primaryStage.setTitle("ROOMY - Test Landing Page");
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();

        System.out.println("✅ Landing Page loaded successfully!");
    }

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("ROOMY LANDING PAGE TEST");
        System.out.println("========================================");
        launch(args);
    }
}