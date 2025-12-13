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
    private List<Image_hotel> imgs;

    public Hotel() {
        this.chambres = new ArrayList<>();
        this.imgs = new ArrayList<>();
    }

    public Hotel(int idhotel, String nomHotel, Adresse adresse, int etoiles, Hotelier hotelier) {
        this.idhotel = idhotel;
        this.nomHotel = nomHotel;
        this.adresse = adresse;
        setEtoiles(etoiles); // Utilise le setter pour valider
        setHotelier(hotelier); // idem
        this.chambres = new ArrayList<>();
        this.imgs = new ArrayList<>();
    }

    // Getters et setters
    public int getIdhotel() {
        return idhotel;
    }

    public void setIdhotel(int idhotel) {
        this.idhotel = idhotel;
    }

    public String getNomHotel() {
        return nomHotel;
    }

    public void setNomHotel(String nomHotel) {
        this.nomHotel = nomHotel;
    }

    public Adresse getAdresse() {
        return adresse;
    }

    public void setAdresse(Adresse adresse) {
        this.adresse = adresse;
    }

    public int getEtoiles() {
        return etoiles;
    }

    public void setEtoiles(int etoiles) {
        if (etoiles < 1 || etoiles > 5) {
            throw new IllegalArgumentException("Le nombre d'étoiles doit être compris entre 1 et 5");
        }
        this.etoiles = etoiles;
    }

    public Hotelier getHotelier() {
        return hotelier;
    }

    public void setHotelier(Hotelier hotelier) {
        this.hotelier = Objects.requireNonNull(hotelier, "L'hotelier est obligatoire");
    }

    public List<Chambre> getChambres() {
        return chambres == null ? List.of() : List.copyOf(chambres);
    }

    public void setChambres(List<Chambre> chambres) {
        this.chambres = chambres == null ? new ArrayList<>() : new ArrayList<>(chambres);
    }

    public List<Image_hotel> getImgs() {
        return imgs == null ? List.of() : List.copyOf(imgs);
    }

    public void setImgs(List<Image_hotel> imgs) {
        this.imgs = imgs == null ? new ArrayList<>() : new ArrayList<>(imgs);
    }

    // Méthodes pour gérer chambres
    public void addChambre(Chambre ch) {
        if (ch == null) return;
        if (chambres == null) chambres = new ArrayList<>();
        chambres.add(ch);
        ch.setHotel(this);
    }

    public void removeChambre(Chambre ch) {
        if (ch == null) return;
        if (chambres != null && ch != null) {
            chambres.remove(ch);
            ch.setHotel(null);
        }
    }

    // Méthodes pour gérer images
    public void addImg(Image_hotel img) {
        if (img == null) return;
        if (imgs == null) imgs = new ArrayList<>();
        imgs.add(img);
        img.setHotel(this);
    }

    public void rmvImg(Image_hotel img) {
        if (img == null) return;
        if (imgs != null && img != null) {
            imgs.remove(img);
            img.setHotel(null);
        }
    }

    public int getNombreDeChambres() {
        return chambres == null ? 0 : chambres.size();
    }

    @Override
    public String toString() {
        return "Hotel {" +
                "\nid: " + idhotel +
                ", \nnom: '" + nomHotel + '\'' +
                ", \nadresse: " + adresse +
                ", \netoiles: " + etoiles +
                ", \nhotelier: " + hotelier +
                ", \nnombre de chambres: " + (chambres == null ? 0 : chambres.size()) +
                ", \nnombre d'images: " + (imgs == null ? 0 : imgs.size()) +
                "\n}";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Hotel hotel = (Hotel) obj;
        return idhotel == hotel.idhotel && adresse.equals(hotel.adresse);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idhotel, adresse);
    }
}
