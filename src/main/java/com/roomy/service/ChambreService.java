package com.roomy.service;

import com.roomy.Dao.ChambreDAO;
import com.roomy.Dao.HotelDAO;
import com.roomy.entities.Chambre;
import com.roomy.entities.Hotel;
import java.util.List;

public class ChambreService {

    private final ChambreDAO chambreDAO;
    private final HotelDAO hotelDAO;

    public ChambreService() {
        this.chambreDAO = new ChambreDAO();
        this.hotelDAO = new HotelDAO();
    }

    // Récupérer les hôtels pour le filtre
    public List<Hotel> getHotelsByHotelier(int idHotelier) {
        return hotelDAO.getHotelsByHotelier(idHotelier);
    }

    // Récupérer les chambres d'un hôtel
    public List<Chambre> getChambresParHotel(int idHotel) {
        return chambreDAO.findByHotel(idHotel); // Assure-toi que cette méthode existe dans ton DAO
    }

    public boolean ajouterChambre(Chambre c) {
        // Tu peux ajouter des vérifications ici (ex: prix positif)
        return chambreDAO.ajouterChambre(c);
    }

    public boolean supprimerChambre(int idChambre) {
        return chambreDAO.supprimerChambre(idChambre);
    }
}