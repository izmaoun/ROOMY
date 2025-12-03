package com.roomy.entities;

import com.roomy.ENUMS.StatutVerification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Hotelier {
    private int idHotelier;
    private String nomEtablissement;
    private String nomGerant;
    private String prenomGerant;
    private String ville;
    private String emailGerant;
    private String telephone;
    private String password;
    private String ice;
    private LocalDateTime dateInscription;
    private StatutVerification statutVerification;// "en_attente", "verifie", "rejete"
    private List<Hotel> Hotels;

    // Constructeurs
    public Hotelier() {}

    public Hotelier(String nomEtablissement, String nomGerant, String prenomGerant,
                    String ville, String emailGerant, String telephone,
                    String password, String ice) {
        this.nomEtablissement = nomEtablissement;
        this.nomGerant = nomGerant;
        this.prenomGerant = prenomGerant;
        this.ville = ville;
        this.emailGerant = emailGerant;
        this.telephone = telephone;
        this.password = password;
        this.ice = ice;
        this.dateInscription = LocalDateTime.now();
        this.statutVerification = StatutVerification.en_attente;
        this.Hotels = new ArrayList<>();
    }

    // Getters et Setters
    public int getIdHotelier() { return idHotelier; }
    public void setIdHotelier(int idHotelier) { this.idHotelier = idHotelier; }

    public String getNomEtablissement() { return nomEtablissement; }
    public void setNomEtablissement(String nomEtablissement) { this.nomEtablissement = nomEtablissement; }

    public String getNomGerant() { return nomGerant; }
    public void setNomGerant(String nomGerant) { this.nomGerant = nomGerant; }

    public String getPrenomGerant() { return prenomGerant; }
    public void setPrenomGerant(String prenomGerant) { this.prenomGerant = prenomGerant; }

    public String getVille() { return ville; }
    public void setVille(String ville) { this.ville = ville; }

    public String getEmailGerant() { return emailGerant; }
    public void setEmailGerant(String emailGerant) { this.emailGerant = emailGerant; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

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
        return "Hotelier{id=" + idHotelier +
                ", établissement='" + nomEtablissement + '\'' +
                ", gérant='" + prenomGerant + " " + nomGerant + '\'' +
                ", ville='" + ville + '\'' +
                ", email='" + emailGerant + '\'' +
                ", statut='" + statutVerification + '\'' +
                '}';    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Hotelier hotelier = (Hotelier) obj;
        return idHotelier == hotelier.idHotelier;
    }
}