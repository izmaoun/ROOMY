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
        System.out.println("Loading FXML from: /fxml/landing-page.fxml");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/landing-page.fxml"));

        // Get screen dimensions and set window to 80% of screen size
        javafx.stage.Screen screen = javafx.stage.Screen.getPrimary();
        javafx.geometry.Rectangle2D bounds = screen.getVisualBounds();
        double width = bounds.getWidth() * 0.8;
        double height = bounds.getHeight() * 0.8;
        
        Scene scene = new Scene(loader.load(), width, height);

        // Set icon and title
        try {
            javafx.scene.image.Image icon = new javafx.scene.image.Image(getClass().getResourceAsStream("/images/Logo_favicon.png"));
            primaryStage.getIcons().add(icon);
        } catch (Exception e) {
            System.err.println("Could not load favicon: " + e.getMessage());
        }
        primaryStage.setTitle("Roomy: Landing Page");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
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