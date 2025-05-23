package com.sistematickets.sistematickets;


import com.sistematickets.sistematickets.Departamento;
import com.sistematickets.sistematickets.Tecnico;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;

public class DepartamentoController {

    @FXML private TextField txtNombre;
    @FXML private TextArea txtDescripcion;
    @FXML private ListView<Tecnico> listTecnicos;
    @FXML private ListView<Tecnico> listSeleccionados;

    private ObservableList<Tecnico> tecnicosDisponibles = FXCollections.observableArrayList();
    private ObservableList<Tecnico> tecnicosSeleccionados = FXCollections.observableArrayList();



    private void agregarTecnico(MouseEvent event) {
        Tecnico seleccionado = listTecnicos.getSelectionModel().getSelectedItem();
        if (seleccionado != null && !tecnicosSeleccionados.contains(seleccionado)) {
            tecnicosSeleccionados.add(seleccionado);
        }
    }

    private void removerTecnico(MouseEvent event) {
        Tecnico seleccionado = listSeleccionados.getSelectionModel().getSelectedItem();
        tecnicosSeleccionados.remove(seleccionado);
    }

    @FXML
    private void guardarDepartamento() {
        String nombre = txtNombre.getText().trim();
        String descripcion = txtDescripcion.getText().trim();

        if (nombre.length() < 3 || nombre.length() > 50) {
            showAlert("Error", "El nombre del departamento debe tener entre 3 y 50 caracteres.");
            return;
        }

        if (tecnicosSeleccionados.isEmpty()) {
            showAlert("Error", "Debe asignar al menos un técnico al departamento.");
            return;
        }

        Departamento depto = new Departamento(nombre, descripcion, new ArrayList<>(tecnicosSeleccionados));
        System.out.println("Departamento guardado: " + depto.getNombre());
        showAlert("Éxito", "Departamento registrado correctamente.");
    }

    private void showAlert(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}