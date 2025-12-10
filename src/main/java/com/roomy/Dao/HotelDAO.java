package com.roomy.Dao;

import com.roomy.entities.Hotel;
import com.roomy.entities.Hotelier;
import com.roomy.entities.Adresse;
import com.roomy.entities.Chambre;
import com.roomy.entities.Image_hotel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HotelDAO {

    // ===================== TROUVER PAR ID =====================
    public Hotel findById(int id) {
        String sql = "SELECT * FROM hotels WHERE id_hotel = ?";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapToHotel(rs);
            }

        } catch (SQLException e) {
            System.err.println("Erreur Hotel findById : " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // ===================== MAPPER RESULTSET → OBJET HOTEL =====================
    private Hotel mapToHotel(ResultSet rs) throws SQLException {
        Hotel hotel = new Hotel();

        hotel.setIdhotel(rs.getInt("id_hotel"));
        hotel.setNomHotel(rs.getString("nom_hotel"));
        hotel.setEtoiles(rs.getInt("etoiles"));

        // Adresse
        AdresseDAO adresseDAO = new AdresseDAO();
        Adresse adresse = adresseDAO.findById(rs.getInt("id_adresse"));
        hotel.setAdresse(adresse);

        // Hotelier
        HotelierDAO hotelierDAO = new HotelierDAO();
        Hotelier hotelier = hotelierDAO.findById(rs.getInt("id_hotelier"));
        hotel.setHotelier(hotelier);

        // Chambres
        ChambreDAO chambreDAO = new ChambreDAO();
        List<Chambre> chambres = chambreDAO.findByHotel(hotel.getIdhotel());
        for (Chambre ch : chambres) {
            hotel.addChambre(ch);
        }

        // Images d'hôtel
        ImageHotelDAO imageHotelDAO = new ImageHotelDAO();
        List<Image_hotel> imagesHotel = imageHotelDAO.findByHotel(hotel.getIdhotel());
        for (Image_hotel img : imagesHotel) {
            hotel.addImg(img);
        }

        return hotel;
    }

    // ===================== TROUVER TOUS LES HOTELS =====================
    public List<Hotel> findAll() {
        List<Hotel> list = new ArrayList<>();
        String sql = "SELECT * FROM hotels";

        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(mapToHotel(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erreur Hotel findAll : " + e.getMessage());
            e.printStackTrace();
        }

        return list;
    }

    // ===================== AJOUTER UN HOTEL =====================
    public boolean ajouterHotel(Hotel hotel) {
        String sql = "INSERT INTO hotels (nom_hotel, etoiles, id_adresse, id_hotelier) VALUES (?, ?, ?, ?)";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, hotel.getNomHotel());
            ps.setInt(2, hotel.getEtoiles());
            ps.setInt(3, hotel.getAdresse().getIdAdresse());
            ps.setInt(4, hotel.getHotelier().getIdHotelier());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) {
                    hotel.setIdhotel(keys.getInt(1));
                }

                // Ajouter les images
                ImageHotelDAO imageHotelDAO = new ImageHotelDAO();
                for (Image_hotel img : hotel.getImgs()) {
                    img.setHotel(hotel);
                    imageHotelDAO.addImage(img);
                }

                return true;
            }

        } catch (SQLException e) {
            System.err.println("Erreur ajouterHotel : " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    // ===================== METTRE À JOUR UN HOTEL =====================
    public boolean updateHotel(Hotel hotel) {
        String sql = "UPDATE hotels SET nom_hotel=?, etoiles=?, id_adresse=?, id_hotelier=? WHERE id_hotel=?";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, hotel.getNomHotel());
            ps.setInt(2, hotel.getEtoiles());
            ps.setInt(3, hotel.getAdresse().getIdAdresse());
            ps.setInt(4, hotel.getHotelier().getIdHotelier());
            ps.setInt(5, hotel.getIdhotel());

            boolean updated = ps.executeUpdate() > 0;

            // Mettre à jour les images
            ImageHotelDAO imageHotelDAO = new ImageHotelDAO();
            for (Image_hotel img : hotel.getImgs()) {
                if (img.getId() == 0) {
                    img.setHotel(hotel);
                    imageHotelDAO.addImage(img);
                } // sinon tu peux ajouter updateImage si besoin
            }

            return updated;

        } catch (SQLException e) {
            System.err.println("Erreur updateHotel : " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    // ===================== SUPPRIMER UN HOTEL =====================
    public boolean deleteHotel(int idHotel) {
        String sql = "DELETE FROM hotels WHERE id_hotel=?";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, idHotel);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erreur deleteHotel : " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }
}
