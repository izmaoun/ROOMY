package com.roomy.Controller;

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
import java.util.Map;
import java.util.ResourceBundle;

public class StatistiquesController implements Initializable {

    @FXML private ComboBox<Hotel> comboHotels;

    // Textes
    @FXML private Text txtTotalReservations;
    @FXML private Text txtChambresDispo;
    @FXML private Text txtChambresOccupees;

    // Graphes
    @FXML private BarChart<String, Number> chartMois;
    @FXML private BarChart<String, Number> chartAnnee;
    @FXML private PieChart chartType;

    private final StatistiquesService statsService = new StatistiquesService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        chargerListeHotels();

        // Ecouteur : changement d'hôtel -> mise à jour
        comboHotels.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                afficherStats(newVal.getIdhotel());
            }
        });
    }

    private void chargerListeHotels() {
        // Récupère l'ID depuis le contrôleur principal
        int idHotelier = Dash_hotelier_Control.getCurrentHotelierId();

        List<Hotel> hotels = statsService.getHotelsByHotelier(idHotelier);

        // Afficher le nom dans la ComboBox
        comboHotels.setConverter(new StringConverter<Hotel>() {
            @Override public String toString(Hotel h) { return (h != null) ? h.getNomHotel() : ""; }
            @Override public Hotel fromString(String s) { return null; }
        });

        comboHotels.setItems(FXCollections.observableArrayList(hotels));

        if (!hotels.isEmpty()) {
            comboHotels.getSelectionModel().selectFirst();
        }
    }

    private void afficherStats(int idHotel) {
        HotelStats stats = statsService.getDetailedStats(idHotel);

        // 1. Textes
        txtTotalReservations.setText(String.valueOf(stats.getNbReservationsTotal()));
        txtChambresDispo.setText(String.valueOf(stats.getNbChambresDispo()));
        txtChambresOccupees.setText(String.valueOf(stats.getNbChambresOccupees()));

        // 2. BarChart MOIS
        chartMois.getData().clear();
        XYChart.Series<String, Number> sMois = new XYChart.Series<>();
        sMois.setName("Réservations");
        stats.getReservationsParMois().forEach((k, v) -> sMois.getData().add(new XYChart.Data<>(k, v)));
        chartMois.getData().add(sMois);

        // 3. BarChart ANNEE
        chartAnnee.getData().clear();
        XYChart.Series<String, Number> sAnnee = new XYChart.Series<>();
        sAnnee.setName("Annuel");
        stats.getReservationsParAnnee().forEach((k, v) -> sAnnee.getData().add(new XYChart.Data<>(String.valueOf(k), v)));
        chartAnnee.getData().add(sAnnee);

        // 4. PieChart TYPE
        chartType.getData().clear();
        stats.getReservationsParType().forEach((type, count) -> {
            chartType.getData().add(new PieChart.Data(type + " (" + count + ")", count));
        });
        chartType.setLabelsVisible(true);
    }
}