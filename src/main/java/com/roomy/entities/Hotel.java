package com.roomy.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Hotel {
    private int idhotel;
    private String nomHotel;
    private String description;
    private Adresse adresse;
    private int etoiles;
    private Hotelier hotelier;
    private List<Chambre> chambres;
    private List<Image_hotel> imgs;
    private List<String> services;
    private String description;

<<<<<<< HEAD
    public Hotel() {
=======
    public Hotel(){
        this.chambres = new ArrayList<>();
        this.imgs = new ArrayList<>();
        this.services = new ArrayList<>();
    }

    public Hotel(int idhotel, String nomHotel, Adresse adresse, int e, Hotelier hotelier){
        this.idhotel = idhotel;
        this.nomHotel = nomHotel;
        this.adresse = adresse;
        this.etoiles = e;
        this.hotelier = hotelier;
>>>>>>> 146ddc43664c4b11e5d3f96cac87047998ebacd1
        this.chambres = new ArrayList<>();
        this.imgs = new ArrayList<>();
        this.services = new ArrayList<>();
    }

    public Hotel(int idhotel, String nomHotel, String description, Adresse adresse, int etoiles, Hotelier hotelier) {
        this.idhotel = idhotel;
        this.nomHotel = nomHotel;
        this.description = description;
        this.adresse = adresse;
        setEtoiles(etoiles);
        setHotelier(hotelier);
        this.chambres = new ArrayList<>();
        this.imgs = new ArrayList<>();
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
<<<<<<< HEAD

    public Hotelier getHotelier() {
        return hotelier;
=======
    public List<String> getServices() { return services; }
    public void setServices(List<String> services) { this.services = services; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    // Méthode pour obtenir l'image principale
    public String getMainImage() {
        if (imgs != null && !imgs.isEmpty()) {
            return imgs.get(0).getUrl();
        }
        return null;
    }

    // Méthode pour obtenir le prix minimum
    public double getMinPrice() {
        if (chambres == null || chambres.isEmpty()) return 0;
        return chambres.stream()
                .mapToDouble(Chambre::getPrix_nuit)
                .min()
                .orElse(0);
    }
    public Hotelier getHotelier() { return hotelier; }
    public void setHotelier(Hotelier hotlier){
        this.hotelier = Objects.requireNonNull(hotelier, "l'hotelier est obligatoire");
>>>>>>> 146ddc43664c4b11e5d3f96cac87047998ebacd1
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
                ", \ndescription: '" + description + '\'' +
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