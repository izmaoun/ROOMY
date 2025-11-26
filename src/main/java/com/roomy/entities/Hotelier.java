package com.roomy.entities;

import lombok.Data;

@Data
public class Hotelier {
    private int id;

    // Infos hôtel
    private String nom;           // Nom de l'hôtel
    private String ville;

    // Infos gérant + authentification
    private String email;
    private String password;      // déjà hashé en BCrypt
    private String ice;
    // Constructeur vide
    public Hotelier() {}

    // Constructeur utilisé lors de l'inscription hôtel
    public Hotelier(String nom, String ville, String email, String password, String ice) {
        this.nom = nom;
        this.ville = ville;
        this.email = email;
        this.password = password;
        this.ice = ice;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getVille() { return ville; }
    public void setVille(String ville) { this.ville = ville; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getIce() { return ice; }
    public void setIce(String ice) { this.ice = ice; }

    @Override
    public String toString() {
        return "Hotelier{" +
                "id=" + id +
                ", nomHotel='" + nom + '\'' +
                ", ville='" + ville + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}