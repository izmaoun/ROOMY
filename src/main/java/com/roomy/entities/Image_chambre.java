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

    // ---------- GETTERS ----------
    public int getId() {
        return id;
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
