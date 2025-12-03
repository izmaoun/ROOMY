module com.roomy {
    // JavaFX modules
    requires javafx.controls;
    requires javafx.fxml;

    // Java SE
    requires java.sql;
    requires java.mail;

    // Lombok est utilisé seulement à la compilation
    requires static lombok;

    // Ajout du module jbcrypt pour accès à BCrypt
    requires jbcrypt;

    // Open packages used by JavaFX FXMLLoader (reflection)
    opens com.roomy.Controller to javafx.fxml;
    opens com.roomy.entities to javafx.base;
    opens com.roomy.service to javafx.graphics;

    // Export packages that may be utilisés par d'autres modules
    exports com.roomy;
    exports com.roomy.Controller;
    exports com.roomy.entities;
    exports com.roomy.service;
}
