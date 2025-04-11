package com.sistematickets.sistematickets;

import java.util.List;

public class Administrador extends Persona implements Stage {

    private Stage stage;
    public Administrador() {
        super();
        this.stage = stage;
    }

    private List <Permiso> permisos;

    public List <Permiso> getPermisos() {
        return permisos;
    }

    public void setPermisos(List <Permiso> permisos) {
        this.permisos = permisos;
    }


    public void gestionParametros(){

    }

    public void gestionEmpresa (){

    }

    public void configuracionInicial (){

    }

    public void gestionRoles (){

    }

    public void gestionPermisos(){

    }

    public void gestionDepartamentos (){

    }

    public void gestionEstadosTicket(){

    }

    public void gestionTicket (){

    }

    public void gestionFlujos (){

    }

    public void gestionUsuarios (){

    }

    @Override
    public void setStage(Stage stage) {

    }
}
