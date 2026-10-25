package modelo;

import java.io.Serializable;
import java.time.LocalDateTime;

public final class ResultadoTetris implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String nombreJugador;
    private final int puntuacion;
    private final LocalDateTime fecha;

    public ResultadoTetris(String nombreJugador, int puntuacion) {
        this(nombreJugador, puntuacion, LocalDateTime.now());
    }

    public ResultadoTetris(
            String nombreJugador,
            int puntuacion,
            LocalDateTime fecha
    ) {
        if (nombreJugador == null || nombreJugador.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El nombre del jugador no puede estar vacío."
            );
        }
        if (puntuacion < 0) {
            throw new IllegalArgumentException(
                    "La puntuación no puede ser negativa."
            );
        }
        if (fecha == null) {
            throw new IllegalArgumentException(
                    "La fecha del resultado no puede ser nula."
            );
        }

        this.nombreJugador = nombreJugador.trim();
        this.puntuacion = puntuacion;
        this.fecha = fecha;
    }

    public String getNombreJugador() {
        return nombreJugador;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    @Override
    public String toString() {
        return nombreJugador + " — " + puntuacion + " puntos (" + fecha + ")";
    }
}
