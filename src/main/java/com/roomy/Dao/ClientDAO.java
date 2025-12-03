package com.roomy.Dao;

import com.roomy.entities.Client;
import java.sql.*;
import java.util.List;
import java.util.Optional;

    public class ClientDAO {

        // Trouver un client par email (pour le login)
        public Client findByEmail(String email) {
            String sql = "SELECT * FROM clients WHERE email = ?";
            try (Connection c = DBConnection.getConnection();
                 PreparedStatement ps = c.prepareStatement(sql)) {

                ps.setString(1, email);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    Client client = new Client();
                    client.setIdClient(rs.getInt("id_client")); // CORRECTION : id_client au lieu de id
                    client.setNom(rs.getString("nom")); // CORRECTION : nom au lieu de username
                    client.setPrenom(rs.getString("prenom")); // CORRECTION : ajout du prénom
                    client.setEmail(rs.getString("email"));
                    client.setTelephone(rs.getString("telephone"));
                    client.setPassword(rs.getString("password"));
                    client.setDateInscription(rs.getTimestamp("date_inscription").toLocalDateTime());
                    client.setEstBloque(rs.getBoolean("est_bloque"));
                    return client;
                }
            } catch (SQLException e) {
                System.err.println("Erreur lors de la recherche par email: " + e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        // Inscription client
        // Inscription client - CORRIGÉ
        public boolean signup(Client client) {
            if (findByEmail(client.getEmail()) != null) {
                System.err.println("ERREUR: Email déjà utilisé - " + client.getEmail());
                return false;
            }

            String sql = "INSERT INTO clients (nom, prenom, email, telephone, password) VALUES (?, ?, ?, ?, ?)";
            try (Connection c = DBConnection.getConnection();
                 PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                ps.setString(1, client.getNom());
                ps.setString(2, client.getPrenom()); // CORRECTION : ajout du prénom
                ps.setString(3, client.getEmail());
                ps.setString(4, client.getTelephone());
                ps.setString(5, client.getPassword()); // déjà hashé avec BCrypt

                int affectedRows = ps.executeUpdate();

                if (affectedRows > 0) {
                    // Récupérer l'ID généré
                    try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            client.setIdClient(generatedKeys.getInt(1));
                        }
                    }
                    System.out.println("Inscription client réussie - ID: " + client.getIdClient());
                    return true;
                }
            } catch (SQLException e) {
                System.err.println("Erreur inscription client : " + e.getMessage());
                e.printStackTrace();
            }
            return false;
        }
        public boolean updatePassword(String email, String hashedPassword) {
            try (Connection conn = DBConnection.getConnection()) {
                String sql = "UPDATE client SET password = ? WHERE email = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, hashedPassword);
                stmt.setString(2, email);
                stmt.executeUpdate();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

    /**
     * Retourne tous les clients (pour dashboard admin)
     */
    public List<Client> findAll() {
        List<Client> clients = new java.util.ArrayList<>();
        String sql = "SELECT * FROM clients";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Client client = new Client();
                client.setIdClient(rs.getInt("id_client"));
                client.setNom(rs.getString("nom"));
                client.setPrenom(rs.getString("prenom"));
                client.setEmail(rs.getString("email"));
                client.setTelephone(rs.getString("telephone"));
                client.setPassword(rs.getString("password"));
                client.setDateInscription(rs.getTimestamp("date_inscription").toLocalDateTime());
                client.setEstBloque(rs.getBoolean("est_bloque"));
                clients.add(client);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des clients: " + e.getMessage());
            e.printStackTrace();
        }
        return clients;
    }

    /**
     * Met à jour le statut bloqué d'un client
     */
    public boolean updateBlockStatus(int idClient, boolean estBloque) {
        String sql = "UPDATE clients SET est_bloque = ? WHERE id_client = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setBoolean(1, estBloque);
            ps.setInt(2, idClient);
            int affected = ps.executeUpdate();
            return affected > 0;
        } catch (SQLException e) {
            System.err.println("Erreur update block status: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    }
