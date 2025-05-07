package com.sistematickets.sistematickets;

import java.util.ArrayList;
import java.util.List;

public class Administrador extends Persona implements Stage {

    private transient javafx.stage.Stage stage;
    private List<Permiso> permisos;

    public Administrador() {
        super();
        this.permisos = new ArrayList<>();
    }

    public List <Permiso> getPermisos(){
        return permisos;
    }

    public void setPermisos(List<Permiso> permisos) {
        this.permisos = permisos;
    }

    @Override
    public void setStage(Stage stage) {
    }

    @Override
    public void setStage(javafx.stage.Stage stage) {
        this.stage = stage;
    }

    public void gestionParametros() {
        System.out.println("Configurando parámetros generales del sistema...");
        //Simulando carga de parámetros
        System.out.println("Nombre: Patito S.A.");
        System.out.println("Zona horaria: GTM-6");
    }

    public void gestionEmpresa(){
        System.out.println("Gestión de información de la empresa");
    }


    public void configuracionInicial (){
        System.out.println("Ejecutando configuración inicial...");
        gestionEmpresa();
        gestionParametros();
        gestionRoles();
        gestionPermisos();
        gestionDepartamentos();
        gestionEstadosTicket();
        gestionUsuarios();
        System.out.println("Configuración inicial completada.");
    }

    public void gestionRoles (){
        System.out.println("Gestión de roles del sistema...");
        Rol rol = new Rol();
        rol.setNombre("Administrador");
        rol.setDescripcion("Acceso completo al sistema");
        System.out.println("Rol creado: " + rol.getNombre());
    }

    public void gestionPermisos(){
        System.out.println("Gestión de permisos...");
        if (permisos == null || permisos.isEmpty()) {
            System.out.println("No hay permisos asignados.");
        } else {
            for (Permiso p : permisos) {
                System.out.println(p);
            }
        }
    }

    public void gestionDepartamentos (){
        System.out.println("Gestión de departamentos ...");
        System.out.println("Ventas");
    }

    public void gestionEstadosTicket(){
        System.out.println("Abierto, En Proceso, Cerrado.");
    }

    public void gestionTicket (){
        System.out.println("Gestión de tickets...");
        List<Ticket> lista = Ticket.listar();
        for (Ticket t : lista) {
            System.out.println("Ticket #" + t.getId() + " - Estado: " + t.getEstado());
        }
    }

    public void gestionFlujos (){

    }

    public void gestionUsuarios (){
        System.out.println("Gestión de usuarios...");
        List<Persona> personas = Persona.listar();
        for (Persona p : personas) {
            System.out.println("Usuario: " + p.getNombre() + " - Email: " + p.getEmail());
        }
    }


}
