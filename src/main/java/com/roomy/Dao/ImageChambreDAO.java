package com.roomy.Dao;

import com.roomy.entities.Chambre;
import com.roomy.entities.Image_chambre;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ImageChambreDAO {

    // 1️⃣ Ajouter une image
    public boolean addImage(Image_chambre img) {
        String sql = "INSERT INTO chambre_images (id_chambre, url, description) VALUES (?, ?, ?)";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, img.getChambre().getId());
            ps.setString(2, img.getUrl());
            ps.setString(3, img.getDescription());

            int rows = ps.executeUpdate();

            if (rows > 0) {
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) {
                    img.setId(keys.getInt(1));
                }
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Erreur ajout image chambre : " + e.getMessage());
        }
        return false;
    }


    // 2️⃣ Récupérer toutes les images d’une chambre
    public List<Image_chambre> findByChambre(int idChambre) {
        List<Image_chambre> list = new ArrayList<>();
        String sql = "SELECT * FROM chambre_images WHERE id_chambre = ?";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, idChambre);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Image_chambre img = new Image_chambre();
                img.setId(rs.getInt("id_image"));
                img.setUrl(rs.getString("url"));
                img.setDescription(rs.getString("description"));

                Chambre ch = new Chambre();
                ch.setId(idChambre);
                img.setChambre(ch);

                list.add(img);
            }

        } catch (SQLException e) {
            System.err.println("Erreur findByChambre images : " + e.getMessage());
        }
        return list;
    }


    // 3️⃣ Supprimer une image
    public boolean deleteImage(int idImage) {
        String sql = "DELETE FROM chambre_images WHERE id_image = ?";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, idImage);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erreur suppression image : " + e.getMessage());
        }
        return false;
    }
}
