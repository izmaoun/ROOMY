package com.roomy.Controller;
import com.roomy.Dao.HotelDAO;
import com.roomy.entities.Hotel;  // À créer ou adapter selon ton modèle
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

public class Hotels_control {

        @FXML private TableView<Hotel> tableHotels;
        @FXML private Label lblNoHotels;

        private int currentHotelierId = Dash_hotelier_Control.getCurrentHotelierId(); // Récupère l'ID connecté

        @FXML
        private void initialize() {
            loadHotels();
        }

        private void loadHotels() {
            HotelDAO hotelDAO = new HotelDAO();
            ObservableList<Hotel> hotels = FXCollections.observableArrayList(
                    hotelDAO.getHotelsByHotelier(currentHotelierId)
            );

            if (hotels.isEmpty()) {
                lblNoHotels.setVisible(true);
                tableHotels.setVisible(false);
            } else {
                lblNoHotels.setVisible(false);
                tableHotels.setVisible(true);
                tableHotels.setItems(hotels);
            }
        }

        @FXML
        private void openAddHotelForm() {
            // À implémenter plus tard : ouvrir un formulaire modal ou nouvelle page
            System.out.println("Ouvrir le formulaire d'ajout d'hôtel");
        }
}

