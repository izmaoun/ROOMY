package com.roomy.Controller;

import com.roomy.entities.Hotel;
import com.roomy.entities.Chambre;

/**
 * Classe pour stocker le contexte de réservation pendant l'inscription
 * Permet la redirection automatique après inscription
 */
public class BookingContext {
    private static Hotel pendingHotel;
    private static Chambre pendingChambre;
    
    public static void setHotel(Hotel hotel) {
        pendingHotel = hotel;
    }
    
    public static Hotel getHotel() {
        return pendingHotel;
    }
    
    public static void setChambre(Chambre chambre) {
        pendingChambre = chambre;
    }
    
    public static Chambre getChambre() {
        return pendingChambre;
    }
    
    public static boolean hasPendingBooking() {
        return pendingHotel != null;
    }
    
    public static void clear() {
        pendingHotel = null;
        pendingChambre = null;
    }
}