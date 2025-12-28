package com.roomy.entities;

public class Adresse {

    private int idAdresse;
    private String rue;
    private String ville;
    private String codepostal;
    private String pays;

    // Constructeur vide
    public Adresse() {}

    // Constructeur AVEC id (utilisé quand on lit depuis la BD)
    public Adresse(int idAdresse, String rue, String ville, String codepostal, String pays) {
        this.idAdresse = idAdresse;
        this.rue = rue;
        this.ville = ville;
        this.codepostal = codepostal;
        this.pays = pays;
    }
    public String getRue() { return rue; }
    public void setRue(String rue) { this.rue = rue; }

    public String getVille() { return ville; }           // ✅ GETTER MANQUANT
    public void setVille(String ville) { this.ville = ville; } // ✅ SETTER MANQUANT

    public String getCodepostal() { return codepostal; }
    public void setCodepostal(String codepostal) { this.codepostal = codepostal; }

    public String getPays() { return pays; }             // ✅ GETTER MANQUANT
    public void setPays(String pays) { this.pays = pays; } // ✅ SETTER MANQUANT

    // Constructeur SANS id (utilisé avant insertion BD)
    public Adresse(String rue, String ville, String codepostal, String pays) {
        this.rue = rue;
        this.ville = ville;
        this.codepostal = codepostal;
        this.pays = pays;
    }

    // -------------------------
    // toString
    // -------------------------
    @Override
    public String toString() {
        return rue + ", " + ville + " " + codepostal + ", " + pays;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Adresse a = (Adresse) obj;

        return this.idAdresse == a.idAdresse;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(idAdresse);
    }

    public int getIdAdresse() {
        return idAdresse;
    }

    public void setIdAdresse(int idAdresse) {
        this.idAdresse = idAdresse;
    }
}
