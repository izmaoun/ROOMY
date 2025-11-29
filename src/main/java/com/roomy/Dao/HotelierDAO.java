package com.roomy.Dao;

import com.roomy.entities.Hotelier;
import java.sql.*;

public class HotelierDAO {

    // Trouver un hotelier par email (pour le login)
    public Hotelier findByEmail(String email) {
        String sql = "SELECT * FROM hoteliers WHERE email_gerant = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Hotelier hotelier = new Hotelier();
                hotelier.setIdHotelier(rs.getInt("id_hotelier"));
                hotelier.setNomEtablissement(rs.getString("nom_etablissement"));
                hotelier.setNomGerant(rs.getString("nom_gerant"));
                hotelier.setPrenomGerant(rs.getString("prenom_gerant"));
                hotelier.setVille(rs.getString("ville"));
                hotelier.setEmailGerant(rs.getString("email_gerant"));
                hotelier.setTelephone(rs.getString("telephone"));
                hotelier.setPassword(rs.getString("password"));
                hotelier.setIce(rs.getString("ice"));
                hotelier.setDateInscription(rs.getTimestamp("date_inscription").toLocalDateTime());
                hotelier.setStatutVerification(rs.getString("statut_verification"));
                return hotelier;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche par email: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // Trouver un hotelier par ICE
    public Hotelier findByIce(String ice) {
        String sql = "SELECT * FROM hoteliers WHERE ice = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, ice);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Hotelier hotelier = new Hotelier();
                hotelier.setIdHotelier(rs.getInt("id_hotelier"));
                hotelier.setNomEtablissement(rs.getString("nom_etablissement"));
                hotelier.setNomGerant(rs.getString("nom_gerant"));
                hotelier.setPrenomGerant(rs.getString("prenom_gerant"));
                hotelier.setVille(rs.getString("ville"));
                hotelier.setEmailGerant(rs.getString("email_gerant"));
                hotelier.setTelephone(rs.getString("telephone"));
                hotelier.setPassword(rs.getString("password"));
                hotelier.setIce(rs.getString("ice"));
                hotelier.setDateInscription(rs.getTimestamp("date_inscription").toLocalDateTime());
                hotelier.setStatutVerification(rs.getString("statut_verification"));
                return hotelier;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche par ICE: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // Inscription hotelier
    public boolean signup(Hotelier hotelier) {
        String sql = "INSERT INTO hoteliers (nom_etablissement, nom_gerant, prenom_gerant, ville, email_gerant, telephone, password, ice) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, hotelier.getNomEtablissement());
            ps.setString(2, hotelier.getNomGerant());
            ps.setString(3, hotelier.getPrenomGerant());
            ps.setString(4, hotelier.getVille());
            ps.setString(5, hotelier.getEmailGerant());
            ps.setString(6, hotelier.getTelephone());
            ps.setString(7, hotelier.getPassword()); // déjà hashé avec BCrypt
            ps.setString(8, hotelier.getIce());

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                // Récupérer l'ID généré
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        hotelier.setIdHotelier(generatedKeys.getInt(1));
                    }
                }
                System.out.println("Inscription hotelier réussie - ID: " + hotelier.getIdHotelier());
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erreur inscription hotelier : " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // Mettre à jour le statut de vérification
    public boolean updateStatutVerification(int idHotelier, String statut) {
        String sql = "UPDATE hoteliers SET statut_verification = ? WHERE id_hotelier = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, statut);
            ps.setInt(2, idHotelier);

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Statut de vérification mis à jour pour l'hotelier ID: " + idHotelier);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erreur mise à jour statut vérification : " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // Vérifier si un email existe déjà
    public boolean existsByEmail(String email) {
        return findByEmail(email) != null;
    }

    // Vérifier si un ICE existe déjà
    public boolean existsByIce(String ice) {
        return findByIce(ice) != null;
    }

    // Lister les hoteliers en attente de validation
    public java.util.List<Hotelier> findAllEnAttente() {
        String sql = "SELECT * FROM hoteliers WHERE statut_verification = 'en_attente'";
        java.util.List<Hotelier> result = new java.util.ArrayList<>();
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Hotelier hotelier = new Hotelier();
                hotelier.setIdHotelier(rs.getInt("id_hotelier"));
                hotelier.setNomEtablissement(rs.getString("nom_etablissement"));
                hotelier.setNomGerant(rs.getString("nom_gerant"));
                hotelier.setPrenomGerant(rs.getString("prenom_gerant"));
                hotelier.setVille(rs.getString("ville"));
                hotelier.setEmailGerant(rs.getString("email_gerant"));
                hotelier.setTelephone(rs.getString("telephone"));
                hotelier.setPassword(rs.getString("password"));
                hotelier.setIce(rs.getString("ice"));
                java.sql.Timestamp ts = rs.getTimestamp("date_inscription");
                if (ts != null) hotelier.setDateInscription(ts.toLocalDateTime());
                hotelier.setStatutVerification(rs.getString("statut_verification"));
                result.add(hotelier);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des hoteliers en attente: " + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
}