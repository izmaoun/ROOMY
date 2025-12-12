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
            ps.setString(2, ch.getType().name());
            ps.setDouble(3, ch.getPrix_nuit());
            ps.setInt(4, ch.getCapacity());
            ps.setInt(5, ch.getSurface());
            ps.setString(6, ch.getStatut().name());
            ps.setString(7, ch.getDescription());
            ps.setInt(8, ch.getHotel().getIdhotel());

            int rows = ps.executeUpdate();

            if (rows > 0) {
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) ch.setId(keys.getInt(1));
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Erreur ajout Chambre: " + e.getMessage());
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

    // ============================================================
    // 5️⃣ SUPPRIMER UNE CHAMBRE
    // ============================================================
    public boolean deleteChambre(int idChambre) {
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

    // ============================================================
    // 6️⃣ MAPPER RESULTSET → OBJET CHAMBRE
    // ============================================================
    private Chambre mapToChambre(ResultSet rs) throws SQLException {
        Chambre ch = new Chambre();
        ch.setId(rs.getInt("id_chambre"));
        ch.setNumchambre(rs.getInt("num_chambre"));
        ch.setType(TypeChambre.valueOf(rs.getString("type")));
        ch.setPrix_nuit(rs.getDouble("prix_nuit"));
        ch.setCapacity(rs.getInt("capacity"));
        ch.setSurface(rs.getInt("surface"));
        ch.setStatut(Statut_technique_Chambre.valueOf(rs.getString("statut")));
        ch.setDescription(rs.getString("description"));

        // Charger hotel via HotelDAO
        HotelDAO hotelDAO = new HotelDAO();
        Hotel h = hotelDAO.findById(rs.getInt("id_hotel"));
        ch.setHotel(h);

        return ch;
    }
}

