package com.sistematickets.sistematickets;

import javafx.event.ActionEvent;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Ticket {

    private static List<Ticket> tickets = new ArrayList<>();

    private int id;
    private String titulo;
    private String descripcion;
    private LocalDateTime fechaCreacion;
    private String estado;
    private String departamento;
    private String prioridad;
    private boolean asignado;
    private String personaId;

    public Ticket() {
        this.fechaCreacion = LocalDateTime.now();
    }

    public Ticket(int id, String titulo, String descripcion, String estado, String prioridad, String personaId) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaCreacion = LocalDateTime.now();
        this.estado = estado;
        this.departamento = departamento;
        this.prioridad = prioridad;
        this.asignado = false;
        this.personaId = personaId;
    }

    public static List<Ticket> listar() {
        return List.of();
    }

    // Getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) throws Exception {
        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new Exception("La descripción no puede estar vacía");
        }
        this.descripcion = descripcion;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    public String getPersonaId() {
        return personaId;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public boolean isAsignado() {
        return asignado;
    }

    public void setAsignado(boolean asignado) {
        this.asignado = asignado;
    }

    public void setPersonaId(String personaId) {
        this.personaId = personaId;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", fechaCreacion=" + fechaCreacion +
                ", estado='" + estado + '\'' +
                ", departamento='" + departamento + '\'' +
                ", prioridad='" + prioridad + '\'' +
                ", asignado=" + asignado +
                ", personaId='" + personaId + '\'' +
                '}';
    }

}
