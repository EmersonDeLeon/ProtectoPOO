package view.pruebas;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import view.Fuentes;
import view.PanelJuego;

public class PruebaCaida extends JFrame {

    // Contamos desde cero: 5 corresponde a la sexta casilla.
    private static final int COLUMNA_PRUEBA = 5;

    // Tres segundos expresados en nanosegundos.
    private static final double DURACION_NANOS = 3_000_000_000.0;

    private final PanelJuego juego = new PanelJuego();
    private final Timer temporizador;
    private long inicio;

    public PruebaCaida() {
        setTitle("Prueba del primer bloque");
        setSize(680, 820);
        setMinimumSize(new Dimension(620, 700));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JButton repetir = new JButton("Repetir caída");

        JPanel controles = new JPanel();
        controles.add(repetir);

        add(controles, BorderLayout.NORTH);
        add(juego, BorderLayout.CENTER);

        // Solicita una actualizacion aproximadamente cada 16 ms.
        temporizador = new Timer(16, e -> avanzar());

        repetir.addActionListener(e -> iniciarCaida());

        juego.getBotonVolver().setText("Cerrar prueba");
        juego.getBotonVolver().addActionListener(e -> dispose());

        setLocationRelativeTo(null);
    }

    private void iniciarCaida() {
        temporizador.stop();

        juego.mostrarBits(new int[8]);
        juego.mostrarObjetivo(4);
        juego.mostrarPuntuacion(0);
        juego.mostrarInfeccion(false);
        juego.mostrarBloque(COLUMNA_PRUEBA, 0);

        inicio = System.nanoTime();
        temporizador.start();
    }

    private void avanzar() {
        double progreso = Math.min(
                1.0,
                (System.nanoTime() - inicio) / DURACION_NANOS
        );

        juego.mostrarBloque(COLUMNA_PRUEBA, progreso);

        if (progreso >= 1.0) {
            temporizador.stop();

            // Simulacion de la llegada del bloque.
            int[] bits = new int[8];
            bits[COLUMNA_PRUEBA] = 1;

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