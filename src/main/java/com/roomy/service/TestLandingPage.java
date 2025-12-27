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
        
        // Ajouter un client par défaut pour tester
        createDefaultClient();

        // Load landing page
        System.out.println("Loading FXML from: /com/roomy/fxml/landing-page.fxml");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/landing-page.fxml"));

        Scene scene = new Scene(loader.load(), 1200, 800);

        primaryStage.setTitle("ROOMY - Test Landing Page");
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();

        System.out.println("✅ Landing Page loaded successfully!");
        System.out.println("📧 Client test: test@roomy.com / password: 123456");
    }
    
    private void createDefaultClient() {
        try {
            com.roomy.Dao.ClientDAO clientDAO = new com.roomy.Dao.ClientDAO();
            
            // Vérifier si le client existe déjà
            if (clientDAO.findByEmail("test@roomy.com") == null) {
                com.roomy.entities.Client client = new com.roomy.entities.Client();
                client.setNom("Test");
                client.setPrenom("User");
                client.setEmail("test@roomy.com");
                client.setTelephone("0123456789");
                
                // Hasher le mot de passe
                String hashedPassword = org.mindrot.jbcrypt.BCrypt.hashpw("123456", org.mindrot.jbcrypt.BCrypt.gensalt());
                client.setPassword(hashedPassword);
                
                boolean created = clientDAO.signup(client);
                if (created) {
                    System.out.println("✅ Client test créé: test@roomy.com / 123456");
                } else {
                    System.out.println("❌ Erreur création client test");
                }
            } else {
                System.out.println("✅ Client test existe déjà: test@roomy.com / 123456");
            }
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de la création du client test: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("ROOMY LANDING PAGE TEST");
        System.out.println("========================================");
        launch(args);
    }
}