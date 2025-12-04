package com.roomy.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Hotel {
    private int idhotel;
    private String nomHotel;
    private Adresse adresse;
    private int etoiles;
    private Hotelier hotelier;
    private List<Chambre> chambres;

    public Hotel(){}

    public Hotel(int idhotel, String nomHotel, Adresse adresse, int e, Hotelier hotelier){
        this.idhotel = idhotel;
        this.nomHotel = nomHotel;
        this.adresse = adresse;
        this.etoiles = e;
        this.hotelier = hotelier;
        this.chambres = new ArrayList<>();
    }

    //getters, setters
    public int getIdhotel() { return idhotel; }
    public void setIdhotel(int idhotel) { this.idhotel = idhotel; }
    public String getNomHotel() { return nomHotel; }
    public void setNomHotel(String nom){ this.nomHotel = nom; }
    public Adresse getAdresse(){ return adresse; }
    public void setAdresse(Adresse adresse){ this.adresse = adresse; }
    public int getEtoiles(){ return etoiles; }
    public void setEtoiles(int etoiles){
        if ( etoiles < 1 || etoiles > 5){
            throw new IllegalArgumentException("le nbre d'etoiles est compris entre 1 et 5");
        }
        this.etoiles = etoiles;
    }
    public Hotelier getHotelier() { return hotelier; }
    public void setHotelier(Hotelier hotlier){
        this.hotelier = Objects.requireNonNull(hotelier, "l'hotelier est obligatoire");
    }

    public List<Chambre> getChambres(){
        return chambres == null ? List.of() : List.copyOf(chambres);
    }

    public void addChambre(Chambre ch){
        if (ch == null) return;
        if (chambres == null) chambres = new ArrayList<>();
        chambres.add(ch);
        ch.setHotel(this);
    }

    public void removeChambre(Chambre ch){
        if (ch == null) return;
        if (chambres != null && ch != null){
            chambres.remove(ch);
            ch.setHotel(null);
        }
    }

    @Override
    public String toString() {
        return "l'Hotel  :"+ idhotel +
                "nom    :"+ nomHotel +
                "adresse:"+ adresse+
                "etoiles:"+ etoiles+
                "manager :"+ hotelier+
                "nombre de chambres"+ chambres.size();
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;
        Hotel hotel = (Hotel) obj;
        return idhotel == hotel.idhotel && adresse.equals(hotel.adresse);
    }
}
