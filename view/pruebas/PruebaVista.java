package view.pruebas;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import view.PanelJuego;

public class PruebaVista {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame ventana = new JFrame(
                    "Prueba de actualización de la vista"
            );

            ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            ventana.setSize(850, 600);
            ventana.setMinimumSize(new Dimension(750, 500));

            PanelJuego juego = new PanelJuego();

            JButton mostrarEjemplo = new JButton("Mostrar 5");
            JButton infectar = new JButton("Infectar fila");
            JButton desinfectar = new JButton("Desinfectar");
            JButton restablecer = new JButton("Restablecer");

            JPanel botonesPrueba = new JPanel();
            botonesPrueba.add(mostrarEjemplo);
            botonesPrueba.add(infectar);
            botonesPrueba.add(desinfectar);
            botonesPrueba.add(restablecer);

            mostrarEjemplo.addActionListener(e -> {
                juego.mostrarObjetivo(5);
                juego.mostrarBits(
                        new int[] {0, 0, 0, 0, 0, 1, 0, 1}
                );
                juego.mostrarPuntuacion(100); // Dato de prueba.
            });

            infectar.addActionListener(e -> {
                juego.mostrarInfeccion(true);
            });

            desinfectar.addActionListener(e -> {
                juego.mostrarInfeccion(false);
            });

            restablecer.addActionListener(e -> {
                juego.mostrarBits(new int[8]);
                juego.mostrarObjetivo(5);
                juego.mostrarPuntuacion(0);
                juego.mostrarInfeccion(false);
            });

            // Esta prueba abre directamente el tablero.
            juego.getBotonVolver().setText("Cerrar prueba");
            juego.getBotonVolver().addActionListener(e -> {
                ventana.dispose();
            });

            ventana.add(botonesPrueba, BorderLayout.NORTH);
            ventana.add(juego, BorderLayout.CENTER);

            ventana.setLocationRelativeTo(null);
            ventana.setVisible(true);
        });
    }
}