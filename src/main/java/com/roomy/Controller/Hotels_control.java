package com.roomy.Controller;

import com.roomy.Dao.AdresseDAO;
import com.roomy.Dao.HotelDAO;
import com.roomy.Dao.HotelierDAO;
import com.roomy.Dao.ImageHotelDAO;
import com.roomy.entities.Adresse;
import com.roomy.entities.Hotel;
import com.roomy.entities.Hotelier;
import com.roomy.entities.Image_hotel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

public class Hotels_control {

    @FXML private FlowPane flowHotels;
    @FXML private Label lblNoHotels;

    private final HotelDAO hotelDAO = new HotelDAO();
    private final HotelierDAO hotelierDAO = new HotelierDAO();
    private final AdresseDAO adresseDAO = new AdresseDAO();
    private final ImageHotelDAO imageHotelDAO = new ImageHotelDAO();

    private final int currentHotelierId = Dash_hotelier_Control.getCurrentHotelierId();

    @FXML
    private void initialize() {
        loadHotels();
    }

    private void loadHotels() {
        flowHotels.getChildren().clear();

        List<Hotel> hotels = hotelDAO.getHotelsByHotelier(currentHotelierId);

        if (hotels == null || hotels.isEmpty()) {
            lblNoHotels.setVisible(true);
        } else {
            lblNoHotels.setVisible(false);
            for (Hotel h : hotels) {
                flowHotels.getChildren().add(createHotelTile(h));
            }
        }
    }

