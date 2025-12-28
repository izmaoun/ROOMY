package com.roomy.entities;

import java.time.LocalDateTime;

public class Admin extends Utilisateur {
    private String username;

    // Constructeurs
    public Admin() {
        super();
    }

    public Admin(String nom, String prenom, String email, String motDePasseHash, String username, String telephone, LocalDateTime dateInscription) {
        super(0, nom, prenom, email, motDePasseHash, telephone, dateInscription);
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