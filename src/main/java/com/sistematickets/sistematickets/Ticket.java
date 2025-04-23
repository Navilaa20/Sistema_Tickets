package com.sistematickets.sistematickets;

import java.util.ArrayList;
import java.util.List;

public class Ticket {

    private static List<Ticket> tickets = new ArrayList<>();

    private int id;
    private String description;
    private String estado;
    private String prioridad;
    private String descripcion;

    public Ticket() {

    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) throws Exception {
        if (descripcion == null || descripcion.isEmpty()) {
            throw new Exception("La descripción no puede estar vacía");
        }
        this.descripcion = ("descripcion");
    }

    public Ticket(int id, String description, String estado, String prioridad) {
        this.id = id;
        this.description = description;
        this.estado = estado;
        this.prioridad = prioridad;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public void historial (){
    }

    public static void crear(Ticket t) {
        tickets.add(t);
    }

    public static Ticket leer(int id) {
        for (Ticket t : tickets) {
            if (t.getId() == id) return t;
        }
        return null;
    }

    public static void actualizar(Ticket actualizado) {
        for (int i = 0; i < tickets.size(); i++) {
            if (tickets.get(i).getId() == actualizado.getId()) {
                tickets.set(i, actualizado);
                break;
            }
        }
    }

    public static void eliminar(int id) {
        tickets.removeIf(t -> t.getId() == id);
    }

    public static List<Ticket> listar() {
        return tickets;
    }

}
