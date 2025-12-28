package com.roomy.Controller;

import com.roomy.Dao.HotelDAO;
import com.roomy.Dao.HotelierDAO;
import com.roomy.entities.Hotelier;
import com.roomy.entities.Hotel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.geometry.Pos;

import java.util.List;

public class Profile_control {

    @FXML private TextField tfNomEtablissement;
    @FXML private TextField tfNomGerant;
    @FXML private TextField tfPrenomGerant;
    @FXML private TextField tfVille;
    @FXML private TextField tfEmail;
    @FXML private TextField tfTelephone;
    @FXML private TextField tfIce;
    @FXML private Button btnModifier;
    @FXML private Button btnAnnuler;
    @FXML private Label lblNombreHotels;
    @FXML private VBox vboxHotels;

    private HotelierDAO hotelierDAO = new HotelierDAO();
    private HotelDAO hotelDAO = new HotelDAO();
    private Hotelier currentHotelier;
    private boolean isEditMode = false;

    @FXML
    private void initialize() {
        loadHotelierData();
        loadHotelsData();
        setupButtonListeners();
    }

    private void loadHotelierData() {
        try {
            int hoteleId = Dash_hotelier_Control.getCurrentHotelierId();
            if (hoteleId > 0) {
                currentHotelier = hotelierDAO.findById(hoteleId);
                if (currentHotelier != null) {
                    displayHotelierInfo();
                    setFieldsEditable(false);
                } else {
                    showError("Hôtelier non trouvé");
                }
            } else {
                showError("ID hôtelier non défini");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showError("Erreur lors du chargement des données");
        }
    }

    private void displayHotelierInfo() {
        tfNomEtablissement.setText(currentHotelier.getNomEtablissement() != null ? currentHotelier.getNomEtablissement() : "");
        tfNomGerant.setText(currentHotelier.getNomGerant() != null ? currentHotelier.getNomGerant() : "");
        tfPrenomGerant.setText(currentHotelier.getPrenomGerant() != null ? currentHotelier.getPrenomGerant() : "");
        tfVille.setText(currentHotelier.getVille() != null ? currentHotelier.getVille() : "");
        tfEmail.setText(currentHotelier.getEmailGerant() != null ? currentHotelier.getEmailGerant() : "");
        tfTelephone.setText(currentHotelier.getTelephone() != null ? currentHotelier.getTelephone() : "");
        tfIce.setText(currentHotelier.getIce() != null ? currentHotelier.getIce() : "");
    }

    private void loadHotelsData() {
        try {
            int hotelerId = Dash_hotelier_Control.getCurrentHotelierId();
            if (hotelerId > 0) {
                List<Hotel> hotels = hotelDAO.getHotelsByHotelier(hotelerId);
                lblNombreHotels.setText("(" + hotels.size() + " hôtel" + (hotels.size() > 1 ? "s" : "") + ")");

                vboxHotels.getChildren().clear();
                if (hotels.isEmpty()) {
                    Label lblEmpty = new Label("Aucun hôtel géré");
                    lblEmpty.setStyle("-fx-font-size: 12px; -fx-text-fill: #999;");
                    vboxHotels.getChildren().add(lblEmpty);
                } else {
                    for (Hotel hotel : hotels) {
                        vboxHotels.getChildren().add(createHotelItem(hotel));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private HBox createHotelItem(Hotel hotel) {
        HBox item = new HBox(15);
        item.setAlignment(Pos.CENTER_LEFT);
        item.setStyle("-fx-border-color: #e0e0e0; -fx-border-radius: 5; -fx-padding: 10; -fx-background-color: #fafafa;");
        item.setPrefHeight(60);

        VBox info = new VBox(5);
        Text nomHotel = new Text(hotel.getNomHotel());
        nomHotel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        // Utiliser la méthode getNombreChambres() de HotelDAO
        int nbChambres = hotelDAO.getNombreChambres(hotel.getIdhotel());

        Text detailsHotel = new Text(hotel.getEtoiles() + " étoiles | " +
                nbChambres + " chambre" + (nbChambres > 1 ? "s" : ""));
        detailsHotel.setStyle("-fx-font-size: 11px; -fx-fill: #666;");

        info.getChildren().addAll(nomHotel, detailsHotel);
        item.getChildren().add(info);

        HBox.setHgrow(info, javafx.scene.layout.Priority.ALWAYS);

        return item;
    }

    private void setupButtonListeners() {
        btnModifier.setOnAction(event -> toggleEditMode());
        btnAnnuler.setOnAction(event -> cancelEdit());
    }

    @FXML
    private void toggleEditMode() {
        if (!isEditMode) {
            // Passer en mode édition
            setFieldsEditable(true);
            btnModifier.setText("Enregistrer");
            btnAnnuler.setVisible(true);
            isEditMode = true;
        } else {
            // Enregistrer les modifications
            saveHotelierData();
        }
    }

    private void saveHotelierData() {
        try {
            currentHotelier.setNomEtablissement(tfNomEtablissement.getText());
            currentHotelier.setNomGerant(tfNomGerant.getText());
            currentHotelier.setPrenomGerant(tfPrenomGerant.getText());
            currentHotelier.setVille(tfVille.getText());
            currentHotelier.setEmailGerant(tfEmail.getText());
            currentHotelier.setTelephone(tfTelephone.getText());

            if (hotelierDAO.updateHotelier(currentHotelier)) {
                showSuccess("Profil mis à jour avec succès");
                setFieldsEditable(false);
                btnModifier.setText("Modifier");
                btnAnnuler.setVisible(false);
                isEditMode = false;
            } else {
                showError("Erreur lors de la mise à jour");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showError("Erreur : " + e.getMessage());
        }
    }

    @FXML
    private void cancelEdit() {
        displayHotelierInfo();
        setFieldsEditable(false);
        btnModifier.setText("Modifier");
        btnAnnuler.setVisible(false);
        isEditMode = false;
    }

    private void setFieldsEditable(boolean editable) {
        tfNomEtablissement.setEditable(editable);
        tfNomGerant.setEditable(editable);
        tfPrenomGerant.setEditable(editable);
        tfVille.setEditable(editable);
        tfEmail.setEditable(editable);
        tfTelephone.setEditable(editable);
    }

    private void showError(String message) {
        System.err.println("ERREUR : " + message);
    }

    private void showSuccess(String message) {
        System.out.println("SUCCÈS : " + message);
    }
}