package com.sistematickets.sistematickets;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Administrador extends Persona {

    @FXML
    Label lblNombre;
    private String userId;
    private String currentUserId;

    private transient javafx.stage.Stage stage;
    private List<Permiso> permisos;

    public Administrador() {
        super();
        this.permisos = new ArrayList<>();
    }

    @FXML
    public void btnParametros(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ParametroController-view.fxml"));
        Parent root = fxmlLoader.load();
        javafx.stage.Stage stage = (javafx.stage.Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 700 , 500);

        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }


    @FXML
    public void btnRol(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Rol-view.fxml"));
        Parent root = fxmlLoader.load();
        javafx.stage.Stage stage = (javafx.stage.Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 700 , 500);

        stage.setTitle("Adiminstrando Roles");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void btnDepartamentos (ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Departamento-view.fxml"));
        Parent root = fxmlLoader.load();
        javafx.stage.Stage stage = (javafx.stage.Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 700 , 500);

        stage.setTitle("Gestión de Departamentos");
        stage.setScene(scene);
        stage.show();

    }


    @FXML
    public void btnCrearUsuario (ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CrearUsuario.fxml"));
        Parent root = fxmlLoader.load();
        javafx.stage.Stage stage = (javafx.stage.Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 700 , 500);

        stage.setTitle("Bienvenido a Sistema Tickets Nilver");
        stage.setScene(scene);
        stage.show();
    }


    @FXML
    public void CerrarSesionAdm(ActionEvent actionEvent) {
        Stage currentStage = (Stage) ((Node) actionEvent.getSource())
                .getScene()
                .getWindow();

        new HelloApplication()
                .muestraVentana(currentStage, "InicioSesion-view.fxml");

    }

    public void initData(Persona persona) {
        this.userId = persona.getId().toString();        this.lblNombre.setText(persona.getNombre());
        lblNombre.setText(persona.getNombre());    }
}