package com.sistematickets.sistematickets;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import conexionBD.ConexionBaseDatos;

import java.sql.*;
import java.util.UUID;


public class AgregarUsuario extends Persona {

    private Stage scene;

    @FXML
    private TextField campoNombre,campoCorreo;


    @FXML
    private PasswordField campoContrasenia;

    @FXML private ComboBox<Rol> comboRol;
    private final ObservableList<Rol> comboRolList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        System.out.println("[DEBUG] initialize() — cargando roles...");
        cargarRolesDesdeBD();
        comboRol.setItems(comboRolList);
        System.out.println("[DEBUG] roles cargados: " + comboRolList);
    }

    private void cargarRolesDesdeBD() {
        String sql = "SELECT id, nombre FROM rol";
        try (Connection conn = ConexionBaseDatos.BaseDatos();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Rol r = new Rol(rs.getInt("id"), rs.getString("nombre"));
                comboRolList.add(r);
                System.out.println("[DEBUG] agregué rol: " + r);
            }

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "No se pudieron cargar los roles:\n" + e.getMessage());
        }
    }


    @FXML
    public void btnGuardarUsuario() {
        String nombre = campoNombre.getText().trim();
        String correo = campoCorreo.getText().trim();
        String contrasenia = campoContrasenia.getText();
        Rol rol = comboRol.getSelectionModel().getSelectedItem();


        if (rol== null){
            showAlert(Alert.AlertType.WARNING, "Advertencia", "Debe seleccionar un rol.");
            return;
        }

        String sql = """
    INSERT INTO persona (id, nombre, email, contrasenia, rol)
    VALUES (?,?,?,?,?)
    """;
        try ( Connection conn = ConexionBaseDatos.BaseDatos();
              PreparedStatement ps = conn.prepareStatement(sql) ) {

            // 1) Generar el UUID
            String id = UUID.randomUUID().toString();

            // 2) Asignar parámetros
            ps.setObject(1, UUID.fromString(id));      // para el id UUID
            ps.setString(2, nombre);
            ps.setString(3, correo);
            ps.setString(4, contrasenia);

            // 🔑 Aquí pasamos el nombre del rol, no el id
            ps.setString(5, comboRol.getValue().getNombre());

            // 3) Ejecutar
            ps.executeUpdate();
            showAlert(Alert.AlertType.INFORMATION, "Éxito", "Usuario agregado correctamente.");

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR,
                    "No se pudo agregar el usuario:",
                    e.getMessage());
        }

    }






    @FXML
    private void btnCancelarUsuario(ActionEvent actionEvent) {
        Stage stage = (Stage) campoNombre.getScene().getWindow();
        new HelloApplication().muestraVentana(stage, "Administrador-view.fxml");
    }

    private void limpiarFormulario() {
        campoNombre.clear();
        campoCorreo.clear();
        campoContrasenia.clear();
        comboRol.getSelectionModel().clearSelection();
    }

    private static void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }




}
