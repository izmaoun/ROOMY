module com.roomy {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires java.sql;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires static lombok;
    requires jbcrypt;


    opens com.roomy to javafx.fxml;
    exports com.roomy;
    exports com.roomy.Controller;
    opens com.roomy.Controller to javafx.fxml;
}