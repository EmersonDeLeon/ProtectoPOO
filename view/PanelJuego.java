package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class PanelJuego extends JPanel {

    private static final Color COLOR_NORMAL = new Color(220, 235, 250);
    private static final Color COLOR_INFECTADA = new Color(255, 180, 180);

    // Componentes que podremos actualizar desde otros metodos.
    private final JLabel[] casillas = new JLabel[8];
    private final JLabel etiquetaObjetivo =
            new JLabel("", SwingConstants.CENTER);
    private final JLabel etiquetaPuntuacion = new JLabel();
    private final JLabel etiquetaEstado = new JLabel();
    private final JButton botonVolver = new JButton("Volver al menú");

    public PanelJuego() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel informacion = new JPanel(new BorderLayout());
        informacion.add(new JLabel("Jugador: Prueba"), BorderLayout.WEST);
        informacion.add(etiquetaPuntuacion, BorderLayout.EAST);
        add(informacion, BorderLayout.NORTH);

        JPanel tablero = new JPanel(new BorderLayout(10, 10));
        tablero.setBorder(BorderFactory.createTitledBorder("Tablero binario"));

        JPanel zonaCaida = new JPanel();
        zonaCaida.setBackground(new Color(30, 40, 55));
        tablero.add(zonaCaida, BorderLayout.CENTER);

        JPanel filaBits = new JPanel(new GridLayout(1, 8, 5, 0));

        for (int i = 0; i < casillas.length; i++) {
            casillas[i] = new JLabel("0", SwingConstants.CENTER);
            casillas[i].setFont(new Font("Monospaced", Font.BOLD, 26));
            casillas[i].setForeground(Color.BLACK);
            casillas[i].setOpaque(true);
            casillas[i].setBorder(
                    BorderFactory.createLineBorder(Color.GRAY)
            );
            casillas[i].setPreferredSize(new Dimension(55, 55));

            filaBits.add(casillas[i]);
        }

        etiquetaObjetivo.setFont(new Font("SansSerif", Font.BOLD, 18));
        etiquetaObjetivo.setPreferredSize(new Dimension(140, 55));
        etiquetaObjetivo.setBorder(
                BorderFactory.createLineBorder(Color.GRAY)
        );

        JPanel filaInferior = new JPanel(new BorderLayout(10, 0));
        filaInferior.add(filaBits, BorderLayout.CENTER);
        filaInferior.add(etiquetaObjetivo, BorderLayout.EAST);

        tablero.add(filaInferior, BorderLayout.SOUTH);
        add(tablero, BorderLayout.CENTER);

        JPanel controles = new JPanel(new BorderLayout(10, 0));
        controles.add(etiquetaEstado, BorderLayout.WEST);
        controles.add(botonVolver, BorderLayout.EAST);
        add(controles, BorderLayout.SOUTH);

        // Estado inicial de la demostracion.
        mostrarBits(new int[8]);
        mostrarObjetivo(5);
        mostrarPuntuacion(0);
        mostrarInfeccion(false);
    }

    // Los bits llegan ordenados de izquierda a derecha.
    public void mostrarBits(int[] bits) {
        if (bits == null || bits.length != casillas.length) {
            throw new IllegalArgumentException(
                    "Se necesitan exactamente 8 bits."
            );
        }

        // Comprobar el formato antes de cambiar la pantalla.
        for (int bit : bits) {
            if (bit != 0 && bit != 1) {
                throw new IllegalArgumentException(
                        "Cada bit debe ser 0 o 1."
                );
            }
        }

        for (int i = 0; i < casillas.length; i++) {
            casillas[i].setText(String.valueOf(bits[i]));
        }
    }

    public void mostrarObjetivo(int numero) {
        etiquetaObjetivo.setText("Objetivo: " + numero);
    }

    public void mostrarPuntuacion(int puntos) {
        etiquetaPuntuacion.setText("Puntuación: " + puntos);
    }

    // Representa visualmente el estado que recibe.
    public void mostrarInfeccion(boolean infectada) {
        Color color = infectada ? COLOR_INFECTADA : COLOR_NORMAL;

        for (JLabel casilla : casillas) {
            casilla.setBackground(color);
        }

        etiquetaEstado.setText(
                infectada ? "Fila infectada" : "Fila normal"
        );

        etiquetaEstado.setForeground(
                infectada ? new Color(160, 0, 0) : Color.BLACK
        );
    }

    public JButton getBotonVolver() {
        return botonVolver;
    }
}