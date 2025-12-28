package com.roomy.Dao;


import com.roomy.entities.Reservation;
import com.roomy.entities.Chambre;
import com.roomy.ENUMS.StatutReservation;
import com.roomy.ENUMS.TypeChambre; // Assure-toi d'avoir cet import

import com.roomy.entities.*;
import com.roomy.ENUMS.StatutReservation;
import java.sql.*;
import java.time.LocalDateTime;
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

    public boolean createReservation(Reservation reservation) {
        String sql = "INSERT INTO reservations (id_client, id_chambre, date_debut_sejour, date_fin_sejour, nombre_personnes, montant_total, statut) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setInt(1, reservation.getClient().getIdClient());
            ps.setInt(2, reservation.getChambre().getIdChambre());
            ps.setTimestamp(3, Timestamp.valueOf(reservation.getDateDebutSejour()));
            ps.setTimestamp(4, Timestamp.valueOf(reservation.getDateFinSejour()));
            ps.setInt(5, reservation.getNombrePersonnes());
            ps.setDouble(6, reservation.getMontantTotal());
            ps.setString(7, reservation.getStatut().toString());
            
            int affectedRows = ps.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        reservation.setIdReservation(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erreur création réservation: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    public List<Reservation> getReservationsByClientId(int clientId) {
        System.out.println("🔍 ReservationDAO: Recherche réservations pour client ID: " + clientId);
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT r.*, ch.num_chambre, ch.prix_nuit, ch.type, h.nom_hotel " +
                    "FROM reservations r " +
                    "JOIN chambres ch ON r.id_chambre = ch.id_chambre " +
                    "JOIN hotels h ON ch.id_hotel = h.id_hotel " +
                    "WHERE r.id_client = ? ORDER BY r.date_debut_sejour DESC";
        
        System.out.println("📊 Requête SQL: " + sql);
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, clientId);
            ResultSet rs = ps.executeQuery();
            
            int count = 0;
            while (rs.next()) {
                count++;
                System.out.println("✅ Ligne " + count + " trouvée: Réservation ID=" + rs.getInt("id_reservation"));
                
                Reservation reservation = new Reservation();
                reservation.setIdReservation(rs.getInt("id_reservation"));
                reservation.setDateReservation(rs.getTimestamp("date_reservation").toLocalDateTime());
                reservation.setDateDebutSejour(rs.getTimestamp("date_debut_sejour").toLocalDateTime());
                reservation.setDateFinSejour(rs.getTimestamp("date_fin_sejour").toLocalDateTime());
                reservation.setNombrePersonnes(rs.getInt("nombre_personnes"));
                reservation.setMontantTotal(rs.getDouble("montant_total"));
                reservation.setStatut(StatutReservation.valueOf(rs.getString("statut")));
                
                // Créer les objets liés
                Client client = new Client();
                client.setIdClient(clientId);
                reservation.setClient(client);
                
                Chambre chambre = new Chambre();
                chambre.setIdChambre(rs.getInt("id_chambre"));
                chambre.setNumchambre(rs.getInt("num_chambre"));
                chambre.setPrix_nuit(rs.getDouble("prix_nuit"));
                
                // Gérer le type de chambre
                String typeStr = rs.getString("type");
                if (typeStr != null) {
                    try {
                        chambre.setType(com.roomy.ENUMS.TypeChambre.valueOf(typeStr));
                    } catch (IllegalArgumentException e) {
                        // Type non reconnu, laisser null
                    }
                }
                
                Hotel hotel = new Hotel();
                hotel.setNomHotel(rs.getString("nom_hotel"));
                chambre.setHotel(hotel);
                
                reservation.setChambre(chambre);
                reservations.add(reservation);
            }
            
            System.out.println("📊 Total réservations récupérées: " + reservations.size());
            
        } catch (SQLException e) {
            System.err.println("❌ Erreur SQL récupération réservations: " + e.getMessage());
            e.printStackTrace();
        }
        return reservations;
    }
    
    public boolean annulerReservation(int reservationId) {
        String sql = "UPDATE reservations SET statut = 'ANNULEE' WHERE id_reservation = ?";
        
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            
            ps.setInt(1, reservationId);
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Erreur annulation réservation: " + e.getMessage());
            return false;
        }

    }
}