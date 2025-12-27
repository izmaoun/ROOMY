package com.roomy.Dao;

import com.roomy.entities.Chambre;
import com.roomy.entities.Hotel;
import com.roomy.ENUMS.TypeChambre;
import com.roomy.ENUMS.Statut_technique_Chambre;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChambreDAO {

    public Chambre findById(int id) {
        String sql = "SELECT * FROM chambres WHERE id_chambre = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapToChambre(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erreur findById Chambre: " + e.getMessage());
        }
        return null;
    }

    public List<Chambre> findByHotel(int idHotel) {
        List<Chambre> chambres = new ArrayList<>();
        String sql = "SELECT * FROM chambres WHERE id_hotel = ?";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, idHotel);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                chambres.add(mapToChambre(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur findByHotel Chambre: " + e.getMessage());
        }
        return chambres;
    }

    public boolean ajouterChambre(Chambre ch) {
        String sql = "INSERT INTO chambres (num_chambre, type, prix_nuit, capacity, surface, statut, description, id_hotel) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, ch.getNumchambre());
            // Envoie "Simple", "Double" etc. (Exactement comme dans l'Enum)
            ps.setString(2, ch.getType().name());
            ps.setDouble(3, ch.getPrix_nuit());
            ps.setInt(4, ch.getCapacity());
            ps.setInt(5, ch.getSurface());
            // Envoie "disponible", "en_maintenance" etc.
            ps.setString(6, ch.getStatut().name());
            ps.setString(7, ch.getDescription());

            // Correction: Utilise getIdhotel() (minuscule)
            ps.setInt(8, ch.getHotel().getIdhotel());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) ch.setId(keys.getInt(1));
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erreur ajout Chambre: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateChambre(Chambre ch) {
        String sql = "UPDATE chambres SET num_chambre=?, type=?, prix_nuit=?, capacity=?, surface=?, statut=?, description=?, id_hotel=? "
                + "WHERE id_chambre = ?";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, ch.getNumchambre());
            ps.setString(2, ch.getType().name());
            ps.setDouble(3, ch.getPrix_nuit());
            ps.setInt(4, ch.getCapacity());
            ps.setInt(5, ch.getSurface());
            ps.setString(6, ch.getStatut().name());
            ps.setString(7, ch.getDescription());
            ps.setInt(8, ch.getHotel().getIdhotel());
            ps.setInt(9, ch.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur update Chambre: " + e.getMessage());
        }
        return false;
    }

    public boolean supprimerChambre(int idChambre) {
        String sql = "DELETE FROM chambres WHERE id_chambre = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idChambre);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur delete Chambre: " + e.getMessage());
        }
        return false;
    }


    private Chambre mapToChambre(ResultSet rs) throws SQLException {
        Chambre ch = new Chambre();
        ch.setId(rs.getInt("id_chambre"));
        ch.setNumchambre(rs.getInt("num_chambre"));
        ch.setPrix_nuit(rs.getDouble("prix_nuit"));
        ch.setCapacity(rs.getInt("capacity"));
        ch.setSurface(rs.getInt("surface"));
        ch.setDescription(rs.getString("description"));

        // 1. TYPE
        try {
            // Lecture directe : "Simple" correspond à TypeChambre.Simple
            ch.setType(TypeChambre.valueOf(rs.getString("type")));
        } catch (Exception e) {
            System.err.println("Erreur conversion Enum Type: " + rs.getString("type"));
            ch.setType(TypeChambre.Simple); // Valeur par défaut existante
        }

        // 2. STATUT
        try {
            // Lecture directe : "disponible" correspond à Statut_technique_Chambre.disponible
            ch.setStatut(Statut_technique_Chambre.valueOf(rs.getString("statut")));
        } catch (Exception e) {
            System.err.println("Erreur conversion Enum Statut: " + rs.getString("statut"));
            ch.setStatut(Statut_technique_Chambre.disponible); // Valeur par défaut existante
        }

        // Chargement de l'hôtel
        HotelDAO hotelDAO = new HotelDAO();
        Hotel h = hotelDAO.findById(rs.getInt("id_hotel"));
        ch.setHotel(h);

        return ch;
    }

    // ============================================================
    // Méthodes pour les statistiques du dashboard hotelier
    // ============================================================

    /**
     * Retourne le nombre TOTAL de chambres pour un hôtelier (tous statuts confondus)
     */
    public int getNombreChambresTotal(int idHotelier) {
        String sql = """
            SELECT COUNT(*) FROM chambres c
            JOIN hotels h ON c.id_hotel = h.id_hotel
            WHERE h.id_hotelier = ?
            """;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idHotelier);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            System.err.println("Erreur getNombreChambresTotal: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Retourne le nombre de chambres DISPONIBLES pour un hôtelier
     * (statut = 'disponible')
     */
    public int getNombreChambresDisponibles(int idHotelier) {
        String sql = """
            SELECT COUNT(*) FROM chambres c
            JOIN hotels h ON c.id_hotel = h.id_hotel
            WHERE h.id_hotelier = ? AND c.statut = ?
            """;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idHotelier);
            // Utiliser le nom de l'ENUM pour la comparaison
            ps.setString(2, Statut_technique_Chambre.disponible.name());
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            System.err.println("Erreur getNombreChambresDisponibles: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }
}