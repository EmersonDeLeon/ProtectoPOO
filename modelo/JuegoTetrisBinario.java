package modelo;

public class JuegoTetrisBinario {

    public enum Estado {
        SIN_INICIAR,
        EN_CURSO,
        ESPERANDO_NUEVA_FILA,
        ESPERANDO_RESOLUCION,
        TERMINADO
    }

    public enum ResultadoCaida {
        BLOQUE_CORRECTO,
        FILA_COMPLETADA,
        FILA_INFECTADA,
        PARTIDA_TERMINADA
    }

    public static final int CANTIDAD_PISTAS = 3;

    private final GeneradorConsignas generador;
    private final boolean[] pistasDisponibles = new boolean[CANTIDAD_PISTAS];

    private TableroBinario tablero;
    private Estado estado = Estado.SIN_INICIAR;
    private int puntuacion;

    public JuegoTetrisBinario() {
        this(new GeneradorConsignas());
    }

    public JuegoTetrisBinario(GeneradorConsignas generador) {
        if (generador == null) {
            throw new IllegalArgumentException(
                    "El generador de consignas no puede ser nulo."
            );
        }
        this.generador = generador;
        reiniciarPistas();
    }

    public void iniciar() {
        ConsignaBinaria primera = generador.generar();
        tablero = new TableroBinario(primera);
        puntuacion = 0;
        reiniciarPistas();
        estado = Estado.EN_CURSO;
    }

    public void reiniciar() {
        iniciar();
    }

    public ResultadoCaida aterrizarBloque(int columna) {
        exigirEstado(Estado.EN_CURSO);

        boolean correcta = tablero.colocarUno(columna);
        
        if (!correcta) {
            if (tablero.getCantidadFilas() >= 10) {
                estado = Estado.TERMINADO;
                return ResultadoCaida.PARTIDA_TERMINADA;
            }

            estado = Estado.ESPERANDO_NUEVA_FILA;
            return ResultadoCaida.FILA_INFECTADA;
        }

        puntuacion++;
        if (tablero.getFilaActiva().estaResuelta()) {
            estado = Estado.ESPERANDO_RESOLUCION;
            return ResultadoCaida.FILA_COMPLETADA;
        }

        return ResultadoCaida.BLOQUE_CORRECTO;
    }

    public ConsignaBinaria agregarNuevaFila() {
        exigirEstado(Estado.ESPERANDO_NUEVA_FILA);
        ConsignaBinaria nueva = generador.generar();
        tablero.agregarFila(nueva);
        estado = Estado.EN_CURSO;
        return nueva;
    }

    public boolean resolverFila() {
        exigirEstado(Estado.ESPERANDO_RESOLUCION);
        boolean partidaTerminada = tablero.resolverFilaActiva();
        if (partidaTerminada) {
            tablero.reiniciar(generador.generar());
        }
        estado = Estado.EN_CURSO;
        return false;
    }

    public boolean usarPista(int indice) {
        validarIndicePista(indice);
        if (estado != Estado.EN_CURSO) {
            return false;
        }
        if (!pistasDisponibles[indice]) {
            return false;
        }

        pistasDisponibles[indice] = false;
        return true;
    }

    public boolean estaDisponiblePista(int indice) {
        validarIndicePista(indice);
        return pistasDisponibles[indice];
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public Estado getEstado() {
        return estado;
    }

    public TableroBinario getTablero() {
        if (tablero == null) {
            throw new IllegalStateException(
                    "Primero debes iniciar la partida."
            );
        }
        return tablero;
    }

    private void reiniciarPistas() {
        for (int i = 0; i < pistasDisponibles.length; i++) {
            pistasDisponibles[i] = true;
        }
    }

    private void validarIndicePista(int indice) {
        if (indice < 0 || indice >= CANTIDAD_PISTAS) {
            throw new IllegalArgumentException(
                    "El índice de pista debe estar entre 0 y "
                    + (CANTIDAD_PISTAS - 1) + "."
            );
        }
    }

    private void exigirEstado(Estado requerido) {
        if (estado != requerido) {
            throw new IllegalStateException(
                    "Esta acción requiere el estado " + requerido
                    + ", pero la partida está en " + estado + "."
            );
        }
    }
}
