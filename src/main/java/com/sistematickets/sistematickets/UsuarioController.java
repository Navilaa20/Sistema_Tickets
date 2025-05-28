package com.sistematickets.sistematickets;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import conexionBD.ConexionBaseDatos;
import java.util.List;

public class UsuarioController {

    @FXML private TextField tfTitulo;
    @FXML private TextField tfDepartamento;
    @FXML private TextField tfPrioridad;
    @FXML private TextArea tfDescripcion;
    @FXML private RadioButton rbAsignadoSi;
    @FXML private RadioButton rbAsignadoNo;
    @FXML private ComboBox<String> cbTecnicoAsignado;
    

    private ToggleGroup asignadoGroup = new ToggleGroup();

    private Connection conexion;

    @FXML
    public void initialize() {
        conexion = ConexionBaseDatos.BaseDatos();

        // Grupo para los RadioButtons
        rbAsignadoSi.setToggleGroup(asignadoGroup);
        rbAsignadoNo.setToggleGroup(asignadoGroup);

        cargarTecnicos();

    }


    public void ticCerrarSesion(ActionEvent event) {
        new Usuario().cerrarSesUser(event);
    }

    public void ticGuardar(ActionEvent event) {
        try {
            String titulo = tfTitulo.getText();
            String departamento = tfDepartamento.getText();
            String prioridad = tfPrioridad.getText();
            String descripcion = tfDescripcion.getText();
            boolean asignado = rbAsignadoSi.isSelected();
            String tecnicoNombre = cbTecnicoAsignado.getValue();
            String estado = "Nuevo";
            String personaId = "123"; // Cambia por el ID real del usuario logueado

            if (titulo.isEmpty() || departamento.isEmpty() || prioridad.isEmpty() || descripcion.isEmpty()) {
                mostrarAlerta("Todos los campos deben estar llenos.");
                return;
            }

            String tecnicoId = obtenerIdTecnico(tecnicoNombre);

            PreparedStatement stmt = conexion.prepareStatement(
                    "INSERT INTO ticket (titulo, descripcion, estado, departamento, prioridad, asignado, persona_id, tecnico_id, fecha_creacion) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"
            );
            stmt.setString(1, titulo);
            stmt.setString(2, descripcion);
            stmt.setString(3, estado);
            stmt.setString(4, departamento);
            stmt.setString(5, prioridad);
            stmt.setBoolean(6, asignado);
            stmt.setString(7, personaId);
            stmt.setString(8, tecnicoId);
            stmt.setTimestamp(9, Timestamp.valueOf(LocalDateTime.now()));

            stmt.executeUpdate();
            mostrarAlerta("Ticket guardado con éxito.");
            limpiarCampos();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error al guardar el ticket: " + e.getMessage());
        }
    }

    public void ticDeshacer(ActionEvent event) {
        limpiarCampos();
    }

    private void limpiarCampos() {
        tfTitulo.clear();
        tfDepartamento.clear();
        tfPrioridad.clear();
        tfDescripcion.clear();
        asignadoGroup.selectToggle(null);
        cbTecnicoAsignado.setValue(null);
    }

    private void cargarTecnicos() {
        ObservableList<String> tecnicos = FXCollections.observableArrayList();

        try (Statement stmt = conexion.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM persona WHERE rol = 'Tecnico'");

            while (rs.next()) {
                tecnicos.add(rs.getString("nombre"));
            }

            cbTecnicoAsignado.setItems(tecnicos);

        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta("Error al cargar técnicos.");
        }
    }

    private String obtenerIdTecnico(String nombre) throws SQLException {
        if (nombre == null || nombre.isEmpty()) return null;

        PreparedStatement ps = conexion.prepareStatement("SELECT * FROM persona WHERE nombre = ?");
        ps.setString(1, nombre);
        PreparedStatement stmt = null;
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getString("id");
        }
        return null;
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
