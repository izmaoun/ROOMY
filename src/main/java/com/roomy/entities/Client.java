package com.roomy.entities;

import lombok.Data;

@Data
public class Client {
    private int id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String password;        // déjà hashé en BCrypt
    private String dateInscription;

    // Constructeur vide (obligatoire pour JDBC)
    public Client() {}

    // Constructeur pour l'inscription
    public Client(String nom, String prenom, String email, String telephone, String password) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
        this.password = password;
    }

    // Getters & Setters
//    public int getId() { return id; }
//    public void setId(int id) { this.id = id; }
//
//    public String getNom() { return nom; }
//    public void setNom(String nom) { this.nom = nom; }
//
//    public String getPrenom() { return prenom; }
//    public void setPrenom(String prenom) { this.prenom = prenom; }
//
//    public String getEmail() { return email; }
//    public void setEmail(String email) { this.email = email; }
//
//    public String getTelephone() { return telephone; }
//    public void setTelephone(String telephone) { this.telephone = telephone; }
//
//    public String getPassword() { return password; }
//    public void setPassword(String password) { this.password = password; }
//
//    public String getDateInscription() { return dateInscription; }
//    public void setDateInscription(String dateInscription) { this.dateInscription = dateInscription; }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}