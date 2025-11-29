module com.roomy {
    // JavaFX modules
    requires javafx.controls;
    requires javafx.fxml;

    // Java SE
    requires java.sql;

    // jBCrypt automatic module
    requires jbcrypt;

    // Lombok est utilisé seulement à la compilation
    requires static lombok;

    // Open packages used by JavaFX FXMLLoader (reflection)
    opens com.roomy.Controller to javafx.fxml;
    opens com.roomy.service to javafx.fxml;
    opens com.roomy.database to javafx.fxml;
    opens com.roomy.entities to javafx.fxml;
    opens com.roomy.Dao to javafx.fxml;

    // Export packages that may be used across modules (optional but safe)
    exports com.roomy.Controller;
    exports com.roomy.service;
    exports com.roomy.database;
    exports com.roomy.entities;
    exports com.roomy.Dao;
}
