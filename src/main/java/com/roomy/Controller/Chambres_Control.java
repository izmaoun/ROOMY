package com.roomy.Controller;

    import com.roomy.Dao.ChambreDAO;
    import com.roomy.Dao.HotelDAO;
    import com.roomy.entities.Chambre;
    import com.roomy.entities.Hotel;
    import com.roomy.ENUMS.TypeChambre;
    import com.roomy.ENUMS.Statut_technique_Chambre;
    import javafx.collections.FXCollections;
    import javafx.fxml.FXML;
    import javafx.fxml.Initializable;
    import javafx.scene.control.*;
    import javafx.util.StringConverter;

    import java.net.URL;
    import java.util.List;
    import java.util.ResourceBundle;

    public class Chambres_Control implements Initializable {

        @FXML private ComboBox<Hotel> comboFiltreHotel;
        @FXML private TableView<Chambre> tableChambres;
        @FXML private TableColumn<Chambre, Integer> colNum;
        @FXML private TableColumn<Chambre, String> colType;
        @FXML private TableColumn<Chambre, Double> colPrix;
        @FXML private TableColumn<Chambre, Integer> colCapacite;
        @FXML private TableColumn<Chambre, Integer> colSurface;
        @FXML private TableColumn<Chambre, String> colStatut;
        @FXML private TableColumn<Chambre, String> colDescription;

        @FXML private TextField txtNum, txtPrix, txtCapacite, txtSurface;
        @FXML private ComboBox<TypeChambre> comboType;
        @FXML private ComboBox<Statut_technique_Chambre> comboStatut;
        @FXML private TextArea txtDescription;

        private ChambreDAO chambreDAO = new ChambreDAO();
        private HotelDAO hotelDAO = new HotelDAO();

        @Override
        public void initialize(URL location, ResourceBundle resources) {
            setupTableColumns();
            chargerListeHotels();
            setupComboBoxes();

            // Listener pour changement d'hôtel
            comboFiltreHotel.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null) {
                    chargerChambres(newVal.getIdhotel());
                }
            });
        }

        private void setupTableColumns() {
            colNum.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getNumchambre()).asObject());
            colType.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getType().name()));
            colPrix.setCellValueFactory(cellData -> new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getPrix_nuit()).asObject());
            colCapacite.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getCapacity()).asObject());
            colSurface.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getSurface()).asObject());
            colStatut.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getStatut().name()));
            colDescription.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDescription()));
        }

        private void chargerListeHotels() {
            int idHotelier = Dash_hotelier_Control.getCurrentHotelierId();
            List<Hotel> hotels = hotelDAO.getHotelsByHotelier(idHotelier);

            comboFiltreHotel.setConverter(new StringConverter<Hotel>() {
                @Override public String toString(Hotel h) { return (h != null) ? h.getNomHotel() : ""; }
                @Override public Hotel fromString(String s) { return null; }
            });

            comboFiltreHotel.setItems(FXCollections.observableArrayList(hotels));

            if (!hotels.isEmpty()) {
                comboFiltreHotel.getSelectionModel().selectFirst();
                chargerChambres(hotels.get(0).getIdhotel());
            }
        }

        private void chargerChambres(int idHotel) {
            List<Chambre> chambres = chambreDAO.findByHotel(idHotel);
            tableChambres.setItems(FXCollections.observableArrayList(chambres));
        }

        private void setupComboBoxes() {
            comboType.setItems(FXCollections.observableArrayList(TypeChambre.values()));
            comboStatut.setItems(FXCollections.observableArrayList(Statut_technique_Chambre.values()));
        }

        @FXML
        private void handleAjouter() {
            try {
                Hotel hotel = comboFiltreHotel.getSelectionModel().getSelectedItem();
                if (hotel == null) {
                    showError("Veuillez sélectionner un hôtel");
                    return;
                }

                Chambre chambre = new Chambre();
                chambre.setNumchambre(Integer.parseInt(txtNum.getText()));
                chambre.setType(comboType.getValue());
                chambre.setPrix_nuit(Double.parseDouble(txtPrix.getText()));
                chambre.setCapacity(Integer.parseInt(txtCapacite.getText()));
                chambre.setSurface(Integer.parseInt(txtSurface.getText()));
                chambre.setStatut(comboStatut.getValue());
                chambre.setDescription(txtDescription.getText());
                chambre.setHotel(hotel);

                if (chambreDAO.ajouterChambre(chambre)) {
                    showSuccess("Chambre ajoutée avec succès");
                    chargerChambres(hotel.getIdhotel());
                    handleVider();
                } else {
                    showError("Erreur lors de l'ajout");
                }
            } catch (Exception e) {
                showError("Erreur : " + e.getMessage());
                e.printStackTrace();
            }
        }

        @FXML
        private void handleSupprimer() {
            Chambre selected = tableChambres.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showError("Veuillez sélectionner une chambre");
                return;
            }

            if (chambreDAO.supprimerChambre(selected.getId())) {
                showSuccess("Chambre supprimée");
                chargerChambres(comboFiltreHotel.getSelectionModel().getSelectedItem().getIdhotel());
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

        private void showError(String msg) { System.err.println("ERREUR : " + msg); }
        private void showSuccess(String msg) { System.out.println("SUCCÈS : " + msg); }
    }