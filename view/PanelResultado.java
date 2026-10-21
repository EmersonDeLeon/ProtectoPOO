package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class PanelResultado extends JPanel {

    private final JLabel etiquetaPuntuacion =
            new JLabel(" ", SwingConstants.CENTER);

    private final JTextField campoNombre = new JTextField();

    private final JButton botonGuardar =
            new JButton("Guardar puntuación");

    private final JButton botonVolver =
            new JButton("Volver al menú");

    private final JLabel etiquetaMensaje =
            new JLabel(" ", SwingConstants.CENTER);

    public PanelResultado() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(40, 30, 40, 30));

        JLabel titulo = new JLabel(
                "Fin de la partida", SwingConstants.CENTER
        );
        titulo.setFont(Fuentes.pixel(20f));
        add(titulo, BorderLayout.NORTH);

        etiquetaPuntuacion.setFont(Fuentes.pixel(16f));

        JLabel etiquetaNombre = new JLabel("Nombre para la tabla:");
        etiquetaNombre.setLabelFor(campoNombre);

        campoNombre.setFont(Fuentes.pixel(12f));
        campoNombre.setMargin(new Insets(8, 8, 8, 8));

        JPanel formulario = new JPanel(new GridLayout(5, 1, 0, 12));
        formulario.setPreferredSize(new Dimension(420, 260));

        formulario.add(etiquetaPuntuacion);
        formulario.add(etiquetaNombre);
        formulario.add(campoNombre);
        formulario.add(botonGuardar);
        formulario.add(etiquetaMensaje);

        JPanel centro = new JPanel(new GridBagLayout());
        centro.add(formulario);
        add(centro, BorderLayout.CENTER);

        JPanel controles = new JPanel();
        controles.add(botonVolver);
        add(controles, BorderLayout.SOUTH);

        // Enter en el campo equivale a pulsar Guardar.
        campoNombre.addActionListener(e -> botonGuardar.doClick());
    }

    public void prepararResultado(int puntuacion) {
        etiquetaPuntuacion.setText("Puntuación: " + puntuacion);
        campoNombre.setText("");
        etiquetaMensaje.setText(" ");
        etiquetaMensaje.setForeground(Color.DARK_GRAY);
        setEdicionHabilitada(true);
    }

    public String getNombreIngresado() {
        return campoNombre.getText();
    }

    public JButton getBotonGuardar() {
        return botonGuardar;
    }

    public JButton getBotonVolver() {
        return botonVolver;
    }

    public void mostrarError(String mensaje) {
        etiquetaMensaje.setForeground(new Color(180, 40, 40));
        etiquetaMensaje.setText(mensaje);
    }

    public void mostrarConfirmacion(String mensaje) {
        etiquetaMensaje.setForeground(new Color(0, 110, 45));
        etiquetaMensaje.setText(mensaje);
    }

    public void setEdicionHabilitada(boolean habilitada) {
        campoNombre.setEnabled(habilitada);
        botonGuardar.setEnabled(habilitada);
    }

    public void enfocarNombre() {
        campoNombre.requestFocusInWindow();
    }
}