module com.sistematickets.sistematickets {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;

    opens com.sistematickets.sistematickets to javafx.fxml;
    exports com.sistematickets.sistematickets;
}