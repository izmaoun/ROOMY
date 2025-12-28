package com.roomy.entities;

public class Image_hotel {
    private int id;
    private String url;
    private String description;
    private Hotel hotel;

    public Image_hotel(){}
    public Image_hotel(int id, String url, String description, Hotel hotel) {
        this.id = id;
        this.url=url;
        this.description=description;
        this.hotel=hotel;
    }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Hotel getHotel() { return hotel; }
    public void setHotel(Hotel hotel) {
        this.hotel = hotel;

    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Image_hotel img = (Image_hotel) obj;
        return this.id == img.id && this.url.equals(img.url);
    }

}
