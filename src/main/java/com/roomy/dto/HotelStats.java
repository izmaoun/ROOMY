package com.roomy.dto;

import java.util.HashMap;
import java.util.Map;

public class HotelStats {
    // --- Stats Globales (Dashboard) ---
    private int nbHotels;
    private int nbChambresTotal;
    private int nbChambresDispo;
    private int nbChambresOccupees;

    // --- Stats Détaillées (Page Statistiques) ---
    private int nbReservationsTotal;

    // Graphes
    private Map<String, Integer> reservationsParMois = new HashMap<>();   // Ex: "2024-01" -> 10
    private Map<Integer, Integer> reservationsParAnnee = new HashMap<>(); // Ex: 2024 -> 150
    private Map<String, Integer> reservationsParType = new HashMap<>();   // Ex: "SUITE" -> 5

    public HotelStats() {}

    // Getters & Setters
    public int getNbHotels() { return nbHotels; }
    public void setNbHotels(int nbHotels) { this.nbHotels = nbHotels; }

    public int getNbChambresTotal() { return nbChambresTotal; }
    public void setNbChambresTotal(int nbChambresTotal) { this.nbChambresTotal = nbChambresTotal; }

    public int getNbChambresDispo() { return nbChambresDispo; }
    public void setNbChambresDispo(int nbChambresDispo) { this.nbChambresDispo = nbChambresDispo; }

    public int getNbChambresOccupees() { return nbChambresOccupees; }
    public void setNbChambresOccupees(int nbChambresOccupees) { this.nbChambresOccupees = nbChambresOccupees; }

    public int getNbReservationsTotal() { return nbReservationsTotal; }
    public void setNbReservationsTotal(int nbReservationsTotal) { this.nbReservationsTotal = nbReservationsTotal; }

    public Map<String, Integer> getReservationsParMois() { return reservationsParMois; }
    public void setReservationsParMois(Map<String, Integer> reservationsParMois) { this.reservationsParMois = reservationsParMois; }

    public Map<Integer, Integer> getReservationsParAnnee() { return reservationsParAnnee; }
    public void setReservationsParAnnee(Map<Integer, Integer> reservationsParAnnee) { this.reservationsParAnnee = reservationsParAnnee; }

    public Map<String, Integer> getReservationsParType() { return reservationsParType; }
    public void setReservationsParType(Map<String, Integer> reservationsParType) { this.reservationsParType = reservationsParType; }
}