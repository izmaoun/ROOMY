package com.roomy.entities;

import java.time.LocalDateTime;
import com.roomy.ENUMS.StatutReservation;

public class Reservation {

    private int idReservation;
    private Client client;          // Client qui réserve
    private Chambre chambre;        // Chambre réservée
    private LocalDateTime dateReservation;
    private LocalDateTime dateDebutSejour;
    private LocalDateTime dateFinSejour;
    private int nombrePersonnes;
    private double montantTotal;
    private StatutReservation statut;  // Enum pour le statut

    // Constructeur vide
    public Reservation() {}

    // Constructeur principal
    public Reservation(Client client, Chambre chambre,
                       LocalDateTime dateDebutSejour, LocalDateTime dateFinSejour,
                       int nombrePersonnes, double montantTotal) {
        this.client = client;
        this.chambre = chambre;
        this.dateReservation = LocalDateTime.now();
        this.dateDebutSejour = dateDebutSejour;
        this.dateFinSejour = dateFinSejour;
        this.nombrePersonnes = nombrePersonnes;
        this.montantTotal = montantTotal;
        this.statut = StatutReservation.CONFIRMEE; // statut initial par défaut
    }

    // Méthode pour annuler la réservation
    public void annulerReservation() {
        this.statut = StatutReservation.ANNULEE;
    }

    // Getters et Setters
    public int getIdReservation() { return idReservation; }
    public void setIdReservation(int idReservation) { this.idReservation = idReservation; }

    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }

    public Chambre getChambre() { return chambre; }
    public void setChambre(Chambre chambre) { this.chambre = chambre; }

    public LocalDateTime getDateReservation() { return dateReservation; }
    public void setDateReservation(LocalDateTime dateReservation) { this.dateReservation = dateReservation; }

    public LocalDateTime getDateDebutSejour() { return dateDebutSejour; }
    public void setDateDebutSejour(LocalDateTime dateDebutSejour) { this.dateDebutSejour = dateDebutSejour; }

    public LocalDateTime getDateFinSejour() { return dateFinSejour; }
    public void setDateFinSejour(LocalDateTime dateFinSejour) { this.dateFinSejour = dateFinSejour; }

    public int getNombrePersonnes() { return nombrePersonnes; }
    public void setNombrePersonnes(int nombrePersonnes) { this.nombrePersonnes = nombrePersonnes; }

    public double getMontantTotal() { return montantTotal; }
    public void setMontantTotal(double montantTotal) { this.montantTotal = montantTotal; }

    public StatutReservation getStatut() { return statut; }
    public void setStatut(StatutReservation statut) { this.statut = statut; }

    @Override
    public String toString() {
        return "Reservation{" +
                "idReservation=" + idReservation +
                ", client=" + client.getNom() + " " + client.getPrenom() +
                ", chambre=" + (chambre != null ? chambre.getNumchambre() : "Aucune") +
                ", hotel=" + (chambre != null && chambre.getHotel() != null ? chambre.getHotel().getNomHotel() : "Aucun") +
                ", dateReservation=" + dateReservation +
                ", dateDebutSejour=" + dateDebutSejour +
                ", dateFinSejour=" + dateFinSejour +
                ", nombrePersonnes=" + nombrePersonnes +
                ", montantTotal=" + montantTotal +
                ", statut=" + statut +
                '}';
    }
}
