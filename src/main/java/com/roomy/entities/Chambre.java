package com.roomy.entities;

import com.roomy.ENUMS.Statut_technique_Chambre;
import com.roomy.ENUMS.TypeChambre;
import com.roomy.ENUMS.StatutReservation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Chambre {
    private int id;
    private int numchambre;
    private TypeChambre type;
    private double prix_nuit;
    private int capacity;
    private int surface;
    private Statut_technique_Chambre statut;
    private String description;
    private Hotel hotel;
    private List<Image_chambre> imgs;

    public Chambre () {
        this.imgs = new ArrayList<>();
    }

    public Chambre(int id, int numchambre, TypeChambre t, double prix, int capacity, int surface,
                   Statut_technique_Chambre statut, String description, Hotel h ) {
        this.id = id;
        this.numchambre = numchambre;
        this.type = t;
        this.prix_nuit = prix;
        this.capacity = capacity;
        this.surface = surface;
        this.statut = statut;
        this.description = description;
        this.hotel = h;
        this.imgs = new ArrayList<>();
    }

    // Getters et setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getNumchambre() { return numchambre; }
    public void setNumchambre(int numchambre) { this.numchambre = numchambre; }
    public TypeChambre getType() { return type; }
    public void setType(TypeChambre type) { this.type = type; }
    public double getPrix_nuit() { return prix_nuit; }
    public void setPrix_nuit(double prix) { this.prix_nuit = prix; }
    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    public int getSurface() { return surface; }
    public void setSurface(int surface) { this.surface = surface; }
    public Statut_technique_Chambre getStatut() { return statut; }
    public void setStatut(Statut_technique_Chambre statut) { this.statut = statut; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Hotel getHotel() { return hotel; }
    public void setHotel(Hotel hotel) { this.hotel = hotel; }
    public List<Image_chambre> getImgs() { return imgs; }

    public void addImg(Image_chambre img){
        if (img == null) return;
        imgs.add(img);
        img.setChambre(this);
    }

    public void rmvImg(Image_chambre img){
        if(img == null) return;
        imgs.remove(img);
        img.setChambre(null);
    }

    /**
     * Vérifie si la chambre est disponible pour la période demandée.
     * @param dateDebut début du séjour
     * @param dateFin fin du séjour
     * @param reservations liste de toutes les réservations du système
     * @return true si la chambre est disponible, false sinon
     */
    public boolean estDisponible(LocalDate dateDebut, LocalDate dateFin, List<Reservation> reservations) {
        if (reservations == null || reservations.isEmpty()) {
            return true;
        }

        for (Reservation r : reservations) {
            // Vérifie uniquement les réservations confirmées pour cette chambre
            if (r.getChambre().equals(this) && r.getStatut() == StatutReservation.CONFIRMEE) {
                LocalDate debutRes = r.getDateDebutSejour().toLocalDate();
                LocalDate finRes = r.getDateFinSejour().toLocalDate();

                // Si les dates se chevauchent, la chambre n'est pas disponible
                if (!(dateFin.isBefore(debutRes) || dateDebut.isAfter(finRes))) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "Chambre{" +
                "id=" + id +
                ", numchambre=" + numchambre +
                ", type=" + type +
                ", prix_nuit=" + prix_nuit +
                ", capacity=" + capacity +
                ", surface=" + surface +
                ", statut=" + statut +
                ", description='" + description + '\'' +
                ", hotel=" + (hotel != null ? hotel.getNomHotel() : "Aucun") +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;
        Chambre ch = (Chambre) obj;
        return id == ch.id && numchambre == ch.numchambre;
    }
}
