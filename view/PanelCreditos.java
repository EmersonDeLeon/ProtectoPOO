package view;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class PanelCreditos extends JPanel {

    private final JButton botonVolver =
            new JButton("Volver al menú");

    public PanelCreditos() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(40, 30, 40, 30));

        JLabel titulo = new JLabel(
                "Créditos", SwingConstants.CENTER
        );
        titulo.setFont(Fuentes.pixel(20f));
        add(titulo, BorderLayout.NORTH);

        JPanel textos = new JPanel(new GridLayout(5, 1, 0, 20));

        String[] lineas = {
            "Tetris binario",
            "Proyecto de POO",
            "Integrantes",
            "Justin Paz",
            "Emerson de León"
        };

        for (String texto : lineas) {
            JLabel etiqueta = new JLabel(
                    texto, SwingConstants.CENTER
            );
            etiqueta.setFont(Fuentes.pixel(12f));
            textos.add(etiqueta);
        }

        JPanel centro = new JPanel(new GridBagLayout());
        centro.add(textos);
        add(centro, BorderLayout.CENTER);

        JPanel controles = new JPanel();
        controles.add(botonVolver);
        add(controles, BorderLayout.SOUTH);
    }

    public JButton getBotonVolver() {
        return botonVolver;
    }
} 
