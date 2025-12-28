package com.roomy.entities;

public class Image_chambre {
    private int id;
    private String url;
    private String description;
    private Chambre chambre;

    public Image_chambre() {}

    public Image_chambre(int id, String url, String description, Chambre chambre) {
        this.id = id;
        this.url = url;
        this.description = description;
        this.chambre = chambre;
    }

<<<<<<< HEAD
    // ---------- GETTERS ----------
    public int getId() {
        return id;
=======
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUrl() { return url; }           // ✅ GETTER MANQUANT
    public void setUrl(String url) { this.url = url; } // ✅ SETTER MANQUANT

    public String getDescription() { return description; } // ✅ GETTER MANQUANT
    public void setDescription(String description) { this.description = description; } // ✅ SETTER MANQUANT

    public Chambre getChambre() { return chambre; }
    public void setChambre (Chambre ch){
        this.chambre=ch;
>>>>>>> 146ddc43664c4b11e5d3f96cac87047998ebacd1
    }

    public String getUrl() {
        return url;
    }

    public String getDescription() {
        return description;
    }

    public Chambre getChambre() {
        return chambre;
    }

    // ---------- SETTERS ----------
    public void setId(int id) {
        this.id = id;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setChambre(Chambre chambre) {
        this.chambre = chambre;
    }

    // ---------- EQUALS ----------
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Image_chambre img = (Image_chambre) obj;
        return this.id == img.id && this.url.equals(img.url);
    }

    @Override
    public String toString() {
        return "Image_chambre{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
