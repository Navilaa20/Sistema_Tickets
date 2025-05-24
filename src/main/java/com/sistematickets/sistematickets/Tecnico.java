package com.sistematickets.sistematickets;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Tecnico extends Persona {

    @FXML
    Label lblNombre;
    private String userId;
    private String currentUserId;

    private List<Ticket> ticketsAsignados;

    public void setTicketsAsignados(List<Ticket> ticketsAsignados) {
        this.ticketsAsignados = ticketsAsignados;
    }

    public Tecnico(UUID id, String nombre, String email) {
        super(id, nombre, email);
        this.ticketsAsignados = new ArrayList<>();
    }

    public List<Ticket> getTicketsAsignados() {
        return ticketsAsignados;
    }

    public void asignarTicket(Ticket ticket) {
        ticketsAsignados.add(ticket);
        ticket.setAsignado(true);
        ticket.setEstado("Asignado");
    }

    public void resolverTicket(Ticket ticket) {
        if (ticketsAsignados.contains(ticket)) {
            ticket.setEstado("Resuelto");
        }
    }

    public void cerrarSesTec(ActionEvent actionEvent) {
        javafx.stage.Stage currentStage = (Stage) ((Node) actionEvent.getSource())
                .getScene()
                .getWindow();

        new HelloApplication()
                .muestraVentana(currentStage, "InicioSesion-view.fxml");
    }


    public void initData(Persona persona) {
        this.userId = String.valueOf(persona.getId());
        this.lblNombre.setText(persona.getNombre());
        lblNombre.setText(persona.getNombre());
    }
}

