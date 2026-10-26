package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class PanelMenu extends JPanel {

    private final JButton botonJugar = new JButton("Jugar");
    private final JButton botonPuntuaciones =
            new JButton("Puntuaciones");
    private final JButton botonCreditos = new JButton("Créditos");

    public PanelMenu() {
        setLayout(new GridBagLayout());
        setBorder(
                BorderFactory.createEmptyBorder(30, 20, 30, 20)
        );

        // Título, botones e instrucciones en un mismo bloque.
        JPanel contenido = new JPanel(new BorderLayout(0, 25));
        contenido.setOpaque(false);

        JLabel titulo = new JLabel(
                "Tetris binario",
                SwingConstants.CENTER
        );
        titulo.setFont(Fuentes.pixel(24f));
        titulo.setForeground(Color.BLACK);

        contenido.add(titulo, BorderLayout.NORTH);

        JPanel opciones = new JPanel(
                new GridLayout(3, 1, 0, 15)
        );
        opciones.setOpaque(false);

        JButton[] botones = {
            botonJugar,
            botonPuntuaciones,
            botonCreditos
        };

        for (JButton boton : botones) {
            boton.setFont(Fuentes.pixel(12f));
            boton.setForeground(Color.BLACK);
            boton.setPreferredSize(new Dimension(340, 55));

            // Fondo transparente y borde negro.
            boton.setContentAreaFilled(false);
            boton.setOpaque(false);
            boton.setBorderPainted(true);
            boton.setBorder(
                    BorderFactory.createLineBorder(Color.BLACK, 2)
            );

            opciones.add(boton);
        }

        contenido.add(opciones, BorderLayout.CENTER);

        JPanel instrucciones = new JPanel(
                new GridLayout(3, 1, 0, 12)
        );
        instrucciones.setOpaque(false);

        String[] textos = {
            "<  Izquierda",
            ">  Derecha",
            "SPACE  Soltar"
        };

        for (String texto : textos) {
            JLabel etiqueta = new JLabel(
                    texto,
                    SwingConstants.CENTER
            );
            etiqueta.setFont(Fuentes.pixel(10f));
            etiqueta.setForeground(Color.BLACK);
            instrucciones.add(etiqueta);
        }

        contenido.add(instrucciones, BorderLayout.SOUTH);

        add(contenido);
    }

    public JButton getBotonJugar() {
        return botonJugar;
    }

    public JButton getBotonPuntuaciones() {
        return botonPuntuaciones;
    }

    public JButton getBotonCreditos() {
        return botonCreditos;
    }
}