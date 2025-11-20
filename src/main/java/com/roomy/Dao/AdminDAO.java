package com.roomy.Dao;

import com.roomy.entities.Admin;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class AdminDAO {

    public Admin find(String username) {
        String sql = "SELECT * FROM administrateurs WHERE username=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Admin a = new Admin();
                a.setId(rs.getInt("id"));
                a.setUsername(rs.getString("username"));
                a.setPassword(rs.getString("password"));
                return a;
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    public boolean signup(Admin admin) {
        String sql = "INSERT INTO administrateurs (username, password) VALUES (?, ?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, admin.getUsername());
            ps.setString(2, admin.getPassword());
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("Erreur lors de l'inscription admin : " + e.getMessage());
            return false;
        }
    }
}
