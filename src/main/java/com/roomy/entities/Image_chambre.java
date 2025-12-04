package com.roomy.entities;

public class Image_chambre {
    private int id;
    private String url;
    private String description;
    private Chambre chambre;

    public Image_chambre(){}
    public Image_chambre(int id, String url, String description, Chambre ch) {
        this.id = id;
        this.url=url;
        this.description=description;
        this.chambre=ch;
    }

    public void setChambre (Chambre ch){
        this.chambre=ch;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Image_chambre img = (Image_chambre) obj;
        return this.id == img.id && this.url.equals(img.url);
    }

}

