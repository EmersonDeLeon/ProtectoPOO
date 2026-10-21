package view.pruebas;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import view.Fuentes;
import view.PanelJuego;


public class PruebaCaida extends JFrame {

    private static final int COLUMNA_INICIAL = 5;
    private static final double DURACION_NANOS = 5_000_000_000.0;

    // Objetivos fijos para esta demostración.
    private static final int[] OBJETIVOS_PRUEBA = {
        5, 3, 9, 6, 10
    };

    private final PanelJuego juego = new PanelJuego();
    private final int[] bits = new int[8];
    private final boolean[] pistasUsadas = new boolean[3];

    private final Timer temporizador;
    private final Timer pausaEntreBloques;
    private final Timer pausaInfeccion;
    private final Timer pausaAcierto;

    private int indiceObjetivo;
    private int columnaActual;
    private double progreso;
    private long inicio;


    public PruebaCaida() {
        setTitle("Prueba de caída e infección");
        setSize(680, 820);
        setMinimumSize(new Dimension(620, 700));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JButton reiniciar = new JButton("Reiniciar prueba");

        JPanel controles = new JPanel();
        controles.add(
                controles.add(new JLabel("Izq/der: mover | Abajo: caer"))
        );
        controles.add(reiniciar);

        add(controles, BorderLayout.NORTH);
        add(juego, BorderLayout.CENTER);

        temporizador = new Timer(16, e -> avanzar());

        pausaEntreBloques = new Timer(
                600, e -> crearBloque()
        );
        pausaEntreBloques.setRepeats(false);

        // Permite observar la infección antes de agregar otra fila.
        pausaInfeccion = new Timer(
                900, e -> crearFilaTrasError()
        );
        pausaInfeccion.setRepeats(false);

        // Muestra brevemente la respuesta correcta antes de retirar la fila.
        pausaAcierto = new Timer(700, e -> resolverFila());
        pausaAcierto.setRepeats(false);

        reiniciar.addActionListener(e -> reiniciarPrueba());

        configurarTeclas();

        configurarPistas();

        juego.getBotonVolver().setText("Cerrar prueba");
        juego.getBotonVolver().addActionListener(e -> dispose());

        setLocationRelativeTo(null);
    }

