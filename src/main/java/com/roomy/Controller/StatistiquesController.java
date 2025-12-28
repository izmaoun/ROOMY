package com.roomy.Controller;

import com.roomy.Dao.ChambreDAO;
import com.roomy.dto.HotelStats;
import com.roomy.entities.Hotel;
import com.roomy.service.StatistiquesService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.text.Text;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class StatistiquesController implements Initializable {

    @FXML private ComboBox<Hotel> comboHotels;
    @FXML private Text txtTotalReservations;
    @FXML private Text txtChambresDispo;
    @FXML private Text txtChambresOccupees;
    @FXML private BarChart<String, Number> chartMois;
    @FXML private BarChart<String, Number> chartAnnee;
    @FXML private PieChart chartType;

    private final StatistiquesService statsService = new StatistiquesService();
    private final ChambreDAO chambreDAO = new ChambreDAO();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        chargerListeHotels();

        comboHotels.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                afficherStats(newVal.getIdhotel());
            }
        });
    }

    private void chargerListeHotels() {
        int idHotelier = Dash_hotelier_Control.getCurrentHotelierId();
        List<Hotel> hotels = statsService.getHotelsByHotelier(idHotelier);

        comboHotels.setConverter(new StringConverter<Hotel>() {
            @Override public String toString(Hotel h) { return (h != null) ? h.getNomHotel() : ""; }
            @Override public Hotel fromString(String s) { return null; }
        });

        comboHotels.setItems(FXCollections.observableArrayList(hotels));

        if (!hotels.isEmpty()) {
            comboHotels.getSelectionModel().selectFirst();
            afficherStats(hotels.get(0).getIdhotel());
        }
    }

    private void afficherStats(int idHotel) {
        HotelStats stats = statsService.getDetailedStats(idHotel);

        if (stats == null) {
            System.err.println("Erreur: stats NULL pour l'hôtel " + idHotel);
            return;
        }

        txtTotalReservations.setText(String.valueOf(stats.getNbReservationsTotal()));

        // Charger les statistiques des chambres depuis la base de données
        chargerStatsChambres(idHotel);

        // Charger les graphiques
        chargerGraphiques(stats);
    }

    private void chargerStatsChambres(int idHotel) {
        int nbChambresDispo = chambreDAO.getNombreChambresDisponiblesByHotel(idHotel);
        int nbChambresOccupees = chambreDAO.getNombreChambresOccupeesByHotel(idHotel);

        txtChambresDispo.setText(String.valueOf(nbChambresDispo));
        txtChambresOccupees.setText(String.valueOf(nbChambresOccupees));
    }

    private void chargerGraphiques(HotelStats stats) {
        // Graphique par mois
        chartMois.getData().clear();
        XYChart.Series<String, Number> sMois = new XYChart.Series<>();
        sMois.setName("Réservations");
        if (stats.getReservationsParMois() != null) {
            stats.getReservationsParMois().forEach((k, v) ->
                sMois.getData().add(new XYChart.Data<>(k, v))
            );
        }
        chartMois.getData().add(sMois);

        // Graphique par année
        chartAnnee.getData().clear();
        XYChart.Series<String, Number> sAnnee = new XYChart.Series<>();
        sAnnee.setName("Annuel");
        if (stats.getReservationsParAnnee() != null) {
            stats.getReservationsParAnnee().forEach((k, v) ->
                sAnnee.getData().add(new XYChart.Data<>(String.valueOf(k), v))
            );
        }
        chartAnnee.getData().add(sAnnee);

        // Pie chart par type de chambre
        chartType.getData().clear();
        if (stats.getReservationsParType() != null) {
            stats.getReservationsParType().forEach((type, count) -> {
                chartType.getData().add(new PieChart.Data(type + " (" + count + ")", count));
            });
        }
        chartType.setLabelsVisible(true);
    }
}