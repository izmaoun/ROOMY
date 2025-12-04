package com.roomy.entities;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Utilisateur {
    protected int id;
    protected String nom;
    protected String prenom;
    protected String email;
    protected String motDePasseHash;
    protected String telephone;
    protected LocalDateTime dateInscription;

    public Utilisateur (){}

    public Utilisateur (String nom, String prenom, String email, String motDePasseHash, String telephone, LocalDateTime dateInscription){
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motDePasseHash = motDePasseHash;
        this.telephone = telephone;
        this.dateInscription = LocalDateTime.now();
    }


}
