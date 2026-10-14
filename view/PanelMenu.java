package view;

import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class PanelMenu extends JPanel {

    private final JButton botonJugar = new JButton("Jugar");

    public PanelMenu() {
        //creación del panel
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        //titulo
        JLabel titulo = new JLabel("Tetris binario", SwingConstants.CENTER);
        titulo.setFont(Fuentes.pixel(24f));
        botonJugar.setFont(Fuentes.pixel(12f));

        //espacio para botones
        JPanel zonaBotones = new JPanel();
        zonaBotones.add(botonJugar);

        //posición de los objetos
        add(titulo, BorderLayout.CENTER);
        add(zonaBotones, BorderLayout.SOUTH);
    }

    
    public JButton getBotonJugar() {
        return botonJugar;
    }
}