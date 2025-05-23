package com.sistematickets.sistematickets;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

import java.util.List;

public class Tecnico extends Persona {

    private List<Ticket> ticketsAsignados;


    public List<Ticket> getTicketsAsignados() {
        return ticketsAsignados;
    }

    public void setTicketsAsignados(List<Ticket> ticketsAsignados) {
        this.ticketsAsignados = ticketsAsignados;
    }

    public void gestionarTicket(){

    }

    public void asignarTicket(){

    }

    public void resolverTicket(){

    }

    public void cerrarSesTec(ActionEvent actionEvent) {
        javafx.stage.Stage currentStage = (Stage) ((Node) actionEvent.getSource())
                .getScene()
                .getWindow();

        new HelloApplication()
                .muestraVentana(currentStage, "InicioSesion-view.fxml");
    }
}
