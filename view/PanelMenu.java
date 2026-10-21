package view;

import java.awt.BorderLayout;
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
    private final JButton botonPuntuaciones = new JButton("Puntuaciones");
    private final JButton botonCreditos = new JButton("Créditos");

    public PanelMenu() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(40, 30, 40, 30));

        JLabel titulo = new JLabel(
                "Tetris binario", SwingConstants.CENTER
        );
        titulo.setFont(Fuentes.pixel(24f));
        add(titulo, BorderLayout.NORTH);

        JPanel opciones = new JPanel(new GridLayout(3, 1, 0, 15));

        JButton[] botones = {
            botonJugar, botonPuntuaciones, botonCreditos
        };

        for (JButton boton : botones) {
            boton.setFont(Fuentes.pixel(12f));
            boton.setPreferredSize(new Dimension(300, 55));
            opciones.add(boton);
        }

        JPanel centro = new JPanel(new GridBagLayout());
        centro.add(opciones);
        add(centro, BorderLayout.CENTER);
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