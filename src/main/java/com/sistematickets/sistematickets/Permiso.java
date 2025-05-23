package com.sistematickets.sistematickets;

import conexionBD.ConexionBaseDatos;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Permiso {

    // --- MODELO: Datos del permiso ---
    private int id;
    private String nombre;
    private String descripcion;

    public Permiso(int id, String nombre, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    @Override
    public String toString() {
        return nombre + ": " + descripcion;
    }

    // --- CONTROLADOR: Lógica de la vista FXML ---
    @FXML private TextField txtIdRol;
    @FXML private TextField txtNombreRol;
    @FXML private TextField txtUsuario;

    @FXML private RadioButton rbCrearTicket, rbEditarTicket, rbCrearUsuario,
            rbCambiarEstado, rbVerTicket, rbEliminarTicket,
            rbAsignarPermisos, rbCrearRoles, rbDefinirParametros, rbCrearDepartamentos;

    @FXML private Button btnGrabarRol;

    // Mapa para asociar cada RadioButton a un objeto Permiso
    private final Map<RadioButton, Permiso> mapaPermisos = new HashMap<>();

    @FXML
    public void initialize() {
        // Asociar cada RadioButton con un objeto Permiso
        mapaPermisos.put(rbCrearTicket, new Permiso(1, "Crear Ticket", "Permite crear tickets"));
        mapaPermisos.put(rbEditarTicket, new Permiso(2, "Editar Ticket", "Permite editar tickets"));
        mapaPermisos.put(rbCrearUsuario, new Permiso(3, "Crear Usuario", "Permite crear usuarios"));
        mapaPermisos.put(rbCambiarEstado, new Permiso(4, "Cambiar Estado", "Permite cambiar el estado del ticket"));
        mapaPermisos.put(rbVerTicket, new Permiso(5, "Ver Ticket", "Permite ver tickets"));
        mapaPermisos.put(rbEliminarTicket, new Permiso(6, "Eliminar Ticket", "Permite eliminar tickets"));
        mapaPermisos.put(rbAsignarPermisos, new Permiso(7, "Asignar Permisos", "Permite asignar permisos a roles"));
        mapaPermisos.put(rbCrearRoles, new Permiso(8, "Crear Roles", "Permite crear roles de usuario"));
        mapaPermisos.put(rbDefinirParametros, new Permiso(9, "Definir Parámetros", "Permite definir parámetros del sistema"));
        mapaPermisos.put(rbCrearDepartamentos, new Permiso(10, "Crear Departamentos", "Permite crear departamentos"));
    }

    @FXML
    public void grabarRol() {
        String nombreRol = txtNombreRol.getText().trim();

        if (nombreRol.length() < 3 || nombreRol.length() > 50) {
            System.err.println("El nombre del rol debe tener entre 3 y 50 caracteres.");
            return;
        }

        try (Connection conn = ConexionBaseDatos.BaseDatos()) {
            conn.setAutoCommit(false);

            // Insertar el rol
            String insertRolSQL = "INSERT INTO rol(nombre) VALUES(?) RETURNING id";
            PreparedStatement psRol = conn.prepareStatement(insertRolSQL);
            psRol.setString(1, nombreRol);
            ResultSet rs = psRol.executeQuery();
            int rolId = -1;
            if (rs.next()) {
                rolId = rs.getInt("id");
            } else {
                throw new SQLException("No se pudo obtener el ID del nuevo rol.");
            }

            // Insertar permisos asociados al rol
            insertPermisoSiMarcado(rbCrearTicket, "Crear Ticket", rolId, conn);
            insertPermisoSiMarcado(rbEditarTicket, "Editar Ticket", rolId, conn);
            insertPermisoSiMarcado(rbCrearUsuario, "Crear Usuario", rolId, conn);
            insertPermisoSiMarcado(rbCambiarEstado, "Cambiar Estado de Ticket", rolId, conn);
            insertPermisoSiMarcado(rbVerTicket, "Ver Ticket", rolId, conn);
            insertPermisoSiMarcado(rbEliminarTicket, "Eliminar Ticket", rolId, conn);
            insertPermisoSiMarcado(rbAsignarPermisos, "Asignar Permisos", rolId, conn);
            insertPermisoSiMarcado(rbCrearRoles, "Crear Roles", rolId, conn);
            insertPermisoSiMarcado(rbDefinirParametros, "Definir Parámetros", rolId, conn);
            insertPermisoSiMarcado(rbCrearDepartamentos, "Crear Departamentos", rolId, conn);

            conn.commit();

            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Éxito");
            alerta.setContentText("Rol y permisos guardados correctamente.");
            alerta.showAndWait();

        } catch (SQLException e) {
            System.err.println("Error al guardar el rol: " + e.getMessage());
        }
    }

    // Método auxiliar para insertar permiso si está marcado
    private void insertPermisoSiMarcado(RadioButton rb, String nombrePermiso, int rolId, Connection conn) throws SQLException {
        if (rb.isSelected()) {
            String insertPermisoSQL = "INSERT INTO rol_permiso(rol_id, permiso_nombre) VALUES(?, ?)";
            PreparedStatement psPermiso = conn.prepareStatement(insertPermisoSQL);
            psPermiso.setInt(1, rolId);
            psPermiso.setString(2, nombrePermiso);
            psPermiso.executeUpdate();
        }
    }

}

