package com.sistematickets.sistematickets;

import java.util.ArrayList;
import java.util.List;

public class Persona {

    private static List<Persona> personas = new ArrayList<>();

    private String id;
    private String nombre;
    private String email;
    private String contrasenia;

    public Persona(String id, String nombre, String email) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
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
}
