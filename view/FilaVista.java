package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class FilaVista extends JPanel {

    public static final int ANCHO_OBJETIVO = 140;

    private static final Color COLOR_NORMAL =
            new Color(220, 235, 250);

    private static final Color COLOR_INFECTADA =
            new Color(255, 180, 180);

    private final JLabel[] casillas = new JLabel[8];

    private final JPanel informacion =
        new JPanel(new GridLayout(2, 1));

    private final JLabel etiquetaObjetivo =
            new JLabel("", SwingConstants.CENTER);

    private final JLabel etiquetaEstado =
            new JLabel("", SwingConstants.CENTER);

    private boolean activa;
    private boolean infectada;

    public FilaVista(int objetivo) {
        setLayout(new BorderLayout(10, 0));

        JPanel filaBits = new JPanel(new GridLayout(1, 8, 5, 0));

        for (int i = 0; i < casillas.length; i++) {
            casillas[i] = new JLabel("0", SwingConstants.CENTER);
            casillas[i].setFont(Fuentes.pixel(24f));
            casillas[i].setForeground(Color.BLACK);
            casillas[i].setOpaque(true);
            casillas[i].setPreferredSize(new Dimension(55, 55));

            filaBits.add(casillas[i]);
        }

        add(filaBits, BorderLayout.CENTER);

        
        informacion.setPreferredSize(
                new Dimension(ANCHO_OBJETIVO, 55)
        );
        informacion.setBorder(
                BorderFactory.createLineBorder(Color.GRAY)
        );

        etiquetaObjetivo.setFont(Fuentes.pixel(10f));
        etiquetaEstado.setFont(Fuentes.pixel(8f));

        informacion.add(etiquetaObjetivo);
        informacion.add(etiquetaEstado);

        mostrarObjetivo(objetivo);
        actualizarApariencia();
    }

    public void mostrarBits(int[] bits) {
        if (bits == null || bits.length != casillas.length) {
            throw new IllegalArgumentException(
                    "Se necesitan exactamente 8 bits."
            );
        }

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

    public void mostrarInfeccion(boolean infectada) {
        this.infectada = infectada;
        actualizarApariencia();
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
        actualizarApariencia();
    }

    public boolean estaInfectada() {
        return infectada;
    }

    private void actualizarApariencia() {
        Color fondo = infectada
                ? COLOR_INFECTADA
                : COLOR_NORMAL;

        Color borde = activa
                ? new Color(45, 110, 220)
                : Color.GRAY;

        for (JLabel casilla : casillas) {
            casilla.setBackground(fondo);
            casilla.setBorder(
                    BorderFactory.createLineBorder(borde, 2)
            );
        }

        etiquetaEstado.setText(
                infectada
                        ? "INFECTADA"
                        : (activa ? "ACTIVA" : "PENDIENTE")
        );

        etiquetaEstado.setForeground(
                infectada ? new Color(160, 0, 0) : Color.BLACK
        );
    }

    public JPanel getPanelObjetivo() {
    return informacion;
    }
}