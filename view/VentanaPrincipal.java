package view;

import java.awt.CardLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class VentanaPrincipal extends JFrame {

    public VentanaPrincipal() {
        setTitle("Tetris binario");
        setSize(850, 550);
        setMinimumSize(new Dimension(750, 450));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // CardLayout muestra un panel a la vez en la misma ventana.
        CardLayout distribucion = new CardLayout();
        JPanel pantallas = new JPanel(distribucion);

        PanelMenu menu = new PanelMenu();
        PanelJuego juego = new PanelJuego();

        pantallas.add(menu, "MENU");
        pantallas.add(juego, "JUEGO");
        setContentPane(pantallas);

        // Conectar los botones con la navegacion.
        menu.getBotonJugar().addActionListener(e -> {
            distribucion.show(pantallas, "JUEGO");
        });

        juego.getBotonVolver().addActionListener(e -> {
            distribucion.show(pantallas, "MENU");
        });

        distribucion.show(pantallas, "MENU");
    }

    // Inicio temporal mientras se adapta Aplicacion a Swing.
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new VentanaPrincipal().setVisible(true);
        });
    }
}