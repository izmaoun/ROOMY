package com.roomy.entities;

import com.roomy.ENUMS.Statut_technique_Chambre;
import com.roomy.ENUMS.TypeChambre;

import java.time.LocalDate;
import java.util.Date;

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

    public Chambre (){}
    public Chambre(int id, int numchambre, TypeChambre t,double prix, int capacity, int surface,
                   Statut_technique_Chambre statut,String description, Hotel h ) {
        this.id = id;
        this.numchambre = numchambre;
        this.type = t;
        this.prix_nuit = prix;
        this.capacity = capacity;
        this.surface = surface;
        this.statut = statut;
        this.description = description;
        this.hotel = h;
    }

    //getters, setters


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

    //public boolean estDisponible(LocalDate datedebut, LocalDate datefin){
            //li xadin lblan ta3 reservation hadi raha en relation avec reservation class
    //}

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
        return (id == ch.id )&& (numchambre == ch.numchambre);
    }
}
