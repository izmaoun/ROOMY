package com.roomy.Dao;

import com.roomy.entities.Adresse;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdresseDAO {
    // Méthode pour trouver une adresse par son ID
    public Adresse findById(int id) {
        String sql = "SELECT * FROM adresses WHERE id_adresse = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Adresse a = new Adresse();
                a.setIdAdresse(rs.getInt("id_adresse"));
                a.setRue(rs.getString("rue"));
                a.setVille(rs.getString("ville"));
                a.setCodepostal(rs.getString("codepostal"));
                a.setPays(rs.getString("pays"));
                return a;
            }

        } catch (SQLException e) {
            System.err.println("Erreur findById Adresse : " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    //Methode pour ajouter une adresse
    public boolean ajouterAdresse(Adresse a) {
        String sql = "INSERT INTO adresses (rue, ville, codepostal, pays) VALUES (?, ?, ?, ?)";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, a.getRue());
            ps.setString(2, a.getVille());
            ps.setString(3, a.getCodepostal());
            ps.setString(4, a.getPays());

            int rows = ps.executeUpdate();

            if (rows > 0) {
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) {
                    a.setIdAdresse(keys.getInt(1));  // récupère l'id généré
                }
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Erreur ajouterAdresse : " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    //Methode pour mettre à jour une adresse
    public boolean updateAdresse(Adresse a) {
        String sql = "UPDATE adresses SET rue=?, ville=?, codepostal=?, pays=? WHERE id_adresse=?";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, a.getRue());
            ps.setString(2, a.getVille());
            ps.setString(3, a.getCodepostal());
            ps.setString(4, a.getPays());
            ps.setInt(5, a.getIdAdresse());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erreur updateAdresse : " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    //Methode pour supprimer une adresse
    public boolean deleteAdresse(int id) {
        String sql = "DELETE FROM adresses WHERE id_adresse=?";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erreur deleteAdresse : " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    //Methode pour trouver toutes les adresses
    public List<Adresse> findAll() {
        List<Adresse> list = new ArrayList<>();
        String sql = "SELECT * FROM adresses";

        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Adresse a = new Adresse();
                a.setIdAdresse(rs.getInt("id_adresse"));
                a.setRue(rs.getString("rue"));
                a.setVille(rs.getString("ville"));
                a.setCodepostal(rs.getString("codepostal"));
                a.setPays(rs.getString("pays"));
                list.add(a);
            }

        } catch (SQLException e) {
            System.err.println("Erreur findAll Adresse : " + e.getMessage());
            e.printStackTrace();
        }

        return list;
    }
}
