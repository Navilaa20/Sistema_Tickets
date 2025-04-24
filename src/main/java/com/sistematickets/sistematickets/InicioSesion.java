package com.sistematickets.sistematickets;

import conexionBD.ConexionBaseDatos;
import javafx.fxml.FXML;

import javax.swing.*;

import javafx.event.ActionEvent;

import javafx.scene.control.*;

import java.sql.ResultSet;
import java.sql.SQLException;


public class InicioSesion implements Stage {

    @FXML
    private TextField txtUsuario;
    @FXML
    private TextField txtContrasena;

    private Stage stage;
    public InicioSesion() {
        this.stage = stage;
    }


    @Override
    public void setStage(Stage stage) {

    }

    @FXML
    public void btnIniciar(ActionEvent actionEvent) throws SQLException {

        String Usuario = txtUsuario.getText();
        String Contrasena = txtContrasena.getText();
        String sql = "SELECT * FROM persona WHERE email = '" + Usuario + "' AND contrasenia = '" + Contrasena + "'";
        ResultSet rs = ConexionBaseDatos.ConsultaSQL(sql);

        if(rs.next()){
            System.out.println(rs.getString("nombre"));
        } else {
            System.out.println("No se encontro el nombre");
        }


    }

}
