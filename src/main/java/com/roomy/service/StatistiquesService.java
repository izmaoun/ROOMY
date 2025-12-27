package com.roomy.service;

import com.roomy.Dao.HotelDAO;
import com.roomy.Dao.HotelierDAO;
import com.roomy.Dao.ReservationDAO;
import com.roomy.entities.Hotel;
import com.roomy.entities.Reservation;
import com.roomy.ENUMS.StatutReservation;
import com.roomy.dto.HotelStats;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatistiquesService {

    private final HotelDAO hotelDAO;
    private final HotelierDAO hotelierDAO;
    private final ReservationDAO reservationDAO;

    public StatistiquesService() {
        this.hotelDAO = new HotelDAO();
        this.hotelierDAO = new HotelierDAO();
        this.reservationDAO = new ReservationDAO();
    }

    // --- 1. STATS GLOBALES (DASHBOARD) ---
    public HotelStats getGlobalStats(int idHotelier) {
        HotelStats stats = new HotelStats();

        List<Hotel> hotels = hotelDAO.getHotelsByHotelier(idHotelier);
        stats.setNbHotels(hotels.size());

        // Calcul total chambres (via la liste chargée)
        int totalChambres = 0;
        for (Hotel h : hotels) {
            if(h.getChambres() != null) totalChambres += h.getChambres().size();
        }
        stats.setNbChambresTotal(totalChambres);

        // Occupation actuelle (tous hotels confondus)
        List<Reservation> resas = reservationDAO.findByHotelier(idHotelier);
        int occup = calculerOccupationsActuelles(resas);

        stats.setNbChambresOccupees(occup);
        stats.setNbChambresDispo(Math.max(0, totalChambres - occup));

        return stats;
    }

    // --- 2. STATS DÉTAILLÉES (PAGE STATISTIQUES) ---
    public HotelStats getDetailedStats(int idHotel) {
        HotelStats stats = new HotelStats();

        // Infos Hotel
        Hotel h = hotelDAO.findById(idHotel);
        int totalChambres = (h.getChambres() != null) ? h.getChambres().size() : 0;
        stats.setNbChambresTotal(totalChambres);

        // Récupérer réservations
        List<Reservation> resas = reservationDAO.findByHotel(idHotel);
        stats.setNbReservationsTotal(resas.size());

        // Occupations
        int occup = calculerOccupationsActuelles(resas);
        stats.setNbChambresOccupees(occup);
        stats.setNbChambresDispo(Math.max(0, totalChambres - occup));

        // Remplir les Graphes
        preparerDonneesGraphiques(stats, resas);

        return stats;
    }

    public List<Hotel> getHotelsByHotelier(int id) { return hotelDAO.getHotelsByHotelier(id); }
    public String getNomHotelier(int id) { return hotelierDAO.getNomComplet(id); }

    // --- LOGIQUE PRIVÉE ---
    private int calculerOccupationsActuelles(List<Reservation> list) {
        LocalDateTime now = LocalDateTime.now();
        int count = 0;
        for (Reservation r : list) {
            boolean confirme = (r.getStatut() == StatutReservation.CONFIRMEE);
            boolean enCours = !now.isBefore(r.getDateDebutSejour()) && !now.isAfter(r.getDateFinSejour());
            if (confirme && enCours) count++;
        }
        return count;
    }

    private void preparerDonneesGraphiques(HotelStats stats, List<Reservation> list) {
        Map<String, Integer> moisMap = new HashMap<>();
        Map<Integer, Integer> anneeMap = new HashMap<>();
        Map<String, Integer> typeMap = new HashMap<>();

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM");

        for (Reservation r : list) {
            // 1. Mois
            String m = r.getDateReservation().format(fmt);
            moisMap.put(m, moisMap.getOrDefault(m, 0) + 1);

            // 2. Année
            int y = r.getDateReservation().getYear();
            anneeMap.put(y, anneeMap.getOrDefault(y, 0) + 1);

            // 3. Type de chambre
            if (r.getChambre() != null && r.getChambre().getType() != null) {
                String t = r.getChambre().getType().toString();
                typeMap.put(t, typeMap.getOrDefault(t, 0) + 1);
            }
        }

        stats.setReservationsParMois(moisMap);
        stats.setReservationsParAnnee(anneeMap);
        stats.setReservationsParType(typeMap);
    }
}