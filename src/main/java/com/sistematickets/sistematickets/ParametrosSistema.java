package com.sistematickets.sistematickets;

import conexionBD.ConexionBaseDatos;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ParametrosSistema {
    private String nombreEmpresa;
    private String logoPath;
    private String idioma;
    private String zonaHoraria;
    private int diasVencimiento;
    private List<String> nivelesPrioridad;

    // Getters y Setters
    public String getNombreEmpresa() { return nombreEmpresa; }
    public void setNombreEmpresa(String nombreEmpresa) { this.nombreEmpresa = nombreEmpresa; }

    public String getLogoPath() { return logoPath; }
    public void setLogoPath(String logoPath) { this.logoPath = logoPath; }

    public String getIdioma() { return idioma; }
    public void setIdioma(String idioma) { this.idioma = idioma; }

    public String getZonaHoraria() { return zonaHoraria; }
    public void setZonaHoraria(String zonaHoraria) { this.zonaHoraria = zonaHoraria; }

    public int getDiasVencimiento() { return diasVencimiento; }
    public void setDiasVencimiento(int diasVencimiento) { this.diasVencimiento = diasVencimiento; }

    public List<String> getNivelesPrioridad() { return nivelesPrioridad; }
    public void setNivelesPrioridad(List<String> nivelesPrioridad) { this.nivelesPrioridad = nivelesPrioridad; }

    // Método para cargar los parámetros desde la base de datos
    public static ParametrosSistema cargarParametros() {
        ParametrosSistema parametros = new ParametrosSistema();
        try (Connection conn = ConexionBaseDatos.BaseDatos();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM parametros_sistema LIMIT 1;")) {

            if (rs.next()) {
                String prueba = rs.getString("nombre_empresa");
                parametros.setNombreEmpresa(rs.getString("nombre_empresa"));
                parametros.setLogoPath(rs.getString("logo_path"));
                parametros.setIdioma(rs.getString("idioma"));
                parametros.setZonaHoraria(rs.getString("zona_horaria"));
                parametros.setDiasVencimiento(rs.getInt("tiempo_vencimiento"));

            }

            Statement stmt2 = conn.createStatement();
            ResultSet rs2 = stmt2.executeQuery("SELECT * FROM nivel_prioridad;");

            while (rs2.next()) {
                String niveles = rs.getString(1);
                String[] nivelesArray = niveles.split(",");
                List<String> listaNiveles = new ArrayList<>();
                for (String nivel : nivelesArray) {
                    listaNiveles.add(nivel.trim());
                }
                parametros.setNivelesPrioridad(listaNiveles);
            }

        } catch (SQLException e) {
            ConexionBaseDatos.showAlert("Error", "Error al cargar los parámetros: " + e.getMessage());
        }
        return parametros;
    }

    public ParametrosSistema() {

    }

    // Método para guardar los parámetros en la base de datos
    public void guardarParametros() {
        try (Connection conn = ConexionBaseDatos.BaseDatos();
             PreparedStatement pstmt = conn.prepareStatement(
                     "UPDATE parametros_sistema SET nombre_empresa = ?, logo_path = ?, idioma = ?, zona_horaria = ?, tiempo_vencimiento = ?")) {

            pstmt.setString(1, this.nombreEmpresa);
            pstmt.setString(2, this.logoPath);
            pstmt.setString(3, this.idioma);
            pstmt.setString(4, this.zonaHoraria);
            pstmt.setInt(5, this.diasVencimiento);
            //pstmt.setString(6, String.join(",", this.nivelesPrioridad));

            pstmt.executeUpdate();

            PreparedStatement pstmt2 = conn.prepareStatement(
                    "UPDATE nivel_prioridad SET id = ?,nombre = ?,orden=?");

            PreparedStatement pstmt3 = conn.prepareStatement(
                    "UPDATE nivel_prioridad SET id = ?,nombre = ?,orden=?");

            PreparedStatement pstmt4 = conn.prepareStatement(
                    "UPDATE nivel_prioridad SET id = ?,nombre = ?,orden=?");

            pstmt2.setString(2, this.nivelesPrioridad.get(0));
            pstmt3.setString(2, this.nivelesPrioridad.get(1));
            pstmt4.setString(2, this.nivelesPrioridad.get(2));


            pstmt2.setInt(3, Integer.parseInt("1"));
            pstmt3.setInt(3, Integer.parseInt("2"));
            pstmt4.setInt(3, Integer.parseInt("3"));

            pstmt2.setInt(1, Integer.parseInt(this.nivelesPrioridad.get(0)));
            pstmt3.setInt(1, Integer.parseInt(this.nivelesPrioridad.get(1)));
            pstmt4.setInt(1, Integer.parseInt(this.nivelesPrioridad.get(2)));

            pstmt2.executeUpdate();
            pstmt3.executeUpdate();
            pstmt4.executeUpdate();

        } catch (SQLException e) {
            ConexionBaseDatos.showAlert("Error", "Error al guardar los parámetros: " + e.getMessage());
            System.err.println(e.getMessage());
        }
    }
}
