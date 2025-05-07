package com.sistematickets.sistematickets;

import java.util.List;

public class Rol {

    private int id;
    private String nombre;
    private String descripcion;
    private List<Permiso> permisos;

    public void Rol (int id, String nombre, String descripcion, String permisos) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

}
