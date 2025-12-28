package com.roomy.Dao;

import com.roomy.entities.Hotel;
import com.roomy.entities.Image_hotel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ImageHotelDAO {

    // Mapper ResultSet → Image_hotel
    private Image_hotel mapToImage(ResultSet rs, Hotel hotel) throws SQLException {
        Image_hotel img = new Image_hotel();
        img.setId(rs.getInt("id_image"));
        img.setUrl(rs.getString("url"));
        img.setDescription(rs.getString("description"));
        img.setHotel(hotel);
        return img;
    }

    // Charger toutes les images d’un hôtel
   public List<Image_hotel> findByHotel(int idHotel) {
       List<Image_hotel> list = new ArrayList<>();
       String sql = "SELECT * FROM hotel_images WHERE id_hotel = ?";

       try (Connection c = DBConnection.getConnection();
            PreparedStatement ps = c.prepareStatement(sql)) {

           ps.setInt(1, idHotel);
           ResultSet rs = ps.executeQuery();

           while (rs.next()) {
               Image_hotel img = new Image_hotel();
               img.setId(rs.getInt("id_image"));
               img.setUrl(rs.getString("url"));
               img.setDescription(rs.getString("description"));
               list.add(img);
               // N'assigne PAS l'hôtel ici pour éviter la recursion
           }

       } catch (SQLException e) {
           System.err.println("Erreur ImageHotelDAO.findByHotel : " + e.getMessage());
       }

       return list;
   }

    // Ajouter une image
    public boolean addImage(Image_hotel img) {
        String sql = "INSERT INTO hotel_images (id_hotel, url, description) VALUES (?, ?, ?)";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, img.getHotel().getIdhotel());
            ps.setString(2, img.getUrl());
            ps.setString(3, img.getDescription());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) img.setId(keys.getInt(1));
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Erreur addImage : " + e.getMessage());
        }
        return false;
    }

    // Supprimer une image
    public boolean deleteImage(int idImage) {
        String sql = "DELETE FROM hotel_images WHERE id_image = ?";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, idImage);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erreur deleteImage : " + e.getMessage());
        }
        return false;
    }

    // Mettre à jour une image
    public boolean updateImage(Image_hotel img) {
        String sql = "UPDATE hotel_images SET url = ?, description = ? WHERE id_image = ?";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, img.getUrl());
            ps.setString(2, img.getDescription());
            ps.setInt(3, img.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erreur updateImage : " + e.getMessage());
        }
        return false;
    }
}
