package com.sistematickets.sistematickets;

import javax.management.relation.Role;
import javax.print.DocFlavor;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Persona {

    private static List<Persona> personas = new ArrayList<>();

    private String id;
    private String nombre;
    private String email;
    private String contrasenia;
    private String role;


    public Persona(String id, String nombre, String email, String contrasenia, String role) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.contrasenia = contrasenia;
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Persona() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws IllegalArgumentException {
        if (email.contains("@")){
            this.email = email;
        } else {
            throw new IllegalArgumentException("El email no es valido");
        }
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public static void crear(Persona p) {
        personas.add(p);
    }

    public static Persona leer(String id) {
        for (Persona p : personas) {
            if (p.getId().equals(id)) return p;
        }
        return null;
    }

    public static void actualizar(Persona actualizada) {
        for (int i = 0; i < personas.size(); i++) {
            if (personas.get(i).getId().equals(actualizada.getId())) {
                personas.set(i, actualizada);
                break;
            }
        }
    }

    public static void eliminar(String id) {
        personas.removeIf(p -> p.getId().equals(id));
    }

    public static List<Persona> listar() {
        return personas;

    }

    @Override
    public String toString() {
        return "Persona{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", contrasenia='" + contrasenia + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
