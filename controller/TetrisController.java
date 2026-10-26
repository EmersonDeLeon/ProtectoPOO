package controller;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import modelo.FilaBinaria;
import modelo.JuegoTetrisBinario;
import modelo.ResultadoTetris;
import repositorio.RepositorioPuntuaciones;
import view.PanelJuego;
import view.VentanaPrincipal;

public class TetrisController {

    private static final int INTERVALO_ANIMACION_MS = 30;
    private static final int DURACION_CAIDA_MS = 3500;
    private static final int PAUSA_TRANSICION_MS = 700;

    private final JuegoTetrisBinario juego;
    private final RepositorioPuntuaciones repositorioPuntuaciones;
    private final VentanaPrincipal ventana;
    private final PanelJuego panel;
    private final Timer temporizadorCaida;
    private final Timer temporizadorTransicion;

    private int columna;
    private double progreso;
    private boolean partidaActiva;
    private boolean resultadoGuardado;

    public TetrisController(
            JuegoTetrisBinario juego,
            VentanaPrincipal ventana,
            RepositorioPuntuaciones repositorioPuntuaciones
    ) {
        if (juego == null || ventana == null || repositorioPuntuaciones == null) {
            throw new IllegalArgumentException(
                    "El juego, la ventana y el repositorio son obligatorios."
            );
        }
        this.juego = juego;
        this.ventana = ventana;
        this.repositorioPuntuaciones = repositorioPuntuaciones;
        this.panel = ventana.getPanelJuego();

        temporizadorCaida = new Timer(
                INTERVALO_ANIMACION_MS, e -> avanzarCaida()
        );
        temporizadorTransicion = new Timer(
                PAUSA_TRANSICION_MS, e -> completarTransicion()
        );
        temporizadorTransicion.setRepeats(false);

        instalarControlesTeclado();
        instalarBotonesPista();
        ventana.getPanelMenu().getBotonJugar().addActionListener(
                e -> iniciarPartida()
        );
        panel.getBotonVolver().addActionListener(e -> detenerPartida());
        ventana.getPanelResultado().getBotonGuardar()
                .addActionListener(e -> guardarResultado());
    }

    public void iniciarPartida() {
        detenerTemporizadores();

        temporizadorTransicion.setInitialDelay(PAUSA_TRANSICION_MS);
        temporizadorTransicion.setDelay(PAUSA_TRANSICION_MS);

        juego.iniciar();
        partidaActiva = true;
        resultadoGuardado = false;
        
        columna = 0;
        progreso = 0;

        FilaBinaria fila = juego.getTablero().getFilaActiva();
        panel.reiniciarFilas(fila.getObjetivo());
        actualizarFilaActiva();
        panel.mostrarPuntuacion(0);
        for (int i = 0; i < JuegoTetrisBinario.CANTIDAD_PISTAS; i++) {
            panel.mostrarPistaDisponible(i, true);
        }
        mostrarBloque();
        panel.mostrarEstado("← → mover · Espacio soltar");
        temporizadorCaida.start();
    }

    public void detenerPartida() {
        partidaActiva = false;
        detenerTemporizadores();
        panel.ocultarBloque();
    }

