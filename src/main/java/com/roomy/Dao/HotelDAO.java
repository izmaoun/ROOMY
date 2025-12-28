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

    private Hotel mapToHotel(ResultSet rs) throws SQLException {
        Hotel hotel = new Hotel();

        hotel.setIdhotel(rs.getInt("id_hotel"));
        hotel.setNomHotel(rs.getString("nom_hotel"));
        hotel.setDescription(rs.getString("description"));
        hotel.setEtoiles(rs.getInt("etoiles"));

        int idAdresse = rs.getInt("id_adresse");
        if (idAdresse > 0) {
            AdresseDAO adresseDAO = new AdresseDAO();
            Adresse adresse = adresseDAO.findById(idAdresse);
            if (adresse != null) hotel.setAdresse(adresse);
        }

        int idHotelier = rs.getInt("id_hotelier");
        if (idHotelier > 0) {
            HotelierDAO hotelierDAO = new HotelierDAO();
            Hotelier hotelier = hotelierDAO.findById(idHotelier);
            if (hotelier != null) hotel.setHotelier(hotelier);
        }

        return hotel;
    }

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

    public boolean ajouterHotel(Hotel hotel) {
        String sql = "INSERT INTO hotels (nom_hotel, description, etoiles, id_adresse, id_hotelier) VALUES (?, ?, ?, ?, ?)";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, hotel.getNomHotel());
            ps.setString(2, hotel.getDescription());
            ps.setInt(3, hotel.getEtoiles());
            ps.setInt(4, hotel.getAdresse().getIdAdresse());
            ps.setInt(5, hotel.getHotelier().getIdHotelier());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) {
                    hotel.setIdhotel(keys.getInt(1));
                }

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

    public boolean updateHotel(Hotel hotel) {
        String sql = "UPDATE hotels SET nom_hotel=?, description=?, etoiles=?, id_adresse=?, id_hotelier=? WHERE id_hotel=?";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, hotel.getNomHotel());
            ps.setString(2, hotel.getDescription());
            ps.setInt(3, hotel.getEtoiles());
            ps.setInt(4, hotel.getAdresse().getIdAdresse());
            ps.setInt(5, hotel.getHotelier().getIdHotelier());
            ps.setInt(6, hotel.getIdhotel());

            boolean updated = ps.executeUpdate() > 0;

            ImageHotelDAO imageHotelDAO = new ImageHotelDAO();
            for (Image_hotel img : hotel.getImgs()) {
                if (img.getId() == 0) {
                    img.setHotel(hotel);
                    imageHotelDAO.addImage(img);
                }
            }

            return updated;

        } catch (SQLException e) {
            System.err.println("Erreur updateHotel : " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

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

    public int getNombreHotels(int idHotelier) {
        String sql = "SELECT COUNT(*) FROM hotels WHERE id_hotelier = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idHotelier);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public List<Hotel> getHotelsByHotelier(int idHotelier) {
        List<Hotel> hotels = new ArrayList<>();
        String sql = "SELECT * FROM hotels WHERE id_hotelier = ?";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, idHotelier);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Hotel hotel = mapToHotel(rs);
                List<Image_hotel> images = loadHotelImages(hotel.getIdhotel());
                hotel.setImgs(images);
                hotels.add(hotel);
            }

        } catch (SQLException e) {
            System.err.println("Erreur getHotelsByHotelier : " + e.getMessage());
        }

        return hotels;
    }

    public List<Image_hotel> loadHotelImages(int idHotel) {
        List<Image_hotel> images = new ArrayList<>();
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
                images.add(img);
            }

        } catch (SQLException e) {
            System.err.println("Erreur loadHotelImages : " + e.getMessage());
        }

        return images;
    }

    public boolean ajouterChambre(Chambre c) {
        String sql = "INSERT INTO chambres (num_chambre, type, prix_nuit, capacity, surface, statut, description, id_hotel) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, c.getNumchambre());
            ps.setString(2, c.getType().name());
            ps.setDouble(3, c.getPrix_nuit());
            ps.setInt(4, c.getCapacity());
            ps.setInt(5, c.getSurface());
            ps.setString(6, c.getStatut().name());
            ps.setString(7, c.getDescription());
            ps.setInt(8, c.getHotel().getIdhotel());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean supprimerChambre(int idChambre) {
        String sql = "DELETE FROM chambres WHERE id_chambre = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idChambre);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getNombreChambres(int idHotel) {
        String sql = "SELECT COUNT(*) FROM chambres WHERE id_hotel = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idHotel);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
}