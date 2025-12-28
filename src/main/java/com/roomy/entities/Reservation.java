package com.roomy.entities;

import com.roomy.ENUMS.StatutReservation;
import java.time.LocalDateTime;

public class Reservation {
    private int idReservation;
    private Client client;
    private Chambre chambre;
    private LocalDateTime dateReservation;
    private LocalDateTime dateDebutSejour;
    private LocalDateTime dateFinSejour;
    private int nombrePersonnes;
    private double montantTotal;
    private StatutReservation statut;

    // Constructeurs
    public Reservation() {
        this.dateReservation = LocalDateTime.now();
        this.statut = StatutReservation.EN_ATTENTE;
    }

    public Reservation(Client client, Chambre chambre, LocalDateTime dateDebut,
                       LocalDateTime dateFin, int nombrePersonnes, double montantTotal) {
        this();
        this.client = client;
        this.chambre = chambre;
        this.dateDebutSejour = dateDebut;
        this.dateFinSejour = dateFin;
        this.nombrePersonnes = nombrePersonnes;
        this.montantTotal = montantTotal;
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
        return "Reservation #" + idReservation + " - " + statut + " - " + montantTotal + "€";
    }
}