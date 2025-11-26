package com.roomy.Dao;

import com.roomy.entities.Client;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
                    client.setId(rs.getInt("id"));
                    client.setNom(rs.getString("username"));
//                    client.setPrenom(rs.getString("prenom"));
                    client.setEmail(rs.getString("email"));
                    client.setTelephone(rs.getString("telephone"));
                    client.setPassword(rs.getString("password")); // déjà hashé
                    return client;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

        // Inscription client
        public boolean signup(Client client) {
            String sql = "INSERT INTO clients (username, email, telephone, password) VALUES (?, ?, ?, ?)";
            try (Connection c = DBConnection.getConnection();
                 PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setString(1, client.getNom());
//                ps.setString(2, client.getPrenom());Erreur inscription clien
                ps.setString(2, client.getEmail());
                ps.setString(3, client.getTelephone());
                ps.setString(4, client.getPassword()); // déjà hashé avec BCrypt
                System.out.println("voila inscrription client");

                ps.executeUpdate();
                System.out.println("inscription client réussie");
                return true;
            } catch (SQLException e) {
                System.out.println("Erreur inscription client : " + e.getMessage());
                return false;
            }
        }
    }