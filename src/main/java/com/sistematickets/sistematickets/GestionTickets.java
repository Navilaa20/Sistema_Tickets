package com.sistematickets.sistematickets;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

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

}
