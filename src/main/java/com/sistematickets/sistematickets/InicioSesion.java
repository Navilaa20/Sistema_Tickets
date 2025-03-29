package com.sistematickets.sistematickets;

import javafx.fxml.FXML;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class InicioSesion implements Stage {


    private Stage stage;
    public InicioSesion() {
        this.stage = stage;
    }


    @Override
    public void setStage(Stage stage) {

    }

    @FXML
    protected void iniciarSesion(ActionEvent event) {

        String Usuario = JOptionPane.showInputDialog("Ingrese su usuario");

    }
}
