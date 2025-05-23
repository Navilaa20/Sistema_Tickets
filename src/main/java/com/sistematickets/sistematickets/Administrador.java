package com.sistematickets.sistematickets;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Administrador extends Persona {

    private transient javafx.stage.Stage stage;
    private List<Permiso> permisos;

    public Administrador() {
        super();
        this.permisos = new ArrayList<>();
    }

    public List <Permiso> getPermisos(){
        return permisos;
    }

    public void setPermisos(List<Permiso> permisos) {
        this.permisos = permisos;
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

    public void gestionEmpresa(){
        System.out.println("Gestión de información de la empresa");
    }


    public void btnGestionarRoles(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Rol-view.fxml"));
        Parent root = fxmlLoader.load();
        javafx.stage.Stage stage = (javafx.stage.Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 700 , 500);

        stage.setTitle("Gestión de Roles y Permisos");
        stage.setScene(scene);
        stage.show();
    }

    public void gestionPermisos(){

    }

    public void gestionDepartamentos (){

    }

    public void gestionEstadosTicket(){
        System.out.println("Abierto, En Proceso, Cerrado.");
    }

    public void gestionTicket (){
        System.out.println("Gestión de tickets...");
        List<Ticket> lista = Ticket.listar();
        for (Ticket t : lista) {
            System.out.println("Ticket #" + t.getId() + " - Estado: " + t.getEstado());
        }
    }

    public void gestionFlujos (){

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
}
