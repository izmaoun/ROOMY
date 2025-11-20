package com.roomy.Dao;

import com.roomy.entities.Client;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ClientDAO {

    public Client find(String username) {
        String sql = "SELECT * FROM clients WHERE username=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Client cl = new Client();
                cl.setId(rs.getInt("id"));
                cl.setUsername(rs.getString("username"));
                cl.setPassword(rs.getString("password"));
                return cl;
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    public boolean signup(Client client) {
        String sql = "INSERT INTO clients (username, password) VALUES (?, ?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, client.getUsername());
            ps.setString(2, client.getPassword());
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("Erreur lors de l'inscription : " + e.getMessage());
            return false;
        }
    }
}
