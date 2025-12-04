package com.roomy.entities;

public class Adresse {
    private String rue;
    private String ville;
    private String codepostal;
    private String pays;

    public Adresse(){}

    public Adresse(String rue, String ville, String codepostal, String pays){
        this.rue = rue;
        this.ville=ville;
        this.codepostal=codepostal;
        this.pays=pays;
    }

    @Override
    public String toString() {
        return "rue : "+ rue+
                "ville :"+ ville +
                "codepostal :"+ codepostal+
                "pays : "+ pays;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if ( obj == null || this.getClass() != obj.getClass() ) return false;
        Adresse adresse = (Adresse)obj;
        return this.rue.equals(rue) && this.ville.equals(ville) && this.codepostal.equals(codepostal) && this.pays.equals(pays);
    }
}