    private void configurarTeclas() {
    registrarTecla("LEFT", "moverIzquierda", -1);
    registrarTecla("RIGHT", "moverDerecha", 1);

    getRootPane()
            .getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(
                    KeyStroke.getKeyStroke("DOWN"),
                    "caerDirectamente"
            );

    getRootPane().getActionMap().put(
            "caerDirectamente",
            new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    caerDirectamente();
                }
            }
    );
    }

    private void registrarTecla(
            String tecla,
            String accion,
            final int direccion
    ) {
        getRootPane()
                .getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(tecla), accion);

        getRootPane().getActionMap().put(
                accion,
                new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        mover(direccion);
                    }
                }
        );
    }

    private void mover(int direccion) {

        // No permite mover el bloque durante las pausas.
        if (!temporizador.isRunning()) {
            return;
        }

        int nuevaColumna = columnaActual + direccion;

        if (nuevaColumna < 0 || nuevaColumna > 7) {
            return;
        }

        columnaActual = nuevaColumna;
        juego.mostrarBloque(columnaActual, progreso);
    }

    private void reiniciarPrueba() {
    detenerTemporizadores();

    indiceObjetivo = 0;
    Arrays.fill(bits, 0);

    juego.reiniciarFilas(
            OBJETIVOS_PRUEBA[indiceObjetivo]
    );

    // Recupera las tres pistas al comenzar otra prueba.
    Arrays.fill(pistasUsadas, false);

    for (int i = 0; i < pistasUsadas.length; i++) {
        juego.mostrarPistaDisponible(i, true);
    }

        juego.mostrarPuntuacion(0);
        crearBloque();
    }

    private void crearBloque() {
        columnaActual = COLUMNA_INICIAL;
        progreso = 0;

        juego.mostrarBloque(columnaActual, progreso);

        inicio = System.nanoTime();
        temporizador.start();
    }

    private void avanzar() {
        progreso = Math.min(
                1.0,
                (System.nanoTime() - inicio) / DURACION_NANOS
        );

        juego.mostrarBloque(columnaActual, progreso);

        if (progreso >= 1.0) {
            temporizador.stop();
            aterrizar();
        }
    }

    private void aterrizar() {

        // Activa la casilla donde aterrizó el bloque.
        bits[columnaActual] = 1;

        juego.mostrarBits(bits);
        juego.ocultarBloque();

        int objetivo = OBJETIVOS_PRUEBA[indiceObjetivo];

        // El error ocurre si esa posición debía permanecer en 0.
        if (!posicionPermitida(objetivo, columnaActual)) {
            juego.mostrarInfeccion(true);
            pausaInfeccion.restart();
            return;
        }

        if (valorDecimal(bits) == objetivo) {
        juego.mostrarEstado("Fila correcta");
        pausaAcierto.restart();
        return;
        }

        pausaEntreBloques.restart();
    }

    private void crearFilaTrasError() {
        if (indiceObjetivo + 1 >= OBJETIVOS_PRUEBA.length) {

            // Límite de esta demostración, no del juego definitivo.
            juego.mostrarEstado("Fin de prueba. Reinicia.");
            return;
        }

        indiceObjetivo++;
        Arrays.fill(bits, 0);

        juego.agregarFila(
                OBJETIVOS_PRUEBA[indiceObjetivo]
        );

        crearBloque();
    }

    static boolean posicionPermitida(int objetivo, int columna) {

        // Calcula el peso de la columna: 128, 64, ..., 1.
        int valorCasilla = 1 << (7 - columna);

        // Comprueba si el objetivo necesita un 1 en esa posición.
        return (objetivo & valorCasilla) != 0;
    }

    static int valorDecimal(int[] bits) {
        int valor = 0;

        for (int bit : bits) {
            valor = valor * 2 + bit;
        }

        return valor;
    }

    private void detenerTemporizadores() {
    temporizador.stop();
    pausaEntreBloques.stop();
    pausaInfeccion.stop();
    pausaAcierto.stop();
    }

    @Override
    public void dispose() {
        detenerTemporizadores();
        super.dispose();
    }

    private void resolverFila() {
    if (indiceObjetivo == 0) {
        // No quedan filas infectadas debajo.
        juego.mostrarEstado("Completado. Reinicia la prueba.");
        return;
    }

    // Retira la fila resuelta junto con su objetivo.
    juego.retirarFilaActiva();

    // Regresa al objetivo de la fila anterior.
    indiceObjetivo--;

    // Limpia sus bits para volver a intentarlo.
    Arrays.fill(bits, 0);
    juego.mostrarBits(bits);

    // Elimina la apariencia de infección.
    juego.mostrarInfeccion(false);
    juego.mostrarEstado("Fila recuperada");

    crearBloque();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Fuentes.configurar();

            PruebaCaida ventana = new PruebaCaida();
            ventana.setVisible(true);
            ventana.reiniciarPrueba();
        });
    }

    private void configurarPistas() {
        for (int i = 0; i < pistasUsadas.length; i++) {
            final int indice = i;

            juego.getBotonPista(i).addActionListener(
                    e -> usarPista(indice)
            );
        }
    }

    private void usarPista(int indice) {

        // Impide repetir una pista o consumir otra mientras hay una visible.
        if (pistasUsadas[indice]
                || !juego.getBotonPista(indice).isEnabled()) {
            return;
        }

        pistasUsadas[indice] = true;

        juego.mostrarPistaDisponible(indice, false);
        juego.mostrarPista(true);
    }

    private void caerDirectamente() {

    // Durante las pausas no hay un bloque que pueda caer.
    if (!temporizador.isRunning()) {
        return;
    }

    // Detiene la animación y lleva el bloque hasta abajo.
    temporizador.stop();
    progreso = 1.0;

    juego.mostrarBloque(columnaActual, progreso);

    // Activa el bit y comprueba si la posición es correcta.
    aterrizar();
    }
}