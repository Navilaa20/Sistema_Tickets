package com.sistematickets.sistematickets;

import conexionBD.ConexionBaseDatos;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

import java.sql.*;


public class PermisoController {

    @FXML
    private TextField txtNombreRol;

    @FXML private RadioButton rbCrearTicket, rbEditarTicket, rbCrearUsuario,
            rbCambiarEstado, rbVerTicket, rbEliminarTicket,
            rbAsignarPermisos, rbCrearRoles, rbDefinirParametros, rbCrearDepartamentos;

    @FXML
    public void btnGrabarRol(ActionEvent event) {

        String nombreRol = txtNombreRol.getText().trim();

        if (nombreRol.length() < 3 || nombreRol.length() > 50) {
            mostrarAlerta("El nombre del rol debe tener entre 3 y 50 caracteres.");
            return;
        }

        String insertRol = "INSERT INTO rol (nombre) VALUES (?) RETURNING id";

        try (Connection conn = ConexionBaseDatos.BaseDatos();
             PreparedStatement stmt = conn.prepareStatement(insertRol)) {

            stmt.setString(1, nombreRol);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int rolId = rs.getInt("id");

                    if (rbCrearTicket.isSelected()) insertarPermiso(rolId, 1);
                    if (rbEditarTicket.isSelected()) insertarPermiso(rolId, 2);
                    if (rbCrearUsuario.isSelected()) insertarPermiso(rolId, 3);
                    if (rbCambiarEstado.isSelected()) insertarPermiso(rolId, 4);
                    if (rbVerTicket.isSelected()) insertarPermiso(rolId, 5);
                    if (rbEliminarTicket.isSelected()) insertarPermiso(rolId, 6);
                    if (rbAsignarPermisos.isSelected()) insertarPermiso(rolId, 7);
                    if (rbCrearRoles.isSelected()) insertarPermiso(rolId, 8);
                    if (rbDefinirParametros.isSelected()) insertarPermiso(rolId, 9);
                    if (rbCrearDepartamentos.isSelected()) insertarPermiso(rolId, 10);

                    mostrarConfirmacion("Rol y permisos guardados correctamente.");
                }
            }

        } catch (SQLException e) {
            mostrarAlerta("Error al guardar el rol: " + e.getMessage());
        }
    }


    private void insertarPermiso(int rolId, int permisoId) {
        String query = "INSERT INTO rol_permiso (rol_id, permiso_id) VALUES (?, ?)";

        try (Connection conn = ConexionBaseDatos.BaseDatos();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, rolId);
            stmt.setInt(2, permisoId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            mostrarAlerta("Error al insertar permiso: " + e.getMessage());
        }
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarConfirmacion(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Éxito");
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    private Button btnDeshacerRol;

    @FXML
    public void initialize() {
        btnDeshacerRol.setOnAction(e -> limpiarFormulario());
    }

    private void limpiarFormulario() {
        txtNombreRol.clear();
        rbCrearTicket.setSelected(false);
        rbEditarTicket.setSelected(false);
        rbCrearUsuario.setSelected(false);
        rbCambiarEstado.setSelected(false);
        rbVerTicket.setSelected(false);
        rbEliminarTicket.setSelected(false);
        rbAsignarPermisos.setSelected(false);
        rbCrearRoles.setSelected(false);
        rbDefinirParametros.setSelected(false);
        rbCrearDepartamentos.setSelected(false);
    }
}


