package com.roomy.entities;

import java.time.LocalDateTime;

public class Admin {
    // Inlined fields from former Utilisateur
    protected int id;
    protected String nom;
    protected String prenom;
    protected String email;
    protected String motDePasseHash;
    protected String telephone;
    protected LocalDateTime dateInscription;

    private String username;

    // Constructeurs
    public Admin() {}

    public Admin(String nom, String prenom, String email, String motDePasseHash, String username, String telephone, LocalDateTime dateInscription) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motDePasseHash = motDePasseHash;
        this.telephone = telephone;
        this.dateInscription = dateInscription != null ? dateInscription : LocalDateTime.now();
        this.username = username;
    }

    // Getters et Setters
    public int getIdAdmin() { return id; }
    public void setIdAdmin(int idAdmin) { this.id = idAdmin; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return motDePasseHash; }
    public void setPassword(String password) { this.motDePasseHash = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return "Administrateur{idAdmin=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}