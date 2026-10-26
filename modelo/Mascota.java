package modelo;

public final class Mascota {

    public static final String RECURSO_PREDETERMINADO = "/imagenes/images.jpg";

    private final String nombre;
    private final String recursoImagen;

    public Mascota() {
        this("Mascota", RECURSO_PREDETERMINADO);
    }

    public Mascota(String nombre, String recursoImagen) {
        this.nombre = validarTexto(nombre, "El nombre");
        this.recursoImagen = validarTexto(recursoImagen, "La ruta de imagen");
    }

    public String getNombre() {
        return nombre;
    }

    /** Ruta de recurso que puede recibir PanelImagen. */
    public String getRecursoImagen() {
        return recursoImagen;
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
