package com.roomy.entities;

import java.util.ArrayList;
import java.util.List;

public class Hotel {
    private int idhotel;
    private String nomHotel;
    private String adresse;
    private int etoiles;
    private Hotelier hotelier;
    private List<Chambre> chambres;

    public Hotel(){}

    public Hotel(int idhotel, String nomHotel, String adresse, int e, Hotelier hotelier){
        this.idhotel = idhotel;
        this.nomHotel = nomHotel;
        this.adresse = adresse;
        this.etoiles = e;
        this.hotelier = hotelier;
        this.chambres = new ArrayList<>();
    }










}
