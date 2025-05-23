package com.sistematickets.sistematickets;

import conexionBD.ConexionBaseDatos;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class GestionTickets {

    private final Queue<Ticket> colaTickets = new LinkedList<>();
    private final Stack<Ticket> historialCambios = new Stack<>();
    private int contadorId = 1;

    public Ticket crearTicket(String titulo, String descripcion) {
        Ticket nuevo = new Ticket(contadorId++, titulo, descripcion, "Pendiente", "Media", "usuario1");
        colaTickets.offer(nuevo);
        historialCambios.push(clonar(nuevo));
        return nuevo;
    }

    public Ticket atenderTicket() {
        return colaTickets.poll();
    }

    public Ticket deshacerUltimoCambio() {
        if (!historialCambios.isEmpty()) {
            Ticket ultimo = historialCambios.pop();
            colaTickets.removeIf(t -> t.getId() == ultimo.getId());
            colaTickets.offer(ultimo);
            return ultimo;
        }
        return null;
    }

    public Queue<Ticket> getColaTickets() {
        return colaTickets;
    }

    public boolean hayHistorial() {
        return !historialCambios.isEmpty();
    }

    private Ticket clonar(Ticket original) {
        return new Ticket(original.getId(), original.getTitulo(), original.getDescripcion(),
                original.getEstado(), original.getPrioridad(), original.getPersonaId());
    }

    public void reiniciar() {
        colaTickets.clear();
        historialCambios.clear();
        contadorId = 1;
    }

    public static class HelloController {
        @FXML
        private Label welcomeText;

        @FXML
        protected void onHelloButtonClick() {
            welcomeText.setText("Welcome to JavaFX Application!");
        }
    }

    public static class ParametrosController {

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


        private javafx.stage.Stage stage;

        private ParametrosSistema parametros;

        public ParametrosController(){

        }

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
            // Cerrar la ventana o limpiar campos
            // Aquí puedes implementar la lógica para cerrar la ventana o limpiar los campos
        }

        public void setStage(Stage stage) {
            this.stage = stage;
        }
    }

    public static class PermisoController {

        @FXML private TextField txtIdRol;
        @FXML private TextField txtNombreRol;
        @FXML private TextField txtUsuario;

        @FXML private RadioButton rbCrearTicket, rbEditarTicket, rbCrearUsuario,
                rbCambiarEstado, rbVerTicket, rbEliminarTicket,
                rbAsignarPermisos, rbCrearRoles, rbDefinirParametros, rbCrearDepartamentos;

        @FXML private Button btnGrabarRol;

        @FXML
        public void btnGrabarRol() {
            String nombreRol = txtNombreRol.getText().trim();

            if (nombreRol.length() < 3 || nombreRol.length() > 50) {
                mostrarAlerta("El nombre del rol debe tener entre 3 y 50 caracteres.");
                return;
            }

            String insertRol = "INSERT INTO rol (nombre) VALUES (?) RETURNING id";

            try (Connection conn = ConexionBaseDatos.BaseDatos();
                 PreparedStatement stmt = conn.prepareStatement(insertRol)) {

                stmt.setString(1, nombreRol);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        int rolId = rs.getInt("id");

                        if (rbCrearTicket.isSelected()) insertarPermiso(rolId, 1);
                        if (rbEditarTicket.isSelected()) insertarPermiso(rolId, 2);
                        if (rbCrearUsuario.isSelected()) insertarPermiso(rolId, 3);
                        if (rbCambiarEstado.isSelected()) insertarPermiso(rolId, 4);
                        if (rbVerTicket.isSelected()) insertarPermiso(rolId, 5);
                        if (rbEliminarTicket.isSelected()) insertarPermiso(rolId, 6);
                        if (rbAsignarPermisos.isSelected()) insertarPermiso(rolId, 7);
                        if (rbCrearRoles.isSelected()) insertarPermiso(rolId, 8);
                        if (rbDefinirParametros.isSelected()) insertarPermiso(rolId, 9);
                        if (rbCrearDepartamentos.isSelected()) insertarPermiso(rolId, 10);

                        mostrarConfirmacion("Rol y permisos guardados correctamente.");
                    }
                }

            } catch (SQLException e) {
                mostrarAlerta("Error al guardar el rol: " + e.getMessage());
            }
        }

        public void btnDeshacerRol() {
            txtNombreRol.clear();
            txtIdRol.clear();
            txtUsuario.clear();

            rbCrearTicket.setSelected(false);
            rbEditarTicket.setSelected(false);
            rbCrearUsuario.setSelected(false);
            rbCambiarEstado.setSelected(false);
            rbVerTicket.setSelected(false);
            rbEliminarTicket.setSelected(false);
            rbAsignarPermisos.setSelected(false);
            rbCrearRoles.setSelected(false);
            rbDefinirParametros.setSelected(false);
            rbCrearDepartamentos.setSelected(false);
        }


        private void insertarPermiso(int rolId, int permisoId) {
            String query = "INSERT INTO rol_permiso (rol_id, permiso_id) VALUES (" + rolId + ", " + permisoId + ")";
            ConexionBaseDatos.ejecutarInsercion(query);
        }

        private void mostrarAlerta(String mensaje) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(mensaje);
            alert.showAndWait();
        }

        private void mostrarConfirmacion(String mensaje) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Éxito");
            alert.setContentText(mensaje);
            alert.showAndWait();
        }

        @FXML
        public void btnCerrarSesion (ActionEvent actionEvent) {
            Stage currentStage = (Stage) ((Node) actionEvent.getSource())
                    .getScene()
                    .getWindow();

            new HelloApplication()
                    .muestraVentana(currentStage, "InicioSesion-view.fxml");

        }

    }
}
