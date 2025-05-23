package com.sistematickets.sistematickets;

import conexionBD.ConexionBaseDatos;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class ParametrosController {

    @FXML
    private TextField txtNombreEmpresa;

    @FXML
    private TextField txtLogoPath;

    @FXML
    private ComboBox<String> cmbIdioma;

    @FXML
    private ComboBox<String> cmbZonaHoraria;

    @FXML
    private Spinner<Integer> spnDiasVencimiento;

    @FXML
    private TextField txtPrioridadAlta;

    @FXML
    private TextField txtPrioridadMedia;

    @FXML
    private TextField txtPrioridadBaja;


    private Stage stage;

    private ParametrosSistema parametros;

    public ParametrosController(){

    }

    @FXML
    public void initialize() {
        // Inicializar ComboBox de idioma
        cmbIdioma.getItems().addAll("Español", "Inglés");

        // Inicializar ComboBox de zona horaria
        cmbZonaHoraria.getItems().addAll("GMT-6", "GMT-5", "GMT-4", "GMT-3", "GMT-2", "GMT-1", "GMT", "GMT+1", "GMT+2", "GMT+3");

        // Inicializar Spinner de días de vencimiento
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 365, 30);
        spnDiasVencimiento.setValueFactory(valueFactory);

        // Cargar parámetros existentes
        parametros = ParametrosSistema.cargarParametros();
        if (parametros != null) {
            txtNombreEmpresa.setText(parametros.getNombreEmpresa());
            txtLogoPath.setText(parametros.getLogoPath());
            cmbIdioma.setValue(parametros.getIdioma());
            cmbZonaHoraria.setValue(parametros.getZonaHoraria());
            spnDiasVencimiento.getValueFactory().setValue(parametros.getDiasVencimiento());
            List<String> niveles = parametros.getNivelesPrioridad();
            if (niveles.size() >= 3) {
                txtPrioridadAlta.setText(niveles.get(0));
                txtPrioridadMedia.setText(niveles.get(1));
                txtPrioridadBaja.setText(niveles.get(2));
            }
        }
    }

    @FXML
    private void seleccionarLogo() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Logo");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg")
        );
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            if (selectedFile.length() > 2 * 1024 * 1024) {
                ConexionBaseDatos.showAlert("Error", "El archivo seleccionado supera los 2MB.");
                return;
            }
            txtLogoPath.setText(selectedFile.getAbsolutePath());
        }
    }

    @FXML
    private void guardarParametros() {
        String nombreEmpresa = txtNombreEmpresa.getText().trim();
        String logoPath = txtLogoPath.getText().trim();
        String idioma = cmbIdioma.getValue();
        String zonaHoraria = cmbZonaHoraria.getValue();
        Integer diasVencimiento = spnDiasVencimiento.getValue();
        String prioridadAlta = txtPrioridadAlta.getText().trim();
        String prioridadMedia = txtPrioridadMedia.getText().trim();
        String prioridadBaja = txtPrioridadBaja.getText().trim();

        // Validaciones
        if (nombreEmpresa.isEmpty() || nombreEmpresa.length() < 3 || nombreEmpresa.length() > 100) {
            ConexionBaseDatos.showAlert("Error", "El nombre de la empresa debe tener entre 3 y 100 caracteres.");
            return;
        }

        if (logoPath.isEmpty()) {
            ConexionBaseDatos.showAlert("Error", "Debe seleccionar un logo para la empresa.");
            return;
        }

        if (idioma == null || idioma.isEmpty()) {
            ConexionBaseDatos.showAlert("Error", "Debe seleccionar un idioma predeterminado.");
            return;
        }

        if (zonaHoraria == null || zonaHoraria.isEmpty()) {
            ConexionBaseDatos.showAlert("Error", "Debe seleccionar una zona horaria.");
            return;
        }

        if (diasVencimiento < 1 || diasVencimiento > 365) {
            ConexionBaseDatos.showAlert("Error", "El tiempo de vencimiento debe estar entre 1 y 365 días.");
            return;
        }

        if (prioridadAlta.isEmpty() || prioridadMedia.isEmpty() || prioridadBaja.isEmpty()) {
            ConexionBaseDatos.showAlert("Error", "Debe definir al menos tres niveles de prioridad.");
            return;
        }

        // Guardar parámetros
        parametros.setNombreEmpresa(nombreEmpresa);
        parametros.setLogoPath(logoPath);
        parametros.setIdioma(idioma);
        parametros.setZonaHoraria(zonaHoraria);
        parametros.setDiasVencimiento(diasVencimiento);
        parametros.setNivelesPrioridad(Arrays.asList(prioridadAlta, prioridadMedia, prioridadBaja));

        parametros.guardarParametros();

        ConexionBaseDatos.showAlert("Éxito", "Parámetros guardados correctamente.");
    }

    @FXML
    private void cancelar() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancelar");
        alert.setHeaderText(null);
        alert.setContentText("¿Qué deseas hacer?");
        alert.getButtonTypes().setAll(
                new ButtonType("Limpiar", ButtonBar.ButtonData.YES),
                new ButtonType("Cerrar", ButtonBar.ButtonData.NO),
                new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE)
        );

        alert.showAndWait().ifPresent(response -> {
            if (response.getButtonData() == ButtonBar.ButtonData.YES) {
                limpiarFormulario();
            } else if (response.getButtonData() == ButtonBar.ButtonData.NO) {
                if (stage != null) stage.close();
            }
        });
    }

    private void limpiarFormulario() {
        txtNombreEmpresa.clear();
        txtLogoPath.clear();
        cmbIdioma.getSelectionModel().clearSelection();
        cmbZonaHoraria.getSelectionModel().clearSelection();

        SpinnerValueFactory<Integer> valueFactory = spnDiasVencimiento.getValueFactory();
        if (valueFactory != null) {
            valueFactory.setValue(30);
        }

        txtPrioridadAlta.clear();
        txtPrioridadMedia.clear();
        txtPrioridadBaja.clear();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
