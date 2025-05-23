package com.sistematickets.sistematickets;

import java.util.List;

public class Departamento {

    private int id;
    private String nombre;
    private String descripcion;
    private List<Tecnico> tecnicos;

    public Departamento(String nombre, String descripcion, List<Tecnico> tecnicos) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tecnicos = tecnicos;
    }

    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public List<Tecnico> getTecnicos() { return tecnicos; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setTecnicos(List<Tecnico> tecnicos) { this.tecnicos = tecnicos; }

}
