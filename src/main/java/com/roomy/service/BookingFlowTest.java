package com.roomy.service;

import com.roomy.Controller.BookingContext;
import com.roomy.entities.Hotel;
import com.roomy.entities.Chambre;
import com.roomy.entities.Client;

/**
 * Classe de test pour vérifier les flux de réservation
 */
public class BookingFlowTest {
    
    public static void testBookingFlow() {
        System.out.println("=== Test des flux de réservation ===");
        
        // Test FLUX 1 : Utilisateur non connecté
        System.out.println("\n--- FLUX 1 : Utilisateur non connecté ---");
        Session.logout(); // S'assurer qu'aucun utilisateur n'est connecté
        
        Hotel hotel = new Hotel();
        hotel.setNomHotel("Test Hotel");
        
        Chambre chambre = new Chambre();
        chambre.setNumchambre(101);
        chambre.setPrix_nuit(150.0);
        
        // Simuler la sélection d'un hôtel pour réservation
        BookingContext.setHotel(hotel);
        BookingContext.setChambre(chambre);
        
        System.out.println("Contexte de réservation stocké:");
        System.out.println("- Hôtel: " + BookingContext.getHotel().getNomHotel());
        System.out.println("- Chambre: " + BookingContext.getChambre().getNumchambre());
        System.out.println("- A une réservation en attente: " + BookingContext.hasPendingBooking());
        
        // Test FLUX 2 : Utilisateur connecté
        System.out.println("\n--- FLUX 2 : Utilisateur connecté ---");
        Client client = new Client();
        client.setIdClient(1);
        client.setNom("Test");
        client.setPrenom("User");
        client.setEmail("test@example.com");
        
        Session.setCurrentClient(client);
        System.out.println("Utilisateur connecté: " + Session.getCurrentClient().getEmail());
        System.out.println("Est connecté: " + Session.isLoggedIn());
        
        // Nettoyer
        BookingContext.clear();
        Session.logout();
        
        System.out.println("\n=== Test terminé ===");
    }
    
    public static void main(String[] args) {
        testBookingFlow();
    }
}