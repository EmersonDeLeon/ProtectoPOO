package modelo;

import java.time.LocalDateTime;

public class Usuario {

    private final int id;
    private String nombre;
    private final LocalDateTime fechaRegistro;

    public Usuario(int id, String nombre) {
        this(id, nombre, LocalDateTime.now());
    }

    public Usuario(int id, String nombre, LocalDateTime fechaRegistro) {
        if (id <= 0) {
            throw new IllegalArgumentException(
                    "El ID del usuario debe ser mayor que cero."
            );
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El nombre del usuario no puede estar vacío."
            );
        }
        if (fechaRegistro == null) {
            throw new IllegalArgumentException(
                    "La fecha de registro no puede ser nula."
            );
        }

        this.id = id;
        this.nombre = nombre.trim();
        this.fechaRegistro = fechaRegistro;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El nombre del usuario no puede estar vacío."
            );
        }
        this.nombre = nombre.trim();
    }

    @Override
    public String toString() {
        return nombre + " (ID: " + id + ")";
    }
}
