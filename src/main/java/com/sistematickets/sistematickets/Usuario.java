package com.sistematickets.sistematickets;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Usuario extends Persona {


    public Ticket crearTicket(String descripcion) throws Exception {
        Ticket ticket = new Ticket();
        ticket.setDescripcion(descripcion);
        return ticket;
    }

    public void mostrarUsuario(Persona persona){
        System.out.println("Nombre: " + persona.getNombre());
    }


    public void cerrarSesUser(ActionEvent actionEvent) {
        javafx.stage.Stage currentStage = (Stage) ((Node) actionEvent.getSource())
                .getScene()
                .getWindow();

        new HelloApplication()
                .muestraVentana(currentStage, "InicioSesion-view.fxml");
    }
}
