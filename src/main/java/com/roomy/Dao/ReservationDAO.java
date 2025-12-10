package com.roomy.Dao;

import com.roomy.entities.Reservation;
import com.roomy.entities.Client;
import com.roomy.entities.Chambre;
import com.roomy.ENUMS.StatutReservation;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {
    public boolean ajouterReservation(Reservation r) {
        String sql = "INSERT INTO reservations (id_client, id_chambre, date_reservation, date_debut, date_fin, nb_personnes, montant_total, statut) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, r.getClient().getIdClient());
            ps.setInt(2, r.getChambre().getId());
            ps.setTimestamp(3, Timestamp.valueOf(r.getDateReservation()));
            ps.setTimestamp(4, Timestamp.valueOf(r.getDateDebutSejour()));
            ps.setTimestamp(5, Timestamp.valueOf(r.getDateFinSejour()));
            ps.setInt(6, r.getNombrePersonnes());
            ps.setDouble(7, r.getMontantTotal());
            ps.setString(8, r.getStatut().name());

            int rows = ps.executeUpdate();

            if (rows > 0) {
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) {
                    r.setIdReservation(keys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erreur ajout réservation : " + e.getMessage());
        }
        return false;
    }

    public Reservation findById(int id) {
        String sql = "SELECT * FROM reservations WHERE id_reservation = ?";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Reservation r = new Reservation();

                r.setIdReservation(rs.getInt("id_reservation"));

                // Charger Client et Chambre via leurs DAO
                ClientDAO clientDAO = new ClientDAO();
                ChambreDAO chambreDAO = new ChambreDAO();

                Client client = clientDAO.findById(rs.getInt("id_client"));
                Chambre chambre = chambreDAO.findById(rs.getInt("id_chambre"));

                r.setClient(client);
                r.setChambre(chambre);

                r.setDateReservation(rs.getTimestamp("date_reservation").toLocalDateTime());
                r.setDateDebutSejour(rs.getTimestamp("date_debut").toLocalDateTime());
                r.setDateFinSejour(rs.getTimestamp("date_fin").toLocalDateTime());
                r.setNombrePersonnes(rs.getInt("nb_personnes"));
                r.setMontantTotal(rs.getDouble("montant_total"));
                r.setStatut(StatutReservation.valueOf(rs.getString("statut")));

                return r;
            }

        } catch (SQLException e) {
            System.err.println("Erreur findById réservation : " + e.getMessage());
        }

        return null;
    }

    // ------------------------------
    // 3️⃣ ANNULER UNE RÉSERVATION
    // ------------------------------
    public boolean annulerReservation(int idReservation) {
        String sql = "UPDATE reservations SET statut = 'ANNULEE' WHERE id_reservation = ?";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, idReservation);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erreur annulation réservation : " + e.getMessage());
        }
        return false;
    }
}
