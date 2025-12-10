package com.roomy.entities;

import java.time.LocalDateTime;

public class Client {
    // Inlined fields from former Utilisateur
    protected int id;
    protected String nom;
    protected String prenom;
    protected String email;
    protected String motDePasseHash;
    protected String telephone;
    protected LocalDateTime dateInscription;

    private boolean estBloque;

    // Constructeur vide (obligatoire pour JDBC)
    public Client() {}

    // Constructeur pour l'inscription
    public Client(String nom, String prenom, String email, String telephone, String motDePasseHash) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
        this.motDePasseHash = motDePasseHash;
        this.dateInscription = LocalDateTime.now();
        this.estBloque = false;
    }

    // Getters et Setters
    public int getIdClient() { return this.id; }
    public void setIdClient(int idClient) { this.id = idClient; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getPassword() { return this.motDePasseHash; }
    public void setPassword(String password) { this.motDePasseHash = password; }

    public LocalDateTime getDateInscription() { return dateInscription; }
    public void setDateInscription(LocalDateTime dateInscription) { this.dateInscription = dateInscription; }

    public boolean isEstBloque() { return estBloque; }
    public void setEstBloque(boolean estBloque) { this.estBloque = estBloque; }

    @Override
    public String toString() {
        return "Client{idClient=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", telephone='" + telephone + '\'' +
                ", dateInscription=" + dateInscription +
                ", estBloque=" + estBloque +
                '}';
    }
}