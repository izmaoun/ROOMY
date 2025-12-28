package com.roomy.Controller;

import com.roomy.Dao.ChambreDAO;
import com.roomy.Dao.HotelDAO;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class DashHotelierDashboardControl {

    @FXML private Text dashNbHotels;
    @FXML private Text dashNbChambresTotal;
    @FXML private Text dashNbChambresDispo;
    @FXML private Text dashNbChambresOccupees;

    private final HotelDAO hotelDAO = new HotelDAO();
    private final ChambreDAO chambreDAO = new ChambreDAO();

    @FXML
    private void initialize() {
        refreshStats();
    }

    private void refreshStats() {
        int idHotelier = Dash_hotelier_Control.getCurrentHotelierId();

        int nbHotels = hotelDAO.getNombreHotels(idHotelier);
        int nbChambresTotal = chambreDAO.getNombreChambresTotal(idHotelier);
        int nbChambresDisponibles = chambreDAO.getNombreChambresDisponibles(idHotelier);
        int nbChambresOccupees = nbChambresTotal - nbChambresDisponibles;

        dashNbHotels.setText(String.valueOf(nbHotels));
        dashNbChambresTotal.setText(String.valueOf(nbChambresTotal));
        dashNbChambresDispo.setText(String.valueOf(nbChambresDisponibles));
        dashNbChambresOccupees.setText(String.valueOf(nbChambresOccupees));
    }

    @FXML
    private void goToMesHotels() {
        Dash_hotelier_Control.getInstance().goToMesHotels();
    }
}