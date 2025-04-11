package com.sistematickets.sistematickets;

public class Usuario extends Persona {


    public Ticket crearTicket(String descripcion) throws  Exception{
        Ticket ticket = new Ticket();
        Ticket.setDescripcion (descripcion);
        return ticket;
    }

    public void mostrarUsuario(Persona persona){
        System.out.println("Nombre: " + persona.getNombre());
    }


}
