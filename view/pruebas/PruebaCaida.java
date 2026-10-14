package view.pruebas;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
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

    private final PanelJuego juego = new PanelJuego();
    private final Timer temporizador;

    private int columnaActual;
    private double progreso;
    private long inicio;

    public PruebaCaida() {
        setTitle("Prueba del bloque con movimiento");
        setSize(680, 820);
        setMinimumSize(new Dimension(620, 700));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JButton repetir = new JButton("Repetir caída");

        JPanel controles = new JPanel();
        controles.add(new JLabel("Flechas: izquierda y derecha"));
        controles.add(repetir);

        add(controles, BorderLayout.NORTH);
        add(juego, BorderLayout.CENTER);

        temporizador = new Timer(16, e -> avanzar());
        repetir.addActionListener(e -> iniciarCaida());

        configurarTeclas();

        juego.getBotonVolver().setText("Cerrar prueba");
        juego.getBotonVolver().addActionListener(e -> dispose());

        setLocationRelativeTo(null);
    }

    private void configurarTeclas() {
        // Flecha izquierda.
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke("LEFT"), "moverIzquierda");

        getRootPane().getActionMap().put(
                "moverIzquierda",
                new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        mover(-1);
                    }
                }
        );

        // Flecha derecha.
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke("RIGHT"), "moverDerecha");

        getRootPane().getActionMap().put(
                "moverDerecha",
                new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        mover(1);
                    }
                }
        );
    }

    private void mover(int direccion) {
        // Solo se puede mover mientras esta cayendo.
        if (!temporizador.isRunning()) {
            return;
        }

        int nuevaColumna = columnaActual + direccion;

        // Evitar que salga de las ocho columnas.
        if (nuevaColumna < 0 || nuevaColumna > 7) {
            return;
        }

        columnaActual = nuevaColumna;

        // Cambiar de columna conservando el progreso vertical.
        juego.mostrarBloque(columnaActual, progreso);
    }

    private void iniciarCaida() {
        temporizador.stop();

        columnaActual = COLUMNA_INICIAL;
        progreso = 0;

        juego.mostrarBits(new int[8]);
        juego.mostrarObjetivo(4);
        juego.mostrarPuntuacion(0);
        juego.mostrarInfeccion(false);
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

            // Encender el bit de la columna donde aterrizo.
            int[] bits = new int[8];
            bits[columnaActual] = 1;

            juego.mostrarBits(bits);
            juego.ocultarBloque();
        }
    }

    @Override
    public void dispose() {
        temporizador.stop();
        super.dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Fuentes.configurar();

            PruebaCaida ventana = new PruebaCaida();
            ventana.setVisible(true);
            ventana.iniciarCaida();
        });
    }
}