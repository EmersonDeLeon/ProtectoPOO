package view;

import java.awt.CardLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class VentanaPrincipal extends JFrame {

    private final CardLayout distribucion = new CardLayout();
    private final JPanel pantallas = new JPanel(distribucion);

    private final PanelMenu menu = new PanelMenu();
    private final PanelJuego juego = new PanelJuego();
    private final PanelResultado resultado = new PanelResultado();
    private final PanelPuntuaciones puntuaciones = new PanelPuntuaciones();
    private final PanelCreditos creditos = new PanelCreditos();

    public VentanaPrincipal() {
        setTitle("Tetris binario");
        setSize(680, 820);
        setMinimumSize(new Dimension(620, 700));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        pantallas.add(menu, "MENU");
        pantallas.add(juego, "JUEGO");
        pantallas.add(resultado, "RESULTADO");
        pantallas.add(puntuaciones, "PUNTUACIONES");
        pantallas.add(creditos, "CREDITOS");

        setContentPane(pantallas);

        menu.getBotonJugar().addActionListener(e -> mostrarJuego());
        menu.getBotonPuntuaciones().addActionListener(
                e -> mostrarPuntuaciones()
        );
        menu.getBotonCreditos().addActionListener(e -> mostrarCreditos());

        juego.getBotonVolver().addActionListener(e -> mostrarMenu());
        resultado.getBotonVolver().addActionListener(e -> mostrarMenu());
        puntuaciones.getBotonVolver().addActionListener(e -> mostrarMenu());
        creditos.getBotonVolver().addActionListener(e -> mostrarMenu());

        mostrarMenu();
    }

    public void mostrarMenu() {
        distribucion.show(pantallas, "MENU");
    }

    public void mostrarJuego() {
        distribucion.show(pantallas, "JUEGO");
    }

    public void mostrarResultado(int puntuacion) {
        resultado.prepararResultado(puntuacion);
        distribucion.show(pantallas, "RESULTADO");
        resultado.enfocarNombre();
    }

    public void mostrarPuntuaciones() {
        distribucion.show(pantallas, "PUNTUACIONES");
    }

    public void mostrarCreditos() {
        distribucion.show(pantallas, "CREDITOS");
    }

    public PanelMenu getPanelMenu() {
        return menu;
    }

    public PanelJuego getPanelJuego() {
        return juego;
    }

    public PanelResultado getPanelResultado() {
        return resultado;
    }

    public PanelPuntuaciones getPanelPuntuaciones() {
        return puntuaciones;
    }

    // Inicio temporal mientras se conecta Aplicacion.
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Fuentes.configurar();
            new VentanaPrincipal().setVisible(true);
        });
    }
}