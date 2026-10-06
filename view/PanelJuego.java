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

    private final JButton botonVolver = new JButton("Volver al menú");

    public PanelJuego() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 1. Informacion superior con datos de prueba.
        JPanel informacion = new JPanel(new BorderLayout());
        informacion.add(new JLabel("Jugador: Prueba"), BorderLayout.WEST);
        informacion.add(new JLabel("Puntuación: 0"), BorderLayout.EAST);
        add(informacion, BorderLayout.NORTH);

        // 2. Espacio para el tablero y los futuros bloques.
        JPanel tablero = new JPanel(new BorderLayout(10, 10));
        tablero.setBorder(BorderFactory.createTitledBorder("Tablero binario"));

        JPanel zonaCaida = new JPanel();
        zonaCaida.setBackground(new Color(30, 40, 55));
        tablero.add(zonaCaida, BorderLayout.CENTER);

        // 3. Una fila con ocho casillas del mismo tamaño.
        JPanel filaBits = new JPanel(new GridLayout(1, 8, 5, 0));

        for (int i = 0; i < 8; i++) {
            JLabel casilla = new JLabel("0", SwingConstants.CENTER);
            casilla.setFont(new Font("Monospaced", Font.BOLD, 26));
            casilla.setOpaque(true);
            casilla.setBackground(new Color(220, 235, 250));
            casilla.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            casilla.setPreferredSize(new Dimension(55, 55));
            filaBits.add(casilla);
        }

        // 4. El objetivo queda al lado de los ocho bits.
        JLabel objetivo = new JLabel("Objetivo: 5", SwingConstants.CENTER);
        objetivo.setFont(new Font("SansSerif", Font.BOLD, 18));
        objetivo.setPreferredSize(new Dimension(140, 55));
        objetivo.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        JPanel filaInferior = new JPanel(new BorderLayout(10, 0));
        filaInferior.add(filaBits, BorderLayout.CENTER);
        filaInferior.add(objetivo, BorderLayout.EAST);

        tablero.add(filaInferior, BorderLayout.SOUTH);
        add(tablero, BorderLayout.CENTER);

        // 5. Boton para regresar al menu.
        JPanel controles = new JPanel();
        controles.add(botonVolver);
        add(controles, BorderLayout.SOUTH);
    }

    public JButton getBotonVolver() {
        return botonVolver;
    }
}