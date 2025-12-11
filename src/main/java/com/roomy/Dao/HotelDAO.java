package com.roomy.Dao;

import com.roomy.entities.Hotel;
import com.roomy.entities.Adresse;
import com.roomy.entities.Image_hotel;
import com.roomy.entities.Chambre;
import com.roomy.entities.Image_chambre;
import com.roomy.ENUMS.TypeChambre;
import com.roomy.ENUMS.Statut_technique_Chambre;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HotelDAO {

    // 1. RÉCUPÉRER TOUS LES HÔTELS
    public List<Hotel> findAll() {
        List<Hotel> hotels = new ArrayList<>();
        String sql = "SELECT h.*, a.rue, a.ville, a.codepostal, a.pays " +
                "FROM hotels h " +
                "LEFT JOIN adresses a ON h.id_adresse = a.id_adresse " +
                "ORDER BY h.nom_hotel";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Hotel hotel = extractHotel(rs);
                loadHotelImages(hotel);
                loadHotelServices(hotel);
                loadHotelChambres(hotel);
                hotels.add(hotel);
            }

        } catch (SQLException e) {
            System.err.println("❌ Erreur dans findAll(): " + e.getMessage());
            e.printStackTrace();
        }

        return hotels;
    }

    // 2. MÉTHODE DE DÉBOGAGE POUR VOIR LES PRIX
    public void debugHotelPrices() {
        System.out.println("\n═══════════════════════════════════════════════════════");
        System.out.println("           DEBUG: PRIX DES HÔTELS DANS LA BD");
        System.out.println("═══════════════════════════════════════════════════════");

        String sql = "SELECT h.nom_hotel, a.ville, " +
                "MIN(c.prix_nuit) as prix_min, " +
                "MAX(c.prix_nuit) as prix_max, " +
                "COUNT(c.id_chambre) as nb_chambres " +
                "FROM hotels h " +
                "LEFT JOIN adresses a ON h.id_adresse = a.id_adresse " +
                "LEFT JOIN chambres c ON h.id_hotel = c.id_hotel " +
                "WHERE c.statut = 'disponible' " +
                "GROUP BY h.id_hotel, h.nom_hotel, a.ville " +
                "ORDER BY prix_min ASC";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.printf("%-30s %-15s %-10s %-10s %s%n",
                    "HOTEL", "VILLE", "MIN", "MAX", "CHAMBRES");
            System.out.println("───────────────────────────────────────────────────────");

            while (rs.next()) {
                String hotel = rs.getString("nom_hotel");
                String ville = rs.getString("ville");
                Double min = rs.getDouble("prix_min");
                Double max = rs.getDouble("prix_max");
                int count = rs.getInt("nb_chambres");

                System.out.printf("%-30s %-15s $%-8.2f $%-8.2f %d%n",
                        hotel != null ? (hotel.length() > 28 ? hotel.substring(0, 25) + "..." : hotel) : "N/A",
                        ville != null ? ville : "N/A",
                        min,
                        max,
                        count);
            }

        } catch (SQLException e) {
            System.err.println("❌ Erreur dans debugHotelPrices(): " + e.getMessage());
        }
        System.out.println("═══════════════════════════════════════════════════════\n");
    }

    // 3. MÉTHODE CORRIGÉE POUR LE FILTRAGE AVEC PRIX (VERSION STRICTE)
    public List<Hotel> findWithFiltersStrict(String search, String city, Double minPrice,
                                             Double maxPrice, List<Integer> stars,
                                             List<String> services, String roomType) {
        List<Hotel> hotels = new ArrayList<>();

        // Construction de la requête
        StringBuilder sql = new StringBuilder(
                "SELECT DISTINCT h.*, a.rue, a.ville, a.codepostal, a.pays " +
                        "FROM hotels h " +
                        "LEFT JOIN adresses a ON h.id_adresse = a.id_adresse " +
                        "WHERE 1=1"
        );

        List<Object> params = new ArrayList<>();

        // DEBUG: Afficher les paramètres
        System.out.println("\n=== PARAMÈTRES DU FILTRE STRICT ===");
        System.out.println("MinPrice: " + minPrice);
        System.out.println("MaxPrice: " + maxPrice);
        System.out.println("Search: " + search);
        System.out.println("City: " + city);
        System.out.println("RoomType: " + roomType);
        System.out.println("Stars: " + stars);
        System.out.println("Services: " + services);

        // Filtre recherche
        if (search != null && !search.isEmpty()) {
            sql.append(" AND (LOWER(h.nom_hotel) LIKE LOWER(?) OR LOWER(a.ville) LIKE LOWER(?))");
            params.add("%" + search + "%");
            params.add("%" + search + "%");
        }

        // Filtre ville
        if (city != null && !city.isEmpty() && !"All".equals(city)) {
            sql.append(" AND LOWER(a.ville) = LOWER(?)");
            params.add(city);
        }

        // Filtre étoiles
        if (stars != null && !stars.isEmpty()) {
            sql.append(" AND h.etoiles IN (");
            for (int i = 0; i < stars.size(); i++) {
                sql.append(i == 0 ? "?" : ",?");
                params.add(stars.get(i));
            }
            sql.append(")");
        }

        // CORRECTION CRITIQUE: Filtre par prix STRICT
        // On vérifie que le prix MINIMUM de l'hôtel est dans l'intervalle
        if (minPrice != null || maxPrice != null) {
            sql.append(" AND EXISTS (");
            sql.append("   SELECT 1 FROM chambres c ");
            sql.append("   WHERE c.id_hotel = h.id_hotel ");
            sql.append("   AND c.statut = 'disponible' ");

            // Condition pour le prix minimum
            if (minPrice != null) {
                sql.append("   AND c.prix_nuit >= ?");
                params.add(minPrice);
            }

            if (maxPrice != null) {
                sql.append("   AND c.prix_nuit <= ?");
                params.add(maxPrice);
            }

            // Vérifier que c'est le prix MINIMUM de l'hôtel
            sql.append("   AND c.prix_nuit = (");
            sql.append("     SELECT MIN(c2.prix_nuit) FROM chambres c2 ");
            sql.append("     WHERE c2.id_hotel = h.id_hotel ");
            sql.append("     AND c2.statut = 'disponible'");
            sql.append("   )");

            sql.append(")");
        }

        // Filtre type de chambre
        if (roomType != null && !roomType.isEmpty() && !"All".equals(roomType)) {
            sql.append(" AND EXISTS (SELECT 1 FROM chambres c WHERE c.id_hotel = h.id_hotel AND c.type = ?)");
            params.add(roomType);
        }

        // Filtre services
        if (services != null && !services.isEmpty()) {
            // Pour chaque service, vérifier qu'il existe
            for (int i = 0; i < services.size(); i++) {
                sql.append(" AND EXISTS (SELECT 1 FROM hotel_services hs WHERE hs.id_hotel = h.id_hotel AND hs.service_name = ?)");
                params.add(services.get(i));
            }
        }

        // Trier par prix minimum
        sql.append(" ORDER BY (SELECT MIN(c3.prix_nuit) FROM chambres c3 WHERE c3.id_hotel = h.id_hotel AND c3.statut = 'disponible')");

        System.out.println("Requête SQL STRICTE: " + sql.toString());
        System.out.println("Paramètres: " + params);

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            // Définir les paramètres
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Hotel hotel = extractHotel(rs);
                loadHotelImages(hotel);
                loadHotelServices(hotel);
                loadHotelChambres(hotel);
                hotels.add(hotel);
            }

            System.out.println("✅ Nombre d'hôtels trouvés avec filtre strict: " + hotels.size());

        } catch (SQLException e) {
            System.err.println("❌ Erreur dans findWithFiltersStrict(): " + e.getMessage());
            e.printStackTrace();
        }

        return hotels;
    }

    // 4. ANCIENNE MÉTHODE (gardée pour compatibilité)
    public List<Hotel> findWithFilters(String search, String city, Double minPrice,
                                       Double maxPrice, List<Integer> stars,
                                       List<String> services, String roomType) {
        // Appeler la version stricte par défaut
        return findWithFiltersStrict(search, city, minPrice, maxPrice, stars, services, roomType);
    }

    // 5. MÉTHODE POUR TROUVER LES HÔTELS PAR INTERVALLE DE PRIX UNIQUEMENT
    public List<Hotel> findHotelsByPriceRange(double minPrice, double maxPrice) {
        List<Hotel> hotels = new ArrayList<>();

        String sql = "SELECT DISTINCT h.*, a.rue, a.ville, a.codepostal, a.pays " +
                "FROM hotels h " +
                "LEFT JOIN adresses a ON h.id_adresse = a.id_adresse " +
                "WHERE EXISTS ( " +
                "  SELECT 1 FROM chambres c " +
                "  WHERE c.id_hotel = h.id_hotel " +
                "  AND c.statut = 'disponible' " +
                "  AND c.prix_nuit >= ? " +
                "  AND c.prix_nuit <= ? " +
                "  AND c.prix_nuit = ( " +
                "    SELECT MIN(c2.prix_nuit) FROM chambres c2 " +
                "    WHERE c2.id_hotel = h.id_hotel " +
                "    AND c2.statut = 'disponible'" +
                "  )" +
                ") " +
                "ORDER BY (SELECT MIN(c3.prix_nuit) FROM chambres c3 WHERE c3.id_hotel = h.id_hotel AND c3.statut = 'disponible')";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, minPrice);
            stmt.setDouble(2, maxPrice);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Hotel hotel = extractHotel(rs);
                loadHotelImages(hotel);
                loadHotelServices(hotel);
                loadHotelChambres(hotel);
                hotels.add(hotel);
            }

        } catch (SQLException e) {
            System.err.println("❌ Erreur dans findHotelsByPriceRange(): " + e.getMessage());
            e.printStackTrace();
        }

        return hotels;
    }

    // 6. MÉTHODE POUR TESTER LES INTERVALLES DE PRIX
    public void testPriceFilters() {
        System.out.println("\n═══════════════════════════════════════════════════════");
        System.out.println("              TESTS D'INTERVALLES DE PRIX");
        System.out.println("═══════════════════════════════════════════════════════");

        // Test 1: Large intervalle
        testInterval(0, 10000, "Large (0-10000)");

        // Test 2: Intervalle moyen
        testInterval(100, 500, "Moyen (100-500)");

        // Test 3: Petit intervalle
        testInterval(200, 300, "Petit (200-300)");

        // Test 4: Intervalle spécifique
        testInterval(300, 450, "Spécifique (300-450)");

        // Test 5: Intervalle bas
        testInterval(0, 200, "Bas (0-200)");

        // Test 6: Intervalle haut
        testInterval(500, 1000, "Haut (500-1000)");

        // Test 7: Intervalle 2000-3000 (PROBLÈME)
        testInterval(2000, 3000, "PROBLÈME (2000-3000)");
    }

    private void testInterval(double min, double max, String label) {
        List<Hotel> hotels = findHotelsByPriceRange(min, max);
        System.out.println("\nTest: " + label + " ($" + min + " - $" + max + ")");
        System.out.println("Hôtels trouvés: " + hotels.size());

        for (Hotel hotel : hotels) {
            double hotelMinPrice = getHotelMinPrice(hotel);
            System.out.println("  • " + hotel.getNomHotel() + " - Prix min: $" + hotelMinPrice);
        }
    }

    // 7. MÉTHODE UTILITAIRE POUR OBTENIR LE PRIX MINIMUM D'UN HÔTEL
    private double getHotelMinPrice(Hotel hotel) {
        double minPrice = Double.MAX_VALUE;
        for (Chambre chambre : hotel.getChambres()) {
            if (chambre.getPrix_nuit() < minPrice) {
                minPrice = chambre.getPrix_nuit();
            }
        }
        return minPrice == Double.MAX_VALUE ? 0.0 : minPrice;
    }

    // 8. RECHERCHE D'HÔTELS
    public List<Hotel> searchHotels(String searchTerm) {
        List<Hotel> hotels = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
                "SELECT DISTINCT h.*, a.rue, a.ville, a.codepostal, a.pays " +
                        "FROM hotels h " +
                        "LEFT JOIN adresses a ON h.id_adresse = a.id_adresse " +
                        "LEFT JOIN chambres c ON h.id_hotel = c.id_hotel " +
                        "WHERE (LOWER(h.nom_hotel) LIKE LOWER(?) " +
                        "   OR LOWER(a.ville) LIKE LOWER(?) " +
                        "   OR LOWER(a.pays) LIKE LOWER(?) " +
                        "   OR LOWER(a.rue) LIKE LOWER(?)) " +
                        "GROUP BY h.id_hotel, a.rue, a.ville, a.codepostal, a.pays " +
                        "ORDER BY h.etoiles DESC"
        );

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            String likePattern = "%" + searchTerm + "%";
            stmt.setString(1, likePattern);
            stmt.setString(2, likePattern);
            stmt.setString(3, likePattern);
            stmt.setString(4, likePattern);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Hotel hotel = extractHotel(rs);
                loadHotelImages(hotel);
                loadHotelServices(hotel);
                loadHotelChambres(hotel);
                hotels.add(hotel);
            }

        } catch (SQLException e) {
            System.err.println("❌ Erreur dans searchHotels(): " + e.getMessage());
            e.printStackTrace();
        }

        return hotels;
    }

    // 9. TROUVER PAR ID
    public Hotel findById(int hotelId) {
        String sql = "SELECT h.*, a.rue, a.ville, a.codepostal, a.pays " +
                "FROM hotels h " +
                "LEFT JOIN adresses a ON h.id_adresse = a.id_adresse " +
                "WHERE h.id_hotel = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, hotelId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Hotel hotel = extractHotel(rs);
                loadHotelImages(hotel);
                loadHotelServices(hotel);
                loadHotelChambres(hotel);
                return hotel;
            }

        } catch (SQLException e) {
            System.err.println("❌ Erreur dans findById(): " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    // 10. EXTRACTION HÔTEL
    private Hotel extractHotel(ResultSet rs) throws SQLException {
        Hotel hotel = new Hotel();
        hotel.setIdhotel(rs.getInt("id_hotel"));
        hotel.setNomHotel(rs.getString("nom_hotel"));
        hotel.setEtoiles(rs.getInt("etoiles"));

        // Description (vérifie si la colonne existe)
        try {
            hotel.setDescription(rs.getString("description"));
        } catch (SQLException e) {
            hotel.setDescription("Hôtel de qualité avec services premium.");
        }

        // Adresse
        Adresse adresse = new Adresse();
        adresse.setRue(rs.getString("rue"));
        adresse.setVille(rs.getString("ville"));
        adresse.setCodepostal(rs.getString("codepostal"));
        adresse.setPays(rs.getString("pays"));
        hotel.setAdresse(adresse);

        return hotel;
    }

    // 11. CHARGER IMAGES HÔTEL
    private void loadHotelImages(Hotel hotel) throws SQLException {
        String sql = "SELECT * FROM hotel_images WHERE id_hotel = ? ORDER BY id_image";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, hotel.getIdhotel());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Image_hotel img = new Image_hotel();
                img.setId(rs.getInt("id_image"));
                img.setUrl(rs.getString("url"));
                img.setDescription(rs.getString("description"));
                hotel.addImg(img);
            }
        }
    }

    // 12. CHARGER SERVICES HÔTEL
    private void loadHotelServices(Hotel hotel) throws SQLException {
        String sql = "SELECT service_name FROM hotel_services WHERE id_hotel = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, hotel.getIdhotel());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String service = rs.getString("service_name");
                if (service != null && !service.trim().isEmpty()) {
                    hotel.getServices().add(service);
                }
            }
        } catch (SQLException e) {
            // Services par défaut si pas de table
            hotel.getServices().addAll(List.of("Wi-Fi", "Parking", "Petit déjeuner"));
        }
    }

    // 13. CHARGER CHAMBRES HÔTEL
    private void loadHotelChambres(Hotel hotel) throws SQLException {
        String sql = "SELECT * FROM chambres WHERE id_hotel = ? AND statut = 'disponible' ORDER BY prix_nuit";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, hotel.getIdhotel());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Chambre chambre = extractChambre(rs);
                chambre.setHotel(hotel);
                loadChambreImages(chambre);
                hotel.addChambre(chambre);
            }
        }
    }

    // 14. EXTRACTION CHAMBRE
    private Chambre extractChambre(ResultSet rs) throws SQLException {
        Chambre chambre = new Chambre();
        chambre.setId(rs.getInt("id_chambre"));
        chambre.setNumchambre(rs.getInt("num_chambre"));

        // TypeChambre
        String typeStr = rs.getString("type");
        if (typeStr != null) {
            try {
                chambre.setType(TypeChambre.valueOf(typeStr));
            } catch (IllegalArgumentException e) {
                chambre.setType(TypeChambre.Simple);
            }
        }

        chambre.setPrix_nuit(rs.getDouble("prix_nuit"));
        chambre.setCapacity(rs.getInt("capacity"));
        chambre.setSurface(rs.getInt("surface"));

        // Statut
        String statutStr = rs.getString("statut");
        if (statutStr != null) {
            try {
                // Convertir en minuscules pour votre enum
                statutStr = statutStr.toLowerCase();
                chambre.setStatut(Statut_technique_Chambre.valueOf(statutStr));
            } catch (IllegalArgumentException e) {
                chambre.setStatut(Statut_technique_Chambre.disponible);
            }
        }

        chambre.setDescription(rs.getString("description"));
        return chambre;
    }

    // 15. CHARGER IMAGES CHAMBRE
    private void loadChambreImages(Chambre chambre) throws SQLException {
        String sql = "SELECT * FROM chambre_images WHERE id_chambre = ? ORDER BY id_image";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, chambre.getId());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Image_chambre img = new Image_chambre();
                img.setId(rs.getInt("id_image"));
                img.setUrl(rs.getString("url"));
                img.setDescription(rs.getString("description"));
                chambre.addImg(img);
            }
        }
    }

    // 16. VILLES DISTINCTES
    public List<String> getDistinctCities() {
        List<String> cities = new ArrayList<>();
        String sql = "SELECT DISTINCT a.ville FROM hotels h " +
                "LEFT JOIN adresses a ON h.id_adresse = a.id_adresse " +
                "WHERE a.ville IS NOT NULL " +
                "ORDER BY a.ville";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String ville = rs.getString("ville");
                if (ville != null && !ville.trim().isEmpty()) {
                    cities.add(ville);
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Erreur dans getDistinctCities(): " + e.getMessage());
            // Villes par défaut
            cities.addAll(List.of("Paris", "Lyon", "Marseille", "Nice", "Bordeaux", "Toulouse", "Strasbourg"));
        }

        return cities;
    }

    // 17. TYPES DE CHAMBRES DISPONIBLES
    public List<String> getDistinctRoomTypes() {
        List<String> types = new ArrayList<>();
        String sql = "SELECT DISTINCT type FROM chambres WHERE statut = 'disponible' ORDER BY type";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String type = rs.getString("type");
                if (type != null) {
                    types.add(type);
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Erreur dans getDistinctRoomTypes(): " + e.getMessage());
            types.addAll(List.of("Simple", "Double", "Suite", "Familiale"));
        }

        return types;
    }
}