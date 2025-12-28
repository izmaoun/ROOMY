package com.roomy.Dao;

import com.roomy.entities.Client;
import java.sql.*;
import java.util.List;
import java.util.Optional;

<<<<<<< HEAD
    public class ClientDAO {
        public Client findById(int id) {
            String sql = "SELECT * FROM clients WHERE id_client = ?";

            try (Connection c = DBConnection.getConnection();
                 PreparedStatement ps = c.prepareStatement(sql)) {

                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    Client client = new Client();
                    client.setIdClient(rs.getInt("id_client"));
                    client.setNom(rs.getString("nom"));
                    client.setPrenom(rs.getString("prenom"));
                    client.setEmail(rs.getString("email"));
                    client.setTelephone(rs.getString("telephone"));
                    client.setPassword(rs.getString("password"));
                    client.setDateInscription(rs.getTimestamp("date_inscription").toLocalDateTime());
                    client.setEstBloque(rs.getBoolean("est_bloque"));
                    return client;
                }
            } catch (SQLException e) {
                System.err.println("Erreur lors du findById Client : " + e.getMessage());
            }

            return null;
        }

=======
public class ClientDAO {
    public Client findById(int id) {
        String sql = "SELECT * FROM clients WHERE id_client = ?";
>>>>>>> 146ddc43664c4b11e5d3f96cac87047998ebacd1

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Client client = new Client();
                client.setIdClient(rs.getInt("id_client"));
                client.setNom(rs.getString("nom"));
                client.setPrenom(rs.getString("prenom"));
                client.setEmail(rs.getString("email"));
                client.setTelephone(rs.getString("telephone"));
                client.setPassword(rs.getString("password"));
                
                // Gérer le cas où date_inscription peut être NULL
                Timestamp dateInscription = rs.getTimestamp("date_inscription");
                if (dateInscription != null) {
                    client.setDateInscription(dateInscription.toLocalDateTime());
                }
                
                client.setEstBloque(rs.getBoolean("est_bloque"));
                return client;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors du findById Client : " + e.getMessage());
        }

        return null;
    }


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
                
                // Gérer le cas où date_inscription peut être NULL
                Timestamp dateInscription = rs.getTimestamp("date_inscription");
                if (dateInscription != null) {
                    client.setDateInscription(dateInscription.toLocalDateTime());
                }
                
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

    public boolean update(Client client) {
        System.out.println("=== ClientDAO.update() appelé ===");
        System.out.println("Client à mettre à jour:");
        System.out.println("  ID: " + client.getIdClient());
        System.out.println("  Nom: " + client.getNom());
        System.out.println("  Prénom: " + client.getPrenom());
        System.out.println("  Email: " + client.getEmail());
        System.out.println("  Téléphone: " + client.getTelephone());

        if (client.getIdClient() <= 0) {
            System.err.println("ERREUR: ID client invalide: " + client.getIdClient());
            return false;
        }

        String sql = "UPDATE clients SET nom = ?, prenom = ?, telephone = ? WHERE id_client = ?";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            System.out.println("Connexion à la base établie pour update");
            System.out.println("Exécution de la requête: " + sql);

            ps.setString(1, client.getNom());
            ps.setString(2, client.getPrenom());
            ps.setString(3, client.getTelephone());
            ps.setInt(4, client.getIdClient());

            int affectedRows = ps.executeUpdate();
            System.out.println("Lignes affectées par la mise à jour: " + affectedRows);

            if (affectedRows > 0) {
                System.out.println("SUCCÈS: Client mis à jour avec succès - ID: " + client.getIdClient());
                return true;
            } else {
                System.out.println("ATTENTION: Aucune ligne mise à jour. Vérifiez l'ID client.");
                System.out.println("Vérifiez que le client avec ID=" + client.getIdClient() + " existe dans la table.");
            }

        } catch (SQLException e) {
            System.err.println("ERREUR SQL lors de la mise à jour: " + e.getMessage());
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            e.printStackTrace();

            // Afficher plus de détails sur l'erreur SQL
            if (e.getMessage().contains("Table") || e.getMessage().contains("doesn't exist")) {
                System.err.println("PROBLÈME: La table 'clients' n'existe pas ou a un nom différent.");
            }
            if (e.getMessage().contains("Unknown column")) {
                System.err.println("PROBLÈME: Une colonne n'existe pas dans la table.");
            }
        } catch (Exception e) {
            System.err.println("ERREUR inattendue lors de la mise à jour: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    public boolean updateEmail(int clientId, String newEmail) {
        System.out.println("=== ClientDAO.updateEmail() ===");
        System.out.println("Client ID: " + clientId);
        System.out.println("Nouvel email: " + newEmail);

        String sql = "UPDATE clients SET email = ? WHERE id_client = ?";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, newEmail);
            ps.setInt(2, clientId);

            int affectedRows = ps.executeUpdate();
            System.out.println("Lignes affectées: " + affectedRows);

            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Erreur SQL updateEmail: " + e.getMessage());
            e.printStackTrace();
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

<<<<<<< HEAD
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

    /**
     * Compte le nombre total de clients
     */
    public int countAll() {
        String sql = "SELECT COUNT(*) as total FROM clients";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("Erreur count clients: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Compte le nombre d'inscriptions de clients par mois pour une année donnée
     * Retourne un Map avec le mois (1-12) comme clé et le nombre d'inscriptions comme valeur
     */
    public java.util.Map<Integer, Integer> countByMonthForYear(int year) {
        java.util.Map<Integer, Integer> result = new java.util.HashMap<>();
        // Initialiser tous les mois à 0
        for (int i = 1; i <= 12; i++) {
            result.put(i, 0);
        }

        String sql = "SELECT MONTH(date_inscription) as mois, COUNT(*) as total " +
                     "FROM clients " +
                     "WHERE YEAR(date_inscription) = ? " +
                     "GROUP BY MONTH(date_inscription)";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, year);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.put(rs.getInt("mois"), rs.getInt("total"));
            }
        } catch (SQLException e) {
            System.err.println("Erreur count by month: " + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
}
=======

    // Met à jour le mot de passe d'un client

    public boolean updatePassword(int clientId, String hashedPassword) {
        System.out.println("=== ClientDAO.updatePassword() ===");
        System.out.println("Client ID: " + clientId);

        String sql = "UPDATE clients SET password = ? WHERE id_client = ?";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, hashedPassword);
            ps.setInt(2, clientId);

            int affectedRows = ps.executeUpdate();
            System.out.println("Lignes affectées: " + affectedRows);

            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Erreur SQL updatePassword: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
>>>>>>> 146ddc43664c4b11e5d3f96cac87047998ebacd1
