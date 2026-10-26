package modelo;

public final class Credito {

    private final String nombre;
    private final String rol;
    private final String descripcion;

    public Credito(String nombre, String rol, String descripcion) {
        this.nombre = validarTexto(nombre, "El nombre");
        this.rol = validarTexto(rol, "El rol");
        this.descripcion = validarTexto(descripcion, "La descripción");
    }

    public String getNombre() {
        return nombre;
    }

    public String getRol() {
        return rol;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return nombre + " — " + rol + ": " + descripcion;
    }

    private static String validarTexto(String valor, String campo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    campo + " no puede estar vacío."
            );
        }
        return valor.trim();
    }
}
