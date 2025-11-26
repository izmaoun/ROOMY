package com.roomy.Dao;

import com.roomy.entities.Hotelier;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

    public class HotelierDAO {

        // Trouver un hôtelier par email (pour le login)
        public Hotelier findByEmail(String email) {
            String sql = "SELECT * FROM hoteliers WHERE email = ?";
            try (Connection c = DBConnection.getConnection();
                 PreparedStatement ps = c.prepareStatement(sql)) {

                ps.setString(1, email);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    Hotelier h = new Hotelier();
                    h.setId(rs.getInt("id"));
                    h.setNom(rs.getString("username"));           // nom_hotel
                    h.setVille(rs.getString("ville"));
                    h.setEmail(rs.getString("email"));
                    h.setPassword(rs.getString("password")); // hashé
                    h.setId(rs.getInt("id"));
                    return h;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

        // Inscription hôtelier (ton formulaire "Inscription Hôtel")
        public boolean signup(Hotelier hotelier) {
            String sql = "INSERT INTO hoteliers (username, ville, email, password, ice) VALUES (?, ?, ?, ?, ?)";
            try (Connection c = DBConnection.getConnection();
                 PreparedStatement ps = c.prepareStatement(sql)) {

                ps.setString(1, hotelier.getNom());      // nom de l'hôtel
                ps.setString(2, hotelier.getVille());
                ps.setString(3, hotelier.getEmail());
                ps.setString(4, hotelier.getPassword()); // déjà hashé
                ps.setString(5, hotelier.getIce());


                ps.executeUpdate();
                return true;
            } catch (SQLException e) {
                System.out.println("Erreur inscription hôtelier : " + e.getMessage());
                e.printStackTrace();
                return false;
            }
        }
    }
