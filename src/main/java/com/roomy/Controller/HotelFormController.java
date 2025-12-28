package com.roomy.Controller;

import com.roomy.Dao.AdresseDAO;
import com.roomy.Dao.HotelDAO;
import com.roomy.Dao.ImageHotelDAO;
import com.roomy.entities.Adresse;
import com.roomy.entities.Hotel;
import com.roomy.entities.Hotelier;
import com.roomy.entities.Image_hotel;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.application.Platform;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class HotelFormController {

    @FXML private Text titleText;
    @FXML private TextField tfNomHotel;
    @FXML private ComboBox<Integer> cbEtoiles;
    @FXML private TextField tfRue;
    @FXML private TextField tfVille;
    @FXML private TextField tfCodepostal;
    @FXML private TextField tfPays;
    @FXML private Label lblMessage;
    @FXML private Button btnSave;
    @FXML private Button btnCancel;
    @FXML private Button btnDelete;
    @FXML private Button btnSelectImage;
    @FXML private ImageView imgPreview;
    @FXML private Label lblImageStatus;
    @FXML private VBox imagesContainer;
    @FXML private ScrollPane scrollImages;

    private Hotel hotelActuel = null;
    private Hotelier hotelierActuel;
    private String selectedImagePath = null;
    private List<Image_hotel> imagesToAdd = new ArrayList<>();

    // Injection des DAO
    private final HotelDAO hotelDAO;
    private final AdresseDAO adresseDAO;
    private final ImageHotelDAO imageHotelDAO;

    public HotelFormController() {
        this.hotelDAO = new HotelDAO();
        this.adresseDAO = new AdresseDAO();
        this.imageHotelDAO = new ImageHotelDAO();
    }

    @FXML
    private void initialize() {
        cbEtoiles.setItems(FXCollections.observableArrayList(1, 2, 3, 4, 5));
        btnSave.setOnAction(e -> saveHotel());
        btnCancel.setOnAction(e -> cancelForm());
        btnDelete.setOnAction(e -> deleteHotel());
        btnSelectImage.setOnAction(e -> selectImage());
    }

    public void setModeAjout(Hotelier hotelier) {
        this.hotelActuel = null;
        this.hotelierActuel = hotelier;
        this.imagesToAdd = new ArrayList<>();
        titleText.setText("Ajouter un nouvel hôtel");
        btnDelete.setVisible(false);
        btnDelete.setManaged(false);
        tfVille.setText(hotelier.getVille());
        tfPays.setText("Maroc");

        // Afficher la section images pour l'ajout
        if (imagesContainer != null) {
            imagesContainer.setVisible(true);
            imagesContainer.setManaged(true);
        }
    }

    public void setModeModification(Hotel hotel, Hotelier hotelier) {
        this.hotelActuel = hotel;
        this.hotelierActuel = hotelier;
        this.imagesToAdd = new ArrayList<>();
        titleText.setText("Modifier l'hôtel");
        btnDelete.setVisible(true);
        btnDelete.setManaged(true);

        tfNomHotel.setText(hotel.getNomHotel());
        cbEtoiles.setValue(hotel.getEtoiles());
        tfRue.setText(hotel.getAdresse().getRue());
        tfVille.setText(hotel.getAdresse().getVille());
        tfCodepostal.setText(hotel.getAdresse().getCodepostal());
        tfPays.setText(hotel.getAdresse().getPays());

        // Afficher la section images pour la modification
        if (imagesContainer != null) {
            imagesContainer.setVisible(true);
            imagesContainer.setManaged(true);
            afficherImagesExistantes();
        }
    }

    /**
     * Affiche les images déjà sauvegardées de l'hôtel en mode modification
     */
    private void afficherImagesExistantes() {
        if (imagesContainer == null || hotelActuel == null) return;

        List<Image_hotel> images = hotelDAO.loadHotelImages(hotelActuel.getIdhotel());

        if (images.isEmpty()) {
            lblImageStatus.setText("Aucune image actuellement");
            return;
        }

        imagesContainer.getChildren().clear();
        for (Image_hotel img : images) {
            VBox imageBox = creerBoiteImage(img);
            imagesContainer.getChildren().add(imageBox);
        }
    }

    /**
     * Crée une boîte affichant une image avec bouton de suppression
     */
    private VBox creerBoiteImage(Image_hotel img) {
        VBox imageBox = new VBox(5);
        imageBox.setStyle("-fx-border-color: #A59090; -fx-border-radius: 8; -fx-padding: 10; -fx-background-color: #f9f9f9;");

        ImageView preview = new ImageView();
        preview.setFitWidth(150);
        preview.setFitHeight(120);
        preview.setPreserveRatio(true);

        try {
            File imageFile = new File(img.getUrl());
            if (imageFile.exists()) {
                preview.setImage(new Image(imageFile.toURI().toString()));
            }
        } catch (Exception e) {
            System.err.println("Erreur chargement image : " + e.getMessage());
        }

        Label lblDescription = new Label(img.getDescription() != null ? img.getDescription() : "Image hôtel");
        lblDescription.setStyle("-fx-font-size: 12px; -fx-text-fill: #380F17;");

        Button btnRemove = new Button("🗑️ Supprimer");
        btnRemove.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-size: 11px;");
        btnRemove.setOnAction(e -> supprimerImage(img));

        imageBox.getChildren().addAll(preview, lblDescription, btnRemove);
        return imageBox;
    }

    /**
     * Supprime une image existante de la base de données
     */
    private void supprimerImage(Image_hotel img) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation");
        confirmation.setHeaderText("Supprimer cette image ?");
        confirmation.setContentText("Cette action est irréversible.");

        if (confirmation.showAndWait().get() == ButtonType.OK) {
            if (imageHotelDAO.deleteImage(img.getId())) {
                afficherSucces("✓ Image supprimée");
                afficherImagesExistantes();
            } else {
                afficherErreur("✗ Erreur lors de la suppression");
            }
        }
    }

    /**
     * Ouvre le sélecteur de fichier pour choisir une image
     */
    @FXML
    private void selectImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionner une image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.jpg", "*.jpeg", "*.png", "*.gif"),
                new FileChooser.ExtensionFilter("Tous les fichiers", "*.*")
        );

        File selectedFile = fileChooser.showOpenDialog(btnSelectImage.getScene().getWindow());
        if (selectedFile != null) {
            selectedImagePath = selectedFile.getAbsolutePath();
            afficherApercu(selectedFile);
            lblImageStatus.setText("✓ Image sélectionnée : " + selectedFile.getName());
        }
    }

    /**
     * Affiche l'aperçu de l'image sélectionnée
     */
    private void afficherApercu(File imageFile) {
        try {
            Image image = new Image(imageFile.toURI().toString());
            imgPreview.setImage(image);
            imgPreview.setFitWidth(150);
            imgPreview.setFitHeight(120);
            imgPreview.setPreserveRatio(true);
        } catch (Exception e) {
            afficherErreur("Erreur lors du chargement de l'aperçu");
        }
    }

    /**
     * Sauvegarde l'hôtel et gère l'ajout d'images
     */
    private void saveHotel() {
        if (tfNomHotel.getText().trim().isEmpty() ||
            cbEtoiles.getValue() == null ||
            tfRue.getText().trim().isEmpty() ||
            tfVille.getText().trim().isEmpty() ||
            tfCodepostal.getText().trim().isEmpty() ||
            tfPays.getText().trim().isEmpty()) {
            afficherErreur("Veuillez remplir tous les champs obligatoires");
            return;
        }

        try {
            if (hotelActuel == null) {
                ajouterHotel();
            } else {
                modifierHotel();
            }
        } catch (Exception e) {
            afficherErreur("Erreur lors de la sauvegarde : " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Ajoute un nouvel hôtel avec image optionnelle
     */
    private void ajouterHotel() {
        Adresse adresse = new Adresse();
        adresse.setRue(tfRue.getText().trim());
        adresse.setVille(tfVille.getText().trim());
        adresse.setCodepostal(tfCodepostal.getText().trim());
        adresse.setPays(tfPays.getText().trim());

        if (!adresseDAO.add(adresse)) {
            afficherErreur("Erreur lors de la création de l'adresse");
            return;
        }

        Hotel hotel = new Hotel();
        hotel.setNomHotel(tfNomHotel.getText().trim());
        hotel.setEtoiles(cbEtoiles.getValue());
        hotel.setAdresse(adresse);
        hotel.setHotelier(hotelierActuel);

        if (hotelDAO.ajouterHotel(hotel)) {
            // Ajouter l'image si une est sélectionnée
            if (selectedImagePath != null && !selectedImagePath.isEmpty()) {
                ajouterImageHotel(hotel.getIdhotel());
            }
            afficherSucces("✓ Hôtel ajouté avec succès");
            fermerApres(1500);
        } else {
            afficherErreur("Erreur lors de l'ajout de l'hôtel");
        }
    }

    /**
     * Modifie un hôtel existant et ajoute une nouvelle image si sélectionnée
     */
    private void modifierHotel() {
        Adresse adresse = hotelActuel.getAdresse();

        // Vérifier que l'adresse a un ID valide
        if (adresse == null || adresse.getIdAdresse() <= 0) {
            afficherErreur("Erreur : adresse invalide");
            return;
        }

        adresse.setRue(tfRue.getText().trim());
        adresse.setVille(tfVille.getText().trim());
        adresse.setCodepostal(tfCodepostal.getText().trim());
        adresse.setPays(tfPays.getText().trim());

        if (!adresseDAO.updateAdresse(adresse)) {
            afficherErreur("Erreur lors de la mise à jour de l'adresse");
            return;
        }

        hotelActuel.setNomHotel(tfNomHotel.getText().trim());
        hotelActuel.setEtoiles(cbEtoiles.getValue());

        if (hotelDAO.updateHotel(hotelActuel)) {
            // Ajouter une nouvelle image si sélectionnée
            if (selectedImagePath != null && !selectedImagePath.isEmpty()) {
                ajouterImageHotel(hotelActuel.getIdhotel());
            }
            afficherSucces("✓ Hôtel modifié avec succès");
            fermerApres(1500);
        } else {
            afficherErreur("Erreur lors de la modification de l'hôtel");
        }
    }

    /**
     * Ajoute une image à l'hôtel depuis le fichier local
     */
    private void ajouterImageHotel(int idHotel) {
        try {
            File sourceFile = new File(selectedImagePath);
            if (!sourceFile.exists()) {
                afficherErreur("Le fichier image n'existe pas");
                return;
            }

            // Créer le dossier de destination s'il n'existe pas
            String uploadDir = "uploads/hotels/";
            File uploadFolder = new File(uploadDir);
            if (!uploadFolder.exists()) {
                uploadFolder.mkdirs();
            }

            // Générer un nom unique pour l'image
            String imageName = System.currentTimeMillis() + "_" + sourceFile.getName();
            File destinationFile = new File(uploadDir + imageName);

            // Copier l'image
            Files.copy(sourceFile.toPath(), destinationFile.toPath(),
                    StandardCopyOption.REPLACE_EXISTING);

            // Créer l'objet Image_hotel et le sauvegarder en BD
            Image_hotel image = new Image_hotel();
            image.setUrl(uploadDir + imageName);
            image.setDescription("Image de l'hôtel");

            Hotel hotel = new Hotel();
            hotel.setIdhotel(idHotel);
            image.setHotel(hotel);

            if (imageHotelDAO.addImage(image)) {
                selectedImagePath = null; // Réinitialiser après succès
                lblImageStatus.setText("✓ Image sauvegardée avec succès");
                imgPreview.setImage(null);
                afficherImagesExistantes(); // Rafraîchir l'affichage
            } else {
                afficherErreur("Erreur lors de la sauvegarde de l'image en base");
            }

        } catch (IOException e) {
            afficherErreur("Erreur lors du upload de l'image : " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Supprime l'hôtel avec confirmation
     */
    private void deleteHotel() {
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirmation de suppression");
        confirmDialog.setHeaderText("Êtes-vous sûr ?");
        confirmDialog.setContentText("La suppression de cet hôtel est irréversible.");

        if (confirmDialog.showAndWait().get() == ButtonType.OK) {
            if (hotelDAO.deleteHotel(hotelActuel.getIdhotel())) {
                afficherSucces("✓ Hôtel supprimé avec succès");
                fermerApres(1500);
            } else {
                afficherErreur("Erreur lors de la suppression de l'hôtel");
            }
        }
    }

    /**
     * Ferme la fenêtre du formulaire
     */
    private void cancelForm() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    /**
     * Affiche un message de succès
     */
    private void afficherSucces(String message) {
        lblMessage.setStyle("-fx-text-fill: #4CAF50; -fx-font-weight: bold;");
        lblMessage.setText(message);
    }

    /**
     * Affiche un message d'erreur
     */
    private void afficherErreur(String message) {
        lblMessage.setStyle("-fx-text-fill: #f44336; -fx-font-weight: bold;");
        lblMessage.setText(message);
    }

    /**
     * Ferme la fenêtre après un délai donné
     */
    private void fermerApres(long delai) {
        new Thread(() -> {
            try {
                Thread.sleep(delai);
                javafx.application.Platform.runLater(this::cancelForm);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}