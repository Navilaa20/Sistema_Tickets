package com.sistematickets.sistematickets;

import conexionBD.ConexionBaseDatos;
import javafx.fxml.FXML;

import javafx.event.ActionEvent;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class InicioSesion {

    @FXML private TextField txtUsuario;
    @FXML private TextField txtContrasena;

    @FXML
    public void btnIniciar(ActionEvent actionEvent) {
        String email = txtUsuario.getText().trim();
        String pass  = txtContrasena.getText().trim();

        String sql = """
            SELECT nombre, rol
              FROM persona
             WHERE email = ? 
               AND contrasenia = ?
        """;

        try (Connection conn = ConexionBaseDatos.BaseDatos();
             PreparedStatement ps = conn.prepareStatement(sql) ) {

            ps.setString(1, email);
            ps.setString(2, pass);

            try ( ResultSet rs = ps.executeQuery() ) {
                if (!rs.next()) {
                    // credenciales inválidas
                    new Alert(Alert.AlertType.ERROR, "Usuario o contraseña incorrectos.").showAndWait();
                    return;
                }

                String nombre = rs.getString("nombre");
                String rol    = rs.getString("rol");       // <-- aquí leemos el rol

                // decidir qué FXML abrir según el rol
                String fxml;
                String title;
                switch (rol) {
                    case "Administrador":
                        fxml  = "Administrador-view.fxml";
                        title = "Panel Administrador";
                        break;
                    case "Tecnico":
                        fxml  = "Tecnico-view.fxml";
                        title = "Panel Técnico";
                        break;
                    default:
                        fxml  = "Usuario-view.fxml";
                        title = "Panel Usuario";
                }

                // cargar la nueva escena
                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
                Parent root = loader.load();
                Stage stage = (Stage) ((Node) actionEvent.getSource())
                        .getScene().getWindow();
                stage.setTitle("Bienvenido " + nombre + " — " + title);
                stage.setScene(new Scene(root, 700, 500));
                stage.show();
            }

        } catch (SQLException | IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error al iniciar sesión:\n" + e.getMessage())
                    .showAndWait();
        }
    }

}
