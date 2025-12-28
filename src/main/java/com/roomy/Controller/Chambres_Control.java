package com.roomy.Controller;

import com.roomy.ENUMS.Statut_technique_Chambre;
import com.roomy.ENUMS.TypeChambre;
import com.roomy.entities.Chambre;
import com.roomy.entities.Hotel;
import com.roomy.service.ChambreService;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class Chambres_Control implements Initializable {

    private final ChambreService chambreService = new ChambreService();
    private ObservableList<Chambre> listeChambres = FXCollections.observableArrayList();

    @FXML private ComboBox<Hotel> comboFiltreHotel;
    @FXML private TableView<Chambre> tableChambres;
    @FXML private TableColumn<Chambre, Integer> colNum;
    @FXML private TableColumn<Chambre, String> colType;
    @FXML private TableColumn<Chambre, Double> colPrix;
    @FXML private TableColumn<Chambre, Integer> colCapacite;
    @FXML private TableColumn<Chambre, Integer> colSurface;
    @FXML private TableColumn<Chambre, String> colStatut;
    @FXML private TableColumn<Chambre, String> colDescription;

    @FXML private TextField txtNum;
    @FXML private ComboBox<TypeChambre> comboType;
    @FXML private TextField txtPrix;
    @FXML private TextField txtCapacite;
    @FXML private TextField txtSurface;
    @FXML private ComboBox<Statut_technique_Chambre> comboStatut;
    @FXML private TextArea txtDescription;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configurerTableau();
        chargerEnums();
        chargerHotels();

        comboFiltreHotel.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                // CORRECTION ICI : utilisation de getIdhotel()
                chargerChambres(newVal.getIdhotel());
            }
        });
    }

    private void configurerTableau() {
        colNum.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getNumchambre()));
        colType.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getType().toString()));
        colPrix.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getPrix_nuit()));
        colCapacite.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getCapacity()));
        colSurface.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getSurface()));
        colStatut.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getStatut().toString()));
        colDescription.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getDescription()));

        tableChambres.setItems(listeChambres);
    }

    private void chargerHotels() {
        int idHotelier = Dash_hotelier_Control.getCurrentHotelierId();
        List<Hotel> hotels = chambreService.getHotelsByHotelier(idHotelier);

        comboFiltreHotel.setConverter(new StringConverter<Hotel>() {
            @Override public String toString(Hotel h) { return (h != null) ? h.getNomHotel() : ""; }
            @Override public Hotel fromString(String s) { return null; }
        });

        comboFiltreHotel.setItems(FXCollections.observableArrayList(hotels));

        if (!hotels.isEmpty()) {
            comboFiltreHotel.getSelectionModel().selectFirst();
        }
    }

    private void chargerEnums() {
        comboType.setItems(FXCollections.observableArrayList(TypeChambre.values()));
        comboStatut.setItems(FXCollections.observableArrayList(Statut_technique_Chambre.values()));
    }

    private void chargerChambres(int idHotel) {
        listeChambres.clear();
        listeChambres.addAll(chambreService.getChambresParHotel(idHotel));
    }

    @FXML
    private void handleAjouter() {
        try {
            Hotel hotelSelectionne = comboFiltreHotel.getSelectionModel().getSelectedItem();
            if (hotelSelectionne == null) {
                afficherAlerte("Erreur", "Veuillez sélectionner un hôtel en haut.");
                return;
            }

            int num = Integer.parseInt(txtNum.getText());
            double prix = Double.parseDouble(txtPrix.getText());
            int capacite = Integer.parseInt(txtCapacite.getText());
            int surface = Integer.parseInt(txtSurface.getText());
            TypeChambre type = comboType.getValue();
            Statut_technique_Chambre statut = comboStatut.getValue();

            if(type == null || statut == null) {
                afficherAlerte("Erreur", "Veuillez choisir le type et le statut.");
                return;
            }

            Chambre c = new Chambre();
            c.setNumchambre(num);
            c.setType(type);
            c.setPrix_nuit(prix);
            c.setCapacity(capacite);
            c.setSurface(surface);
            c.setStatut(statut);
            c.setDescription(txtDescription.getText());
            c.setHotel(hotelSelectionne);

            boolean success = chambreService.ajouterChambre(c);
            if (success) {
                afficherAlerte("Succès", "Chambre ajoutée avec succès !");
                // CORRECTION ICI : getIdhotel()
                chargerChambres(hotelSelectionne.getIdhotel());
                handleVider();
            } else {
                afficherAlerte("Erreur", "Impossible d'ajouter la chambre.");
            }

        } catch (NumberFormatException e) {
            afficherAlerte("Erreur de format", "Vérifiez les champs numériques.");
        }
    }

    @FXML
    private void handleSupprimer() {
        Chambre selected = tableChambres.getSelectionModel().getSelectedItem();
        if (selected == null) {
            afficherAlerte("Attention", "Veuillez sélectionner une chambre.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Supprimer la chambre " + selected.getNumchambre() + " ?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            boolean success = chambreService.supprimerChambre(selected.getId());
            if (success) {
                listeChambres.remove(selected);
            } else {
                afficherAlerte("Erreur", "Erreur lors de la suppression.");
            }
        }
    }

    @FXML
    private void handleVider() {
        txtNum.clear();
        txtPrix.clear();
        txtCapacite.clear();
        txtSurface.clear();
        txtDescription.clear();
        comboType.getSelectionModel().clearSelection();
        comboStatut.getSelectionModel().clearSelection();
    }

    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}