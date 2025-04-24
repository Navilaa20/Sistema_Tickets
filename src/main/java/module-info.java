module com.sistematickets.sistematickets {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;
    requires java.sql;

    opens com.sistematickets.sistematickets to javafx.fxml;
    exports com.sistematickets.sistematickets;
}