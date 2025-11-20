package com.roomy.Dao;

import com.roomy.entities.Hotelier;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class HotelierDAO {

    public Hotelier find(String username) {
        String sql = "SELECT * FROM hoteliers WHERE username=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Hotelier h = new Hotelier();
                h.setId(rs.getInt("id"));
                h.setUsername(rs.getString("username"));
                h.setPassword(rs.getString("password"));
                return h;
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    public boolean signup(Hotelier hotelier) {
        String sql = "INSERT INTO hoteliers (username, password) VALUES (?, ?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, hotelier.getUsername());
            ps.setString(2, hotelier.getPassword());
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("Erreur lors de l'inscription hotelier : " + e.getMessage());
            return false;
        }
    }
}