    private VBox createHotelTile(Hotel hotel) {
        Image img = imageForHotel(hotel);
        ImageView iv = new ImageView(img);
        iv.setFitWidth(320);
        iv.setFitHeight(180);
        iv.setPreserveRatio(true);
        iv.setSmooth(true);

        Label lblName = new Label(hotel.getNomHotel() != null ? hotel.getNomHotel() : "—");
        lblName.setStyle("-fx-font-size: 18px; -fx-text-fill: #380F17; -fx-font-weight: bold;");

        Label lblInfo = new Label((hotel.getEtoiles()) + " ⭐   |   " + hotel.getNombreDeChambres() + " chambres");
        lblInfo.setStyle("-fx-text-fill: #666; -fx-font-size: 13px;");

        VBox box = new VBox(iv, lblName, lblInfo);
        box.setAlignment(Pos.CENTER);
        box.setSpacing(8);
        box.setPadding(new Insets(12));
        box.setPrefWidth(340);
        box.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.12), 10, 0, 0, 6);");

        box.setOnMouseClicked((MouseEvent e) -> showHotelDetails(hotel));
        box.setOnMouseEntered(e -> box.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.18), 12, 0, 0, 8);"));
        box.setOnMouseExited(e -> box.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.12), 10, 0, 0, 6);"));

        return box;
    }

    /**
     * Récupère une image pour l'hôtel :
     * - si Image_hotel présente, utilise la première URL (Image_hotel.getUrl())
     * - sinon essaie la resource /images/hotel_placeholder.png
     * - prend en charge file path absolu ou URL http
     */
    private Image imageForHotel(Hotel hotel) {
        try {
            if (hotel.getImgs() != null && !hotel.getImgs().isEmpty()) {
                String url = hotel.getImgs().get(0).getUrl();
                if (url != null && !url.isBlank()) {
                    // fichier local ?
                    File f = new File(url);
                    if (f.exists()) return new Image(f.toURI().toString(), 800, 480, true, true);
                    // sinon, si c'est un resource du jar (commence par /) ou une URL http
                    if (url.startsWith("/")) {
                        InputStream is = getClass().getResourceAsStream(url);
                        if (is != null) return new Image(is, 800, 480, true, true);
                    }
                    // Url externe possible
                    return new Image(url, 800, 480, true, true);
                }
            }
        } catch (Exception ignored) {}
        // fallback placeholder resource
        InputStream placeholder = getClass().getResourceAsStream("/images/hotel_placeholder.png");
        if (placeholder != null) return new Image(placeholder, 800, 480, true, true);
        // fallback internet placeholder
        return new Image("https://via.placeholder.com/800x480.png?text=Hotel", 800, 480, true, true);
    }

    @FXML
    private void openAddHotelForm() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Ajouter un nouvel hôtel");

        // Fields
        TextField tfNom = new TextField();
        TextField tfRue = new TextField();
        TextField tfVille = new TextField();
        TextField tfCodePostal = new TextField();
        TextField tfPays = new TextField();
        ChoiceBox<Integer> cbEtoiles = new ChoiceBox<>();
        cbEtoiles.getItems().addAll(1,2,3,4,5);
        cbEtoiles.getSelectionModel().select(2);
        TextField tfNbChambres = new TextField();
        TextField tfImageUrl = new TextField();

        GridPane form = new GridPane();
        form.setVgap(10); form.setHgap(10); form.setPadding(new Insets(16));
        form.add(new Label("Nom :"), 0, 0); form.add(tfNom, 1, 0);
        form.add(new Label("Rue :"), 0, 1); form.add(tfRue, 1, 1);
        form.add(new Label("Ville :"), 0, 2); form.add(tfVille, 1, 2);
        form.add(new Label("Code postal :"), 0, 3); form.add(tfCodePostal, 1, 3);
        form.add(new Label("Pays :"), 0, 4); form.add(tfPays, 1, 4);
        form.add(new Label("Étoiles :"), 0, 5); form.add(cbEtoiles, 1, 5);
        form.add(new Label("Nombre de chambres :"), 0, 6); form.add(tfNbChambres, 1, 6);
        form.add(new Label("Image (chemin ou URL) :"), 0, 7); form.add(tfImageUrl, 1, 7);

        Button btnSave = new Button("Enregistrer");
        Button btnCancel = new Button("Annuler");
        HBox actions = new HBox(10, btnSave, btnCancel);
        actions.setAlignment(Pos.CENTER_RIGHT);
        form.add(actions, 1, 8);

        btnCancel.setOnAction(e -> dialog.close());

        btnSave.setOnAction(e -> {
            String nom = tfNom.getText().trim();
            if (nom.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Validation", "Le nom de l'hôtel est requis.");
                return;
            }
            try {
                int nbCh = Integer.parseInt(tfNbChambres.getText().trim());
                // Créer adresse et la persist
                Adresse adresse = new Adresse();
                adresse.setRue(tfRue.getText().trim());
                adresse.setVille(tfVille.getText().trim());
                adresse.setCodepostal(tfCodePostal.getText().trim());
                adresse.setPays(tfPays.getText().trim());
                boolean addrOk = adresseDAO.ajouterAdresse(adresse);
                if (!addrOk) {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'enregistrer l'adresse.");
                    return;
                }

                // Récupérer hotelier courant (objet complet requis par Hotel.setHotelier)
                Hotelier hotelier = hotelierDAO.findById(currentHotelierId);
                if (hotelier == null) {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Hotelier non trouvé.");
                    return;
                }

                // Construire l'objet Hotel
                Hotel newHotel = new Hotel();
                newHotel.setNomHotel(nom);
                newHotel.setEtoiles(cbEtoiles.getValue());
                newHotel.setAdresse(adresse);
                newHotel.setHotelier(hotelier);
                // ajouter chambres (on stocke juste le nombre, les chambres détaillées peuvent être créées plus tard)
                // ici on ne crée pas d'objets Chambre ; Hotel.getNombreDeChambres utilisera la liste de chambres
                // si vous voulez créer des chambres vides, implémentez la logique ici.

                // Image (si fourni)
                String imgUrl = tfImageUrl.getText().trim();
                if (!imgUrl.isEmpty()) {
                    Image_hotel ih = new Image_hotel();
                    ih.setUrl(imgUrl);
                    ih.setDescription("Image principale");
                    newHotel.addImg(ih); // HotelDAO.ajouterHotel ajoutera les images via ImageHotelDAO
                }

                boolean ok = hotelDAO.ajouterHotel(newHotel);
                if (ok) {
                    showAlert(Alert.AlertType.INFORMATION, "Succès", "Hôtel ajouté avec succès.");
                    dialog.close();
                    loadHotels();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ajouter l'hôtel.");
                }

            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.WARNING, "Validation", "Le nombre de chambres doit être un entier.");
            } catch (IllegalArgumentException ex) {
                showAlert(Alert.AlertType.WARNING, "Validation", ex.getMessage());
            }
        });

        Scene sc = new Scene(form);
        dialog.setScene(sc);
        dialog.showAndWait();
    }

    private void showHotelDetails(Hotel hotel) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Détails - " + (hotel.getNomHotel() == null ? "" : hotel.getNomHotel()));

        ImageView iv = new ImageView(imageForHotel(hotel));
        iv.setFitWidth(520); iv.setFitHeight(300); iv.setPreserveRatio(true);

        Label lblNom = new Label(hotel.getNomHotel());
        lblNom.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #380F17;");

        Adresse a = hotel.getAdresse();
        String adresseText = a != null ? (a.getRue() + ", " + a.getVille() + " " + a.getCodepostal() + " - " + a.getPays()) : "—";
        Label lblAdresse = new Label("Adresse : " + adresseText);
        Label lblEtoiles = new Label("Étoiles : " + hotel.getEtoiles());
        Label lblNbCh = new Label("Nombre de chambres : " + hotel.getNombreDeChambres());

        VBox info = new VBox(8, lblNom, lblAdresse, lblEtoiles, lblNbCh);
        info.setPadding(new Insets(8));

        Button btnModifier = new Button("Modifier");
        Button btnSupprimer = new Button("Supprimer");
        Button btnFermer = new Button("Fermer");
        HBox actions = new HBox(10, btnModifier, btnSupprimer, btnFermer);
        actions.setAlignment(Pos.CENTER_RIGHT);

        VBox root = new VBox(12, iv, info, actions);
        root.setPadding(new Insets(16));
        root.setPrefWidth(600);

        btnFermer.setOnAction(e -> dialog.close());

        btnSupprimer.setOnAction(e -> {
            Alert conf = new Alert(Alert.AlertType.CONFIRMATION);
            conf.setTitle("Confirmer la suppression");
            conf.setHeaderText("Voulez-vous supprimer l'hôtel \"" + hotel.getNomHotel() + "\" ?");
            Optional<ButtonType> res = conf.showAndWait();
            if (res.isPresent() && res.get() == ButtonType.OK) {
                boolean ok = hotelDAO.deleteHotel(hotel.getIdhotel());
                if (ok) {
                    showAlert(Alert.AlertType.INFORMATION, "Supprimé", "Hôtel supprimé.");
                    dialog.close();
                    loadHotels();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de supprimer l'hôtel.");
                }
            }
        });

        btnModifier.setOnAction(e -> {
            dialog.close();
            openEditHotelForm(hotel);
        });

        Scene sc = new Scene(root);
        dialog.setScene(sc);
        dialog.showAndWait();
    }

    private void openEditHotelForm(Hotel hotel) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Modifier - " + (hotel.getNomHotel() == null ? "" : hotel.getNomHotel()));

        TextField tfNom = new TextField(hotel.getNomHotel());
        TextField tfRue = new TextField(hotel.getAdresse() != null ? hotel.getAdresse().getRue() : "");
        TextField tfVille = new TextField(hotel.getAdresse() != null ? hotel.getAdresse().getVille() : "");
        TextField tfCodePostal = new TextField(hotel.getAdresse() != null ? hotel.getAdresse().getCodepostal() : "");
        TextField tfPays = new TextField(hotel.getAdresse() != null ? hotel.getAdresse().getPays() : "");
        ChoiceBox<Integer> cbEtoiles = new ChoiceBox<>();
        cbEtoiles.getItems().addAll(1,2,3,4,5);
        cbEtoiles.getSelectionModel().select(Integer.valueOf(hotel.getEtoiles()) - 1);
        TextField tfNbChambres = new TextField(String.valueOf(hotel.getNombreDeChambres()));
        String firstImgUrl = hotel.getImgs() != null && !hotel.getImgs().isEmpty() ? hotel.getImgs().get(0).getUrl() : "";
        TextField tfImageUrl = new TextField(firstImgUrl);

        GridPane form = new GridPane();
        form.setVgap(10); form.setHgap(10); form.setPadding(new Insets(16));
        form.add(new Label("Nom :"), 0, 0); form.add(tfNom, 1, 0);
        form.add(new Label("Rue :"), 0, 1); form.add(tfRue, 1, 1);
        form.add(new Label("Ville :"), 0, 2); form.add(tfVille, 1, 2);
        form.add(new Label("Code postal :"), 0, 3); form.add(tfCodePostal, 1, 3);
        form.add(new Label("Pays :"), 0, 4); form.add(tfPays, 1, 4);
        form.add(new Label("Étoiles :"), 0, 5); form.add(cbEtoiles, 1, 5);
        form.add(new Label("Nombre de chambres :"), 0, 6); form.add(tfNbChambres, 1, 6);
        form.add(new Label("Image (chemin ou URL) :"), 0, 7); form.add(tfImageUrl, 1, 7);

        Button btnSave = new Button("Enregistrer");
        Button btnCancel = new Button("Annuler");
        HBox actions = new HBox(10, btnSave, btnCancel);
        actions.setAlignment(Pos.CENTER_RIGHT);
        form.add(actions, 1, 8);

        btnCancel.setOnAction(e -> dialog.close());

        btnSave.setOnAction(e -> {
            String nom = tfNom.getText().trim();
            if (nom.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Validation", "Le nom de l'hôtel est requis.");
                return;
            }
            try {
                int nbCh = Integer.parseInt(tfNbChambres.getText().trim());
                // Mettre à jour l'adresse : si adresse existante => updateAdresse, sinon ajouterAdresse
                Adresse adresse = hotel.getAdresse();
                if (adresse == null) adresse = new Adresse();
                adresse.setRue(tfRue.getText().trim());
                adresse.setVille(tfVille.getText().trim());
                adresse.setCodepostal(tfCodePostal.getText().trim());
                adresse.setPays(tfPays.getText().trim());
                boolean addrOk;
                if (adresse.getIdAdresse() > 0) {
                    addrOk = adresseDAO.updateAdresse(adresse);
                } else {
                    addrOk = adresseDAO.ajouterAdresse(adresse);
                }
                if (!addrOk) {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'enregistrer l'adresse.");
                    return;
                }

                // assurer que l'objet hotelier est présent
                Hotelier hotelier = hotelierDAO.findById(currentHotelierId);
                if (hotelier == null) {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Hotelier non trouvé.");
                    return;
                }

                hotel.setNomHotel(nom);
                hotel.setEtoiles(cbEtoiles.getValue());
                hotel.setAdresse(adresse);
                hotel.setHotelier(hotelier);
                // mise à jour images (simple) : si url fourni et liste vide -> ajouter; sinon update première image
                String url = tfImageUrl.getText().trim();
                if (!url.isEmpty()) {
                    if (hotel.getImgs() == null || hotel.getImgs().isEmpty()) {
                        Image_hotel ih = new Image_hotel();
                        ih.setUrl(url);
                        ih.setDescription("Image principale");
                        hotel.addImg(ih);
                    } else {
                        Image_hotel first = hotel.getImgs().get(0);
                        first.setUrl(url);
                        imageHotelDAO.updateImage(first);
                    }
                }

                boolean ok = hotelDAO.updateHotel(hotel);
                if (ok) {
                    showAlert(Alert.AlertType.INFORMATION, "Succès", "Hôtel mis à jour.");
                    dialog.close();
                    loadHotels();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de mettre à jour l'hôtel.");
                }

            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.WARNING, "Validation", "Le nombre de chambres doit être un entier.");
            } catch (IllegalArgumentException ex) {
                showAlert(Alert.AlertType.WARNING, "Validation", ex.getMessage());
            }
        });

        Scene sc = new Scene(form);
        dialog.setScene(sc);
        dialog.showAndWait();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert a = new Alert(type);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(content);
        a.showAndWait();
    }

    // Placeholders pour les actions du menu (adapter si besoin)
    @FXML private void goToDashboard() { System.out.println("Go to Dashboard"); }
    @FXML private void goToMesHotels() { System.out.println("Go to Mes Hotels"); }
    @FXML private void goToChambres() { System.out.println("Go to Chambres"); }
    @FXML private void goToStatistiques() { System.out.println("Go to Statistiques"); }
    @FXML private void goToProfil() { System.out.println("Go to Profil"); }
    @FXML private void handleDeconnexion() { System.out.println("Déconnexion"); }
}