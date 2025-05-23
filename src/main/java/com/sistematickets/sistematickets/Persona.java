package com.sistematickets.sistematickets;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Persona {

    private static List<Persona> personas = new ArrayList<>();

    private UUID id;
    private String nombre;
    private String email;
    private String contrasenia;
    private String role;

    public Persona(UUID id, String nombre, String email, String contrasenia, String role) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.contrasenia = contrasenia;
        this.role = role;
    }

    public Persona (UUID id, String nombre, String email) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
    }

    public Persona() {
    }

    public UUID getId() {  // Cambiado a String
        return id;
    }

    public UUID setId(UUID id) {
        this.id = id;
        return id;
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
        if (email != null && email.contains("@")) {
            this.email = email;
        } else {
            throw new IllegalArgumentException("El email no es válido");
        }
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public static void crear(Persona p) {
        personas.add(p);
    }

    public static Persona leer(String id) {
        for (Persona p : personas) {
            if (Objects.equals(p.getId(), id)) {
                return p;
            }
        }
        return null;
    }

    public static void actualizar(Persona actualizada) {
        for (int i = 0; i < personas.size(); i++) {
            if (Objects.equals(personas.get(i).getId(), actualizada.getId())) {
                personas.set(i, actualizada);
                break;
            }
        }
    }

    public static void eliminar(String id) {
        personas.removeIf(p -> Objects.equals(p.getId(), id));
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
