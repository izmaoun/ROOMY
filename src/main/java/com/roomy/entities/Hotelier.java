package com.roomy.entities;

import com.roomy.ENUMS.StatutVerification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Hotelier {
    // Inlined fields from former Utilisateur
    protected int id;
    protected String nom;
    protected String prenom;
    protected String email;
    protected String motDePasseHash;
    protected String telephone;
    protected LocalDateTime dateInscription;

    private String nomEtablissement;
    private String ville;
    private String ice;
    private StatutVerification statutVerification;// "en_attente", "verifie", "rejete"
    private List<Hotel> Hotels;

    // Constructeurs
    public Hotelier() {}

    public Hotelier(String nomEtablissement, String nomGerant, String prenomGerant,
                    String ville, String emailGerant, String telephone,
                    String password, String ice) {
        this.nomEtablissement = nomEtablissement;
        this.nom = nomGerant;
        this.prenom = prenomGerant;
        this.ville = ville;
        this.email = emailGerant;
        this.telephone = telephone;
        this.motDePasseHash = password;
        this.ice = ice;
        this.dateInscription = LocalDateTime.now();
        this.statutVerification = StatutVerification.en_attente;
        this.Hotels = new ArrayList<>();
    }

    // Getters et Setters
    public int getIdHotelier() { return id; }
    public void setIdHotelier(int idHotelier) { this.id = idHotelier; }

    public String getNomEtablissement() { return nomEtablissement; }
    public void setNomEtablissement(String nomEtablissement) { this.nomEtablissement = nomEtablissement; }

    public String getNomGerant() { return nom; }
    public void setNomGerant(String nomGerant) { this.nom = nomGerant; }

    public String getPrenomGerant() { return prenom; }
    public void setPrenomGerant(String prenomGerant) { this.prenom = prenomGerant; }

    public String getVille() { return ville; }
    public void setVille(String ville) { this.ville = ville; }

    public String getEmailGerant() { return email; }
    public void setEmailGerant(String emailGerant) { this.email = emailGerant; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getPassword() { return motDePasseHash; }
    public void setPassword(String password) { this.motDePasseHash = password; }

    public String getIce() { return ice; }
    public void setIce(String ice) { this.ice = ice; }

    public LocalDateTime getDateInscription() { return dateInscription; }
    public void setDateInscription(LocalDateTime dateInscription) { this.dateInscription = dateInscription; }

    public StatutVerification getStatutVerification() { return statutVerification; }
    public void setStatutVerification(StatutVerification statutVerification) { this.statutVerification = statutVerification; }

    public List<Hotel> getHotels() { return Hotels == null ? List.of() : List.copyOf(Hotels); }
    public void addHotel (Hotel hotel){
        if(hotel == null) return;
        if(Hotels == null) Hotels = new ArrayList<>();
        Hotels.add(hotel);
        hotel.setHotelier(this);
    }
    public void removehotel (Hotel hotel){  // wa7ed bwa7ed
        if (Hotels != null && hotel != null ){
            Hotels.remove(hotel);
            hotel.setHotelier(null);
        }
    }

    public void clearhotels(){  //da9a wa7da
        if (Hotels != null){
            Hotels.forEach(h -> h.setHotelier(null));
            Hotels.clear();
        }
    }



    @Override
    public String toString() {
        return "Hotelier{id=" + id +
                ", établissement='" + nomEtablissement + '\'' +
                ", gérant='" + prenom + " " + nom + '\'' +
                ", ville='" + ville + '\'' +
                ", email='" + email + '\'' +
                ", statut='" + statutVerification + '\'' +
                '}';    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Hotelier hotelier = (Hotelier) obj;
        return id == hotelier.id;
    }
}