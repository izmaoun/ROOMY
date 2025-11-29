package com.roomy.Dao;

import com.roomy.entities.Admin;
import java.sql.*;

public class AdminDAO {

    // Trouver un administrateur par username (pour le login)
    public Admin findByUsername(String username) {
        String sql = "SELECT * FROM administrateurs WHERE username = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Admin admin = new Admin();
                admin.setIdAdmin(rs.getInt("id_admin"));
                admin.setUsername(rs.getString("username"));
                admin.setPassword(rs.getString("password"));
                admin.setEmail(rs.getString("email"));
                return admin;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche par username: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // Trouver un administrateur par email
    public Admin findByEmail(String email) {
        String sql = "SELECT * FROM administrateurs WHERE email = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Admin admin = new Admin();
                admin.setIdAdmin(rs.getInt("id_admin"));
                admin.setUsername(rs.getString("username"));
                admin.setPassword(rs.getString("password"));
                admin.setEmail(rs.getString("email"));
                return admin;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche par email: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // Créer un nouvel administrateur
    /*public boolean create(Admin admin) {
        String sql = "INSERT INTO administrateurs (username, password, email) VALUES (?, ?, ?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, admin.getUsername());
            ps.setString(2, admin.getPassword()); // déjà hashé avec BCrypt
            ps.setString(3, admin.getEmail());

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                // Récupérer l'ID généré
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        admin.setIdAdmin(generatedKeys.getInt(1));
                    }
                }
                System.out.println("Création administrateur réussie - ID: " + admin.getIdAdmin());
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erreur création administrateur : " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }*/

    // Mettre à jour un administrateur
    public boolean update(Admin admin) {
        String sql = "UPDATE administrateurs SET username = ?, password = ?, email = ? WHERE id_admin = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, admin.getUsername());
            ps.setString(2, admin.getPassword());
            ps.setString(3, admin.getEmail());
            ps.setInt(4, admin.getIdAdmin());

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Administrateur mis à jour - ID: " + admin.getIdAdmin());
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erreur mise à jour administrateur : " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // Supprimer un administrateur
    public boolean delete(int idAdmin) {
        String sql = "DELETE FROM administrateurs WHERE id_admin = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, idAdmin);

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Administrateur supprimé - ID: " + idAdmin);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erreur suppression administrateur : " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // Vérifier si un username existe déjà
    public boolean existsByUsername(String username) {
        return findByUsername(username) != null;
    }

    // Vérifier si un email existe déjà
    public boolean existsByEmail(String email) {
        return findByEmail(email) != null;
    }
}