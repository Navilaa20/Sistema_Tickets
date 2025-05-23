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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class AgregarUsuario extends Persona {

    private Stage scene;

    @FXML
    private TextField campoNombre,campoCorreo;


    @FXML
    private PasswordField campoContrasenia;

    @FXML
    private ComboBox<Rol> comboRol;
    ObservableList<Rol> comboRolList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        cargarRolesDesdeBD();
    }

    private void cargarRolesDesdeBD() {
        String sql = "SELECT id, nombre FROM rol";
        try (Connection conn = ConexionBaseDatos.BaseDatos();
             Statement stmt = conn.createStatement();
             ResultSet rs   = stmt.executeQuery(sql)) {

            while (rs.next()) {
                comboRolList.add(new Rol(rs.getInt("id"), rs.getString("nombre")));
            }
            comboRol.setItems(comboRolList);

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR,
                    "Error", "No se pudieron cargar los roles:\n" + e.getMessage());
        }
    }


    @FXML
    public void btnGuardarUsuario() {
        String nombre = campoNombre.getText().replace("'","''");
        String correo = campoCorreo.getText().replace("'","''");
        Rol rol = comboRol.getSelectionModel().getSelectedItem();
        String contrasenia = campoContrasenia.getText().replace("'","''");

        if (rol== null){
            showAlert(Alert.AlertType.WARNING, "Advertencia", "Debe seleccionar un rol.");
            return;
        }

        String agregarUsuarios = "INSERT INTO persona (nombre,email,contrasenia,rol) VALUES ('"+ nombre + "','" + correo + "','" + contrasenia + "','" + rol.getId() + "');";


        try{
            Statement statement = ConexionBaseDatos.BaseDatos().createStatement();
            statement.executeUpdate(agregarUsuarios);
            showAlert(Alert.AlertType.INFORMATION, "Mensaje", "Registro Exitoso, Usuario Agregado");
            limpiarFormulario();
        } catch(SQLException e){
            showAlert(Alert.AlertType.ERROR, "Mensaje","Error al agregar el usuario");
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
