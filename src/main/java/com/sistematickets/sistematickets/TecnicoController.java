package com.sistematickets.sistematickets;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.UUID;

public class TecnicoController {

    @FXML
    private Label lblNombre;

    @FXML
    private TableView<Ticket> tableTickets;

    @FXML
    private TableColumn<Ticket, Integer> colId;

    @FXML
    private TableColumn<Ticket, String> colTitulo;

    @FXML
    private TableColumn<Ticket, String> colPrioridad;

    @FXML
    private TableColumn<Ticket, String> colDepartamento;

    @FXML
    private TableColumn<Ticket, String> colEstado;

    @FXML
    private Button btnAsignar;

    @FXML
    private Button btnResolver;

    @FXML
    private Button btnCerrarSesion;

    private Tecnico tecnico;
    private ObservableList<Ticket> listaTickets;

    @FXML
    private void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colPrioridad.setCellValueFactory(new PropertyValueFactory<>("prioridad"));
        colDepartamento.setCellValueFactory(new PropertyValueFactory<>("departamento"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        // Lógica para cargar los tickets desde la base de datos
    }

    public void initData(Tecnico tecnico) {
        this.tecnico = tecnico;
        lblNombre.setText(tecnico.getNombre());
    }

    @FXML
    private void asignarTicket(ActionEvent event) {
        Ticket ticketSeleccionado = tableTickets.getSelectionModel().getSelectedItem();
        if (ticketSeleccionado != null && !ticketSeleccionado.isAsignado()) {
            tecnico.asignarTicket(ticketSeleccionado);
            tableTickets.refresh();
        }
    }

    @FXML
    private void resolverTicket(ActionEvent event) {
        Ticket ticketSeleccionado = tableTickets.getSelectionModel().getSelectedItem();
        if (ticketSeleccionado != null && ticketSeleccionado.isAsignado()) {
            tecnico.resolverTicket(ticketSeleccionado);
            tableTickets.refresh();
        }
    }

    @FXML
    private void cerrarSesion(ActionEvent event) {
        Stage stage = (Stage) btnCerrarSesion.getScene().getWindow();
        stage.close();
    }
}
