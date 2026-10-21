package view.pruebas;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import view.Fuentes;
import view.PanelResultado;
import view.VentanaPrincipal;

public class PruebaPantallas {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Fuentes.configurar();

            VentanaPrincipal ventana = new VentanaPrincipal();
            ventana.setTitle("Prueba de vistas — resultados en memoria");

            PanelResultado resultado = ventana.getPanelResultado();

            JSpinner puntos = new JSpinner(
                    new SpinnerNumberModel(1250, 0, 999999, 50)
            );

            JButton simularDerrota = new JButton("Simular derrota");

            JPanel herramientas = new JPanel(
                    new FlowLayout(FlowLayout.LEFT)
            );
            herramientas.add(new JLabel("Puntos de prueba:"));
            herramientas.add(puntos);
            herramientas.add(simularDerrota);

            // Esta barra existe solamente en la prueba.
            Container contenido = ventana.getContentPane();

            JPanel prueba = new JPanel(new BorderLayout());
            prueba.add(herramientas, BorderLayout.NORTH);
            prueba.add(contenido, BorderLayout.CENTER);

            ventana.setContentPane(prueba);

            final int[] puntuacionFinal = {0};

            simularDerrota.addActionListener(e -> {
                try {
                    puntos.commitEdit();
                } catch (java.text.ParseException error) {
                    JOptionPane.showMessageDialog(
                            ventana,
                            "Introduce una puntuación numérica válida."
                    );
                    return;
                }

                puntuacionFinal[0] =
                        ((Number) puntos.getValue()).intValue();

                ventana.mostrarResultado(puntuacionFinal[0]);
            });

            resultado.getBotonGuardar().addActionListener(e -> {
                String nombre = resultado.getNombreIngresado().trim();

                if (nombre.isEmpty()) {
                    resultado.mostrarError("Escribe un nombre.");
                    resultado.enfocarNombre();
                    return;
                }

                // Solo actualiza la tabla de esta prueba.
                // No escribe ningún archivo CSV.
                ventana.getPanelPuntuaciones().agregarResultado(
                        nombre, puntuacionFinal[0]
                );

                resultado.setEdicionHabilitada(false);
                ventana.mostrarPuntuaciones();
            });

            ventana.setVisible(true);
        });
    }
}