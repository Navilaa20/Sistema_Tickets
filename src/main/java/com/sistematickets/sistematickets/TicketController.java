package com.sistematickets.sistematickets;

import conexionBD.ConexionBaseDatos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.UUID;

public class TicketController {

    @FXML private TableView<Ticket> tableTickets;
    @FXML private TableColumn<Ticket, Integer> colId;
    @FXML private TableColumn<Ticket, String> colTitulo;
    @FXML private TableColumn<Ticket, String> colDepartamento;
    @FXML private TableColumn<Ticket, String> colPrioridad;
    @FXML private TableColumn<Ticket, String> colEstado;
    @FXML private TableColumn<Ticket, Boolean> colAsignado;

    @FXML private TextField tfTitulo;
    @FXML private TextField tfDepartamento;
    @FXML private TextField tfPrioridad;
    @FXML private RadioButton rbAsignadoSi;
    @FXML private RadioButton rbAsignadoNo;
    @FXML private ComboBox<Persona> cbTecnicoAsignado;

    private ToggleGroup tgAsignado;
    private UUID currentUserId;  // ID del usuario técnico que está usando la app

    @FXML
    public void initialize() {
        // Configurar columnas de la tabla
        colId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        colTitulo.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTitulo()));
        colDepartamento.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDepartamento()));
        colPrioridad.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getPrioridad()));
        colEstado.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEstado()));
        colAsignado.setCellValueFactory(cellData -> new javafx.beans.property.SimpleBooleanProperty(cellData.getValue().isAsignado()));

        // Grupo de radio buttons para asignado
        tgAsignado = new ToggleGroup();
        rbAsignadoSi.setToggleGroup(tgAsignado);
        rbAsignadoNo.setToggleGroup(tgAsignado);
        rbAsignadoNo.setSelected(true);

        // Cargar técnicos para asignar tickets
        cargarTecnicos();

        // Convertidor para mostrar nombre en comboBox
        cbTecnicoAsignado.setConverter(new javafx.util.StringConverter<Persona>() {
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
        cargarTicketsAsignados();
    }

    // Carga tickets asignados al técnico actual
    private void cargarTicketsAsignados() {
        ObservableList<Ticket> listaTickets = FXCollections.observableArrayList();

        String sql = "SELECT * FROM ticket WHERE persona_id = ?";

        try (Connection conn = ConexionBaseDatos.BaseDatos();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, currentUserId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Ticket t = new Ticket();
                t.setId(rs.getInt("id"));
                t.setTitulo(rs.getString("titulo"));
                t.setDescripcion(rs.getString("descripcion"));
                Timestamp ts = rs.getTimestamp("fecha_creacion");
                if (ts != null) t.setFechaCreacion(ts.toLocalDateTime());
                t.setEstado(rs.getString("estado"));
                t.setDepartamento(rs.getString("departamento"));
                t.setPrioridad(rs.getString("prioridad"));
                t.setAsignado(rs.getBoolean("asignado"));
                t.setPersonaId(rs.getString("persona_id"));
                listaTickets.add(t);
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Error cargando tickets: " + e.getMessage()).showAndWait();
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        tableTickets.setItems(listaTickets);
    }

    // Carga los técnicos desde la BD para asignar tickets
    private void cargarTecnicos() {
        String sql = "SELECT id, nombre FROM persona WHERE rol='Tecnico'";
        try (Connection conn = ConexionBaseDatos.BaseDatos();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Persona t = new Persona();
                t.setId(UUID.fromString(rs.getString("id")));
                t.setNombre(rs.getString("nombre"));
                cbTecnicoAsignado.getItems().add(t);
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "No se pudieron cargar técnicos:\n" + e.getMessage()).showAndWait();
        }
    }

    // Guardar o actualizar ticket
    @FXML
    public void onGrabarTicket() {
        String titulo      = tfTitulo.getText().trim();
        String departamento= tfDepartamento.getText().trim();
        String prioridad   = tfPrioridad.getText().trim();
        boolean asignado   = rbAsignadoSi.isSelected();
        Persona tecnico    = cbTecnicoAsignado.getValue();

        // Validaciones
        if (titulo.length() < 3 || titulo.length() > 100) {
            showWarning("El título debe tener entre 3 y 100 caracteres."); return;
        }
        if (departamento.isEmpty()) {
            showWarning("El departamento no puede quedar vacío."); return;
        }
        if (prioridad.isEmpty()) {
            showWarning("La prioridad no puede quedar vacía."); return;
        }
        if (asignado && tecnico == null) {
            showWarning("Si marcas 'Asignado: Sí', debes seleccionar un técnico."); return;
        }

        String sql = """
            INSERT INTO ticket
              (titulo, descripcion, fecha_creacion, estado,
               persona_id, departamento, prioridad, asignado)
            VALUES (?,?,?,?,?,?,?,?)
        """;

        try (Connection conn = ConexionBaseDatos.BaseDatos();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, titulo);
            ps.setString(2, ""); // descripción vacío o puedes agregar campo para que el usuario escriba
            ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(4, "Pendiente");

            // persona_id = técnico si está asignado, sino currentUser
            String quien = asignado ? tecnico.getId().toString() : currentUserId.toString();
            ps.setObject(5, UUID.fromString(quien));

            ps.setString(6, departamento);
            ps.setString(7, prioridad);
            ps.setBoolean(8, asignado);

            ps.executeUpdate();

            new Alert(Alert.AlertType.INFORMATION, "Ticket guardado correctamente.").showAndWait();
            limpiarFormulario();
            cargarTicketsAsignados(); // refrescar tabla

        } catch (SQLException ex) {
            new Alert(Alert.AlertType.ERROR, "Error al guardar el ticket:\n" + ex.getMessage()).showAndWait();
        }
    }

    // Marcar ticket seleccionado como "Resuelto"
    @FXML
    public void onMarcarResuelto() {
        Ticket seleccionado = tableTickets.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            showWarning("Selecciona un ticket para marcar como resuelto.");
            return;
        }

        String sql = "UPDATE ticket SET estado = ? WHERE id = ?";

        try (Connection conn = ConexionBaseDatos.BaseDatos();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "Resuelto");
            ps.setInt(2, seleccionado.getId());
            int rows = ps.executeUpdate();

            if (rows > 0) {
                new Alert(Alert.AlertType.INFORMATION, "Ticket marcado como resuelto.").showAndWait();
                cargarTicketsAsignados();
            } else {
                showWarning("No se pudo actualizar el ticket.");
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Error al actualizar ticket:\n" + e.getMessage()).showAndWait();
        }
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

}
