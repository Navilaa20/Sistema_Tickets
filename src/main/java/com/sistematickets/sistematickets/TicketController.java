package com.sistematickets.sistematickets;

import conexionBD.ConexionBaseDatos;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.UUID;

public class TicketController {

    @FXML private TextField tfTitulo;
    @FXML private TextField    tfDepartamento;
    @FXML private TextField    tfPrioridad;
    @FXML private RadioButton rbAsignadoSi;
    @FXML private RadioButton  rbAsignadoNo;
    @FXML private ComboBox<Persona> cbTecnicoAsignado;
    private ToggleGroup tgAsignado;
    private UUID currentUserId;


    @FXML
    public void initialize() {
        // Grupo de RadioButtons
        tgAsignado = new ToggleGroup();
        rbAsignadoSi.setToggleGroup(tgAsignado);
        rbAsignadoNo.setToggleGroup(tgAsignado);
        rbAsignadoNo.setSelected(true);

        cargarTecnicos();
        cbTecnicoAsignado.setConverter(new StringConverter<Persona>() {
            @Override
            public String toString(Persona persona) {
                return persona != null ? persona.getNombre() : "";
            }

            @Override
            public Persona fromString(String string) {
                return null;
            }
        });
    }

    public void initData(UUID usuarioId) {
        this.currentUserId = usuarioId;
    }

    private void cargarTecnicos() {
        String sql = "SELECT id,nombre,email FROM persona WHERE rol='Tecnico'";
        try (Connection conn = ConexionBaseDatos.BaseDatos();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery() ) {

            while (rs.next()) {
                Persona t = new Persona();
                t.setId(rs.getString("id"));
                t.setNombre(rs.getString("nombre"));
                cbTecnicoAsignado.getItems().add(t);
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,
                    "No se pudieron cargar técnicos:\n"+ e.getMessage())
                    .showAndWait();
        }
    }


    @FXML
    public void onGrabarTicket() {
        String titulo      = tfTitulo.getText().trim();
        String departamento= tfDepartamento.getText().trim();
        String prioridad   = tfPrioridad.getText().trim();
        boolean asignado   = rbAsignadoSi.isSelected();
        Persona tecnico    = cbTecnicoAsignado.getValue();

        // Validaciones
        if (titulo.length()<3 || titulo.length()>100) {
            showWarning("El título debe tener entre 3 y 100 caracteres."); return;
        }
        if (departamento.isEmpty()) {
            showWarning("El departamento no puede quedar vacío."); return;
        }
        if (prioridad.isEmpty()) {
            showWarning("La prioridad no puede quedar vacía."); return;
        }
        if (asignado && tecnico==null) {
            showWarning("Si marcas 'Asignado: Sí', debes seleccionar un técnico."); return;
        }

        String sql = """
            INSERT INTO ticket
              (titulo, descripcion, fecha_creacion, estado,
               persona_id, departamento, prioridad, asignado)
            VALUES (?,?,?,?,?,?,?,?)
        """;

        try ( Connection conn = ConexionBaseDatos.BaseDatos();
              PreparedStatement ps = conn.prepareStatement(sql) ) {

            ps.setString(1, titulo);
            ps.setString(2, /* puedes reutilizar descripción o poner vacío */ "");
            ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(4, "Pendiente");

            // persona_id = técnico si está asignado, sino currentUser
            String quien = asignado
                    ? tecnico.getId()
                    : currentUserId.toString();
            ps.setObject(5, UUID.fromString(quien));

            ps.setString(6, departamento);
            ps.setString(7, prioridad);
            ps.setBoolean(8, asignado);

            ps.executeUpdate();
            new Alert(Alert.AlertType.INFORMATION, "Ticket guardado correctamente.").showAndWait();
            limpiarFormulario();

        } catch (SQLException ex) {
            new Alert(Alert.AlertType.ERROR, "Error al guardar el ticket:\n"
                    + ex.getMessage()).showAndWait();
        }
    }

    @FXML
    public void onDeshacer() {
        limpiarFormulario();
    }

    private void limpiarFormulario() {
        tfTitulo.clear();
        tfDepartamento.clear();
        tfPrioridad.clear();
        rbAsignadoNo.setSelected(true);
        cbTecnicoAsignado.getSelectionModel().clearSelection();
    }

    private void showWarning(String msg) {
        new Alert(Alert.AlertType.WARNING, msg).showAndWait();
    }

    public void ticCerrarSesion(ActionEvent actionEvent) {
    }

    public void ticGuardar(ActionEvent actionEvent) {
    }

    public void ticDeshacer(ActionEvent actionEvent) {
    }
}
