package com.sistematickets.sistematickets;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("InicioSesion-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700 , 500);

        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public void muestraVentana(Stage stage, String fxmlFile){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
            Pane panel = fxmlLoader.load();
            Scene scene = new Scene(panel);


            stage.setScene(scene);
            stage.show();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void llenarCombo(ComboBox<String> llenarCombo, ObservableList<String> infoCombo){
        llenarCombo.setItems(infoCombo);
    }


}