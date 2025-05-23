package com.sistematickets.sistematickets;

import conexionBD.ConexionBaseDatos;
import javafx.fxml.FXML;

import javafx.event.ActionEvent;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;


public class InicioSesion {

    @FXML
    private TextField txtUsuario;
    @FXML
    private TextField txtContrasena;


    @FXML
    public void btnIniciar(ActionEvent actionEvent) throws SQLException, IOException {

        String Usuario = txtUsuario.getText();
        String Contrasena = txtContrasena.getText();
        String sql = "SELECT * FROM persona WHERE email = '" + Usuario + "' AND contrasenia = '" + Contrasena + "'";
        ResultSet rs = ConexionBaseDatos.ConsultaSQL(sql);

        if(rs.next()){
            System.out.println(rs.getString("nombre"));
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Administrador-view.fxml"));
            Parent root = fxmlLoader.load();
            javafx.stage.Stage stage = (javafx.stage.Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 700 , 500);

            stage.setTitle("Hello!");
            stage.setScene(scene);
            stage.show();

        } else {
            System.out.println("No se encontro el nombre");
        }


    }

}
