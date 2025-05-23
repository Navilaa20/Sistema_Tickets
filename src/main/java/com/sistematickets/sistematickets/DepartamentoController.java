package com.sistematickets.sistematickets;

import conexionBD.ConexionBaseDatos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class DepartamentoController {

    @FXML private TextField txtNombre;
    @FXML private TextArea txtDescripcion;

    @FXML private ListView<Persona> listTecnicos;
    @FXML private ListView<Persona> listSeleccionados;

    private ObservableList<Persona> tecnicosDisponibles = FXCollections.observableArrayList();
    private ObservableList<Persona> tecnicosSeleccionados = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        cargarTecnicos();
        listTecnicos.setItems(tecnicosDisponibles);
        listSeleccionados.setItems(tecnicosSeleccionados);

        listTecnicos.setOnMouseClicked(this::agregarTecnico);
        listSeleccionados.setOnMouseClicked(this::removerTecnico);

        // Mostrar solo nombre en cada ListView
        listTecnicos.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Persona item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNombre());
            }
        });

        listSeleccionados.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Persona item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNombre());
            }
        });
    }

    private void cargarTecnicos() {
        String sql = "SELECT id, nombre, email FROM persona WHERE rol = 'Tecnico'";
        tecnicosDisponibles.clear();

        try (Connection conn = ConexionBaseDatos.BaseDatos();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                UUID id = (UUID) rs.getObject("id");
                String nombre = rs.getString("nombre");
                String email = rs.getString("email");

                Persona tecnico = new Persona(id, nombre, email);
                tecnicosDisponibles.add(tecnico);
                System.out.println("Técnico cargado: " + tecnico.getNombre());
            }

            System.out.println("Total técnicos disponibles: " + tecnicosDisponibles.size());

        } catch (SQLException e) {
            mostrarAlerta("Error", "No se pudieron cargar los técnicos:\n" + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void agregarTecnico(MouseEvent event) {
        Persona seleccionado = listTecnicos.getSelectionModel().getSelectedItem();
        if (seleccionado != null && !tecnicosSeleccionados.contains(seleccionado)) {
            tecnicosSeleccionados.add(seleccionado);
        }
    }

    private void removerTecnico(MouseEvent event) {
        Persona seleccionado = listSeleccionados.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            tecnicosSeleccionados.remove(seleccionado);
        }
    }

    @FXML
    private void guardarDepartamento() {
        String nombre = txtNombre.getText().trim();
        String descripcion = txtDescripcion.getText().trim();

        if (nombre.length() < 3 || nombre.length() > 50) {
            mostrarAlerta("Error", "El nombre debe tener entre 3 y 50 caracteres.", Alert.AlertType.WARNING);
            return;
        }

        if (tecnicosSeleccionados.isEmpty()) {
            mostrarAlerta("Error", "Debe seleccionar al menos un técnico.", Alert.AlertType.WARNING);
            return;
        }

        String insertDepartamento = "INSERT INTO departamento (nombre, descripcion) VALUES (?, ?) RETURNING id";
        String insertAsignacion = "INSERT INTO departamento_tecnico (departamento_id, persona_id) VALUES (?, ?)";

        try (Connection conn = ConexionBaseDatos.BaseDatos()) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmtDept = conn.prepareStatement(insertDepartamento)) {
                stmtDept.setString(1, nombre);
                stmtDept.setString(2, descripcion);
                ResultSet rs = stmtDept.executeQuery();

                if (rs.next()) {
                    int deptId = rs.getInt("id");

                    try (PreparedStatement stmtAsignacion = conn.prepareStatement(insertAsignacion)) {
                        for (Persona tecnico : tecnicosSeleccionados) {
                            stmtAsignacion.setInt(1, deptId);
                            stmtAsignacion.setObject(2, tecnico.getId());
                            stmtAsignacion.executeUpdate();
                        }
                    }
                }
                conn.commit();
                mostrarAlerta("Éxito", "Departamento registrado correctamente.", Alert.AlertType.INFORMATION);
                limpiarFormulario();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }

        } catch (SQLException e) {
            mostrarAlerta("Error", "No se pudo registrar el departamento:\n" + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void limpiarFormulario() {
        txtNombre.clear();
        txtDescripcion.clear();
        tecnicosSeleccionados.clear();
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
