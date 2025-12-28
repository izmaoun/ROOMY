package com.roomy.Dao;

import com.roomy.entities.Reservation;
import com.roomy.entities.Chambre;
import com.roomy.ENUMS.StatutReservation;
import com.roomy.ENUMS.TypeChambre; // Assure-toi d'avoir cet import

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {

    // Tes autres méthodes (ajouter, update...) restent ici...

    // --- METHODE 1 : Pour les stats globales (Dashboard) ---
    public List<Reservation> findByHotelier(int idHotelier) {
        List<Reservation> list = new ArrayList<>();
        // On joint pour vérifier que l'hôtel appartient bien à l'hôtelier
        String sql = "SELECT r.* FROM reservations r " +
                "JOIN chambres c ON r.id_chambre = c.id_chambre " +
                "JOIN hotels h ON c.id_hotel = h.id_hotel " +
                "WHERE h.id_hotelier = ?";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idHotelier);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapSimpleReservation(rs));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // --- METHODE 2 : Pour les stats détaillées (Page Statistiques) ---
    public List<Reservation> findByHotel(int idHotel) {
        List<Reservation> list = new ArrayList<>();
        // IMPORTANT : On sélectionne aussi le TYPE de la chambre pour le PieChart
        String sql = "SELECT r.*, c.type FROM reservations r " +
                "JOIN chambres c ON r.id_chambre = c.id_chambre " +
                "WHERE c.id_hotel = ?";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idHotel);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Reservation r = mapSimpleReservation(rs);

                // On récupère le type de chambre
                Chambre ch = r.getChambre(); // Créé dans mapSimpleReservation
                try {
                    String typeStr = rs.getString("type");
                    if (typeStr != null) {
                        ch.setType(TypeChambre.valueOf(typeStr));
                    }
                } catch (Exception e) {
                    System.err.println("Type inconnu ou erreur conversion: " + e.getMessage());
                }
                r.setChambre(ch); // Mise à jour

                list.add(r);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // Helper pour éviter de répéter le code de mapping
    private Reservation mapSimpleReservation(ResultSet rs) throws SQLException {
        Reservation r = new Reservation();
        r.setIdReservation(rs.getInt("id_reservation"));
        r.setDateReservation(rs.getTimestamp("date_reservation").toLocalDateTime());
        // Utilise les bons noms de colonnes de ta BDD (date_debut vs date_debut_sejour)
        r.setDateDebutSejour(rs.getTimestamp("date_debut_sejour").toLocalDateTime());
        r.setDateFinSejour(rs.getTimestamp("date_fin_sejour").toLocalDateTime());
        r.setStatut(StatutReservation.valueOf(rs.getString("statut")));

        Chambre ch = new Chambre();
        ch.setId(rs.getInt("id_chambre"));
        r.setChambre(ch);

        return r;
    }
}