package com.roomy.Dao;

import com.roomy.ENUMS.TypeChambre;
import com.roomy.entities.Hotel;
import com.roomy.entities.Reservation;
import com.roomy.entities.Client;
import com.roomy.entities.Chambre;
import com.roomy.ENUMS.StatutReservation;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {

    // CORRECTION : Colonnes correctes
    public boolean ajouterReservation(Reservation r) {
        String sql = "INSERT INTO reservations (id_client, id_chambre, date_reservation, date_debut_sejour, date_fin_sejour, nombre_personnes, montant_total, statut) " +
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
            e.printStackTrace();
        }
        return false;
    }

    // CORRECTION : Requête SQL avec les bonnes colonnes
    public List<Reservation> getReservationsByClientId(int clientId) {
        System.out.println("=== ReservationDAO.getReservationsByClientId() ===");
        System.out.println("Client ID: " + clientId);

        List<Reservation> reservations = new ArrayList<>();

        // CORRECTION : date_debut_sejour au lieu de date_debut
        String sql = "SELECT r.*, c.num_chambre, c.type, h.nom_hotel " +
                "FROM reservations r " +
                "LEFT JOIN chambres c ON r.id_chambre = c.id_chambre " +
                "LEFT JOIN hotels h ON c.id_hotel = h.id_hotel " +
                "WHERE r.id_client = ? " +
                "ORDER BY r.date_debut_sejour DESC";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            System.out.println("Connexion à la base établie: " + (c != null));
            ps.setInt(1, clientId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Reservation reservation = new Reservation();

                // CORRECTION : Colonnes correctes
                reservation.setIdReservation(rs.getInt("id_reservation"));
                reservation.setDateReservation(rs.getTimestamp("date_reservation").toLocalDateTime());

                // CORRECTION : date_debut_sejour au lieu de date_debut
                Timestamp dateDebut = rs.getTimestamp("date_debut_sejour");
                Timestamp dateFin = rs.getTimestamp("date_fin_sejour");

                if (dateDebut != null) {
                    reservation.setDateDebutSejour(dateDebut.toLocalDateTime());
                }

                if (dateFin != null) {
                    reservation.setDateFinSejour(dateFin.toLocalDateTime());
                }

                // CORRECTION : nombre_personnes au lieu de nb_personnes
                reservation.setNombrePersonnes(rs.getInt("nombre_personnes"));
                reservation.setMontantTotal(rs.getDouble("montant_total"));

                // Statut
                String statutStr = rs.getString("statut");
                if (statutStr != null) {
                    try {
                        reservation.setStatut(StatutReservation.valueOf(statutStr));
                    } catch (Exception e) {
                        reservation.setStatut(StatutReservation.CONFIRMEE);
                    }
                } else {
                    reservation.setStatut(StatutReservation.CONFIRMEE);
                }

                // Client
                Client client = new Client();
                client.setIdClient(rs.getInt("id_client"));
                reservation.setClient(client);

                // Chambre
                Chambre chambre = new Chambre();
                chambre.setId(rs.getInt("id_chambre"));
                chambre.setNumchambre(rs.getInt("num_chambre"));

                // Type de chambre
                String typeStr = rs.getString("type");
                if (typeStr != null) {
                    try {
                        chambre.setType(TypeChambre.valueOf(typeStr));
                    } catch (Exception e) {
                        chambre.setType(TypeChambre.Simple);
                    }
                }

                // Hotel
                Hotel hotel = new Hotel();
                hotel.setNomHotel(rs.getString("nom_hotel"));
                chambre.setHotel(hotel);

                reservation.setChambre(chambre);
                reservations.add(reservation);

                System.out.println("Réservation trouvée: ID=" + reservation.getIdReservation() +
                        ", Statut=" + reservation.getStatut() +
                        ", Hotel=" + hotel.getNomHotel());
            }

            System.out.println("Total réservations trouvées: " + reservations.size());

        } catch (SQLException e) {
            System.err.println("ERREUR SQL dans getReservationsByClientId: " + e.getMessage());
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("ERREUR inattendue dans getReservationsByClientId: " + e.getMessage());
            e.printStackTrace();
        }

        return reservations;
    }

    // CORRECTION : Colonnes correctes pour findById
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
                r.setDateDebutSejour(rs.getTimestamp("date_debut_sejour").toLocalDateTime()); // CORRIGÉ
                r.setDateFinSejour(rs.getTimestamp("date_fin_sejour").toLocalDateTime()); // CORRIGÉ
                r.setNombrePersonnes(rs.getInt("nombre_personnes")); // CORRIGÉ
                r.setMontantTotal(rs.getDouble("montant_total"));

                String statutStr = rs.getString("statut");
                if (statutStr != null) {
                    r.setStatut(StatutReservation.valueOf(statutStr));
                } else {
                    r.setStatut(StatutReservation.CONFIRMEE);
                }

                return r;
            }

        } catch (SQLException e) {
            System.err.println("Erreur findById réservation : " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    // ANNULER UNE RÉSERVATION
    public boolean annulerReservation(int idReservation) {
        String sql = "UPDATE reservations SET statut = 'ANNULEE' WHERE id_reservation = ?";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, idReservation);
            int rowsAffected = ps.executeUpdate();
            System.out.println("Réservation annulée, lignes affectées: " + rowsAffected);
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Erreur annulation réservation : " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // AJOUTER CETTE MÉTHODE POUR LE BOUTON CANCEL DU CONTRÔLEUR
    public boolean cancel(int reservationId) {
        return annulerReservation(reservationId);
    }
}