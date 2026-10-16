package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class PanelJuego extends JPanel {

    // La posición 0 corresponde a la fila superior.
    private final List<FilaVista> filas = new ArrayList<>();

    private final JPanel contenedorFilas =
            new JPanel(new GridLayout(0, 1, 0, 6));

    private final JLabel etiquetaPuntuacion = new JLabel();
    private final JLabel etiquetaEstado = new JLabel();

    private final JButton botonVolver =
            new JButton("Volver al menú");

    private final PanelCaida zonaCaida = new PanelCaida();

    public PanelJuego() {
        setLayout(new BorderLayout(15, 15));
        setBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        JPanel informacion = new JPanel(new BorderLayout());
        informacion.add(
                new JLabel("Jugador: Prueba"),
                BorderLayout.WEST
        );
        informacion.add(etiquetaPuntuacion, BorderLayout.EAST);

        add(informacion, BorderLayout.NORTH);

        JPanel tablero = new JPanel(new BorderLayout());
        tablero.setBorder(
                BorderFactory.createTitledBorder("Tablero binario")
        );

        // Este espacio mantiene el bloque alineado con las casillas.
        JPanel zonaSuperior = new JPanel(new BorderLayout(10, 0));

        JPanel espacioObjetivos = new JPanel();
        espacioObjetivos.setPreferredSize(
                new Dimension(FilaVista.ANCHO_OBJETIVO, 0)
        );

        zonaSuperior.add(zonaCaida, BorderLayout.CENTER);
        zonaSuperior.add(espacioObjetivos, BorderLayout.EAST);

        tablero.add(zonaSuperior, BorderLayout.CENTER);
        
        JPanel zonaInferior = new JPanel(new BorderLayout(0, 4));

        zonaInferior.add(contenedorFilas, BorderLayout.CENTER);
        zonaInferior.add(crearGuiaPotencias(), BorderLayout.SOUTH);

        tablero.add(zonaInferior, BorderLayout.SOUTH);

        add(tablero, BorderLayout.CENTER);

        JPanel controles = new JPanel(new BorderLayout(10, 0));
        controles.add(etiquetaEstado, BorderLayout.CENTER);
        controles.add(botonVolver, BorderLayout.EAST);

        add(controles, BorderLayout.SOUTH);

        reiniciarFilas(5);
        mostrarPuntuacion(0);
    }

    public void agregarFila(int objetivo) {

        // La fila anterior conserva sus bits y su apariencia.
        if (!filas.isEmpty()) {
            filas.get(0).setActiva(false);
        }

        FilaVista nueva = new FilaVista(objetivo);
        nueva.setActiva(true);

        filas.add(0, nueva);
        contenedorFilas.add(nueva, 0);

        actualizarEstado();
        revalidate();
        repaint();
    }

    public void reiniciarFilas(int objetivo) {
        ocultarBloque();

        filas.clear();
        contenedorFilas.removeAll();

        agregarFila(objetivo);
    }

    public void mostrarBits(int[] bits) {
        filas.get(0).mostrarBits(bits);
    }

    public void mostrarObjetivo(int numero) {
        filas.get(0).mostrarObjetivo(numero);
    }

    public void mostrarPuntuacion(int puntos) {
        etiquetaPuntuacion.setText("Puntuación: " + puntos);
    }

    public void mostrarInfeccion(boolean infectada) {
        filas.get(0).mostrarInfeccion(infectada);
        actualizarEstado();
    }

    private void actualizarEstado() {
        boolean infectada = filas.get(0).estaInfectada();

        mostrarEstado(
                infectada ? "Fila infectada" : "Fila activa"
        );

        etiquetaEstado.setForeground(
                infectada ? new Color(160, 0, 0) : Color.BLACK
        );
    }

    public void mostrarEstado(String mensaje) {
        etiquetaEstado.setText(mensaje);
        etiquetaEstado.setForeground(Color.BLACK);
    }

    public void mostrarBloque(int columna, double progreso) {
        zonaCaida.mostrarBloque(columna, progreso);
    }

    public void ocultarBloque() {
        zonaCaida.ocultarBloque();
    }

    public JButton getBotonVolver() {
        return botonVolver;
    }

    private JPanel crearGuiaPotencias() {
    JPanel potencias = new JPanel(new GridLayout(1, 8, 5, 0));

    for (int i = 0; i < 8; i++) {
        JLabel etiqueta = new JLabel(
                "<html>2<sup>" + (7 - i) + "</sup></html>",
                SwingConstants.CENTER
        );

        etiqueta.setFont(Fuentes.pixel(10f));
        etiqueta.setPreferredSize(new Dimension(55, 22));

        potencias.add(etiqueta);
    }

    // Reserva el espacio de los objetivos para alinear las etiquetas.
    JPanel espacioObjetivos = new JPanel();
    espacioObjetivos.setPreferredSize(
            new Dimension(FilaVista.ANCHO_OBJETIVO, 0)
    );

    JPanel guia = new JPanel(new BorderLayout(10, 0));
    guia.add(potencias, BorderLayout.CENTER);
    guia.add(espacioObjetivos, BorderLayout.EAST);

    return guia;
}

    public void retirarFilaActiva() {
    if (filas.size() < 2) {
        throw new IllegalStateException(
                "No hay una fila anterior para recuperar."
        );
    }

    // Elimina la fila completa, incluido su objetivo decimal.
    FilaVista retirada = filas.remove(0);
    contenedorFilas.remove(retirada);

    // La fila que estaba debajo pasa a ser la activa.
    filas.get(0).setActiva(true);

    actualizarEstado();
    revalidate();
    repaint();
}
}