    private void instalarControlesTeclado() {
        JComponent componente = panel;
        componente.setFocusable(true);
        componente.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "moverIzquierda"
        );
        componente.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "moverDerecha"
        );
        componente.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "soltarBloque"
        );
        componente.getActionMap().put("moverIzquierda", accion(() -> mover(-1)));
        componente.getActionMap().put("moverDerecha", accion(() -> mover(1)));
        componente.getActionMap().put("soltarBloque", accion(this::soltarBloque));
        for (int i = 0; i < JuegoTetrisBinario.CANTIDAD_PISTAS; i++) {
            final int pista = i;
            componente.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                    KeyStroke.getKeyStroke(KeyEvent.VK_1 + i, 0),
                    "usarPista" + i
            );
            componente.getActionMap().put(
                    "usarPista" + i, accion(() -> usarPista(pista))
            );
        }
    }

    private AbstractAction accion(Runnable accion) {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                accion.run();
            }
        };
    }

    private void instalarBotonesPista() {
        for (int i = 0; i < JuegoTetrisBinario.CANTIDAD_PISTAS; i++) {
            final int pista = i;
            panel.getBotonPista(i).addActionListener(e -> usarPista(pista));
        }
    }

    private void mover(int desplazamiento) {
        if (!partidaActiva || juego.getEstado() != JuegoTetrisBinario.Estado.EN_CURSO) {
            return;
        }
        columna = Math.max(0, Math.min(FilaBinaria.CANTIDAD_BITS - 1,
                columna + desplazamiento));
        mostrarBloque();
    }

    private void avanzarCaida() {
        if (!partidaActiva || juego.getEstado() != JuegoTetrisBinario.Estado.EN_CURSO) {
            temporizadorCaida.stop();
            return;
        }
        progreso = Math.min(1, progreso
                + (double) INTERVALO_ANIMACION_MS / DURACION_CAIDA_MS);
        mostrarBloque();
        if (progreso >= 1) {
            registrarAterrizaje();
        }
    }

    private void soltarBloque() {
        if (partidaActiva && juego.getEstado() == JuegoTetrisBinario.Estado.EN_CURSO) {
            registrarAterrizaje();
        }
    }

    private void registrarAterrizaje() {
        temporizadorCaida.stop();
        panel.ocultarBloque();
        FilaBinaria fila = juego.getTablero().getFilaActiva();

        if (fila.getBits()[columna] == 1) {
            panel.mostrarEstado("Esa columna ya tiene un bloque; mueve el bloque.");
            prepararSiguienteBloque();
            return;
        }

        JuegoTetrisBinario.ResultadoCaida resultado = juego.aterrizarBloque(columna);
        actualizarFilaActiva();
        panel.mostrarPuntuacion(juego.getPuntuacion());

        switch (resultado) {
            case BLOQUE_CORRECTO:
                panel.mostrarEstado("¡Correcto! Sigue completando el número.");
                programarSiguienteBloque();
                break;
            case FILA_INFECTADA:
                panel.mostrarInfeccion(true);
                panel.mostrarEstado("Fila infectada. Aparecerá una nueva arriba.");
                temporizadorTransicion.start();
                break;
            case FILA_COMPLETADA:
                panel.mostrarEstado("¡Fila completada!");
                temporizadorTransicion.start();
                break;
            case PARTIDA_TERMINADA:
                partidaActiva = false;
                detenerTemporizadores();
                panel.ocultarBloque();
                resultadoGuardado = false;
                ventana.mostrarResultado(juego.getPuntuacion());
                break;
            default:
                throw new IllegalStateException("Resultado de caída desconocido.");
        }
    }

    private void completarTransicion() {
        if (!partidaActiva) {
            return;
        }
        temporizadorTransicion.setInitialDelay(PAUSA_TRANSICION_MS);
        temporizadorTransicion.setDelay(PAUSA_TRANSICION_MS);
        if (juego.getEstado() == JuegoTetrisBinario.Estado.ESPERANDO_NUEVA_FILA) {
            modelo.ConsignaBinaria consigna = juego.agregarNuevaFila();
            panel.agregarFila(consigna.getNumeroDecimal());
            actualizarFilaActiva();
            panel.mostrarEstado("Nueva fila: completa el objetivo decimal.");
            prepararSiguienteBloque();
        } else if (juego.getEstado()
                == JuegoTetrisBinario.Estado.ESPERANDO_RESOLUCION) {
            boolean terminado = juego.resolverFila();
            if (terminado) {
                partidaActiva = false;
                panel.ocultarBloque();
                resultadoGuardado = false;
                ventana.mostrarResultado(juego.getPuntuacion());
            } else {
                panel.retirarFilaActiva();
                actualizarFilaActiva();
                panel.mostrarInfeccion(false);
                panel.mostrarEstado("Fila recuperada. Continúa la partida.");
                prepararSiguienteBloque();
            }
        } else if (juego.getEstado() == JuegoTetrisBinario.Estado.EN_CURSO) {
            prepararSiguienteBloque();
        }
    }

    private void prepararSiguienteBloque() {
        if (!partidaActiva || juego.getEstado() != JuegoTetrisBinario.Estado.EN_CURSO) {
            return;
        }
        columna = 0;
        progreso = 0;
        mostrarBloque();
        temporizadorCaida.start();
    }

    private void programarSiguienteBloque() {
        temporizadorTransicion.setInitialDelay(350);
        temporizadorTransicion.start();
    }

    private void usarPista(int indice) {
        if (!partidaActiva || !juego.usarPista(indice)) {
            return;
        }
        panel.mostrarPistaDisponible(indice, false);
        panel.mostrarPista(true);
        panel.mostrarEstado("Pista " + (indice + 1)
                + ": pesos de izquierda a derecha, de 128 a 1.");
    }

    private void guardarResultado() {
        if (resultadoGuardado) {
            return;
        }
        String nombre = ventana.getPanelResultado().getNombreIngresado();
        if (nombre == null || nombre.trim().isEmpty()) {
            ventana.getPanelResultado().mostrarError(
                    "Escribe tu nombre antes de guardar."
            );
            return;
        }

        try {
            repositorioPuntuaciones.registrarResultado(
                    new ResultadoTetris(nombre, juego.getPuntuacion())
            );
            resultadoGuardado = true;
            ventana.getPanelResultado().setEdicionHabilitada(false);
            ventana.getPanelResultado().mostrarConfirmacion(
                    "Puntuación guardada en calificaciones.csv."
            );
        } catch (java.io.IOException | IllegalStateException e) {
            ventana.getPanelResultado().mostrarError(
                    "No se pudo guardar la puntuación: " + e.getMessage()
            );
        }
    }

    private void actualizarFilaActiva() {
        FilaBinaria fila = juego.getTablero().getFilaActiva();
        panel.mostrarObjetivo(fila.getObjetivo());
        panel.mostrarBits(fila.getBits());
        panel.mostrarInfeccion(fila.estaInfectada());
    }

    private void mostrarBloque() {
        panel.mostrarBloque(columna, progreso);
    }

    private void detenerTemporizadores() {
        temporizadorCaida.stop();
        temporizadorTransicion.stop();
    }
}
