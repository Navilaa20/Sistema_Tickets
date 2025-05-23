package com.sistematickets.sistematickets;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Usuario extends Persona {

    public void cerrarSesUser(ActionEvent actionEvent) {
        javafx.stage.Stage currentStage = (Stage) ((Node) actionEvent.getSource())
                .getScene()
                .getWindow();

        new HelloApplication()
                .muestraVentana(currentStage, "InicioSesion-view.fxml");
    }

    public void onConsultarTik(ActionEvent actionEvent) {

    }

    public void onCrearTik(ActionEvent actionEvent) {
        javafx.stage.Stage currentStage = (Stage) ((Node) actionEvent.getSource())
                .getScene()
                .getWindow();
        new HelloApplication()
                .muestraVentana(currentStage, "Ticket-view.fxml");
    }
}
